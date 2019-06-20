package me.ztiany.asm.agent.visitor;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

/**
 * @author ztiany
 * Email: ztiany3@gmail.com
 */
public class ClassVisitorFactory {

    public static ClassVisitor createVisitor(String agentArgs, ClassWriter classWriter) {
        return new RemoveMethodAdapter(classWriter, "setAge", "(I)V");
    }

}
