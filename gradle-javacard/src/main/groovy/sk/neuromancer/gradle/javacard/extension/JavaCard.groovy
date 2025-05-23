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
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.util.ClosureBackedAction

import javax.inject.Inject

/**
 * JavaCard extension object (the same as defined in https://github.com/martinpaljak/ant-javacard#syntax
 *
 * @author Bertrand Martel
 */
abstract class JavaCard {

    Config config

    Scripts scripts

    Key key

    String defaultKey

    Test test

    Config config(Closure closure) {
        config(ClosureBackedAction.of(closure))
    }

    Config config(Action<Config> action) {
        def someConfig = objectFactory.newInstance(Config)
        action.execute(someConfig)
        config = someConfig
        return someConfig
    }

    Scripts scripts(Closure closure) {
        scripts(ClosureBackedAction.of(closure))
    }

    Scripts scripts(Action<Scripts> action) {
        def someScript = objectFactory.newInstance(Scripts)
        action.execute(someScript)
        scripts = someScript
        return someScript
    }

    Test test(Closure closure) {
        test(ClosureBackedAction.of(closure))
    }

    Test test(Action<Test> action) {
        def someTest = objectFactory.newInstance(Test)
        action.execute(someTest)
        test = someTest
        return someTest
    }

    Key key(Closure closure) {
        key(ClosureBackedAction.of(closure))
    }

    Key key(Action<Key> action) {
        def someKey = objectFactory.newInstance(Key)
        action.execute(someKey)
        key = someKey
        return someKey
    }

    void defaultKey(String key) {
        this.defaultKey = key
    }

    /**
     * Validate fields
     */
    def validate(Project project) {
        config.validate(project)
    }

    @Inject
    abstract ObjectFactory getObjectFactory()
}