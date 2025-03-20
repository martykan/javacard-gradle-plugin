package sk.neuromancer.gradle.javacard.extension

class TestDependencies {

    /**
     * list of dependencies to compile.
     */
    List<Object> dependencies = []

    void compile(Object dependency) {
        dependencies.add(dependency)
    }

    void implementation(Object dependency) {
        dependencies.add(dependency)
    }
}
