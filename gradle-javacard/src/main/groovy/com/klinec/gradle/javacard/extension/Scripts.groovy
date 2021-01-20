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

package com.klinec.gradle.javacard.extension

import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory
import org.gradle.util.ClosureBackedAction

import javax.inject.Inject

/**
 * scripts used to configure apdu batch request from a gradle task.
 *
 * @author Bertrand Martel
 */
abstract class Scripts {

    /**
     * list of scripts.
     */
    List<Script> scripts = []

    /**
     * list of tasks.
     */
    List<Task> tasks = []

    Script script(Closure closure) {
        script(ClosureBackedAction.of(closure))
    }

    Script script(Action<Script> action) {
        def scriptInst = objectFactory.newInstance(Script)
        action.execute(scriptInst)
        scripts.add(scriptInst)
        scriptInst
    }

    Task task(Closure closure) {
        task(ClosureBackedAction.of(closure))
    }

    Task task(Action<Task> action) {
        def taskInt = objectFactory.newInstance(Task)
        action.execute(taskInt)
        tasks.add(taskInt)
        taskInt
    }

    @Inject
    abstract ObjectFactory getObjectFactory()
}