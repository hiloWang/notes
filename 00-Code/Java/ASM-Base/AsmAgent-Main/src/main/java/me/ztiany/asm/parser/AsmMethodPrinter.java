package me.ztiany.asm.parser;

import org.objectweb.asm.*;

import java.io.IOException;
import java.util.Arrays;

/**
 * @author ztiany
 * Email: ztiany3@gmail.com
 */
public class AsmMethodPrinter {

    public static void main(String... args) throws IOException {

        ClassReader classReader = new ClassReader("me/ztiany/asm/parser/DemoClass");
        classReader.accept(new PrintClassVisitor(Opcodes.ASM5), 0);
    }

    private static class PrintClassVisitor extends ClassVisitor {

        PrintClassVisitor(int i) {
            super(i);
        }


        @Override
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            super.visit(version, access, name, signature, superName, interfaces);
            System.out.print("PrintClassVisitor.visit: ");
            System.out.println("version = [" + version + "], access = [" + access + "], name = [" + name + "], signature = [" + signature + "], superName = [" + superName + "], interfaces = [" + Arrays.toString(interfaces) + "]");
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            return new PrintMethodVisitor(Opcodes.ASM5);
        }

        @Override
        public void visitEnd() {
            System.out.println("PrintClassVisitor.visitEnd");
            super.visitEnd();
        }
    }


    private static class PrintMethodVisitor extends MethodVisitor{

        PrintMethodVisitor(int api) {
            super(api);
        }

        @Override
        public void visitCode() {
            super.visitCode();
            System.out.println("-------------------------------------");
            System.out.println("    PrintMethodVisitor.visitCode");
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
            super.visitMethodInsn(opcode, owner, name, desc, itf);
            System.out.print("      PrintMethodVisitor.visitMethodInsn: ");
            System.out.println("opcode = [" + opcode + "], owner = [" + owner + "], name = [" + name + "], desc = [" + desc + "], itf = [" + itf + "]");
        }

        @Override
        public void visitInsn(int opcode) {
            super.visitInsn(opcode);
            System.out.print("      PrintMethodVisitor.visitInsn:");
            System.out.println("        opcode = [" + opcode + "]");
        }

        @Override
        public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
            System.out.print("      PrintMethodVisitor.visitLocalVariable: ");
            System.out.println("        name = [" + name + "], desc = [" + desc + "], signature = [" + signature + "], start = [" + start + "], end = [" + end + "], index = [" + index + "]");
        }

        @Override
        public void visitEnd() {
            super.visitEnd();
            System.out.println("-------------------------------------");
        }
    }
}
