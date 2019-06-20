package chapter4;

/**
 * 线程中断演示
 */
public class InterceptThread  {

    public static void main(String[] args) {
        Thread sleepThread = new Thread(new SleepRunnable(),"sleepThread");
        Thread busyThread = new Thread(new BusyRunnable(),"busyThread");
        sleepThread.setDaemon(true);
        busyThread.setDaemon(true);
        sleepThread.start();
        busyThread.start();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        sleepThread.interrupt();
        busyThread.interrupt();

        System.out.println("sleepThread isInterrupted "+sleepThread.isInterrupted());
        System.out.println("busyThread isInterrupted "+busyThread.isInterrupted());
    }


    static class SleepRunnable implements Runnable{
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println(Thread.currentThread() + "--e:" + e);
                }
            }
        }
    }

    static class BusyRunnable implements Runnable{
        @Override
        public void run() {
            while (true) {

            }
        }
    }
}
