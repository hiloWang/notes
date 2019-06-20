package chapter5;

import java.util.concurrent.locks.LockSupport;


public class UsingLockSupport {

    private static Object blocker = new Object();

    public static void main(String... args) {

        Thread thread = startThread();


        try {
            Thread.sleep(1000 * 3);
            thread.interrupt();
            Thread.sleep(1000 * 3);
//            LockSupport.unpark(thread);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static Thread startThread() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                System.out.println("Thread run");
                System.out.println("Thread blocked");
                LockSupport.park(blocker);
                System.out.println("Thread free thread state:"+Thread.currentThread().isInterrupted());
                System.out.println("Thread end");
            }
        };
        thread.start();

        return thread;
    }

}
