package me.ztiany.jcipg.chapter06;

import java.util.concurrent.CountDownLatch;

public class AllocatorTester {

    // 测试转账的main方法
    public static void main(String[] args) throws InterruptedException {

        Account src = new Account(10000);
        Account target = new Account(10000);

        CountDownLatch countDownLatch = new CountDownLatch(9999);
        for (int i = 0; i < 9999; i++) {
            new Thread(() -> {
                src.transactionToTarget(1, target);
                countDownLatch.countDown();
            }).start();
        }

        countDownLatch.await();
        System.out.println("src=" + src.getBalance());
        System.out.println("target=" + target.getBalance());

    }

}