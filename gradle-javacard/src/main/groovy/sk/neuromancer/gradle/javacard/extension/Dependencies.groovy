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

package sk.neuromancer.gradle.javacard.extension

import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory
import org.gradle.util.ClosureBackedAction

import javax.inject.Inject

/**
 * Cap extension object (the same as defined in https://github.com/martinpaljak/ant-javacard#syntax
 *
 * @author Bertrand Martel
 */
abstract class Dependencies {

    /**
     * list of local exp/jar dependencies
     */
    List<Import> local = []

    /**
     * list of remote exp/jar dependencies
     */
    List<String> remote = []

    void remote(String remote) {
        this.remote.add(remote)
    }

    Import local(Closure closure) {
        local(ClosureBackedAction.of(closure))
    }

    Import local(Action<Import> action) {
        def someLocal = objectFactory.newInstance(Import)
        action.execute(someLocal)
        local.add(someLocal)
        return someLocal
    }

    @Inject
    abstract ObjectFactory getObjectFactory()
}