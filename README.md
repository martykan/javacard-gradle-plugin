# JavaCard Gradle plugin

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/sk.neuromancer/gradle-javacard/badge.svg)](https://maven-badges.herokuapp.com/maven-central/sk.neuromancer/gradle-javacard)
[![License](http://img.shields.io/:license-mit-blue.svg)](LICENSE.md)

A Gradle plugin for building JavaCard applets.

This plugin is a wrapper on [ant-javacard](https://github.com/martinpaljak/ant-javacard) and [Global Platform Pro](https://github.com/martinpaljak/GlobalPlatformPro), it is inspired by [gradle-javacard](https://github.com/fidesmo/gradle-javacard)

This is a fork of a great work [bertrandmartel/javacard-gradle-plugin](https://github.com/bertrandmartel/javacard-gradle-plugin),
and the extension of [ph4r05/javacard-gradle-plugin](https://github.com/ph4r05/javacard-gradle-plugin) with some fixes.

Gradle 6.4 is the minimal version supported.

## Features

* build JavaCard applets (with the same capabilities as [ant-javacard](https://github.com/martinpaljak/ant-javacard))
* install cap files
* list applets
* write quick testing scripts used to send apdu in a configurable way
* expose `GpExec` task type that enables usage of [Global Platform Pro](https://github.com/martinpaljak/GlobalPlatformPro) tool inside Gradle
* include [jcardsim 3.0.6.0](https://github.com/licel/jcardsim) and [JUnit 4.12](http://junit.org/junit4/) test dependency (clear distinction between JavaCard SDK & jcardsim SDK) 
* ability to specify key for delete/install/list tasks
* possibility to add dependency between modules (exp & jar imported automatically)

## Usage 

It may be the best to start with a simple HelloWorld project using this gradle plugin and demonstrating basic usage.

Clone this [https://github.com/crocs-muni/javacard-gradle-template-edu](https://github.com/crocs-muni/javacard-gradle-template-edu)
template project with `git clone --recursive`, and try:
- `./gradlew buildJavaCard`
- `./gradlew installJavaCard`
- `./gradlew test`

Example gradle script:
```groovy
buildscript {
    repositories {
        jcenter()
        mavenCentral()

        // Repository with Globalplatform, ant-javacard, gppro, gptools, etc.
        maven { url "https://mvn.javacard.pro/maven" }
        maven { url "https://deadcode.me/mvn" }
    }
    dependencies {
        classpath 'sk.neuromancer:gradle-javacard:1.8.1'
    }
}

apply plugin: 'sk.neuromancer.gradle.javacard'

repositories {
    mavenCentral()
    // mavenLocal() // for local maven repository if needed

    // Repository with Globalplatform, ant-javacard, gppro, gptools, etc.
    maven { url "https://mvn.javacard.pro/maven" }
    maven { url "https://deadcode.me/mvn" }
}

javacard {

    config {
        
        cap {
            packageName 'fr.bmartel.javacard'
            version '0.1'
            aid '01:02:03:04:05:06:07:08:09'
            output 'applet.cap'
            applet {
                className 'fr.bmartel.javacard.HelloWorld'
                aid '01:02:03:04:05:06:07:08:09:01:02'
            }
        }
    }
          
    scripts {
        script {
            name 'select'
            apdu '00:A4:04:00:0A:01:02:03:04:05:06:07:08:09:01:00'
        }
        script {
            name 'hello'
            apdu '00:40:00:00:00'
        }
        task {
            name 'sendHello'
            scripts 'select', 'hello'
        }
    }
}
```

plugin is available from `mavenCentral()`

You can specify custom GPtool dependency with configuration `gptool`:

```groovy
dependencies {
  gptool "com.github.martinpaljak:gppro:24.10.15"
  gptool "com.github.martinpaljak:gptool:24.10.15"
  gptool "com.github.martinpaljak:globalplatformpro:24.10.15"
}
```

Check [this project](https://github.com/bertrandmartel/javacard-tutorial) for more usage examples

## JavaCard SDK path

The path to JavaCard SDK can be specified through : 

* use `jc.home` properties in `local.properties` file located in your project root (in the same way as Android projects) : 
  * in project root : `echo "jc.home=$PWD/oracle_javacard_sdks/jc222_kit" >> local.properties`
* using `jckit` attribute (see [ant-javacard](https://github.com/martinpaljak/ant-javacard#syntax))
* `JC_HOME` global environment variable, for instance using : `export JC_HOME="$PWD/sdks/jck222_kit"`

## Tasks

| task name    | description   |
|--------------|---------------|
| buildJavaCard | build JavaCard cap files |
| installJavaCard | delete existing aid & install all JavaCard cap files (`gp --delete XXXX --install file.cap`) |
| listJavaCard | list applets (`gp -l`) |

It's possible to create custom tasks that will send series of custom apdu :

```groovy
scripts {
    script {
        name 'select'
        apdu '00:A4:04:00:0A:01:02:03:04:05:06:07:08:09:01:00'
    }
    script {
        name 'hello'
        apdu '00:40:00:00:00'
    }
    task {
        name 'sendHello'
        scripts 'select', 'hello'
    }
}
```

The above will create task `sendHello` that will select applet ID `01:02:03:04:05:06:07:08:09:01` and send the apdu `00:40:00:00:00`.  
The order of the scripts's apdu in `task.scripts` is respected.  
`00:A4:04:00:0A:01:02:03:04:05:06:07:08:09:01:00` or `'00A404000A0102030405060708090100'` are valid apdu.

## Custom Global Platform Pro task

You can build custom tasks that launch [Global Platform Pro](https://github.com/martinpaljak/GlobalPlatformPro) tool :

```groovy
task displayHelp(type: sk.neuromancer.gradle.javacard.gp.GpExec) {
    description = 'display Global Platform pro help'
    group = 'help'
    args '-h'
}
```

## More complex example

```groovy
apply plugin: 'javacard'

repositories {
    maven {
        url 'http://dl.bintray.com/bertrandmartel/maven'
    }
}

javacard {

    config {
        jckit '../oracle_javacard_sdks/jc222_kit'
        cap {
            packageName 'fr.bmartel.javacard'
            version '0.1'
            aid '01:02:03:04:05:06:07:08:09'
            output 'applet1.cap'
            applet {
                className 'fr.bmartel.javacard.HelloSmartcard'
                aid '01:02:03:04:05:06:07:08:09:01:02'
            }
            applet {
                className 'fr.bmartel.javacard.GoodByeSmartCard'
                aid '01:02:03:04:05:06:07:08:09:01:03'
            }
        }
        cap {
            packageName 'fr.bmartel.javacard'
            version '0.1'
            aid '01:02:03:04:05:06:07:08:0A'
            output 'applet2.cap'
            applet {
                className 'fr.bmartel.javacard.SomeOtherClass'
                aid '01:02:03:04:05:06:07:08:09:01:04'
            }
            dependencies {
                local {
                    jar '/path/to/dependency.jar'
                    exps '/path/to/expfolder'
                }
                remote 'fr.bmartel:gplatform:2.1.1'
            }
        }
    }
    
    defaultKey '40:41:42:43:44:45:46:47:48:49:4A:4B:4C:4D:4E:4F'
    // or 
    /*
    key {
        enc '40:41:42:43:44:45:46:47:48:49:4A:4B:4C:4D:4E:4F'
        kek '40:41:42:43:44:45:46:47:48:49:4A:4B:4C:4D:4E:4F' 
        mac '40:41:42:43:44:45:46:47:48:49:4A:4B:4C:4D:4E:4F' 
    }
    */

    scripts {
        script {
            name 'select'
            apdu '00:A4:04:00:0A:01:02:03:04:05:06:07:08:09:01:00'
        }
        script {
            name 'hello'
            apdu '00:40:00:00:00'
        }
        task {
            name 'sendHello'
            scripts 'select', 'hello'
        }
    }
}
```

Note1 : the `remote` dependency will automatically download the `jar` (the `jar` file must include the `exp` file)  
Note2 : you can add as many `local` or `remote` dependency as you want

## Syntax

* javacard [Closure]
  * config [Closure] - object that holds build configuration **Required**
    * jckit [String] - path to the JavaCard SDK that is used if individual cap does not specify one. Optional if cap defines one, required otherwise. The path is relative to the module
    * logLevel [String] - log level of ant-javacard task ("VERBOSE","DEBUG","INFO","WARN","ERROR"). default : "INFO"
    * antClassPath [String] - option to specify path to the `ant-javacard.jar`
    * jcardSim [dependency] - gradle dependency specifier for the JCardSim to use
    * gptoolVersion [String] - enables you to specify GPtools version that will be used (in case you do not specify custom deps for `gptool` configuration)
    * addImplicitJcardSim [Boolean] - adds JCardSim dependency to the project, true by default. Recommended: false
    * addImplicitJcardSimJunit [Boolean] - adds Junit dependency to the jcardsim dependency. Recommended: false and define you own junit version
    * debugGpPro [Boolean] - if true, adds `-d` param to the GPPro tasks for more verbose output
    * fixClassPath [Boolean] - if true, tries to fix test classpath if JC `api_classic.jar` appears before jcardsim.
    * installGpProArgs [List<String>] - List of additional GPPro args to add to the installJavaCard task
    * cap [Closure] - construct a CAP file **Required**
      * jckit [String] - path to the JavaCard SDK to be used for this CAP. *Optional if javacard defines one, required otherwise*
      * targetsdk [String] - path to the target JavaCard SDK to be used for this CAP. Optional, value of jckit used by default. Allows to use a more recent converter to target older JavaCard platforms.
      * sources [String] - path to Java source code, to be compiled against the current JavaCard SDK. **Required**
      * sources2 [String] - additional sources to build per-platform applets. Optional.
      * findSources [boolean] - default:true, if true the sources are determined automatically. The first existing source dir in source sets is taken
      * defaultSources [boolean] - default:true, if true the first source dir from the source set is used. Otherwise the most recet (last)
      * classes [String] - path to pre-compiled class files to be assembled into a CAP file. If both classes and sources are specified, compiled class files will be put to classes folder, which is created if missing
      * includes [String] - comma or space separated list of patterns of files that must be included.
      * excludes [String] - comma or space separated list of patterns of files that must be excluded.
      * packageName [String] - name of the package of the CAP file. Optional - set to the parent package of the applet class if left unspecified.
      * version [String] - version of the package. Optional - defaults to 0.0 if left unspecified.
      * aid [String] - AID (hex) of the package. Recommended - or set to the 5 first bytes of the applet AID if left unspecified.
      * output [String] - path where to save the generated CAP file. if a filename or a non-absolute path is referenced, the output will be in `build/javacard/{output}` **Required**
      * export [String] - path (folder) where to place the JAR and generated EXP file. Default output directory is `build/javacard`. Filename depends on `output` filename if referenced. Optional.
      * jar [String] - path where to save the generated archive JAR file. Optional.
      * jca [String] - path where to save the generated JavaCard Assembly (JCA) file. Default output directory is `build/javacard`. Filename depends on `output` filename if referenced. Optional.
      * verify [boolean] - if set to false, disables verification of the resulting CAP file with offcardeverifier. Optional.
      * debug [boolean] - if set to true, generates debug CAP components. Optional.
      * ints [boolean] - if set to true, enables support for 32 bit int type. Optional.
      * javaversion [string] - override the Java source and target version. Optional.
      * applet [Closure] - for creating an applet inside the CAP
        * className [String] - class of the Applet where install() method is defined. **Required**
        * aid [String] - AID (hex) of the applet. Recommended - or set to package aid+i where i is index of the applet definition in the build.xml instruction
      * dependencies [Closure] - for linking against external components/libraries, like GPSystem or OPSystem
        * local [Closure] local dependencies must include absolute path to exp/jar
          * exps [String] - path to the folder keeping .exp files. Required
          * jar [String] - path to the JAR file for compilation. Optional - only required if using sources mode and not necessary with classes mode if java code is already compiled
        * remote [String] remote dependencies (ex: "group:module:1.0").the remote repository (maven repo) must be included in the project
  * key [Closure] key configuration (if not defined the default keys will be used)
    * enc [String] ENC key
    * kek [String] KEK key
    * mac [String] MAC key
  * defaultKey [String] default key used (will be used for enc, kek and mac key if not specified in key closure)
  * scripts [Closure] - object that holds the configurable scripts to send apdu
     * script [Closure] - a script referenced by name/apdu value to be sent
       * name [String] - script name (ex: select)
       * apdu [String] - apdu value to be sent (it can hold ":" to separate bytes)
     * task [Closure] - gradle task to create that will map the specified list of apdu to send
       * name [String] - task name
       * scripts [String...] - list of script's name
  * test [Closure] - additional configuration for tests(*)
     * dependencies [Closure] - holds test dependencies
       * compile [String] - add a dependencies (ex: 'junit:junit:4.12')

(*) If you specify at least one dependency, jcardsim & junit won't be automatically added so you will need to add them manually if you need them for example :

```groovy
test {
    dependencies {
        compile 'junit:junit:4.12'
        compile 'com.klinec:jcardsim:3.0.6.0'
    }
}
```

## Compatibility

This plugin has been tested on following IDE : 

* IntelliJ IDEA
* Android Studio
* Eclipse

Recommended IDE : IntelliJ IDEA or Android Studio

## License

The MIT License (MIT) Copyright (c) 2017-2018 Bertrand Martel
