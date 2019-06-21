package chapter6.concurrentutils;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * CountDownLatch犹如倒计时器 ,调用CountDownLatch对象的countdown方法就将计数器减1 ,当计数到达0时 ,则所有等待者或单个等待者开始执行
 */
public class CountDownLatchDemo {

    public static void main(String args[]) {

        //线程池
        ExecutorService service = Executors.newCachedThreadPool();
        //倒计时器
        final CountDownLatch mainOrder = new CountDownLatch(1);
        final CountDownLatch subAnswer = new CountDownLatch(3);


        for (int x = 0; x < 3; x++) {
            service.execute(new Runnable() {
                public void run() {
                    try {
                        System.out.println("将军:" + Thread.currentThread().getName() + "正在等待命定");
                        Thread.sleep(new Random().nextInt(10000));
                        mainOrder.await();
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    System.out.println("将军" + Thread.currentThread().getName() + "正在执行任务");
                    try {
                        Thread.sleep(new Random().nextInt(10000));
                        System.out.println("将军" + Thread.currentThread().getName() + "执行任务完毕  上报了任务结果");
                        subAnswer.countDown();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        try {
            System.out.println("国王 即将发布命令");
            mainOrder.countDown();
            System.out.println("国王 已经发布命令");
            System.out.println("国王 在等待命定的执行结果");
            subAnswer.await();
            System.out.println("国王 收到了将军们的命定的执行结果  万事大吉！！！");
        } catch (Exception e) {
            e.printStackTrace();
        }
        service.shutdown();
    }
}
