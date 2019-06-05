package me.ztiany.asm.agent.visitor;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

/**
 * 修改类的版本
 */
public class ChangeVersionAdapter extends ClassVisitor {

    ChangeVersionAdapter(ClassVisitor cv) {
        super(Opcodes.ASM5, cv);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        cv.visit(Opcodes.V1_5, access, name, signature, superName, interfaces);
    }

}