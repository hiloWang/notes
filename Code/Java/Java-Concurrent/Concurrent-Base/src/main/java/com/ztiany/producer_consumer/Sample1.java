package com.ztiany.producer_consumer;

/**
 * @author Ztiany
 *         Email ztiany3@gmail.com
 *         Date 18.3.18 0:02
 */
public class Sample1 {

    private static int PRODUCT = 0;
    private static int sCustomerCount = 0;
    private static int sProducerCount = 0;
    private static final Object LOCK = new Object();

    public static void main(String... args) {
        new Thread(new Customer()).start();
        new Thread(new Producer()).start();
    }

    private static class Customer implements Runnable {

        @Override
        public void run() {

            for (; ; ) {
                synchronized (LOCK) {
                    while (PRODUCT <= 0) {
                        try {
                            LOCK.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    PRODUCT--;
                    sCustomerCount--;
                    System.out.println("总共消费产品：" + sCustomerCount);
                    LOCK.notifyAll();
                }
            }
        }
    }

    private static class Producer implements Runnable {

        @Override
        public void run() {
            for (; ; ) {

                synchronized (LOCK) {

                    while (PRODUCT >= 100) {
                        try {
                            LOCK.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    PRODUCT++;
                    sProducerCount++;
                    LOCK.notifyAll();
                }
                System.out.println("总共生产产品：" + sProducerCount);

            }
        }
    }
}

