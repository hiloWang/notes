package me.ztiany.javassist.runtime;

import javassist.*;

public class Main2 {

    public static void main(String[] args) throws Throwable {
        Translator t = new TestTranslator();
        ClassPool pool = ClassPool.getDefault();
        Loader cl = new Loader();
        cl.addTranslator(pool, t);
        cl.run("me.ztiany.javassist.runtime.APP2", args);
    }

}