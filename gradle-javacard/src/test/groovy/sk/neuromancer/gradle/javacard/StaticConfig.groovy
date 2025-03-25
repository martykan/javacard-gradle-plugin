/*
 * The MIT License (MIT)
 * <p/>
 * Copyright (c) 2017-2018 Bertrand Martel
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

package sk.neuromancer.gradle.javacard

import sk.neuromancer.gradle.javacard.utils.TestUtils

/**
 * Static javacard configuration used in tests.
 *
 * @author Bertrand Martel
 */
class StaticConfig {

    public static String SDK_PATH = getDefaultSdkPath()

    /*
    public static String DEPENDENCY_PATH = System.getProperty("user.dir") + "/../../../test/libs/test.jar"
    public static String EXP_PATH = System.getProperty("user.dir") + "/../../../test/libs/test.exp"
    */

    public static repositories = {
        maven {
            url 'https://mvn.javacard.pro/maven'
        }
        maven {
            url 'https://deadcode.me/mvn'
        }
    }

    public static String getDefaultSdkPath() {
        String version = System.getProperty("java.version")
        if (TestUtils.compareJdk(version, "1.9") < 0) {
            return getSdkPath("jc222_kit")
        } else if (TestUtils.compareJdk(version, "12") < 0) {
            return getSdkPath("jc305u3_kit")
        } else if (TestUtils.compareJdk(version, "18") < 0) {
            return getSdkPath("jc320v24.0_kit")
        } else {
            return getSdkPath("jc320v24.0_kit")
        }
    }

    public static String getSdkPath(folder) {
        return System.getProperty("user.dir") + "/../sdks/" + folder
    }

    public static Closure VALID_CONFIG = {
        config {
            jckit SDK_PATH
            cap {
                jckit SDK_PATH
                packageName 'fr.bmartel.javacard'
                version '0.1'
                aid '01:02:03:04:05:06:07:08:09'
                output 'applet.cap'
                applet {
                    className 'fr.bmartel.javacard.HelloWorld'
                    aid '01:02:03:04:05:06:07:08:09:01:02'
                }
                dependencies {
                    remote 'org.globalplatform:javacard:1.5'
                }
            }
        }

        test {
            dependencies {
                compile 'junit:junit:4.12'
                compile 'com.klinec:jcardsim:3.0.6.0'
            }
        }

        key {
            enc '40:41:42:43:44:45:46:47:48:49:4A:4B:4C:4D:4E:4F'
            dek '40:41:42:43:44:45:46:47:48:49:4A:4B:4C:4D:4E:4F'
            mac '40:41:42:43:44:45:46:47:48:49:4A:4B:4C:4D:4E:4F'
        }
    }

    public static Closure VALID_CONFIG_MIXED_KEY = {
        config {
            jckit SDK_PATH
            cap {
                jckit SDK_PATH
                packageName 'fr.bmartel.javacard'
                version '0.1'
                aid '01:02:03:04:05:06:07:08:09'
                output 'applet.cap'
                applet {
                    className 'fr.bmartel.javacard.HelloWorld'
                    aid '01:02:03:04:05:06:07:08:09:01:02'
                }
                dependencies {
                    remote 'org.globalplatform:javacard:1.5'
                }
            }
        }

        defaultKey '40:41:42:43:44:45:46:47:48:49:4A:4B:4C:4D:4E:4F'

        key {
            enc '41:41:42:43:44:45:46:47:48:49:4A:4B:4C:4D:4E:4F'
            dek '42:41:42:43:44:45:46:47:48:49:4A:4B:4C:4D:4E:4F'
        }
    }

    public static Closure VALID_SCRIPT_CONFIG = {
        config {
            jckit SDK_PATH
            cap {
                jckit SDK_PATH
                packageName 'fr.bmartel.javacard'
                version '0.1'
                aid '01:02:03:04:05:06:07:08:09'
                output 'applet.cap'
                applet {
                    className 'fr.bmartel.javacard.HelloWorld'
                    aid '01:02:03:04:05:06:07:08:09:01:02'
                }
                dependencies {
                    remote 'org.globalplatform:javacard:1.5'
                }
            }
        }

        scripts {
            script {
                name 'script1'
                apdu '010203'
            }
            script {
                name 'script2'
                apdu '040506'
            }
            task {
                name 'task1'
                scripts 'script1', 'script2'
            }
            task {
                name 'task2'
                scripts 'script1'
            }
            task {
                name 'task3'
                scripts 'script2'
            }
        }
    }

    public static Closure VALID_SCRIPT_CONFIG_DEFAULT_KEY = {

        defaultKey '40:41:42:43:44:45:46:47:48:49:4A:4B:4C:4D:4E:4F'

        config {
            jckit SDK_PATH
            cap {
                jckit SDK_PATH
                packageName 'fr.bmartel.javacard'
                version '0.1'
                aid '01:02:03:04:05:06:07:08:09'
                output 'applet.cap'
                applet {
                    className 'fr.bmartel.javacard.HelloWorld'
                    aid '01:02:03:04:05:06:07:08:09:01:02'
                }
                dependencies {
                    remote 'org.globalplatform:javacard:1.5'
                }
            }
        }

        scripts {
            script {
                name 'script1'
                apdu '010203'
            }
            script {
                name 'script2'
                apdu '040506'
            }
            task {
                name 'task1'
                scripts 'script1', 'script2'
            }
            task {
                name 'task2'
                scripts 'script1'
            }
            task {
                name 'task3'
                scripts 'script2'
            }
        }
    }

    public static Closure VALID_SCRIPT_CONFIG_WITH_KEY = {
        key {
            enc '40:41:42:43:44:45:46:47:48:49:4A:4B:4C:4D:4E:4F'
            dek '40:41:42:43:44:45:46:47:48:49:4A:4B:4C:4D:4E:4F'
            mac '40:41:42:43:44:45:46:47:48:49:4A:4B:4C:4D:4E:4F'
        }
        config {
            jckit SDK_PATH
            cap {
                jckit SDK_PATH
                packageName 'fr.bmartel.javacard'
                version '0.1'
                aid '01:02:03:04:05:06:07:08:09'
                output 'applet.cap'
                applet {
                    className 'fr.bmartel.javacard.HelloWorld'
                    aid '01:02:03:04:05:06:07:08:09:01:02'
                }
                dependencies {
                    remote 'org.globalplatform:javacard:1.5'
                }
            }
        }

        scripts {
            script {
                name 'script1'
                apdu '010203'
            }
            script {
                name 'script2'
                apdu '040506'
            }
            task {
                name 'task1'
                scripts 'script1', 'script2'
            }
            task {
                name 'task2'
                scripts 'script1'
            }
            task {
                name 'task3'
                scripts 'script2'
            }
        }
    }

    public static Closure RUNNABLE_SCRIPT_CONFIG = {
        config {
            jckit SDK_PATH
            cap {
                jckit SDK_PATH
                packageName 'fr.bmartel.javacard'
                version '0.1'
                aid '01:02:03:04:05:06:07:08:09'
                output 'applet.cap'
                applet {
                    className 'fr.bmartel.javacard.HelloWorld'
                    aid '01:02:03:04:05:06:07:08:09:01:02'
                }
                dependencies {
                    remote 'org.globalplatform:javacard:1.5'
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
                name 'task1'
                scripts 'select', 'hello'
            }
        }
    }

    public static Closure SIMPLE_CONFIG = {
        config {
            jckit SDK_PATH
            cap {
                jckit SDK_PATH
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
    }

    public static Closure FULL_OUTPUT = {
        config {
            jckit SDK_PATH
            cap {
                jckit SDK_PATH
                packageName 'fr.bmartel.javacard'
                version '0.1'
                aid '01:02:03:04:05:06:07:08:09'
                output 'applet.cap'
                export 'other.exp'
                jca 'someother.jca'
                applet {
                    className 'fr.bmartel.javacard.HelloWorld'
                    aid '01:02:03:04:05:06:07:08:09:01:02'
                }
                dependencies {
                }
            }
        }
    }

    public static Closure MULTIPLE_APPLETS = {
        config {
            cap {
                jckit SDK_PATH
                packageName 'fr.bmartel.javacard'
                version '0.1'
                aid '01:02:03:04:05:06:07:08:09'
                output 'applet.cap'
                applet {
                    className 'fr.bmartel.javacard.HelloWorld'
                    aid '01:02:03:04:05:06:07:08:09:01:02'
                }
                applet {
                    className 'fr.bmartel.javacard.HelloWorld2'
                    aid '01:02:03:04:05:06:07:08:09:01:03'
                }
                dependencies {
                }
            }
        }
    }

    public static Closure MULTIPLE_CAPS = {
        config {
            cap {
                jckit SDK_PATH
                packageName 'fr.bmartel.javacard'
                version '0.1'
                aid '01:02:03:04:05:06:07:08:09'
                output 'applet.cap'
                applet {
                    className 'fr.bmartel.javacard.HelloWorld'
                    aid '01:02:03:04:05:06:07:08:09:01:02'
                }
                dependencies {
                }
            }
            cap {
                jckit SDK_PATH
                packageName 'fr.bmartel.javacard'
                version '0.1'
                aid '01:02:03:04:05:06:07:08:09'
                output 'applet2.cap'
                applet {
                    className 'fr.bmartel.javacard.HelloWorld'
                    aid '01:02:03:04:05:06:07:08:09:01:02'
                }
                dependencies {
                }
            }
        }
    }

    public static Closure UNDEFINED_JCKIT_PATH = {
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
                dependencies {
                }
            }
        }
    }

    public static Closure INVALID_JCKIT_PATH = {
        config {
            cap {
                jckit 'path/to'
                packageName 'fr.bmartel.javacard'
                version '0.1'
                aid '01:02:03:04:05:06:07:08:09'
                output 'applet.cap'
                applet {
                    className 'fr.bmartel.javacard.HelloWorld'
                    aid '01:02:03:04:05:06:07:08:09:01:02'
                }
                dependencies {
                }
            }
        }
    }

    public static Closure OUTPUT_REQUIRED = {
        config {
            cap {
                jckit SDK_PATH
                packageName 'fr.bmartel.javacard'
                version '0.1'
                aid '01:02:03:04:05:06:07:08:09'
                applet {
                    className 'fr.bmartel.javacard.HelloWorld'
                    aid '01:02:03:04:05:06:07:08:09:01:02'
                }
                dependencies {
                }
            }
        }
    }

    public static Closure APPLET_CLASSNAME_REQUIRED = {
        config {
            cap {
                jckit SDK_PATH
                packageName 'fr.bmartel.javacard'
                version '0.1'
                aid '01:02:03:04:05:06:07:08:09'
                applet {
                    aid '01:02:03:04:05:06:07:08:09:01:02'
                }
                dependencies {
                }
            }
        }
    }

    public static Closure MISSING_ALL_CAPS = {
        config {
            cap {

            }
        }
    }

    public static Closure NO_FIELDS = {
        config {

        }
    }
}
