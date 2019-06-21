package com.ztiany.interrupt;

import java.io.IOException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.5.9 20:14
 */
public class InterruptSample {

    private static final Object SIGNAL = new Object();
    private static final Lock LOCK = new ReentrantLock();

    private static Condition condition;

    static {
        condition = LOCK.newCondition();
    }

    public static void main(String... args) {
        //testSleep();
        //testWait();
        testLock();
        //testLockAwait();
        //testSynchronized();
        //testIO();
    }

    private static void testLock() {
        Thread thread = new Thread(new LockRunnable());
        LOCK.lock();
        thread.start();
        try {
            Thread.sleep(3000);
            thread.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            LOCK.unlock();
        }
    }

    private static void testSynchronized() {
        Thread thread = new Thread(new SynchronizedRunnable());

        synchronized (SIGNAL) {
            thread.start();
            try {
                Thread.sleep(3000);
                thread.interrupt();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void testIO() {
        Thread thread = new Thread(new IORunnable());
        thread.start();
        try {
            Thread.sleep(3000);
            thread.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void testLockAwait() {
        Thread thread = new Thread(new LockAwaitRunnable());
        thread.start();
        try {
            Thread.sleep(3000);
            thread.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /*LOCK.lock();
        try {
            System.out.println();
        } finally {
            condition.signal();
            LOCK.unlock();
        }*/
    }

    private static void testWait() {
        Thread thread = new Thread(new SynchronizedWaitRunnable());
        thread.start();
        try {
            Thread.sleep(3000);
            thread.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

      /*  synchronized (SIGNAL) {
            SIGNAL.notify();
        }*/
    }

    private static void testSleep() {
        Thread thread = new Thread(new SleepRunnable());
        thread.start();
        thread.interrupt();
    }


    /**
     * 当被中断后，会立即抛出异常，然后继续执行
     */
    private static class SleepRunnable implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("SleepRunnable continue");
                    break;
                }
            }
        }
    }

    /**
     * 当被中断后，会立即抛出异常，然后继续执行
     */
    private static class SynchronizedWaitRunnable implements Runnable {

        @Override
        public void run() {
            synchronized (SIGNAL) {
                try {
                    SIGNAL.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("SynchronizedWaitRunnable continue");
            }
        }
    }


    private static class SynchronizedRunnable implements Runnable {

        @Override
        public void run() {
            synchronized (SIGNAL) {

            }
            System.out.println("SynchronizedRunnable continue");
            System.out.println(Thread.interrupted());
        }
    }


    private static class LockRunnable implements Runnable {

        @Override
        public void run() {
            LOCK.lock();
            try {
                System.out.println("LockRunnable continue");
                System.out.println(Thread.interrupted());
            } finally {
                LOCK.unlock();
            }
        }
    }

    /**
     * 当被中断后，会立即抛出异常，然后继续执行
     */
    private static class LockAwaitRunnable implements Runnable {

        @Override
        public void run() {
            while (true) {
                LOCK.lock();
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("LockAwaitRunnable continue");
                    break;
                } finally {
                    LOCK.unlock();
                }
            }
        }
    }

    private static class IORunnable implements Runnable {

        @Override
        public void run() {
            byte[] buff = new byte[1024];
            while (true) {
                try {
                    int read = System.in.read(buff);
                    System.out.println(read);
                    if (read == 1) {//直接回车退出
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(Thread.interrupted());//true
        }
    }
}
