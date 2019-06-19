# ANR 捕获与分析

## 1 ANR 分析示例

制造 ANR：

```java
public class ANRActivity extends AppCompatActivity {

    private static final String TAG = ANRActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anr);
    }

    public void makeComplexANR(View view) {
        new Thread() {
            @Override
            public void run() {
                threadANR();
            }
        }.start();
        SystemClock.sleep(20);
        initView();
    }

    private synchronized void initView() {
        Log.d(TAG, "initView() called");
    }

    private synchronized void threadANR() {
        SystemClock.sleep(26000);
    }
}
```

拉取ANR 信息文件：

`adb pull /data/anr/traces.txt .`

分析文件：

```log
----- pid 7634 at 2018-02-01 09:28:14 -----
Cmd line: com.ztiany.test

JNI: CheckJNI is off; workarounds are off; pins=0; globals=271

DALVIK THREADS:
(mutexes: tll=0 tsl=0 tscl=0 ghl=0)

"main" prio=5 tid=1 MONITOR     主线程
  | group="main" sCount=1 dsCount=0 obj=0xa4d60bd8 self=0xb7abceb0
  | sysTid=7634 nice=0 sched=0/0 cgrp=apps handle=-1216532416
  | state=S schedstat=( 296791622 84269785 1222 ) utm=10 stm=18 core=0
  at com.ztiany.test.anr.ANRActivity.initView(ANRActivity.java:~43)
  // 正在等待一个锁(  - waiting to lock <0xa508dd20>)  ，id为 12的线程持有了这个锁(held by tid=12)
  - waiting to lock <0xa508dd20> (a com.ztiany.test.anr.ANRActivity) held by tid=12 (Thread-112)
  at com.ztiany.test.anr.ANRActivity.makeComplexANR(ANRActivity.java:39)
  at java.lang.reflect.Method.invokeNative(Native Method)
  at java.lang.reflect.Method.invoke(Method.java:515)
  at android.support.v7.app.AppCompatViewInflater$DeclaredOnClickListener.onClick(AppCompatViewInflater.java:288)
  at android.view.View.performClick(View.java:4438)
  at android.view.View$PerformClick.run(View.java:18422)
  at android.os.Handler.handleCallback(Handler.java:733)
  at android.os.Handler.dispatchMessage(Handler.java:95)
  at android.os.Looper.loop(Looper.java:136)
  at android.app.ActivityThread.main(ActivityThread.java:5001)
  at java.lang.reflect.Method.invokeNative(Native Method)
  at java.lang.reflect.Method.invoke(Method.java:515)
  at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:785)
  at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:601)
  at dalvik.system.NativeStart.main(Native Method)

//tid=12就是这个线程持有了锁
"Thread-112" prio=5 tid=12 TIMED_WAIT
  | group="main" sCount=1 dsCount=0 obj=0xa50b9c18 self=0xb7c279d0
  | sysTid=9559 nice=0 sched=0/0 cgrp=apps handle=-1211388904
  | state=S schedstat=( 110038 0 2 ) utm=0 stm=0 core=2
  at java.lang.VMThread.sleep(Native Method)
  at java.lang.Thread.sleep(Thread.java:1013)
  at java.lang.Thread.sleep(Thread.java:995)
  at android.os.SystemClock.sleep(SystemClock.java:115)
  at com.ztiany.test.anr.ANRActivity.threadANR(ANRActivity.java:47)
  at com.ztiany.test.anr.ANRActivity.access$000(ANRActivity.java:17)
  at com.ztiany.test.anr.ANRActivity$1.run(ANRActivity.java:35)
```

## 如何监控 ANR

1. 使用 FileObserver 监听 `/data/anr/traces.txt` 的变化，但是很多高版本的 ROM，已经没有读取这个文件的权限了。Bugly 目前也是使用的此方式。
2. 海外可以使用 Google Play 服务。
3. 国内微信利用 [Hardcoder](https://mp.weixin.qq.com/s/9Z8j3Dv_5jgf7LDQHKA0NQ?) 框架向厂商获取了更大的权限，但是 Hardcoder 并没有开源。

## 参考

- [关于ANR异常捕获与分析，你所需要知道的一切](https://codezjx.com/2017/08/06/anr-trace-analytics/)
- [Bugly即将支持的ANR，精神哥告诉你是个什么鬼？](https://cloud.tencent.com/developer/article/1070483)
- [看完这篇 Android ANR 分析，就可以和面试官装逼了！](https://cloud.tencent.com/developer/article/1425771)
- [理解Android ANR的信息收集过程](https://gityuan.com/2016/12/02/app-not-response/)
- [理解Android ANR的触发原理](https://gityuan.com/2016/07/02/android-anr/)

使用 watch-dog 思想监控 ANR

- [Android ANR在线监控原理](https://www.jianshu.com/p/e3fa7e4540fc)
- [ANR-WatchDog](https://github.com/SalomonBrys/ANR-WatchDog)
