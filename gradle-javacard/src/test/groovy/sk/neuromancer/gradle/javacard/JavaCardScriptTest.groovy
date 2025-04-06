package sk.neuromancer.gradle.javacard

import sk.neuromancer.gradle.javacard.common.CommonTest
import org.gradle.api.Task
import org.gradle.api.tasks.JavaExec
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

class JavaCardScriptTest extends CommonTest {

    @Test
    void validScript() {
        runBuildTask(StaticConfig.VALID_SCRIPT_CONFIG)

        Task task1 = project.getTasks().findByName('task1')
        Task task2 = project.getTasks().findByName('task2')
        Task task3 = project.getTasks().findByName('task3')

        assertTrue(task1 ? true : false)
        assertTrue(task2 ? true : false)
        assertTrue(task3 ? true : false)

        assertTrue(task1 instanceof JavaExec)
        assertTrue(task2 instanceof JavaExec)
        assertTrue(task3 instanceof JavaExec)

        assertEquals(task1.group, 'global platform')
        assertEquals(task2.group, 'global platform')
        assertEquals(task3.group, 'global platform')

        assertEquals(task1.args, ['-a', '010203', '-a', '040506'])
        assertEquals(task2.args, ['-a', '010203'])
        assertEquals(task3.args, ['-a', '040506'])

        assertEquals(task1.mainClass.get(), 'pro.javacard.gptool.GPTool')
        assertEquals(task2.mainClass.get(), 'pro.javacard.gptool.GPTool')
        assertEquals(task3.mainClass.get(), 'pro.javacard.gptool.GPTool')

        //task1.exec()
    }

    @Test
    void runnableScript() {
        runBuildTask(StaticConfig.RUNNABLE_SCRIPT_CONFIG)

        JavaExec installTask = project.getTasks().getByName("installJavaCard")
        JavaExec task1 = project.getTasks().getByName('task1')

        assertTrue(task1 ? true : false)
        assertTrue(task1 instanceof JavaExec)
        assertEquals(task1.group, 'global platform')
        assertEquals(task1.args, ['-a', '00A404000A0102030405060708090100', '-a', '0040000000'])
        assertEquals(task1.mainClass.get(), 'pro.javacard.gptool.GPTool')

        //installTask.exec()
        //task1.exec()
    }
}