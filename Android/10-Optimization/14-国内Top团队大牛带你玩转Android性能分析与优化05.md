[国内Top团队大牛带你玩转Android性能分析与优化](https://coding.imooc.com/class/308.html)

# 6 卡顿优化

## 6.2 卡顿介绍及优化工具选择

背景介绍

- 很多性能问题不易被发现，但是卡顿很容易被直观感受
- 卡顿问题难以排除定位

卡顿问题难在哪里

- 产生愿意错综复杂：代码、内存、绘制、IO...
- 不易复现：与当时的场景强相关

### CPU Profiler

使用方式：

- `Debug.startMethodTracing()、Debug.stopMethodTracing()`
- 生成文件在 Android/data/packagename/files 中，可以直接在 AS 中打开

特点：

- 图形形式展示执行时间、调用栈等
- 信息全面，包含所有线程
- 运行时开销严重，整体都会变慢

### Systrance

- 监控和跟踪 API 调用，线程允许情况，生成 HTML 报告
- 轻量级，开销小
- 直观给出CPU利用率
- 能给出建议

### StrictMode

- 严苛模式：Android 提供的一种运行时检测机制
- 包含线程策略和虚拟机策略
- 线程策略
  - 自定义耗时时间，`detectCustomSlowCalls()`
  - 磁盘读取操作，`detectDiskReads()`
  - 网络操作：`detectNetwork`
- 虚拟机策略：
  - Activity泄露：`detectActivityLeaks()`
  - Sqlite对象泄露：`detectLeakedSqlLiteObjects()`
  - 检测实例数量：`setClassInstanceLimit()`

使用方式：

```java
    private void initStrictMode() {
        if (DEV_MODE) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectCustomSlowCalls() //API等级11，使用StrictMode.noteSlowCode
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()// or .detectAll() for all detectable problems
                    .penaltyLog() //在Logcat 中打印违规异常信息
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .setClassInstanceLimit(NewsItem.class, 1)
                    .detectLeakedClosableObjects() //API等级11
                    .penaltyLog()
                    .build());
        }
    }
```

## 6.3 自动化卡顿检测方案及优化

### 为什么需要自动化检测方案

- 系统工具适合线下分析
- 线上及测试环节也需要自动化检测方案

### 方案原理

- 消息处理机制，一个线程只有一个 Looper
- mLogging 对象在每个 Message 处理前后都会被调用
- 主线程发生卡顿，是在 dispatchMessage 执行耗时操作

```java
//Looper.loop:
public static void loop() {
        final Looper me = myLooper();
        if (me == null) {
            throw new RuntimeException("No Looper; Looper.prepare() wasn't called on this thread.");
        }
        final MessageQueue queue = me.mQueue;

        ......

        for (;;) {
            Message msg = queue.next(); // might block
            if (msg == null) {
                // No message indicates that the message queue is quitting.
                return;
            }

            // This must be in a local variable, in case a UI event sets the logger
            final Printer logging = me.mLogging;
            if (logging != null) {
                logging.println(">>>>> Dispatching to " + msg.target + " " +
                        msg.callback + ": " + msg.what);
            }

            ......

            if (logging != null) {
                logging.println("<<<<< Finished to " + msg.target + " " + msg.callback);
            }
            ......
        }
    }
```

### 具体实现

- Looper.getMainLooper().setMessageLogging()
- 匹配 `>>>>>>Dispatching`，阈值时间前后执行任务(获取堆栈)
- 匹配 `<<<<Finished`，任务启动之前取消低掉
- 开源方案：AndroidPerformanceMonitor

### AndroidPerformanceMonitor

特点：

- 非侵入性性能监控组件
- 定位相对准确
- `com.github.markzhai:blockcanary-android:1.5.0`

该方案存在的问题：

- 确实检测到卡顿了，但是卡顿堆栈可能不准确（监控采集到的堆栈可能不是真实发生卡顿的堆栈）
- 和 OOM 一样，最后的堆栈只是表象，不是真正的问题

针对 AndroidPerformanceMonitor 的优化：

- 获取监控周期内的多个堆栈，而不仅仅是最后一个。（对于某一个具体的堆栈，我们是熟悉的）
- 高频采集堆栈：startMonitor --> 高频采集堆栈 --> endMonitor --> 记录到文件 --> 上层。

高频采集堆栈后，海量堆栈上传对服务器有压力，如何优化：

- 分析：一个卡顿下多个堆栈大概率有重复
- 解决：对一个卡顿下堆栈进行 hash 排重，找出重复的堆栈

## 6.4 ANR分析与实战

### ANR

- KeyDispatchTimeout 5s
- BroadcastTimeout 前台10s，后台20s
- ServiceTimeout 前台20s，后台200s

### ANR 大概的执行流程

- 发生 ANR
- 进程接受到终止信号，开始写入进程 ANR 信息
- 弹出 ANR 提示框

### ANR 解决套路

- `adb pull data/anr/traces.txt`

### 线上 ANR 分析

- 通过 FileObserver 监控文件变化，高版本有权限问题
- ANR watchdog：`'com.github.anrwatchdog:anrwatchdog:1.3.0'`

## 6.5 卡顿单点问题检测方案

### 背景介绍

有了 watchdog 和 AndroidPerformanceMonitor 为什么还需要卡顿单点问题检测方案？

- 自动化卡顿检测方案并不够（有些 Message 并没有达到卡顿的阈值，但是对于用户来说却感觉到了卡顿，或者代码上依旧没有符合性能优化的要求）
- 体系化解决方案务必尽早暴露问题

### 单点问题：IPC 监控

#### 指标

- IPC 调用类型
  - PackageManager 调用
  - TelephoneManager 调用
- 每个调用的耗时、次数
- IPC 的调用堆栈，发生的线程

#### IPC 问题监控方案

##### IPC 方法前后埋点

- 不优雅，维护成本大

##### adb命令

- `adb shell am trace-ipc start`
- `adb shell am trace-ipc stop --dump-file /data/local/tmp/ipc-trace.txt`

##### ARTHook

ARTHook 可以 hook 系统方法：

```java
//在 Application.onCreate 中初始化
public class AppContext extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            DexposedBridge.findAndHookMethod(Class.forName("android.os.BinderProxy"), "transact",
                    int.class, Parcel.class, Parcel.class, int.class, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            LogUtils.i( "BinderProxy beforeHookedMethod " + param.thisObject.getClass().getSimpleName()
                                    + "\n" + Log.getStackTraceString(new Throwable()));
                            super.beforeHookedMethod(param);
                        }
                    });
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
```

### 卡顿问题监控方案完善

- 利用 ARTHook 完善线下工具
- 开发阶段 Hook 相关操作，暴露、分析问题

### 单点问题监控维度

- IPC
- IO、DB
- View 绘制

## 6.6 如何实现界面秒开

- [ ] todo

## 6.7 优雅监控耗时盲区

- [ ] todo

## 6.8 卡顿优化技巧总结初步

- [ ] todo

## 6.9 卡顿优化模拟面试

- [ ] todo
