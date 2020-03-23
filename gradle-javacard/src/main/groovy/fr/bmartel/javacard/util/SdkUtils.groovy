package fr.bmartel.javacard.util

import org.slf4j.Logger

import java.nio.file.Paths
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

import pro.javacard.JavaCardSDK
import pro.javacard.JavaCardSDK

/**
 * Javacard SDK utils taken from ant-javacard(https://github.com/martinpaljak/ant-javacard) by Martin Paljak
 */
class SdkUtils {

    static class JavaCardKit {
        JavaCardSDK.Version version = JavaCardSDK.Version.NONE
        String path = null
        JavaCardSDK sdk = null
    }

    /**
     * Get api classpath depending on SDK version
     *
     * @param extensionKit path to JavaCard SDK
     * @return api jar classpath
     */
    static getApiPath(extensionKit, Logger logger) {
        def jckit = SdkUtils.detectSDK(extensionKit, logger)
        def jars = jckit.sdk.getApiJars()
        return jars.isEmpty() ? null : jars.get(0)
    }

    /**
     * Given a path, return a meta-info object about possible JavaCard SDK in that path.
     *
     * @param path raw string as present in build.xml or environment, or <code>null</code>
     *
     * @return a {@link JavaCardKit} instance
     */
    static JavaCardKit detectSDK(String path, Logger logger) {
        JavaCardKit detected = new JavaCardKit()
        if (path == null || path.trim() == "") {
            return detected
        }

        // Expand user
        String real_path = path.replaceFirst("^~", System.getProperty("user.home"))

        // Check if path is OK
        if (!new File(real_path).exists()) {
            logger.info("JavaCard SDK folder " + path + " does not exist!")
            return detected
        }

        detected.sdk = JavaCardSDK.detectSDK(real_path)
        detected.version = detected.sdk?.version
        detected.path = real_path

        // Identify jckit type
        if (!detected.sdk) {
            logger.info("Could not detect a JavaCard SDK in " + Paths.get(path).toAbsolutePath())
        }
        return detected
    }
}