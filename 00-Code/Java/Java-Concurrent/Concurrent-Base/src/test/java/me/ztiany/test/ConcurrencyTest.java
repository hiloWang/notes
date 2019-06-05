package me.ztiany.test;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 并发和单线程执行测试
 *
 * @author tengfei.fangtf
 * @version $Id: ConcurrencyTest.java, v 0.1 2014-7-18 下午10:03:31 tengfei.fangtf Exp $
 */
public class ConcurrencyTest {

    /**
     * 执行次数
     */
    private static final long S_COUNT = 300L;
    private static final long M_COUNT = 100L;
    private static final Random RANDOM = new Random();
    private static int mSInteger = 0;
    private static int mMInteger = 0;

    public static void main(String[] args) throws InterruptedException {
        //并发计算
//        concurrency();//concurrency :1389ms,mMInteger=300
        //单线程计算
        serial();//serial:1456ms,mSInteger=300
    }

    private static void sleep() {
        try {
            Thread.sleep(RANDOM.nextInt(10));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void concurrency() throws InterruptedException {
        long start = System.currentTimeMillis();

        Runnable runnable = () -> {
            for (long i = 0; i < M_COUNT; i++) {
                safeCount();
            }
        };

        Thread thread1 = new Thread(runnable);
        thread1.start();

        Thread thread2 = new Thread(runnable);
        thread2.start();

        Thread thread3 = new Thread(runnable);
        thread3.start();

        thread1.join();
        thread2.join();
        thread3.join();

        long time = System.currentTimeMillis() - start;
        System.out.println("concurrency :" + time + "ms,mMInteger=" + mMInteger);
    }

    private static void serial() {
        long start = System.currentTimeMillis();
        for (long i = 0; i < S_COUNT; i++) {
            singleSafeCount();
        }
        long time = System.currentTimeMillis() - start;
        System.out.println("serial:" + time + "ms,mSInteger=" + mSInteger );
    }

    /**
     * 使用cas实现线程安全计数器
     */
    private static synchronized void safeCount() {
        sleep();
        mMInteger++;
    }

    private static void singleSafeCount() {
        sleep();
        mSInteger++;
    }

}
