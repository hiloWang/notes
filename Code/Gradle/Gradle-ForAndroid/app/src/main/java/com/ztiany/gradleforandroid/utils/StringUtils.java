package com.ztiany.gradleforandroid.utils;

/**
 * <br/>    Description  :
 * <br/>    Email    : ztiany3@gmail.com
 *
 * @author Ztiany
 *         <p>
 *         Date : 2016-11-23 23:17
 */

public class StringUtils {
    private StringUtils() {

    }

    public static int getLength(String string) {
        return string == null ? 0 : string.toCharArray().length;
    }
}
