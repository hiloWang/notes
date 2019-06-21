# [Android开发高手课](https://time.geekbang.org/column/intro/142) 实践 02

>所属章节——崩溃优化（下）：应用崩溃了，你应该如何去分析？

## 实践：TimeoutException 崩溃修复

如果想向崩溃发起挑战，那么 Top 20 崩溃就是我们无法避免的对手。在这里面会有不少疑难的系统崩溃问题，TimeoutException 就是其中比较经典的一个。

>关乎 TimeoutException，具体参考[how-to-handle-java-util-concurrent-timeoutexception-android-os-binderproxy-fin](https://stackoverflow.com/questions/24021609/how-to-handle-java-util-concurrent-timeoutexception-android-os-binderproxy-fin)

```log
java.util.concurrent.TimeoutException:
         android.os.BinderProxy.finalize() timed out after 10 seconds
at android.os.BinderProxy.destroy(Native Method)
at android.os.BinderProxy.finalize(Binder.java:459)
```

1. 通过源码分析。我们发现 TimeoutException 是由系统的 FinalizerWatchdogDaemon 抛出来的。
2. 寻找可以规避的方法。尝试调用了它的 Stop() 方法，但是线上发现在 Android 6.0 之前会有线程同步问题。
3. 寻找其他可以 Hook 的点。通过代码的依赖关系，发现一个取巧的 Hook 点。

具体参考：[AndroidAdvanceWithGeektime/Chapter02](https://github.com/AndroidAdvanceWithGeektime/Chapter02)

## 学习心得

1. 在崩溃现场应该采集哪些信息？
   1. 崩溃信息，从崩溃的基本信息，我们可以对崩溃有初步的判断。
   2. 系统信息，系统的信息有时候会带有一些关键的线索，对我们解决问题有非常大的帮助。
   3. 内存信息，OOM、ANR、虚拟内存耗尽等，很多崩溃都跟内存有直接关系。
   4. 资源信息，有的时候我们会发现应用堆内存和设备内存都非常充足，还是会出现内存分配失败的情况，这跟**资源泄漏**可能有比较大的关系。
   5. 应用信息，除了系统，其实我们的应用更懂自己，可以留下很多相关的信息。比如崩溃页面，操作路径。
2. 如何采集信息？
   1. UncaughtExceptionHandler
   2. 读取系统中相关位置的文件，比如 event-log `/system/etc/event-log-tags`、系统内存状态 `/proc/meminfo`、PSS 和 RSS 通过 `/proc/self/smap` 计算、虚拟内存可以通过 `/proc/self/status` 得到，通过 `/proc/self/maps` 文件可以得到具体的分布情况、文件句柄的限制可以通过 `/proc/self/limits`。
   3. 分析 Android 系统 log，包括 `kernel、radio、event、main` 四种。
3. 如何分析崩溃：崩溃分析三部曲
   1. 第一步：确定重点
   2. 第二步：查找共性
   3. 第三步：尝试复现
4. 系统崩溃如何解决？它可能是某个 Android 版本的 bug，也可能是某个厂商修改 ROM 导致。这种情况下的崩溃堆栈可能完全没有我们自己的代码，很难直接定位问题。
