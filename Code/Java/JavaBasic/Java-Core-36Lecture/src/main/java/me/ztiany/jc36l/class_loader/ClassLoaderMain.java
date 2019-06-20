package me.ztiany.jc36l.class_loader;

import java.lang.instrument.Instrumentation;

/**
 * @author ztiany
 * Email: ztiany3@gmail.com
 */
public class ClassLoaderMain {

    /*
    使用参数：-XX:+TraceClassLoading -XX:+TraceClassUnloading  打印加载的类型和卸载的类型的信息
     */
    public static void main(String... args) {
        /* 使用 java agent
        Instrumentation instrumentation;
        instrumentation.getAllLoadedClasses()*/
        System.out.println("hha");
    }
}
