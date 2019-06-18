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