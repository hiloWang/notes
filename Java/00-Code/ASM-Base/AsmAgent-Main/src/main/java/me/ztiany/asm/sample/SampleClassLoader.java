package me.ztiany.asm.sample;

import org.objectweb.asm.Opcodes;

public class SampleClassLoader extends ClassLoader implements Opcodes {

    public SampleClassLoader() {
        super();
    }

    public SampleClassLoader(ClassLoader parent) {
        super(parent);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return super.loadClass(name);
    }

    public Class<?> defineClass(String name, byte[] b) {
        return super.defineClass(name, b, 0, b.length);
    }

}