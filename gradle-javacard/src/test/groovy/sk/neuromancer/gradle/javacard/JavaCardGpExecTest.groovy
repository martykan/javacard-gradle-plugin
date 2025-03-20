package sk.neuromancer.gradle.javacard


import sk.neuromancer.gradle.javacard.common.CommonTest
import sk.neuromancer.gradle.javacard.gp.GpExec
import org.gradle.api.tasks.JavaExec
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

class JavaCardGpExecTest extends CommonTest {

    @Test
    void gpExecTest() {
        JavaExec listTask = project.tasks.create(name: 'testTask', type: GpExec)

        assertTrue(listTask ? true : false)
        assertTrue(listTask instanceof JavaExec)
        assertTrue(listTask instanceof GpExec)
        assertEquals(listTask.mainClass.get(), 'pro.javacard.gp.GPTool')
    }
}