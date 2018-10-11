# 锁的内存语义

### 1 锁的释放建立的happens before关系

锁让临界区互斥执行，还可以让释放锁的线程像获取同一个锁的线程发送消息。一个线程对锁的释放happens before 另一个线程对这个锁的获取

### 2 锁的获取与释放的内存语义

- 当线程释放锁时，JVM会把该线程对应本地内存中的共享变量刷新到主内存
- 当线程获取锁匙，JVM会把该线程对应的本地内存中共享变量置为无效，从而使得被监视器保护的临界区代码必须从主内存中读取共享变量




### 3 Java中的锁
- synchronized在并发编程中是元老级角色，synchronized用的锁是Java对象头里的。
- 可重入锁：ReentrantLock，ReentrantLock的实现依赖的是java同步器框架AbstractQeueudSynchronized。

