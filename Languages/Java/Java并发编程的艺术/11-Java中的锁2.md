
---
# 3 重入锁

重入锁ReentrantLock就是支持重入的锁，表示该锁支持一个线程对资源重复的加锁，除此之外该锁还支持获取时的公平与非公平性选择。

前面定义的Mutex是支持重入的，当一个线程获取锁之后再次获取锁时，则该线程将永远被阻塞在该锁上，synchronized是支持重入的，比如递归的调用一个synchronized方法。

**公平性与非公平性**
在绝对时间上，先对锁进行获取的请求一定先被满足。则该锁为公平锁，反之则是非公平锁。ReentrantLock的构造函数允许传入一个boolean值用于指定该锁是否公平。而事实上公平锁的效率往往没有非公平锁高，但是公平锁能够健身**饥饿**发生的概览。


## 实现重入

实现重入需要解决两个问题：
1. 线程再次获取锁，锁需要识别获取锁的线程是否为当前占据锁的线程
2. 锁最终的释放，线程重复获取n次锁，然后在第n次释放该锁后，其他线程能够获取到该锁。

默认的ReentrantLock为非公平锁，其获取状态的代码如下所示：

该方法增加了再次获取同步状态的处理逻辑
```java
     final boolean nonfairTryAcquire(int acquires) {
                final Thread current = Thread.currentThread();
                int c = getState();
                if (c == 0) {//没有线程获取到锁，直接返回true
                    if (compareAndSetState(0, acquires)) {
                        setExclusiveOwnerThread(current);
                        return true;
                    }
                }
                else if (current == getExclusiveOwnerThread()) {//如果是线程已经获取到了锁
                    int nextc = c + acquires;//进行计数自增
                    if (nextc < 0) // overflow
                        throw new Error("Maximum lock count exceeded");
                    setState(nextc);
                    return true;
                }
                return false;
            }
```
再次获取锁只是进行计数自增，则释放锁也同样是计算自减：

```java
protected final boolean tryRelease(int releases) {
    int c = getState() - releases;//自减
    if (Thread.currentThread() != getExclusiveOwnerThread())//只有获取到锁的线程才能释放锁
        throw new IllegalMonitorStateException();
    boolean free = false;
    if (c == 0) {//当状态为0的时候表示已经完全的释放了锁。
        free = true;
        setExclusiveOwnerThread(null);
    }
    setState(c);//独占锁不需要原子操作
    return free;
}
```
## 公平锁与非公平锁的区别

非公平锁只要CAS操作同步状态成功则表示该线程获取到了锁，而公平锁则不同：

```java
    protected final boolean tryAcquire(int acquires) {
                final Thread current = Thread.currentThread();
                int c = getState();
                if (c == 0) {
                    if (!hasQueuedPredecessors() &&
                        compareAndSetState(0, acquires)) {
                        setExclusiveOwnerThread(current);
                        return true;
                    }
                }
                else if (current == getExclusiveOwnerThread()) {
                    int nextc = c + acquires;
                    if (nextc < 0)
                        throw new Error("Maximum lock count exceeded");
                    setState(nextc);
                    return true;
                }
                return false;
            }
        }

     public final boolean hasQueuedPredecessors() {
            // The correctness of this depends on head being initialized
            // before tail and on head.next being accurate if the current
            // thread is first in queue.
            Node t = tail; // Read fields in reverse initialization order
            Node h = head;
            Node s;
            return h != t &&
                ((s = h.next) == null || s.thread != Thread.currentThread());
        }
```
该方法增加了hasQueuedPredecessors()方法的判断。即加入了同步队列中当前节点是否有前驱节点的判断，如果方法返回true表示有线程比当前线程更早的请求获取锁，因此需要等待前驱线程获取锁并释放之后才能获取锁。


非公平锁能够大大的减少线程切换，其性能比公平锁要高的多，一般情况下推荐使用非公共锁，能够保证更大的吞吐量。

---
# 4 读写锁

前面的锁(Mutex和ReentrantLock)都是排它锁，这些锁在同一个时刻只允许一个线程访问，而读写锁允许同一时刻有多个线程访问，但是在写线程访问时，其他的所有写和读线程都要被阻塞。

ReentrantReadWriteLock维护了一对锁，**一个读锁和一个写锁**，通过分离读锁和写锁，使得并发性能比一般的排它锁有了很大的提升。

**ReentrantReadWriteLock的特性**

- 支持公平性选择
- 支持重入
- 锁降级：遵循**获取写锁->获取读锁写再释放写锁**的次序，写锁能够降级为读锁，但是不能在获取了读锁的情况下再获取写锁。

Java中的ReadWriteLock接口指定了两个方法，写锁和读锁：
```java
    public interface ReadWriteLock {
        Lock readLock();
        Lock writeLock();
    }
```
其实现者ReentrantReadWriteLock还提供了另外几个方法便于外界监控其内部状态

- int getReadHoldCount() 返回当前读锁被获取的次数(不是获取读锁的线程数)
- int getReadHoldCount() 返回当前线程获取读锁的次数
- boolean isWriteLocked() 判断写锁释放被获取了
- int getWriteHoldCount() 获取当前写锁被获取的次数


## 使用示例

```java
    public class UsingReentrantReadWriteLock {
    
        private static Map<String, String> cache = new HashMap<>();
    
        static ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        static Lock read = readWriteLock.readLock();
        static Lock write = readWriteLock.writeLock();
     
        public static String getCache(String key) {
            read.lock();
            try {
                return cache.get(key);
            } finally {
                read.unlock();
            }
        }
    
        public static String putCache(String key, String value) {
            write.lock();
            try {
                return cache.put(key, value);
            } finally {
                write.unlock();
            }
        }
    
        public static void clear() {
            write.lock();
            try {
                cache.clear();
            } finally {
                write.unlock();
            }
        }
    }
```

## 实现分析

ReentrantReadWriteLock把一个int变量进行按位切割使用，读写锁将变量切割成两部分，高的16位表示读的状态，低的16位表示写的状态

具体实现有时间再记录。

- 读写锁的状态设计
- 写锁获取与释放
- 读写的获取与释放
- 写锁的降级

---
# 5 LockSupport工具

LockSupport定义了一组静态的方法，这些方法提供了最近的线程阻塞与唤醒功能，而LockSupport也是构建同步组件的基础工具。

LockSupport提供的工具方法如下：

方法名|作用
---|---
void park() |阻塞当前线程，如果调用unpark(Thread)方法或者当前线程被中断，才从该方法返回
void parkNanos(long nanos) |阻塞当前线程，最初步超过nanos纳秒，返回条件在park方法上增加了超时
void parkUntil(long deadline) |阻塞当前线程，直到deadline时间(从197年开始到deadline时间的毫秒值)
void unpark(Thread thread) |唤醒当前线程

在JDK1.6中LockSupport增加了`park(Object blocker)、parkNanos(Object blocker，long nanos)、parkUntil(Object blocker，long deadline)`方法，用于阻塞当前线程，其中参数blocker用来标识当前线程在等待的对象。


**使用示例**：

```java
    public class UsingLockSupport {
    
        private static Object blocker = new Object();
    
        public static void main(String... args) {
    
            Thread thread = startThread();
    
    
            try {
                Thread.sleep(1000 * 3);
                LockSupport.unpark(thread);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    
        private static Thread startThread() {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    System.out.println("Thread run");
                    System.out.println("Thread blocked");
                    LockSupport.park(blocker);
                    System.out.println("Thread free");
                    System.out.println("Thread end");
                }
            };
            thread.start();
    
            return thread;
        }
    }
```

---
# 6 Condition接口

任意一个对象都有一组监视器，主要包含wait()、wait(long timeout)、notify()、nofityAll()方法，这些方法于synchronized同步关键字配合，可以实现等待/通知模式。

Condition接口也提供了类似Object的方法，于Lock配合使用也可以实现等待/通知模式。而且同一个Lock支持多个Condition，即多个等待队列。


**Condition的部分方法和作用**：


方法名|作用
---|---
void await() throws InterruptedException | 当前线程进入等待状态，直到被通知或中断，当前线程进入运行状态且从await方法返回的情况有：<br/>1. 其他线程调用了signal或signalAll方法，而当前线程被选中唤醒；<br/>2. 其他线程调用了interrupt方法中断该当前线程。<br/>如果当前线程从await方法返回，那么表明该线程已经获取了Condition对应的锁。
void awaitUninterruptibly() | 该等待机制不响应中断机制
long awaitNanos(long nanosTimeout) throws InterruptedException | 超时等待机制 
boolean awaitUntil(Date deadline) throws InterruptedException | 等待获取Condition锁直到某个时间
void signal() | 唤醒某个线程
void signalAll() | 唤醒所有线程


