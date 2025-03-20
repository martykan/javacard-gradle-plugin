package sk.neuromancer.gradle.javacard.extension

import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory
import org.gradle.util.ClosureBackedAction

import javax.inject.Inject

abstract class Test {

    TestDependencies dependencies

    void dependencies(Closure closure) {
        dependencies(ClosureBackedAction.of(closure))
    }

    void dependencies(Action<TestDependencies> action) {
        def dependency = objectFactory.newInstance(TestDependencies)
        action.execute(dependency)
        dependencies = dependency
        dependency
    }

    @Inject
    abstract ObjectFactory getObjectFactory()
}
