package chapter4.threadpool;

import java.util.Random;

public class ThreadPoolTest {

    private static ThreadPool<Runnable> sRunnableThreadPool = new DefaultThreadPool<Runnable>(3);
    private static final Random RANDOM = new Random();

    public static void main(String[] args) throws InterruptedException {
        Thread.currentThread().setPriority(10);
        for (int i = 0; i < 100; i++) {
            sRunnableThreadPool.execute(new Job());
        }
        System.out.println(sRunnableThreadPool.getJobSize());
        sRunnableThreadPool.addWorkers(4);
        Thread.sleep(5000);
        System.out.println(sRunnableThreadPool.getJobSize());
        Thread.sleep(5000);
        sRunnableThreadPool.removeWorker(6);
        Thread.sleep(5000);
        System.out.println(sRunnableThreadPool.getJobSize());
        sRunnableThreadPool.addWorkers(8);
    }

    public static class Job implements Runnable{
        @Override
        public void run() {
            try {
                int time = RANDOM.nextInt(5000);
                Thread.sleep(time);
                System.out.println("Job done ,use time = "+time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
