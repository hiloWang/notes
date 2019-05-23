# [国内Top团队大牛带你玩转Android性能分析与优化](https://coding.imooc.com/class/308.html#Anchor)

## 3 App启动优化

>App启动速度是用户的第一印象，本章会介绍精准度量启动速度的方式，启动优化的相关工具、常规优化手段等，同时我会介绍异步初始化以及延迟初始化的最优解，以最优雅、可维护性高的的方式获得闪电般的启动速度。...

### 3.2 App启动优化介绍

#### 背景

- 第一体验
- 八秒定律

#### 启动分类

App startup time

- 冷启动
  - 耗时最多，衡量标准
  - Click Event -> IPC -> Process.start -> ActivityThread.mian -> bindApplication -> Lifecycle -> ViewRootImpl
  - 冷起动前：启动App、加载空白Window、创建进程
  - 随后任务：创建 Application、启动主线程、创建 MainActivity、加载布局、布置屏幕、首帧绘制
- 热启动
  - 最快
  - 后台 -> 前台
- 温启动
  - 仅仅是重走 Activity 的生命周期

优化方向

- Application/Activity 的生命周期

### 3.3 启动时间测量方式

#### adb 命令方式

`adb shell am start -W packagename/首屏Activity`

```log
ThisTime：最后一个 Activity 启动耗时
TotalTime：所有 Activity 启动耗时
WaitTime：AMS 启动 Activity 的总耗时
```

- 线下使用方便
- 非严谨的、精确的时间

#### 手动打点方式

`结束埋点 - 启动埋点 = 耗时`

```java
public class LaunchTimer {

    private static long sTime;

    public static void startRecord() {
        sTime = System.currentTimeMillis();
    }

    public static void endRecord() {
        endRecord("");
    }

    public static void endRecord(String msg) {
        long cost = System.currentTimeMillis() - sTime;
        LogUtils.i(msg + "cost " + cost);
    }

}
```

记录点：

```java
//开始
public class PerformanceApp extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        LaunchTimer.startRecord();
    }
}

//结束，onWindowFocusChanged 只是首帧时间，正确的时间应该是：真实数据的展示，Feed 第一条展示
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private boolean mHasRecorded;

    ......

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        if (position == 0 && !mHasRecorded) {
            mHasRecorded = true;
            holder.layout.getViewTreeObserver()
                    //绘制前的回调
                    .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                            holder.layout.getViewTreeObserver().removeOnPreDrawListener(this);
                            LogUtils.i("FeedShow");
                            LaunchTimer.endRecord("FeedShow");
                            return true;
                        }
                    });
        }
    }
}
```

- 精确，可带到线上，推荐使用
- 避免误区，采用 Feed 第一条展示
- addOnDrawListener 需要 Api16，可采用 addOnPreDrawListener 代替

### 3.4-3.5 启动优化工具选择

- traceview
- systrace

两种方式互补，正确认识工具，在不同场景下选择合适的工具

#### traceview

特点：

- 图形的形式展示执行时间，调用栈等
- 信息全面，包含所有线程
- 运行时开销严重，整体都会变慢
- 可能会带偏优化方向
- 可以手动埋点，可配合 cpu-profiler 一起使用

使用方式：

```java
//生成的文件在 sd 卡，Adnroid/data/packagename/files
Debug.startMethodTracing("");
Debug.stopMethodTracing("")
```

图形界面：

- Call Char
  - 不同级别 API 的颜色不一样
- Flame Chart
  - 收集相同函数调用顺序的次数
- Top Dowm：函数的调用列表
  - Self + Children = Total
  - Thread Time：CPU 的执行时间，比 Wall Clock Time 少，对于一个函数来说，消耗的时间并不是 CPU 真正在上面的时间(有些时间 CPU 可能是挂起的)。
  - Wall Clock Time：线程真正执行的时间
- Bottom Up：查看方法调用者

#### systrace

特点:

- 结合 Android 内核的数据，生成 HTML 报告
- API 18 以上适使用，低版本可使用 TraceCompat
- 具体参考：<https://developer.android.com/studio/profile/systrace>

使用方式：

```java
//先用代码买点
Trace.beginSection()
Trace.endSection()

//然后用 python systrace.py -t 10 [options] [categories] 拉取报告
```

总结：

- 轻量级，开销小
- 直观反映 CPU 利用率
- cputime 与 walltime 的区别：
  - walltime 代码执行的时间
  - cputime 是代码消耗 cpu 的时间，优化要关注 cputime 时间。

### 3.6-3.7  优雅获取方法耗时讲解

启动优化需要知道启动阶段所有方法耗时，方式有手动埋点，AOP 插入统计代码

#### 常规埋点方法

常规埋点方法，即 System.currentTimeMillis()/SystemClock.currentTimeMillis()，常规埋点方法侵入性大，工作量大。

#### AOP

AOP 针对同一问题的统一处理。无需侵入代码

框架：

- AspectJ，可选值沪江开源的插件，也可以选择自行开发插件
- 其他代码插入库

### 3-8 异步优化详解

#### 优化小技巧

- Theme 切换，用图片代替应用启动时的空白 Window，让用户感觉快。
- 核心思想：子线程分担主线程任务，并行较少时间。

#### 异步初始化遇到的问题

- 任务与任务之间有依赖关系
- 有些任务必须在主线程中执行
- 异步初始化的结束时间不确定

解决方案：使用 CountDownLatch。

```java
    private CountDownLatch mCountDownLatch = new CountDownLatch(1);

    //异步初始化
    service.submit(()->mCountDownLatch.countDown())

    //等待
    mCountDownLatch.await();
```

### 3-9-3-10 异步初始化最优解-启动器

- [ ] todo

### 3-11 更优秀的延迟初始化方案

- [ ] todo

### 3-12 启动优化其它方案

- [ ] todo

### 3-13 启动优化方案总结

- [ ] todo