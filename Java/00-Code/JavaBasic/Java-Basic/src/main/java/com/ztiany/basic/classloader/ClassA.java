package com.ztiany.basic.classloader;

/**
 * @author ztiany
 * Email: ztiany3@gmail.com
 */
public class ClassA {

    public static final String NAME = "name";

    static {
        System.out.println("ClassA static code1 run");
    }
}
