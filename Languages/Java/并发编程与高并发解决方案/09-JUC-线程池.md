# 线程池

##  1 线程池简介

### new Thread的弊端

- 每次new Thread 新建对象，性能差
- 线程缺乏统一管理，可能无限制的新建线程，相互竞争，可能占用过多的系统资源导致死机或者OOM（out of memory 内存溢出），单纯的new Thread 不会有这样的问题，但是可能因为程序的 bug 或者设计上的缺陷导致不断 new Thread 造成。
- 缺少更多功能，如更多执行、定期执行、线程中断。

###  线程池的好处

- 重用存在的线程，减少对象创建、消亡的开销，性能好。
- 可有效控制最大并发线程数，提高系统资源利用率，同时可以避免过多资源竞争，避免阻塞。
- 提供定时执行、定期执行、单线程、并发数控制等功能。

---
## 2 线程池核心类-ThreadPoolExecutor

### ThreadPoolExecutor 简介

ThreadPoolExecutor 构建函数共有七个参数，这七个参数配合起来，构成了线程池强大的功能。

- corePoolSize：核心线程数量
- maximumPoolSize：线程最大线程数
- workQueue：阻塞队列，存储等待执行的任务，很重要，会对线程池运行过程产生重大影响
- keepAliveTime：线程没有任务执行时最多保持多久时间终止（当线程中的线程数量大于 corePoolSize 的时候，如果这时没有新的任务提交，核心线程外的线程不会立即销毁，而是等待，直到超过 keepAliveTime）
- unit：keepAliveTime 的时间单位
- threadFactory：线程工厂，用来创建线程，有一个默认的工场来创建线程，这样新创建出来的线程有相同的优先级，是非守护线程、并且设置好了名称
- rejectHandler：当拒绝处理任务时(阻塞队列满)的执行的策略
 - AbortPolicy默认策略直接抛出异常
 - CallerRunsPolicy用调用者所在的线程执行任务
 - DiscardOldestPolicy丢弃队列中最靠前的任务并执行当前任务
 - DiscardPolicy直接丢弃当前任务）

**关于工作队列**：工作队列的类型和边界会对线程池运行过程产生重大影响：当我们提交一个新的任务到线程池，线程池会根据当前池中正在运行的线程数量来决定该任务的处理方式。处理方式有三种：

- 对于 SynchronusQueue，直接切换
- 对于无界队列（LinkedBlockingQueue），能够创建的最大线程数为corePoolSize,这时maximumPoolSize就不会起作用了。当线程池中所有的核心线程都是运行状态的时候，新的任务提交就会放入等待队列中。
- 对于有界队列（ArrayBlockingQueue），最大 maximumPoolSize，能够降低资源消耗，但是这种方式使得线程池对线程调度变的更困难。因为线程池与队列容量都是有限的。所以想让线程池的吞吐率和处理任务达到一个合理的范围，又想使我们的线程调度相对简单，并且还尽可能降低资源的消耗，我们就需要合理的限制这两个数量

**如何合理的配置 ThreadPoolExecutor**：

- 如果想降低资源的消耗包括降低 cpu 使用率、操作系统资源的消耗、上下文切换的开销等等，可以设置一个较大的队列容量和较小的线程池容量，这样会降低线程池的吞吐量。
- 如果我们提交的任务经常发生阻塞，我们可以调整 maximumPoolSize。
 - 如果我们的队列容量较小，我们需要把线程池大小设置的大一些，这样 cpu 的使用率相对来说会高一些。
 - 但是如果线程池的容量设置的过大，而提交的任务过多，但并发量会增加，那么线程之间的调度就是一个需要考虑的问题。这样反而有可能会降低处理任务的吞吐量。

**corePoolSize、maximumPoolSize、workQueue 三者关系**：如果运行的线程数小于 corePoolSize 时，直接创建新线程来处理任务。即使线程池中的其他线程是空闲的。如果运行中的线程数大于 corePoolSize 且小于 maximumPoolSize 时，那么只有当 workQueue 满的时候才创建新的线程去处理任务，否则任务加入到 workQueue 中。如果 corePoolSize 与 maximumPoolSize 是相同的， 那么创建的线程池大小是固定的。这时有新任务提交，当 workQueue 未满时，就把请求放入 workQueue 中。等待空线程从workQueue 取出任务。如果 workQueue 此时也满了，那么就使用另外的拒绝策略参数去执行拒绝策略。

**常用方法：**

- `execute()` 提交任务给线程池执行
- `submit()` 提交任务，返回 Future
- `shutdown()` 关闭线程池，等待任务都执行完
- `shutdownNow()` 关闭线程池，不等待任务执行完，一般很少用
- `getTaskCount()` 线程池已执行和未执行的任务总数
- `getCompleteTaskCount()` 已完成的任务数量
- `getPoolSize()` 线程池当前的线程数量
- `getActiveCount()` 当前线程池中正在执行任务的线程数量

### 线程池状态

线程池状态与各种状态的转换：

![](index_files/e78eaf31-15a7-4abb-bffd-a7c120f94f46.jpg)

- running：能接受新提交的任务，也能处理阻塞队列中的任务
- shutdown：不能处理新的任务，但是能继续处理阻塞队列中任务
- stop：不能接收新的任务，也不处理队列中的任务
- tidying(整洁的)：如果所有的任务都已经终止了，这时有效线程数为 0
- terminated：最终状态

### Executors 提供的程池

- `Executors.newCachedThreadPool()` 创建一个可缓存的线程池，如果线程池的长度超过了处理的需要，可以灵活回收空闲线程。
- `Executors.newFixedThreadPool()` 创建线程数固定的线程池，可以控制线程的最大并发数
- `Executors.newSingleThreadExecutor()` 单一线程的线程池，保证所有任务按指定顺序执行
- `Executors.newScheduledThreadPool()` 支持定时和周期任务执行

### 线程池的合理配置

根据任务类型配置：

- CPU 密集型：参考值 `NCPU + 1`
- IO 密集型：参考值 `2 * NCPU`
- 上面只是参考值，应该使用实践验证配置，然后根据结果合理调整配置

---
## 3 JUC 组件归纳

```java
Tools
    CountDownLatch
    CyclicBarrier
    Semaphore
    Exchanger
    Executors

Locks
    AbstractQueuedSynchronizer
    Lock
    Condition
    LockSupport
    ReentrantLock
    ReadWriteLock
    ReentrantReadWriteLock

Atomic
    AtomicBoolean
    AtomicInteger
    AtomicLong
    AtomicLongArray
    AtomicReference
    AtomicLongFieldUpdater
    LongAdder

Collections
    Queue
        ConcurrentLinkedDeque
        BlockingQueue
            ArrayBlockingQueue
            PriorityBlockingQueue
            SynchronousQueue
            LinkedBlockingQueue
            DelayQueue
        Deque
            ArrayDeque
            IdentityLinkedList
            BlockingDeque
                LinkedBlockingDeque
    CopyOnWriteArrayList
    CopyOnWriteArraySet
    ConcurrentSkipListSet
    ConcurrentMap
        ConcurrentHashMap
        ConcurrentNavigableMap
        ConcurrentSkipListMap

Executor
    Future
    Callable
    Executor
    CompletionExecutor
    RejectedExecutionHandler
    TimeUnit
```


