package me.ztiany.jc36l.jmm;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.lang.Thread.sleep;

/**
 * @author ztiany
 * Email: ztiany3@gmail.com
 */
public class VolatileGuard {

    private Map<String, String> configOptions;
    private char[] configText;
    private volatile boolean initialized = false;

    public static void main(String... args) {
        VolatileGuard volatileGuard = new VolatileGuard();
        volatileGuard.threadRead();
        volatileGuard.threadWrite();
    }


    private void threadRead() {

        new Thread(() -> {
            while (!initialized) {
                try {
                    sleep(1000);
                    System.out.println("------------------------------------------");
                    System.out.println("configText: " + Arrays.toString(configText));
                    System.out.println("configOptions: " + configOptions);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void threadWrite() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                // Thread A
                configOptions = new LinkedHashMap<>();
                configText = readConfigFile();
                processConfigOptions(configText, configOptions);
                initialized = true;
            }
        }.start();
    }

    private void processConfigOptions(char[] configText, Map<String, String> configOptions) {
        for (int i = 0; i < 100; i++) {
            System.out.println("add " + i);
            configOptions.put(String.valueOf(i), String.valueOf(Math.pow(i, 2)));
        }
    }


    private char[] readConfigFile() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "ABCD".toCharArray();
    }

}
