package me.ztiany.asm.agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

/**
 * @author ztiany
 * Email: ztiany3@gmail.com
 */
public class AsmAgent implements ClassFileTransformer {

    private final String agentArgs;
    private AsmTransformer asmTransformer = new AsmTransformer();

    private AsmAgent(String agentArgs) {
        this.agentArgs = agentArgs;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        return asmTransformer.transform(loader, className, classBeingRedefined, protectionDomain, classfileBuffer, agentArgs);
    }

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.printf("     I've been called with options: \"%s\"\n", agentArgs);
        inst.addTransformer(new AsmAgent(agentArgs));
    }


}
