package me.ztiany.jc36l.jvm;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryManagerMXBean;
import java.util.List;

/**
 * @author ztiany
 * Email: ztiany3@gmail.com
 */
public class MemoryMain {

    public static void main(String... args) {
        System.out.println("Runtime.getRuntime().freeMemory() = " + (Runtime.getRuntime().freeMemory() * 1.0 / 1024/1024));
        System.out.println("Runtime.getRuntime().maxMemory() = " + (Runtime.getRuntime().maxMemory() * 1.0 / 1024/1024));
        System.out.println("Runtime.getRuntime().totalMemory() = " + (Runtime.getRuntime().totalMemory() * 1.0 / 1024/1024));

        List<MemoryManagerMXBean> memoryManagerMXBeans = ManagementFactory.getMemoryManagerMXBeans();
        for (MemoryManagerMXBean memoryManagerMXBean : memoryManagerMXBeans) {
            System.out.println(memoryManagerMXBean.getName());
        }
    }

}
