package com.ztiany.basic.reference;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ztiany
 * Email: ztiany3@gmail.com
 */
public class ReferenceQueueMain {

    private static ReferenceQueue<byte[]> rq = new ReferenceQueue<>();
    private static final int _1M = 1024 * 1024;

    public static void main(String... args) {
        Object value = new Object();
        Map<Object, Object> map = new HashMap<>();

        startMonitor();

        for (int i = 0; i < 10000; i++) {
            byte[] bytes = new byte[_1M];
            //在对象被GC的同时，会把该对象的包装类即weakReference放入到ReferenceQueue里面
            //PhantomReference<byte[]> reference = new PhantomReference<>(bytes, rq);
            WeakReference<byte[]> reference = new WeakReference<>(bytes, rq);
            map.put(reference, value);
        }

        System.out.println("map.size->" + map.size());
    }

    @SuppressWarnings("unchecked")
    private static void startMonitor() {
        Thread thread = new Thread(() -> {

            try {
                int cnt = 0;
                Reference<? extends byte[]> remove;

                while ((remove = rq.remove()) != null) {
                    System.out.println((cnt++) + "回收了:" + remove);//null
                }

            } catch (InterruptedException e) {
                //结束循环
            }
        });
        thread.setDaemon(true);
        thread.start();
    }


}
