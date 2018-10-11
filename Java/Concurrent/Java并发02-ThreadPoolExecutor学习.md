# ThreadPoolExecutor学习

---
## 1 线程池的优点

当我们需要频繁的创建线程进行耗时任务操作，每次都通过`new Thread`实现并不是一种好的方式，频繁的线程对象创建和销毁导致性能较差，线程缺乏统一的管理，可能无限制的创建新的线程，相互之间的竞争，可能占用过多的系统资源导致死锁，并且缺乏定时执行，定期执行，线程中断等功能。在实际的开发中我们应该使用线程池技术。线程池的优点总结如下：

- **重用线程池中的线程**，避免因为线程的创建和销毁锁带来的开销
- **能有效的控制最大并发数**，避免线程之间因相互抢占系统资源而导致的阻塞现象
- **能够对线程进行简单的管理**，并提供定时执行以及指定间隔循环执行等功能
- **提高响应速度**，当任务到达时，任务可以不需要等到线程创建就能立即执行
- **提高系统稳定性**，线程是有限资源，使用线程池可以防止因为线程的无限制创建，导致系统的不稳定性

线程池原理就是创建多个线程进行统一的管理，通过对提交的任务进行管理，由线程池通过分配给线程池中的线程去执行，通过线程池对线程的统一调度，管理使得多线程的使用变得简单、高效、安全。

### 线程池的使用标准

不要对那些同步等待其他任务结果的任务排队，这可能会导致死锁，即线程有可能会出现**相互等待**的情况，在死锁中，所有线程都被一些任务占用，但是任务一直不会被完成

### 理解任务

有效的调整线程池的大小，需要了解正在排队的任务以及他们正在做什么。

- 他们是cpu限制的么
- 他们是I/O限制的么

### 调整线程池的大小

避免线程池过大或过小

- 一般对于计算类型任务线程池大小应该接近处理器数目
- 对于阻塞的I/O任务应该让线程数目超过处理器数目，因为并不是所有的线程都一直在工作。

---
## 2 线程池的接口——ThreadPoolExecutor

ThreadPoolExecutor是线程池中的真正实现，ThreadPoolExecutor实现了ExecutorService接口，ExecutorService可以说是一个执行器服务，提供了对任务管理(执行、终止)的服务

### 构造方法参数说明

```java
        public ThreadPoolExecutor(int corePoolSize,
                            int maximumPoolSize,
                            long keepAliveTime,
                            TimeUnit unit,
                            BlockingQueue<Runnable> workQueue) {}


        public ThreadPoolExecutor(int corePoolSize,
                                  int maximumPoolSize,
                                  long keepAliveTime,
                                  TimeUnit unit,
                                  BlockingQueue<Runnable> workQueue,
                                  ThreadFactory threadFactory,
                                  RejectedExecutionHandler handler) {}
```

ThreadPoolExecutor的构造方法参数比较长，下面对各个参数进行说明

### corePoolSize

线程池核心线程数，默认情况下，核心线程会在线程池中一致存活，即使他们处于闲置状态，如果将ThreadPoolExecutor.allowCoreThreadTimeOut的值置为true，那么限制的核心线程在等待新任务到来前会有超时策略，这个时间间隔有keepAliveTime指定，当超过keepAliveTime所指定的时候前还没有新的任务到来，核心线程将被终止。

### maximumPoolSize

线程池所能容纳的最大线程数，当活动线程数这个数值之后，后续的新任务将会被阻塞。

###  keepAliveTime、unit

非核心线程闲置时的超时时间，超过这个时间，非核心将会被回收，如果ThreadPoolExecutor.allowCoreThreadTimeOut的值为true，这个值同样将作用于核心线程。
unit用于指定keepAliveTime参数的单位时间。

### workQueue

workQueue是一个阻塞队列，线程池中的任务，通过线程池的execute方法提交的Runnable对象会被存储这个参数中。

一般可选的阻塞队列如下：
- ArrayBlockingQueue：一个基于数组的有界队列
- LinkedBlockingQueue：一个基于链表结构的阻塞队列，吞吐量高于ArrayBlockQueue
- SynchronousQueue：一个不存储元素的阻塞队列，每个插入操作必须等到另一个线程调用移除操作，吞吐量高于LinkedBlockingQueue。
- ProrityBlockingQueue：一个具有优先级的无限阻塞队列。

###  threadFactory

线程池工厂，为了线程池提供创建线程的功能。

### handler（可选参数)

RejectedExecutionHandler是一个可选参数，当线程池的无法执行新的任务时，这可能是由于任务队列已满或者是无法成功执行任务，这个时候ThreadPoolExecutor会调用handler的rejectedExecution方法，来通知调用者，默认情况下handler抛出一个RejectedExecutionException异常，ThreadPoolExecutor为RejectedExecutionHandler提供了一下一个可选值：
 - AbortPolicy：默认值，抛出RejectedExecutionException异常
 - CallerRunsPolicy：用调用者的线程，执行新的任务，如果任务执行是有严格次序的
 - DiscardOldestPolicy：丢弃最旧的任务，处理网络报文时，可以使用此任务，因为报文处理是有时效的，超过时效的，都必须丢弃
 - DiscardPolicy：静默丢弃任务，不通知调用者，在处理网络报文时，可以使用此任务，静默丢弃没有几乎处理的报文

当然，我们也可以实现自己的RejectedExecutionHandler


---
## 3 ThreadPoolExecutor的执行规则

1. 如果线程池中的线程数量未达到核心线程的数量，直接启动一个核心线程来执行任务
2. 如果线程池中的线程数量已经达到或者超过核心线程的数量，那么认为会被插入到任务队列中排队等待执行
3. 如果在任务队列满无法在插入，但是这时候线程数并没有达到最大线程数，那么立即启动一个非线程来执行任务
4. 如果步骤3中已经达到线程池的最大线程数规定，那么任务会被拒绝执行，并调用RejectedExecutionHandler的rejectedExecution发通知调用者，默认的RejectedExecutionHandler实现将会抛出异常

下面一个执行规则的简单说明：
>假设队列大小为 10，corePoolSize 为 3，maximumPoolSize 为 6，那么当加入 20 个任务时，执行的顺序就是这样的：首先执行任务 1、2、3，然后任务 4~13 被放入队列。这时候队列满了，任务 14、15、16 会被马上执行，而任务 17~20 则会抛出异常。最终顺序是：1、2、3、14、15、16、4、5、6、7、8、9、10、11、12、13。

---
## 4 线程池的状态

下面是ThreadPoolExecutor中定义的几种线程池的状态常量

```
     // runState is stored in the high-order bits
        private static final int RUNNING    = -1 << COUNT_BITS;
        private static final int SHUTDOWN   =  0 << COUNT_BITS;
        private static final int STOP       =  1 << COUNT_BITS;
        private static final int TIDYING    =  2 << COUNT_BITS;
        private static final int TERMINATED =  3 << COUNT_BITS;
```

这几种状态分为表示为：

###  RUNNING

- 状态说明：线程池处在RUNNING状态时，能够接收新任务，以及对已添加的任务进行处理。
- 状态切换：线程池的初始化状态是RUNNING。换句话说，线程池被一旦被创建，就处于RUNNING状态！
道理很简单，在ctl的初始化代码中(如下)，就将它初始化为RUNNING状态，并且"任务数量"初始化为0。

    private final AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));

### SHUTDOWN

- 状态说明：线程池处在SHUTDOWN状态时，不接收新任务，但能处理已添加的任务。
- 状态切换：调用线程池的shutdown()接口时，线程池由RUNNING -> SHUTDOWN。

### STOP

- 状态说明：线程池处在STOP状态时，不接收新任务，不处理已添加的任务，并且会中断正在处理的任务。
- 状态切换：调用线程池的shutdownNow()接口时，线程池由(RUNNING or SHUTDOWN ) -> STOP。

### TIDYING

- 状态说明：当所有的任务已终止，ctl记录的"任务数量"为0，线程池会变为TIDYING状态。当线程池变为TIDYING状态时，会执行钩子函数terminated()。terminated()在ThreadPoolExecutor类中是空的，若用户想在线程池变为TIDYING时，进行相应的处理；可以通过重载terminated()函数来实现。
- 状态切换：当线程池在SHUTDOWN状态下，阻塞队列为空并且线程池中执行的任务也为空时，就会由 SHUTDOWN -> TIDYING。

当线程池在STOP状态下，线程池中执行的任务为空时，就会由STOP -> TIDYING。

### TERMINATED

- 状态说明：线程池彻底终止，就变成TERMINATED状态。
- 状态切换：线程池处在TIDYING状态时，执行完terminated()之后，就会由 TIDYING -> TERMINATED。

如下图所示：

![](index_files/08000847-0a9caed4d6914485b2f56048c668251a.jpg)

---
## 5 线程池的关闭

ThreadPoolExecutor提供了两个方法，用于线程池的关闭，分别是shutdown()和shutdownNow()，其中：
- shutdown()：不会立即终止线程池，而是要等所有任务缓存队列中的任务都执行完后才终止，但再也不会接受新的任务
- shutdownNow()：立即终止线程池，并尝试打断正在执行的任务，并且清空任务缓存队列，返回尚未执行的任务


## 6 合理的配置线程池

想要合理的配置线程池就要先分析任务的特性，主要从以下几个角度分析：

- 任务的性质：CPU密集型、IO密集型、混合型
- 任务的优先级：高、中、低
- 任务的执行时间：长、中、短
- 任务的依赖性：是否依赖其他系统资源、如数据库连接

性质不同的任务应该使用不同规模的线程池分开处理，CPU密集型任务应该尽可能减少线程数量，如配置`CPU核心+1个线程`的线程池。由于IO密集型任务线程并不是一直在执行任务，则应该配置尽可能多的线程，如`2*CPU`核心数，混合型的任务如果可以拆分，将其拆分为一个CPU密集型和一个IO密集型任务。只要这两个任务执行的时间相差不是太大，那么分解后执行的吞吐量将要与串行执行的吞吐量，但是如果这两个任务执行的时间相差太大则没有必要进行分解。

### 建议使用有界队列

有界队列可以增加系统的稳定性和预警能力，可以根据需要设计的大一些，如几千，而无界队列可能会导致系统资源耗尽。

---
## 7 ThreadPoolExecutor在Android中AsycnTask的使用

```java
     public static final Executor THREAD_POOL_EXECUTOR
                    = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE,
                            TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory);

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final int KEEP_ALIVE = 1;
    private static final BlockingQueue sPoolWorkQueue = new LinkedBlockingQueue<Runnable>(128);
```

可以看到Android的AsyncTask的THREAD_POOL_EXECUTOR的配置还是很合理的：

- 核心线程数为cpu核心数+1
- 最大线程数为cpu核心数的两倍+1
- 核心线程无超时机制，而非核心线程为1s
- 任务队列的容量为128














