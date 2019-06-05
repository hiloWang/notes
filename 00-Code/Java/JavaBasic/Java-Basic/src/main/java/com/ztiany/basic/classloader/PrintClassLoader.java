package com.ztiany.basic.classloader;

import java.net.URL;
import java.net.URLClassLoader;

import sun.misc.Launcher;

/**
 * @author Ztiany
 *         Email ztiany3@gmail.com
 *         Date 17.8.13 16:12
 */
class PrintClassLoader {

    public static void main(String... args) {

        //打印AppClassLoader
        ClassLoader appClassLoader = PrintClassLoader.class.getClassLoader();
        URL[] appUrLs = ((URLClassLoader) appClassLoader).getURLs();
        System.out.println("AppClassLoader=====================================================");
        System.out.println("AppClassLoader    " + appClassLoader);
        System.out.println("getSystemClassLoader    "+ClassLoader.getSystemClassLoader());
        for (URL appUrL : appUrLs) {
            System.out.println("url " + appUrL);
        }

        //打印ExtClassLoader
        System.out.println();
        System.out.println("ExtClassLoader=====================================================");
        ClassLoader extraClassLoader = appClassLoader.getParent();
        URL[] extraUrLs = ((URLClassLoader) extraClassLoader).getURLs();
        System.out.println("ExtraClassLoader    " + extraClassLoader);
        for (URL appUrL : extraUrLs) {
            System.out.println("url " + appUrL);
        }

        //打印BootstrapClassLoader
        System.out.println();
        System.out.println("BootstrapClassLoader=====================================================");
        for (URL url : Launcher.getBootstrapClassPath().getURLs()) {
            System.out.println("url " + url);
        }

    }

}
