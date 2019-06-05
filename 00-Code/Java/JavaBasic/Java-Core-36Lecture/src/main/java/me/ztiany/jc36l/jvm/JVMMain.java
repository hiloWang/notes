package me.ztiany.jc36l.jvm;

import java.util.Arrays;

/**
 * @author ztiany
 * Email: ztiany3@gmail.com
 */
public class JVMMain {

    /*启动参数：
             java -XX:NativeMemoryTracking=summary -XX:+UnlockDiagnosticVMOptions -XX:+PrintNMTStatistics
    */
    public static void main(final String[] args) throws InterruptedException {
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                System.out.println(Arrays.toString(args));
                try {
                    Thread.sleep(10 * 60 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
        thread.join();
    }

}
