# 1 CountDownLatch

CountDownLatch允许一个或多个线程等待其他线程完成操作。CountDownLatch犹如倒计时器 ,调用CountDownLatch对象的countdown方法就将计数器减1 ,当计数到达0时 ,则所有等待者或单个等待者开始执行

使用示例：

```java
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
```

# 2 CyclicBarrier

CyclicBarrier允许一组线程互相等待，直到到达某个公共屏障点 (common barrier point)。CyclicBarrier可以用于多线程计算数据，最后合并计算结果的场景。

除了默认的构造函数之外，CyclicBarrier还提供一个接受Runable对象的构造函数，改Runnable会在所有线程达到屏障时被优先执行。这个Runnable一般执行在最后一个达到屏障的线程中。

### CyclicBarrier与CountDownLatch的区别

CountDownLatch只能使用一次，而CyclicBarrier的计数器可以使用reset()方法重置，所以CyclicBarrier能处理更为复杂的业务场景。

### CyclicBarrier的相关方法

- `getNumberWating()`方法可以获取CyclicBarrier阻塞的线程数量。
- `isBroken()`方法用来判断阻塞的线程是否被中断。

示例：
```java
    private static void testCyclicBarrier() {
            //创建缓存线程池
            ExecutorService pool = Executors.newCachedThreadPool();
            //可循环的障碍
            final CyclicBarrier c = new CyclicBarrier(3, new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName());
                }
            });
    
            for (int x = 0; x < 3; x++) {
                pool.execute(new Runnable() {
                    public void run() {
                        try {
                            Thread.sleep(new Random().nextInt(10000));
                            System.out.println(Thread.currentThread().getName() + "到达了目的1号点   目前有： " + (c.getNumberWaiting() + 1) +
                                    (c.getNumberWaiting() == 2 ? "到都了 终于可以走啦" : "个线程等待中...."));
                            c.await();
                        } catch (InterruptedException | BrokenBarrierException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
```

# 3 Semaphore

Semaphore可以用于控制同时访问资源的线程数量。例如：实现一个文件的并发访问，而单个信号灯可以实现互斥锁的功能 ，并且可以由一个线程获得锁 再由另外一个线程释放锁。从概念上讲，信号量维护了一个许可集。如有必要，在许可可用前会阻塞每一个` acquire()`，然后再获取该许可。每个` release()` 添加一个许可，从而可能释放一个正在阻塞的获取者。


# 4 Exchanger

Exchanger交换者，用于线程间进行数据交换。示例如下：

```java
    public class ExchangerDemo {
        public static void main(String args[]) {
    
            //    线程池
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
```