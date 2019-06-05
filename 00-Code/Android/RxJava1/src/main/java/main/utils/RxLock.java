package main.utils;

public class RxLock {


    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void lock() {
        synchronized (RxLock.class) {
            try {
                RxLock.class.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void lock(long time) {
        synchronized (RxLock.class) {
            try {
                RxLock.class.wait(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
