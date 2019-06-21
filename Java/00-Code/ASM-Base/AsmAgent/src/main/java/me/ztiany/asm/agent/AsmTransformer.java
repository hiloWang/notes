package me.ztiany.asm.agent;

import me.ztiany.asm.agent.visitor.ClassVisitorFactory;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.security.ProtectionDomain;

/**
 * @author ztiany
 * Email: ztiany3@gmail.com
 */
public class AsmTransformer {


    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer, String agentArgs) {
        if (className.startsWith("me/ztiany")) {

            System.out.println("transformer: " + className);

            ClassWriter classWriter = new ClassWriter(0);
            ClassVisitor classVisitor = ClassVisitorFactory.createVisitor(agentArgs, classWriter);

            ClassReader classReader = new ClassReader(classfileBuffer);
            classReader.accept(classVisitor, 0);

            /*最终我们返回的是classWriter的字节码*/
            return classWriter.toByteArray();
        }
        return null;
    }

}
