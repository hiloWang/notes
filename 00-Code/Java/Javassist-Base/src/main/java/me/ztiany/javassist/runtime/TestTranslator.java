package me.ztiany.javassist.runtime;

import javassist.*;

public class TestTranslator implements Translator {

    @Override
    public void start(ClassPool pool) throws NotFoundException, CannotCompileException {
        System.out.println("TestTranslator.start");
    }

    @Override

    public void onLoad(ClassPool pool, String classname) throws NotFoundException, CannotCompileException {
        System.out.println("TestTranslator.onLoad");
        CtClass cc = pool.get(classname);
        cc.setModifiers(Modifier.PUBLIC);
    }

}