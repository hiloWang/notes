package chapter5;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class UsingLock {

    private static Lock lock = new ReentrantLock();

    public static void main(String... args) {
        testLock();
    }

    private static void testLock() {
        System.out.println("MainThread run");
        System.out.println("MainThread get Lock");
        lock.lock();


        Thread thread = startThread();

        try {
            Thread.sleep(3000);
            System.out.println("interrupt Thread  ");
            thread.interrupt();
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
            System.out.println("MainThread release Lock");
        }

    }

    private static Thread startThread() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                System.out.println("Thread run");
                System.out.println("Thread blocked");
                lock.lock();
                try {
                    System.out.println("Thread free thread state:" + Thread.currentThread().isInterrupted());
                } finally {
                    lock.unlock();
                    System.out.println("Thread release Lock");
                    System.out.println("Thread end");
                }
            }
        };
        thread.start();
        return thread;
    }
}
