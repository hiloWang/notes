
---
# 1 Lock锁

Lock接口是jdk1.5并发包中提供的一个锁接口，其拥有与synchronized类似的锁功能。

**Lock锁与synchronized的区别**：

- synchronized的锁是隐式的获取与释放的，使用比较方便。但是其将锁的获取与释放固化了
- Lock锁获取与释放是显示的，使用比synchronized稍微负责，但是可操作性更强
- Lock锁支持尝试非阻塞的获取锁
- Lock锁支持能够被中中断的获取锁，获取锁的线程能够响应中断，当线程被中断时，中断异常被抛出，并释放锁
- Lock锁超时获取锁，在指定时间内去获取锁，如果到了时间仍然没有获取，则返回

**Lock使用示例**：

```java
     Lock lock = new ReentrantLock();

     public void method(){
          lock.lock();
           try {
              //do...
           } finally {
                lock.unlock();
           }
     }
```

**注意：**
1. 在finally中释放锁，目的是保证在获取锁之后，最终能够释放锁
2. **不要将获取锁的过程放在try中**，因为如果在获取锁(自定义锁的实现)时发生异常，异常抛出时将会导致锁被**无故的释放**。特别是使用`lockInterruptibly`方法加锁时就必须要这样做，这是为了防止线程在获取锁时被中断后，不必（也不能）释放锁

<br/>Lock的锁的获取与释放的基本操作如下：

方法|作用
--|--
void lock()|获取锁
void lockInterruptibly() throws InterruptedException|可中断的获取锁
boolean tryLock()|尝试非阻塞的获取锁，获取成功返回true，否则返回false
boolean tryLock(long time, TimeUnit unit) throws InterruptedException|超时获取锁，有三种返回情况：1.超时时间被获取到锁，2.超时时间内被中断，3.超时结束，返回false
void unlock()|释放锁
Condition newCondition()|创建等待通知组件。用于实现等待通知模式。



---
# 2 同步队列器

**AbstractQueuedSynchronizer**队列同步器是用来构架锁或者实习其他同步组件的基础框架，它实用一个int成员变量表示同步状态，通过内置的FIFO队列类完成资源获取线程的同步工作。

AbstractQueuedSynchronizer的使用方式是继承，子类通过继承AbstractQueuedSynchronizer并实现其抽象方法来管理同步状态，抽象方法中需要用到以下三个方法，对同步状态进行获取和修改。

- getState() 获取同步状态
- setState(int newState)设置当前同步状态
- compareAndSetState(int newState)使用CAS设置当前同步状态。该方法保证设置状态的原子性

它们能够保证状态的改变是安全的。同步器支持独占或者共享方式去获取锁，并发包中的同步组件都使用其实现。

同步器的设计是基于模板方法模式，使用者需要继承并实现同步器指定的方法，可以实现的方法如下：

>以下方法的默认实现时抛出异常

方法|作用
--|--
boolean tryAcquire(int arg)|独占式的获取同步状态，实现该方法需要查询当前状态并判断当前状态释放符合预期值，然后再进行CAS设置同步状态
boolean tryRelease(int arg)|独占式释放同步状态，等待获取同步状态的线程将有机会获取同步状态
int tryAcquireShared(int arg)|共享式的获取同步状态，返回大于等于0的值时，表示获取成功，反之获取失败
boolean tryReleaseShared(int arg)|共享式的释放锁
boolean isHeldExclusively()|当前同步器释放在独占模式下被线程占用，一般该方法表示是否被当前线程锁独占

实现自定义同步组件时，将会调用同步器提供的模板方法，部分方法如下表所示：

方法|作用
--|--
void acquire(int arg) | 独占式获取同步状态，如果当前线程获取同步状态成功，则由该方法返回，否则将会进入同步队列等待，该方法将会调用实现的tryAcquire(int arg)方法
void acquireInterruptibly(int arg) | 与acquire(int arg)，但是该方法响应中断，当前线程为获取到锁同步状态而进入同步队列中，如果当前线程被中断，该方法将会抛出InterruptedException并返回
boolean tryAcquireNanos(int arg, long nanosTimeout) | 在acquireInterruptibly(int arg)的基础上增加了超时机制
void acquireShared(int arg) | 共享式的获取同状态，如果线程没有获取到同步状态，将会进入同步队列等待，而独占式获取的主要区别是，同一时刻可以有多个线程获取到同步状态
acquireSharedInterruptibly(int arg) | 与 acquireShared(int arg)相同，但是响应中断
boolean tryAcquireSharedNanos(int arg, long nanosTimeout) | acquireSharedInterruptibly(int arg) 的基础上增加了超时机制
boolean release(int arg) | 独占式的释放同步状态，该方法在释放同步状态后，将同步队列中第一个节点包含的线程唤醒。
boolean releaseShared(int arg) | 共享式的释放同步状态
`Collection<Thread> getQueuedThreads()` | 获取等待在同步队列上的线程集合。

同步器提供的模板方法基本上分为三类：
- 独占式的获取与释放锁
- 共享式的获取有释放锁
- 查询同步队列中的等待线程情况

## 一个独占锁的示例

```java
    public class Mutex implements Lock {
    
        private static Mutex mutex = new Mutex();
        private static int value = 0;
    
        public static void main(String... args) {
            testUnSafe(false);
        }
    
        private static void testUnSafe(boolean safe) {
            List<Thread> threadList = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                threadList.add(new Thread() {
                    @Override
                    public void run() {
                        for (int i1 = 0; i1 < 100; i1++) {
                            if (safe) {
                                safeAdd();
                            } else {
                                unSafeAdd();
                            }
                        }
                    }
                });
            }
            threadList.forEach(Thread::start);
            threadList.forEach(thread -> {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
    
            System.out.println(value);
            value = 0;
        }
    
    
        private static void safeAdd() {
            mutex.lock();
            try {
                value++;
            } finally {
                mutex.unlock();
            }
        }
    
        private static void unSafeAdd() {
            value++;
        }
    
    
        private static final int OCCUPY_STATUS = 1;
        private static final int LEISURE_STATUS = 0;
        private final Sync mSync = new Sync();
    
       //释放获取了锁
        public boolean isLock() {
            return mSync.isHeldExclusively();
        }
        
        public boolean hasQueuedThreads() {
            return mSync.hasQueuedThreads();
        }
    
        //获取锁
        @Override
        public void lock() {
            mSync.acquire(OCCUPY_STATUS);//调用acquire方法，这里的OCCUPY_STATUS并没有实际意义
        }
    
        @Override
        public void lockInterruptibly() throws InterruptedException {
            mSync.acquireInterruptibly(OCCUPY_STATUS);
        }
    
        @Override
        public boolean tryLock() {
            return mSync.tryAcquire(OCCUPY_STATUS);
        }
    
        @Override
        public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
            return mSync.tryAcquireNanos(OCCUPY_STATUS, unit.toNanos(time));
        }
    
        @Override
        public void unlock() {
            mSync.release(OCCUPY_STATUS);
        }
    
        @Override
        public Condition newCondition() {
            return mSync.newCondition();
        }
    
    
        private class Sync extends AbstractQueuedSynchronizer {
    
    
            /**
             * 是否处于占用状态,此方法被AbstractQueuedSynchronizer框架调用
             */
            @Override
            protected boolean isHeldExclusively() {
                //这里我们定义当状态为1的时候为占用状态
                return getState() == OCCUPY_STATUS;
            }
    
            /**
             * 当状态为0的时候获取锁，此方法被AbstractQueuedSynchronizer框架调用
             */
            @Override
            protected boolean tryAcquire(int arg) {
                //这里期望同步状态为0(没有线程获取到锁)，如果是期望值则把状态设置为1(表示有线程获取到了锁)，然后调用setExclusiveOwnerThread方法设置当前线程为获取到了锁的线程。并返回true
                //如果不是期望值(表示已经有线程获取到了锁)，则返回false
                if (compareAndSetState(LEISURE_STATUS, OCCUPY_STATUS)) {
                    setExclusiveOwnerThread(Thread.currentThread());
                    return true;
                }
                return false;
            }
    
            /**
             * 释放锁，将状态设置为0，此方法被AbstractQueuedSynchronizer框架调用
             */
            @Override
            protected boolean tryRelease(int arg) {
            //没有获取到锁，不能调用解锁方法
                if (getState() == 0) {
                    throw new IllegalMonitorStateException();
                }
                //这里不需要原子操作，因为只有获取到锁的线程才能执行到这里。
                setExclusiveOwnerThread(null);
                setState(0);
                return true;
            }
    
    
            Condition newCondition() {
                return new ConditionObject();
            }
        }
    }
```

上面实现的独占锁中，使用内部类Sync实现，在tryAcquire(int acquires)方法中，如果经过CAS设置成功(同步状态为1)则代表获取了同步状态，而在tryRelease(int releases)方法将同步状态设置为0.

---
# 3 队列同步器的实现分析

## 同步队列

同步器依赖其内部的同步队列(一个FIFO的双向队列)来完成同步状态的管理。当前线程获取同步状态失败时，同步器会将当前线程以及等待信息构造成为一个节点(Node)并将其加入到同步队列中。

节点的一些属性
- int waitStatus:等待的状态
- Node pre：前驱节点
- Node next：后驱节点
- Node nextWaiter：等待队列中的后驱节点
- Thread thread，获取同步状态的线程

## 独占式同步状态获取式释放

通过调用同步器的arcquire(int arg)方法可以获取同步状态：
```java
     public final void acquire(int arg) {
          //调用自定义同步器的tryAcquire(arg)方法,该方法保证线程安全的获取同步状态
            if (!tryAcquire(arg) && acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
                selfInterrupt();
        }
```
1：调用自定义同步器的tryAcquire(arg)方法,该方法保证线程安全的获取同步状态，比如Mutex实现的：
```java
    protected boolean tryAcquire(int arg) {
                    //如果不是期望值(表示已经有线程获取到了锁)，则返回false
                    if (compareAndSetState(LEISURE_STATUS, OCCUPY_STATUS)) {
                        setExclusiveOwnerThread(Thread.currentThread());
                        return true;
                    }
                    return false;
                }
```
2：如果获取同步状态失败，则构造同步节点(Node.EXCLUSIVE表示独占)并掉用addWaiter方法把节点加入到同步队列的尾部：
```java
     private Node addWaiter(Node mode) {
            Node node = new Node(Thread.currentThread(), mode);
            // Try the fast path of enq; backup to full enq on failure
            Node pred = tail;//获取尾节点
            if (pred != null) {//不为空则更换当前线程为尾节点，compareAndSetTail保证了原子操作，不会有线程安全的问题，如果设置失败，则进入下面enq方法
                node.prev = pred;
                if (compareAndSetTail(pred, node)) {
                    pred.next = node;
                    return node;
                }
            }
            enq(node);
            return node;
        }
```
enq方法中保证了尾节点的初始化，默认指向头节点。
```java
    private Node enq(final Node node) {
            for (;;) {
                Node t = tail;//获取尾节点
                if (t == null) { // Must initialize
                    if (compareAndSetHead(new Node()))
                        tail = head;
                } else {//然后以死循环的方式将节点加入到队列中去
                    node.prev = t;
                    if (compareAndSetTail(t, node)) {
                        t.next = node;
                        return t;
                    }
                }
            }
        }
```
3：调用acquireQueued方法，使得该节点以死循环的方式获取同步状态。
```java
    final boolean acquireQueued(final Node node, int arg) {
            boolean failed = true;
            try {
                boolean interrupted = false;
                for (;;) {
                    final Node p = node.predecessor();//predecessor返回前驱节点
                    if (p == head && tryAcquire(arg)) {//如果式前驱节点式头节点，则调用tryAcquire方法获取同步状态
                        setHead(node);
                        p.next = null; // help GC
                        failed = false;
                        return interrupted;//获取成功后则返回false，表示该节点的线程以及获取了同步状态，也就是获取到了锁。
                    }
                    if (shouldParkAfterFailedAcquire(p, node) &&
                        parkAndCheckInterrupt())
                        interrupted = true;
                }
            } finally {
                if (failed)
                    cancelAcquire(node);
            }
        }
```
从上面逻辑可以看出，只有前驱节点为头节点的线程可以尝试获取同步状态，为什么呢？

1. 头节点是成功获取到同步状态的节点，而头节点的线程释放了同步状态后，将会唤醒后继节点，后继节点被唤醒后需要检查自己的前驱节点是否为头节点。
2. 保证同步队列的FIFO原则。

**当线程获取都锁执行完相应的逻辑之后，就应该释放锁，对应的方法是release(int arg)**：

```java
    public final boolean release(int arg) {
    //调用自定义同步器实现的tryRelease方法
            if (tryRelease(arg)) {
                Node h = head;
                if (h != null && h.waitStatus != 0)
                    unparkSuccessor(h);
                return true;
            }
            return false;
        }
```
release方法调用自定义同步器实现的tryRelease方法，比如Mutex的实现：
```
    protected boolean tryRelease(int arg) {
                //没有获取到锁，不能调用解锁方法
                    if (getState() == 0) {
                        throw new IllegalMonitorStateException();
                    }
                    //这里不需要原子操作，因为只有获取到锁的线程才能执行到这里。
                    setExclusiveOwnerThread(null);
                    setState(0);
                    return true;
                }
```
## 共享式同步状态获取式释放

共享式同步可以允许同一个时刻有多个线程获取到同步状态
```java
     public final void acquireShared(int arg) {
    //调用自定义同步器的tryAcquireShared方法
            if (tryAcquireShared(arg) < 0)
                doAcquireShared(arg);
        }
    
    private void doAcquireShared(int arg) {
            final Node node = addWaiter(Node.SHARED);
            boolean failed = true;
            try {
                boolean interrupted = false;
                for (;;) {
                    final Node p = node.predecessor();
                    if (p == head) {
                        int r = tryAcquireShared(arg);
                        if (r >= 0) {
                            setHeadAndPropagate(node, r);
                            p.next = null; // help GC
                            if (interrupted)
                                selfInterrupt();
                            failed = false;
                            return;
                        }
                    }
                    if (shouldParkAfterFailedAcquire(p, node) &&
                        parkAndCheckInterrupt())
                        interrupted = true;
                }
            } finally {
                if (failed)
                    cancelAcquire(node);
            }
        }
```
1：调用自定义同步器的tryAcquireShared方法，该方法返回int类型值，当返回值大于等于0时，表示能够获取到同步状态，**因此共享式获取的自旋过程中，成功获取到同步状态并退出自旋的条件式tryAcquireShared方法返回值大于或者等于0**

2： 是否共享锁的方法为releaseShared(int arg)方法
```
        public final boolean releaseShared(int arg) {
            if (tryReleaseShared(arg)) {
                doReleaseShared();
                return true;
            }
            return false;
        }
```
该方法调用自定义同步器实现的tryReleaseShared方法。**但是和独占式是释放的区别为，该方法必须保证同步状态被线程安全的释放，因为可能有多个线程在释放同步状态。**


## 独占式超时获取同步状态

通过调用同步器的boolean tryAcquireNanos(int arg, long nanosTimeout)方法可以超时的获取同步状态，在JDK1.5之前对被阻塞在synchronized上的线程进行中断操作，此时该线程的中断标记为将会被修改，但该线程依然被阻塞在synchronized上等待获取锁。


主要逻辑为:
```java
     public final boolean tryAcquireNanos(int arg, long nanosTimeout)
                throws InterruptedException {
            if (Thread.interrupted())
                throw new InterruptedException();
            return tryAcquire(arg) ||
                doAcquireNanos(arg, nanosTimeout);
        }
    
    private boolean doAcquireNanos(int arg, long nanosTimeout)
                throws InterruptedException {
            if (nanosTimeout <= 0L)
                return false;
            final long deadline = System.nanoTime() + nanosTimeout;
            final Node node = addWaiter(Node.EXCLUSIVE);
            boolean failed = true;
            try {
                for (;;) {
                    final Node p = node.predecessor();
                    if (p == head && tryAcquire(arg)) {
                        setHead(node);
                        p.next = null; // help GC
                        failed = false;
                        return true;
                    }
                    //计算时间
                    nanosTimeout = deadline - System.nanoTime();
                    if (nanosTimeout <= 0L)
                        return false;
                    if (shouldParkAfterFailedAcquire(p, node) &&
                        nanosTimeout > spinForTimeoutThreshold)
                        LockSupport.parkNanos(this, nanosTimeout);
                    if (Thread.interrupted())
                        throw new InterruptedException();
                }
            } finally {
                if (failed)
                    cancelAcquire(node);
            }
        }
```
nanosTimeout如果非常小(小于1000纳秒)，将不会使该线程进行等待超时，而是进入快速的自旋过程，原因在于非常断的时间无法做到十分精确，如果这时进行超时等待反而会让nanosTimeout的超时从整体上表现的反而不精确。

分析过程略。

## 自定义同步组件WwinsLock

设计一个同步组件，该同步组件同一个时刻允许两个线程获取同步状态，超过两个线程访问将会被阻塞。

```java
    public class TwinsLock implements Lock {
    
        ///////////////////////////////////////////////////////////////////////////
        // 测试
        ///////////////////////////////////////////////////////////////////////////
        public static void main(String... args) {
            Lock lock = new TwinsLock();
            class Worker extends Thread {
                @Override
                public void run() {
                    while (true) {
                        lock.lock();
                        try {
                            try {
                                Thread.sleep(1000);
                                System.out.print(Thread.currentThread().getName()+" -- ");
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } finally {
                            lock.unlock();
                        }
                    }
                }
            }
    
            for (int i = 0; i < 10; i++) {
                Worker worker = new Worker();
                worker.setDaemon(true);
                worker.start();
            }
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(1000);
                    System.out.println();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
    
            }
        }
    
    
        ///////////////////////////////////////////////////////////////////////////
        // 实现
        ///////////////////////////////////////////////////////////////////////////
    
        private Sync mSync = new Sync(2);
    
    
        private static class Sync extends AbstractQueuedSynchronizer {
            private Sync(int count) {
                if (count <= 0) {
                    throw new IllegalArgumentException();
                }
                setState(count);
            }
    
            /**
             * 返回值小于等于0表示没有获取到同步状态
             */
            @Override
            protected int tryAcquireShared(int arg) {//arg恒为1
                for (; ; ) {
                    int current = getState();//获取当前的状态，默认为构造方法传入的count
                    int newCount = current - arg;//每一个线程获取到同步则-1
                    //状态小于0表示没有获取的同步状态，直接返回即可
                    //如果count大于等于0，则表示获取成功，原子的设置同步状态后返回
                    if (newCount < 0 || compareAndSetState(current, newCount)) {
                        return newCount;
                    }
                }
            }
    
            @Override
            protected boolean tryReleaseShared(int arg) {
                for (; ; ) {
                    int current = getState();//获取状态
                    int newCount = current + arg;//释放则状态+1
                    if (compareAndSetState(current, newCount)) {//原子的设置状态
                        return true;
                    }
                }
            }
    
            Condition newCondition() {
                return new AbstractQueuedSynchronizer.ConditionObject();
            }
        }
    
    
        @Override
        public void lock() {
            mSync.acquireShared(1);
        }
    
        @Override
        public void lockInterruptibly() throws InterruptedException {
            mSync.acquireSharedInterruptibly(1);
        }
    
        @Override
        public boolean tryLock() {
            return mSync.releaseShared(1);
        }
    
        @Override
        public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
            return mSync.tryAcquireSharedNanos(1, unit.toNanos(time));
        }
    
        @Override
        public void unlock() {
            mSync.releaseShared(1);
        }
    
        @Override
        public Condition newCondition() {
            return mSync.newCondition();
        }
    
    }
```


