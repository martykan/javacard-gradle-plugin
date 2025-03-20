package sk.neuromancer.gradle.javacard.utils

import sk.neuromancer.gradle.javacard.util.Utility

class TestUtils {

    public static File getFile(ext) {
        return new File(ext)
    }

    public static String getFileName(filePath, ext) {
        return Utility.removeExtension(filePath) + "." + ext
    }

    public static boolean isInt(String v){
        try {
            Integer.parseInt(v)
            return true
        } catch (Exception ignored){
            return false
        }
    }

    public static int compareVersionElem(String v1, String v2){
        if (isInt(v1) && isInt(v2)){
            return Integer.parseInt(v1) - Integer.parseInt(v2)
        } else {
            return v1.numberAwareCompareTo(v2)
        }
    }

    public static int compareJdk(String version1, String version2) {
        def arr1 = version1.split("\\.") as String[]
        def arr2 = version2.split("\\.") as String[]
        if (arr1[0] == '1') arr1 = arr1[(1..-1)]
        if (arr2[0] == '1') arr2 = arr2[(1..-1)]
        int i=0

        while(i<arr1.size() || i<arr2.size()){
            if(i<arr1.size() && i<arr2.size()){
                def x = compareVersionElem(arr1[i], arr2[i])
                if (x != 0) {
                    return x
                }
            } else if(i<arr1.size()){
                if (compareVersionElem(arr1[i], "0") != 0){
                    return 1
                }
            } else if(i<arr2.size()){
                if (compareVersionElem(arr2[i], "0") != 0){
                    return -1
                }
            }

            i++;
        }
        return 0
    }
}