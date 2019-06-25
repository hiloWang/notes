[国内Top团队大牛带你玩转Android性能分析与优化](https://coding.imooc.com/class/308.html)

# 7 App线程优化

## 7.2 Android线程调度原理剖析

### 线程调度原理

- 任意时刻，只有一个线程占用CPU
- 多线程并发：轮流获取CPU使用权
- JVM负责线程调度：按照特定机制分配CPU使用权

### 线程调度模型

- 分时调度模型：所有线程轮流获取CPU时间，均分CPU时间
- 抢占式调度模型：优先级高的优先执行，JVM采用

### Android线程调度

影响Android线程调度的两个因素：

- nice值
- cgroup

nice值：

- Process中定义
- 值越小，优先级越高
- 默认值是 THREAD_PRIORITY_DEFAULT = 0

>Linux中，使用nice value来设定一个进程的优先级，系统任务调度器根据nice值合理安排调度。由于Android基于Linux Kernel，在Android中也存在nice值。具体参考[](https://droidyue.com/blog/2015/09/05/android-process-and-thread-schedule-nice/?droid_refer=series)

cgroup：

- 更严格的群组调度策略
- 保证前台线程可以获取到更多的CPU
- 不在前台运行的APP的线程都处于后台 cgroup 中

>cgroups，其名称源自控制组群（control groups）的简写，是Linux内核的一个功能，用来限制，控制与分离一个进程组群的资源（如CPU、内存、磁盘输入输出等）具体参考[Android进程线程调度之cgroups](https://droidyue.com/blog/2015/09/17/android-process-and-thread-schedule-cgroups/)

注意点:

- 现场过度会导致 CPU 频繁切换，降低线程运行效率
- 正确认识任务重要性决定哪种优先级
- 优先级具有继承性

## 7.3 Android异步方式汇总

- new Thread
- HanderThread
- IntentService
- AsyncTask
- 线程池
- RxJava

正确的场景选择正确的方式

## 7.4 Android线程优化实战

- 严禁使用 newThread
- 提供基础线程池供各个业务线使用
- 根据任务类型选择合适的异步方式
  - 比如：优先级低，长时间执行适合使用 HandlerThread
- 线程命名，方便调试定位问题
- 关键异步任务监控，一部不等于不耗时
  - 可以使用 AOP 对关键异步任务监控
- 重视优先级设置（可设置多次）`Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);`

## 7.5 如何锁定线程创建者

背景：

- 项目变大之后需要收敛线程（防止线程池泛滥）
- 项目源码、三方库、aar中都有线程的创建
- 避免恶化的一种监控预防手段

方案：

- 使用 Hook 手段可以锁定线程创建者
- 找 Hook 点：构造函数或者特定方法
- Thread 构造函数

```java
        DexposedBridge.hookAllConstructors(Thread.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Thread thread = (Thread) param.thisObject;
                LogUtils.i(thread.getName()+" stack "+Log.getStackTraceString(new Throwable()));
            }
        });
```

## 7.6 线程收敛优雅实践初步

### 需求

- 根据线程创建堆栈考量合理性，使用同一的线程库。
- 各业务线下掉自己的线程库，统一使用基础线程库。

### 遇到的问题

基础库怎么使用线程

- 一般基础库都以 jar/aar 方式进行依赖。
- 缺点：如果基础库中的线程库写死，则线程库更新会导致基础库更新。

优雅的方案：

- 基础库提供 setExecutor API

### 统一线程库

- 区分任务类型：IO，CPU 密集型
- IO 密集型：不消耗 CPU，核心数可以较大
- CPU 密集型：核心数与CPU核心数相关

## 7.7 线程优化模拟面试

1. 线程使用为什么会遇到问题?
2. 怎么在项目中对线程进行优化？
   1. 线程收敛
   2. 任务区分
