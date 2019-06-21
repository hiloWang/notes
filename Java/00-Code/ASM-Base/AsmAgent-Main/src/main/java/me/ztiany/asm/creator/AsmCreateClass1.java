package me.ztiany.asm.creator;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.PrintWriter;

import static jdk.internal.org.objectweb.asm.Opcodes.*;

/**
 * 类的创建：构建一个新的类
 */
public class AsmCreateClass1 {

    public static void main(String... args) throws ClassNotFoundException {

        final byte[] aClass = createClass();

        Class exampleClass = new ClassLoader() {
            @SuppressWarnings("unchecked")
            protected Class findClass(String name) {
                return defineClass(name, aClass, 0, aClass.length);
            }
        }.loadClass("pkg.Comparable");

        System.out.println(exampleClass);
    }

    private static byte[] createClass() {

        /*
        package pkg;
        public interface Comparable  {
            int LESS = -1;
            int EQUAL = 0;
            int GREATER = 1;
            int compareTo(Object o);
        }
         */

        ClassWriter cw = new ClassWriter(0);
        PrintWriter printWriter = new PrintWriter(System.out);
        TraceClassVisitor cv = new TraceClassVisitor(cw, printWriter);

        cv.visit(V1_5, ACC_PUBLIC + ACC_ABSTRACT + ACC_INTERFACE,
                "pkg/Comparable",
                null,
                "java/lang/Object",
                null);

        cv.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "LESS", "I",
                null, -1).visitEnd();
        cv.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "EQUAL", "I",
                null, 0).visitEnd();
        cv.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "GREATER", "I",
                null, 1).visitEnd();

        /*添加一个方法*/
        cv.visitMethod(
                ACC_PUBLIC + ACC_ABSTRACT,
                "compareTo",
                "(Ljava/lang/Object;)I",
                null,
                null)
                .visitEnd();

        cv.visitEnd();

        return cw.toByteArray();
    }

}

