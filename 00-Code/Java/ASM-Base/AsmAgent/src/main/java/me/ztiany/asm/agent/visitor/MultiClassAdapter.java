package me.ztiany.asm.agent.visitor;

import org.objectweb.asm.ClassVisitor;

import static jdk.internal.org.objectweb.asm.Opcodes.ASM5;

/**
 * 一个转换链并不一定是线性的。可以写一个ClassVisitor去转发所有的函数，也可以使用多个ClassVisitor在同一时间接受调用
 */
public class MultiClassAdapter extends ClassVisitor {

    protected final ClassVisitor[] cvs;

    public MultiClassAdapter(ClassVisitor[] cvs) {
        super(ASM5);
        this.cvs = cvs;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        for (ClassVisitor cv : cvs) {
            cv.visit(version, access, name, signature, superName, interfaces);
        }
    }
}