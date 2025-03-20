package sk.neuromancer.gradle.javacard.common

import sk.neuromancer.gradle.javacard.JavaCardBuildTask
import sk.neuromancer.gradle.javacard.utils.TestUtils;
import groovy.lang.Closure;
import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.Before

import static org.junit.Assert.assertTrue;

public class CommonTest {

    protected static Project project
    protected static JavaCardBuildTask task

    protected static String buildDir = System.getProperty("user.dir") + "/build"
    protected static String buildJavaCardDir = buildDir + "/javacard"

    @Before
    void setUpProject() {
        project = ProjectBuilder.builder().build()
        project.apply plugin: 'javacard'

        //set build directory
        project.setBuildDir(buildDir)

        //set source path
        project.sourceSets {
            main {
                java {
                    srcDirs = [System.getProperty("user.dir") + "/../test/src/main/java"]
                }
            }
        }
        task = project.getTasks().getByName("buildJavaCard")
    }

    void configureProject(Closure closure) {
        project.configure(project.javacard, closure)
    }

    void runBuildTask(Closure closure) {
        deleteBuildDir()
        configureProject(closure)
        project.repositories.add(project.repositories.maven {
            name 'build'
            url "https://javacard.pro/maven"
        })
        project.repositories.add(project.repositories.maven {
            name 'build'
            url "https://deadcode.me/mvn"
        })
        project.repositories.add(project.repositories.mavenCentral())
        project.evaluate()
        task.build()
        checkOutputFile(task)
    }

    void checkOutputFile(task) {
        task.getJavaCard().config.caps.each {
            capItem ->
                assertTrue(TestUtils.getFile(capItem.output).exists())
                if (capItem.export?.trim()) {
                    assertTrue(TestUtils.getFile(TestUtils.getFileName(capItem.export, "exp")).exists())
                } else {
                    assertTrue(TestUtils.getFile(TestUtils.getFileName(capItem.output, "exp")).exists())
                }
                if (capItem.jca?.trim()) {
                    assertTrue(TestUtils.getFile(TestUtils.getFileName(capItem.jca, "jca")).exists())
                } else {
                    assertTrue(TestUtils.getFile(TestUtils.getFileName(capItem.output, "jca")).exists())
                }
        }
    }

    void deleteBuildDir() {
        File dir = new File(buildJavaCardDir)
        dir.deleteDir()
    }
}
