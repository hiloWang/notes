package com.ztiany.producer_consumer;

import java.util.concurrent.locks.*;

class Resource {

    private String name;
    private int count = 1;
    private boolean flag = false;

    //	创建一个锁对象。
    private final Lock lock = new ReentrantLock();

    //通过已有的锁获取两组监视器，一组监视生产者，一组监视消费者。
    private final Condition producer_con = lock.newCondition();
    private final Condition consumer_con = lock.newCondition();

    public void set(String name) {
        lock.lock();
        try {
            while (flag)
                try {
                    producer_con.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            this.name = name + count;//烤鸭1  烤鸭2  烤鸭3
            count++;
            System.out.println(Thread.currentThread().getName() + "...生产者5.0..." + this.name);
            flag = true;
            consumer_con.signal();
        } finally {
            lock.unlock();
        }

    }

    public void out() {
        lock.lock();
        try {
            while (!flag)
                try {
                    consumer_con.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            System.out.println(Thread.currentThread().getName() + "...消费者.5.0......." + this.name);//消费烤鸭1
            flag = false;
            producer_con.signal();
        } finally {
            lock.unlock();
        }
    }
}

class Producer implements Runnable {
    private Resource r;

    Producer(Resource r) {
        this.r = r;
    }

    public void run() {
        while (true) {
            r.set("烤鸭");
        }
    }
}

class Consumer implements Runnable {
    private Resource r;

    Consumer(Resource r) {
        this.r = r;
    }

    public void run() {
        while (true) {
            r.out();
        }
    }
}


public class Sample2 {

    public static void main(String[] args) {
        Resource r = new Resource();
        Producer pro = new Producer(r);
        Consumer con = new Consumer(r);

        Thread t0 = new Thread(pro);
        Thread t1 = new Thread(pro);
        Thread t2 = new Thread(con);
        Thread t3 = new Thread(con);
        t0.start();
        t1.start();
        t2.start();
        t3.start();

    }
}

