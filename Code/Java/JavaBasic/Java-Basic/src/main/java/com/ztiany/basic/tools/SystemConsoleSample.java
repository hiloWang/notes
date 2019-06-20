package com.ztiany.basic.tools;

/**
 * console工具
 *
 * @author Ztiany
 *         Date : 2016-12-10 11:27
 */
public class SystemConsoleSample {


    public static void main(String... args) {
        char[] chars = System.console().readPassword();
        System.out.println(new String(chars));
    }
}
