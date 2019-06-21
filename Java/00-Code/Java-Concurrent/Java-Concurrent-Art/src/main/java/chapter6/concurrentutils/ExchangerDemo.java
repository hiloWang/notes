package chapter6.concurrentutils;

import java.util.Random;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/*
可以在对中对元素进行配对和交换的线程的同步点
 每个线程将条目上的某个方法呈现给 exchange 方法，
 与伙伴线程进行匹配，并且在返回时接收其伙伴的对象。
 Exchanger 可能被视为 SynchronousQueue 的双向形式。
 Exchanger 可能在应用程序（比如遗传算法和管道设计）中很有用。
 */

public class ExchangerDemo {
    public static void main(String args[]) {

        //	线程池
        ExecutorService service = Executors.newCachedThreadPool();

        final Exchanger<String> exchanger = new Exchanger<String>();

        service.execute(new Runnable() {
            public void run() {
                String data1 = "白粉";
                System.out.println("毒贩" + Thread.currentThread().getName() + "正在准备把白粉卖出去");
                try {
                    Thread.sleep(new Random().nextInt(10000));
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                try {
                    String data2 = exchanger.exchange(data1);
                    System.out.println("毒贩" + Thread.currentThread().getName() + "已经白粉卖出去 拿到了" + data2 + "元");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        service.execute(new Runnable() {
            public void run() {
                String data1 = "一百万";
                System.out.println("吸毒者" + Thread.currentThread().getName() + "正在准备去买白粉");
                try {
                    Thread.sleep(new Random().nextInt(10000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    String data2 = exchanger.exchange(data1);
                    System.out.println("吸毒者" + Thread.currentThread().getName() + "花了" + data1 + "元" + "买到了" + data2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
