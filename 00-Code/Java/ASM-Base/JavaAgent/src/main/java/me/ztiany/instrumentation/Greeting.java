package me.ztiany.instrumentation;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

/**
 * @author ztiany
 * Email: ztiany3@gmail.com
 */
public class Greeting implements ClassFileTransformer {

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        System.out.println("load->" + className);
        return null;
    }

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.printf("     I've been called with options: \"%s\"\n", agentArgs);
        inst.addTransformer(new Greeting());
    }

}
