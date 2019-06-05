package me.ztiany.asm.creator;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class AsmCreateClass2 extends ClassLoader implements Opcodes {

    public static void main(String[] args) throws IOException {

        // step1：定义一个类
        ClassWriter classWriter = new ClassWriter(0);
        classWriter.visit(V1_8, ACC_PUBLIC, "Example", null, "java/lang/Object", null);

        // step2：生成默认的构造方法
        MethodVisitor methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        // 生成构造方法的字节码指令
        methodVisitor.visitVarInsn(ALOAD, 0);
        //调用父类方法，super();
        methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        //方法返回
        methodVisitor.visitInsn(RETURN);
        //定义方法的 最大栈深度和本地变量表的槽位
        methodVisitor.visitMaxs(1, 1);
        //结束
        methodVisitor.visitEnd();

        // step3：生成main方法
        methodVisitor = classWriter.visitMethod(
                ACC_PUBLIC + ACC_STATIC,
                "main",
                "([Ljava/lang/String;)V",
                null,
                null);
        // 取得一个静态字段将其放入栈，相当于“System.out”。“Ljava/io/PrintStream;”是字段类型的描述，翻译过来相当于：“java.io.PrintStream”类型。在字节码中凡是引用类型均由“L”开头“;”结束表示，中间是类型的完整名称。
        methodVisitor.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        //将字符串“Hello world!”放入栈，此时栈中第一个元素是“System.out”，第二个元素是“Hello Aop”。
        methodVisitor.visitLdcInsn("Hello world!");
        //调用PrintStream类型的“println”方法。签名“(Ljava/lang/String;)V”表示方法需要一个字符串类型的参数，并且无返回值。
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
        //方法返回
        methodVisitor.visitInsn(RETURN);
        //定义方法的最大栈和槽数，表示在执行这个方法期间方法分配的最大空间。
        methodVisitor.visitMaxs(2, 2);
        // 表示方法输出结束。
        methodVisitor.visitEnd();

        //step4：保存
        // 获取生成的的class文件对应的二进制流
        byte[] code = classWriter.toByteArray();
        saveClass(code);
    }

    private static void saveClass(byte[] code) throws IOException {
        // 将二进制流写到本地磁盘上
        FileOutputStream fos = new FileOutputStream("Example.class");
        fos.write(code);
        fos.flush();
        fos.close();

        // 直接将二进制流加载到内存中
        AsmCreateClass2 asmCreateClass2 = new AsmCreateClass2();
        Class<?> example = asmCreateClass2.defineClass("Example", code, 0, code.length);
        try {
            example.getMethods()[0].invoke(null, new Object[]{null});
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
} 