package me.ztiany.asm.agent.visitor;

import org.objectweb.asm.ClassVisitor;

import static org.objectweb.asm.Opcodes.ASM5;


/**
 * 移除了outer和inner类的信息，以及被编译的类的名称（由此产生的类保持完整的功能，因为这些元素仅用于调试目的）
 */
public class RemoveDebugAdapter extends ClassVisitor {

    public RemoveDebugAdapter(ClassVisitor cv) {
        super(ASM5, cv);
    }

    @Override
    public void visitSource(String source, String debug) {
    }

    @Override
    public void visitOuterClass(String owner, String name, String desc) {
    }

    @Override
    public void visitInnerClass(String name, String outerName, String innerName, int access) {
    }

}