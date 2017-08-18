/*
 * The MIT License (MIT)
 * <p/>
 * Copyright (c) 2017 Bertrand Martel
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package fr.bmartel.javacard

import fr.bmartel.javacard.extension.JavaCard
import fr.bmartel.javacard.gp.GpExec
import fr.bmartel.javacard.util.SdkUtils
import fr.bmartel.javacard.util.Utility
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.JavaPlugin
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * JavaCard plugin.
 *
 * @author Bertrand Martel
 */
class JavaCardPlugin implements Plugin<Project> {

    Logger logger = LoggerFactory.getLogger('javacard-logger')

    static String PLUGIN_NAME = 'javacard'

    static String LIST_TASK = 'listJavaCard'
    static String INSTALL_TASK = 'installJavaCard'
    static String BUILD_TASK = 'buildJavaCard'

    void apply(Project project) {

        //define plugin extension
        def extension = project.extensions.create(PLUGIN_NAME, JavaCard)

        project.afterEvaluate {

            File propertyFile = project.rootProject.file('local.properties')

            if (propertyFile.exists()) {
                Properties properties = new Properties()
                properties.load(propertyFile.newDataInputStream())
                if (properties.getProperty('jc.home')?.trim()) {
                    extension.config.jckit = properties.getProperty('jc.home')
                }
            }
            logger.debug("jckit location : " + extension.config.getJcKit())

            //resolve the javacard framework according to SDK version
            project.dependencies {
                compile project.files(SdkUtils.getApiPath(extension.config.getJcKit(), logger))
            }

            extension.config.caps.each { capItem ->

                if (capItem.dependencies != null) {
                    capItem.dependencies.local.each { localItem ->
                        project.dependencies.add("compile", project.files(localItem.jar))
                    }
                    capItem.dependencies.remote.each { remoteItem ->
                        project.dependencies.add("compile", remoteItem)
                    }
                }
            }

            if (extension.scripts != null) {

                extension.scripts.tasks.each { taskItem ->

                    def command = []

                    command.add('-d')

                    taskItem.scripts.each { taskIncludedScript ->
                        extension.scripts.scripts.each { scriptItem ->
                            if (scriptItem.name == taskIncludedScript) {
                                command.add('-a')
                                command.add(Utility.formatApdu(scriptItem.apdu))
                            }
                        }
                    }

                    if (!project.tasks.findByName(taskItem.name)) {
                        createScriptTask(project, taskItem.name, command)
                    }
                }
            }

            if (!project.tasks.findByName(INSTALL_TASK)) {
                createInstallTask(project, extension)
            }

            //validate the extension properties
            extension.validate()
        }

        //apply the java plugin if not defined
        if (!project.plugins.hasPlugin(JavaPlugin)) {
            project.plugins.apply(JavaPlugin)
        }

        def build = project.tasks.create(BUILD_TASK, JavaCardBuildTask)

        build.configure {
            group = 'build'
            description = 'Create CAP file(s) for installation on a smart card'
            dependsOn(project.classes)
        }

        if (!project.tasks.findByName(LIST_TASK)) {
            createListTask(project)
        }

        project.build.dependsOn(build)
    }

    /**
     * create GpExec install cap file task
     *
     * @param project gradle project
     * @param extension gradle extension
     * @return
     */
    def createInstallTask(Project project, extension) {
        def install = project.tasks.create(name: INSTALL_TASK, type: GpExec)
        def args = ['-relax']
        extension.config.caps.each { capItem ->
            args.add('--delete')
            args.add(Utility.formatApdu(capItem.aid))
            args.add('--install')
            args.add(new File(capItem.output).absolutePath)
        }
        createGpExec(project, install, 'install', 'install cap file', args)
    }

    /**
     * Create GpExec list applet task
     *
     * @param project gradle project
     * @return
     */
    def createListTask(Project project) {
        def script = project.tasks.create(name: LIST_TASK, type: GpExec)
        createGpExec(project, script, 'list', 'apdu script', ['-l'])
    }

    /**
     * Create GpExec apdu script task.
     *
     * @param project gradle project
     * @param taskName task name
     * @param args
     * @return
     */
    def createScriptTask(Project project, String taskName, args) {
        def script = project.tasks.create(name: taskName, type: GpExec)
        createGpExec(project, script, 'javacard-script', 'apdu script', args)
    }

    /**
     * Create GpExec task
     *
     * @param project gradle project
     * @param task gradle task object
     * @param grp group name
     * @param desc task description
     * @param arguments arguments to gp tool
     * @return
     */
    def createGpExec(Project project, Task task, String grp, String desc, arguments) {
        task.configure {
            group = grp
            description = desc
            args(arguments)
            doFirst {
                logger.quiet(commandLine)
            }
            dependsOn(project.jar)
        }
    }
}