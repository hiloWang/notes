package chapter6.concurrentutils;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 一个同步辅助类，它允许一组线程互相等待，直到到达某个公共屏障点 (common barrier point)。
 */
public class CyclicBarrierDemo {
    public static void main(String args[]) {
        testCyclicBarrier();

    }


    private static void testCyclicBarrier() {
        //创建缓存线程池
        ExecutorService pool = Executors.newCachedThreadPool();
        //可循环的障碍
        final CyclicBarrier c = new CyclicBarrier(3, new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName());
            }
        });

        for (int x = 0; x < 3; x++) {
            pool.execute(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(new Random().nextInt(10000));
                        System.out.println(Thread.currentThread().getName() + "到达了目的1号点   目前有： " + (c.getNumberWaiting() + 1) +
                                (c.getNumberWaiting() == 2 ? "到都了 终于可以走啦" : "个线程等待中...."));
                        c.await();
                    } catch (InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
