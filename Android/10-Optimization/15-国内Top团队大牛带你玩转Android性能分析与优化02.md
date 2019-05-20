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

- [ ] todo

### 3-6 优雅获取方法耗时讲解

- [ ] todo

### 3-7 优雅获取方法耗时实操

- [ ] todo

### 3-8 异步优化详解

- [ ] tod

### 3-9-3-10 异步初始化最优解-启动器

- [ ] todo

### 3-11 更优秀的延迟初始化方案

- [ ] todo

### 3-12 启动优化其它方案

- [ ] todo

### 3-13 启动优化方案总结

- [ ] todo