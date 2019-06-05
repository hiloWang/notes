package chapter6.concurrentutils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 *
 * 信号灯 可以维护当前访问的线程个数 ，并提供了同步机制 ，使用semaphore可以控制同时会访问资源的线程个数
 * 例如：实现一个文件的并发访问，单个信号灯可以实现互斥锁的功能 ，并且可以由一个线程获得锁 再由另外一个线程释放锁。
 */
public class SemaphoreDemo {
    public static void main(String args[]) {

        ExecutorService pool = Executors.newCachedThreadPool();
        final Semaphore s = new Semaphore(3);
        for (int x = 0; x < 10; x++) {
            pool.execute(new Runnable() {
                public void run() {
                    System.out.println(Thread.currentThread().getName() + "准备进入");
                    try {
                        s.acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + "is come in" + "已经进入  现在有" + (3 - s.availablePermits()) + "个线程获取信号");
                    try {
                        Thread.sleep((long) (Math.random() * 10000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + "准备离开");
                    s.release();
                    System.out.println(Thread.currentThread().getName() + "已经 离开" + "当前有" + (3 - s.availablePermits()) + "个线程正在执行任务");

                }
            });
        }
        System.out.println("ten task was submit");
    }
}
