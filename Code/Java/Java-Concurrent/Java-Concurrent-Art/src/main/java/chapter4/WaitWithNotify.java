package chapter4;

import java.util.Date;

/**
 * 等待/通知模式
 */
public class WaitWithNotify {


    public static void main(String[] args) {
        new Thread(new WaitRunnable(),"WaitRunnable").start();
        new Thread(new NotifyRunnable(),"NotifyRunnable").start();
    }

    private static boolean flag = true;
    private static final Object sObject = new Object();

    public static class WaitRunnable implements Runnable {
        @Override
        public void run() {
            synchronized (sObject) {
                while (flag) {
                    try {
                        System.out.println("flag is true   " + Thread.currentThread() + "   wait @" + new Date());
                        sObject.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("flag is false   " + Thread.currentThread() + "   running @" + new Date());

            }
        }
    }

    public static class NotifyRunnable implements Runnable {
        @Override
        public void run() {
            synchronized (sObject) {
                System.out.println(Thread.currentThread() + "   hold lock , notify@" + new Date());
                sObject.notifyAll();
                flag = false;
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            synchronized (sObject) {
                System.out.println(Thread.currentThread() + "   hold lock again , sleep@" + new Date());
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
