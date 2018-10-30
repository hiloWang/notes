# [全面解析Android线程](http://www.itdks.com/dakashuo/detail/15587#)笔记

看[全面解析Android线程](http://www.itdks.com/dakashuo/detail/15587#) 后，对 Android 线程实现有了新的认识。

---
## 1 什么是线程

- 线程是操作系统能够进行运算调度的最小单位，它被包含在进程中，是进程的实际运行单位。
- 一条线程指的是进程中一个单一执行的控制流，一个进程可以并发多个线程，每条线程并发执行多个任务。
- 在 Unix System 和 SunOs 中也被称为轻量级进程（lightweight-processes），但轻量进程更多指内核线程（kernel thread），而把用户线程（user thread）称为线程。

---
## 2 为什么要有线程

- 资源利用率，防止 CPU 长期等待而浪费 CPU 资源。
- 公平性（让多个任务可以资源竞争，而不是独占）。
- 便利性，多线程的编程模型。
- 发挥多处理器的强大能力（多核CPU，并行能力）。

---
## 3 线程的本质（Android、Linux）

Android 在 Java 层创建线程：

```java
    public synchronized void start() {
        checkNotStarted();

        hasBeenStarted = true;

        nativeCreate(this, stackSize, daemon);
    }
```

追踪到底层实现：

```cpp
//art/runtime/native/java_lang_Thread.cc
static void Thread_nativeCreate(JNIEnv* env, jclass, jobject java_thread, jlong stack_size,
                                jboolean daemon) {
  Thread::CreateNativeThread(env, java_thread, stack_size, daemon == JNI_TRUE);
}
//art/runtime/thread.cc
void Thread::CreateNativeThread(JNIEnv* env, jobject java_peer, size_t stack_size, bool is_daemon) {
    ......
      int pthread_create_result = pthread_create(&new_pthread, &attr, Thread::CreateCallback, child_thread);
    ......
}
```
 
发现 JVM 并没有实现自己的一套线程机制，最终实现还是调用 Linux 系统的 API `pthread_create` 来创建的线程，在其他平台也是类似，比如 Windows、MacOS。这说明：

- 一般情况，虚拟机的进行和线程与目标机器操作系统的进程和线程一一对应。
- 进程和线程调度是操作系统的核心模块，实现是非常复杂的，特别是在多核 CPU 的情况下。
- 因此完全没有必要在虚拟机中提供线程和进程的实现，这也是不现实的。

### 线程的等级/轻量级进程

- 用户线程，指无需内核支持而在用户程序中实现的线程。其不依赖于操作系统核心管理，应用进程利用线程库提供创建、同步、调度和管理线程的函数来控制用户线程。不需要用户态/内核态切换，速度快，操作系统内核不知道多线程的存在，因此用用户级线程阻塞并不会妨碍内核线程调度。
- 在 Android 中建立的线程属于用户态线程，内部线程阻塞只会影响当前进程，而不会影响其他进程调度（当然不仅仅是因为用户级线程，还有系统的调度）。


---
## 4 线程优先级

### 进程优先级

- 前台进程
- 后台进程
- APP 常驻后台（像素点、播放无声的音频、通知栏漏洞、双进程守护）

### NICE Values

在 Android 中对于线程有一份 Nice Values 的机制，高 Nice value（低优先级）的线程运行次数（时间片）少于低 Nice value（高优先级）的线程。

### Control Group

重要的优先级：Foreground、Defalut、Background。

- 一般来说，线程的优先级应当与所需执行的工作量成反比，工作量越多的线程，它的优先级应该越低，以便它不会饿死系统（进程）。
- UI 线程时前台线程的级别，因为 UI 完成的工作量少（测量、绘制等）。
- AsyncTask 需要完成更多的任务，所以默认情况下时 Background 的。
- Nice values 理论上来说时重要的，因为理论而言其减少了中断 UI 线程的可能性，但实际并非如此，比如 20 个后台线程，虽然都在后台，但是数量多了依然会影响到 UI 线程的性能。
- 在实际使用中，Android 会自动把低优先级的线程划分到 CGroup 中去，在 UI 线程繁忙的时候，后台 CGroup 中的线程仅仅只能有限地使用特定比例的（5%-10%）CPU，极大的减少了对 UI 线程的影响。
- 处于后台的 APP（进程）中的线程会自动移动到 CGroup 中去。

### 线程优先级

- 理论上我们无需关心线程优先级的问题，因为系统源码已经设定了优先级，比如 HandlerThread、AsyncTask。
- 然而实际情况往往不是这样的，如果你在 UI 线程直接 `new Thread().start()`，新创建的线程的优先级时继承自父线程的，这样新创建的线程的优先级就与 UI 线程一致了。


---
## 5 线程池

当设置 `allowCoreThreadTimeOut(true)`时，线程池中的核心线程空闲时间达到 keepAliveTime 也会倍回收。

---
## 6 性能

多线程是否比一个线程消耗时间更短？

- 进程切换是需要进行对应内存切换的，这样的事件代价很高，而线程属于进程内部，内存共享，所以代价很低。但线程的切换依然需要消耗时间，所以把一份工作交给两个线程完成所消耗的时间多余一个线程的时间。
- 所以采用一个拥有两个线程的进程执行所需要的时间比一个线程的进程执行两次所需的时间要多一些，**即采用多线程不会提供程序的执行速度，反而会降低速度**。
- 一般而言，多线程的实际运用不是为了提高运行效率，而是为了提供资源利用率（不管是单核还是多核CPU的情况下，多线程都可以提供CPU的使用率）
- 当执行网络等 IO 操作时，其实 CPU 是在忙等的，因为实际执行 IO 调度的并不是 CPU，而是其他处理器（比如DMA）。
- 是否线程越多，CPU 的利用率就越多，这也是不对的，因为线程的上下文切换也是需要消耗 CPU 的，如果 CPU 的大部分工作都用来进行线程调度，而不是做实际有意义的任务，那样返回降低的 CPU 的实际利用率，因此还需要根据任务类型，并发需求等来衡量线程的数量。