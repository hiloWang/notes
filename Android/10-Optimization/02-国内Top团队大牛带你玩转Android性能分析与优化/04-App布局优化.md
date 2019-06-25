[国内Top团队大牛带你玩转Android性能分析与优化](https://coding.imooc.com/class/308.html)

# 5 App 布局优化

## 5.2 Android绘制原理及工具选择

### 绘制原理

- CPU 负责计算显示内容
- GPU 负责栅格化（UI元素元素绘制到屏幕上），比如图片先交给 CPU 计算，转换成纹理再交给 GPU 绘制
- 16 ms 发出 VSync 信号触发 UI 渲染
- 大多数的 Android 设备屏幕刷新率：60Hz（人脑识别极限）

### 优化工具

#### Systrace

- 关注 Frames
- 正常：绿色，丢帧：黄色或红色
- Alerts 栏，自动分析与标注 条目

关于 systrace 的使用，具体参考下面连接：

- [Overview of Systrace](https://developer.android.com/studio/profile/systrace)
- [性能工具Systrace](https://gityuan.com/2016/01/17/systrace/)

#### Layout Inspector

- AndroidStudio 自带工具：`Menu-->Tools-->LayoutInspector`
- 三大面板：ViewTree、LadOverlay、Properties Table

#### CHoreographer

- 获取FPS，可线上使用，具备实时性
- API 16 之后才能使用

使用方式：`Choreographer.getInstance().postFrameCallback`

```java
public class MainActivity extends Activity{

    private long mStartFrameTime = 0;
    private int mFrameCount = 0;
    private static final long MONITOR_INTERVAL = 160L; //单次计算FPS使用160毫秒
    private static final long MONITOR_INTERVAL_NANOS = MONITOR_INTERVAL * 1000L * 1000L;
    private static final long MAX_INTERVAL = 1000L; //设置计算fps的单位时间间隔1000ms,即fps/s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFPS();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void getFPS() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            return;
        }
        Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() {
            @Override
            public void doFrame(long frameTimeNanos) {
                if (mStartFrameTime == 0) {
                    mStartFrameTime = frameTimeNanos;
                }
                long interval = frameTimeNanos - mStartFrameTime;
                if (interval > MONITOR_INTERVAL_NANOS) {
                    double fps = (((double) (mFrameCount * 1000L * 1000L)) / interval) * MAX_INTERVAL;
                    mFrameCount = 0;
                    mStartFrameTime = 0;
                } else {
                    ++mFrameCount;
                }

                Choreographer.getInstance().postFrameCallback(this);
            }
        });
    }

}
```

## 5.3 Android布局加载原理

### 为什么需要知道布局加载原理

- 知其然知其所以然
- 深入源码

`setContentView(int layoutId)`大概流程：

```log
LayoutInflater.from(this.mContext).inflate(resId, contentParent);
    createViewFromTag
        mFactory2
        mFactory
        mPrivateFactory 用于 fragment 标签加载
        onCreateView（使用反射创建View）
```

性能瓶颈点:

- XML 加载与解析是 IO 操作
- View 的创建采用的方法是反射

### LayoutInflater.Factory

- LayoutInflater.Factory 是创建 View 的一个 Hook
- 定制创建 View 的过程：比如全局替换自定义 TextView
- Factory2 继承于 Factory，多了一个参数 parent

## 5.4 优雅获取界面布局耗时

### 常规方式

- 背景：获取每个界面加载耗时
- 实现：覆写方法，手动埋点
- 问题：不够优雅，代码有侵入性

### AOP / ARTHook

- AspectJ 切面点：`Activity.setContentView();` 方法耗时
- ARTHook

### 优雅地获取每一个控件耗时

方案：LayoutInflater.Factory

```java

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        LayoutInflaterCompat.setFactory2(getLayoutInflater(), new LayoutInflater.Factory2() {
            @Override
            public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
                //如果有需求，我们可以定制控件的创建过程
                if (TextUtils.equals(name, "TextView")) {
                    // 生成自定义TextView
                }
                long time = System.currentTimeMillis();
                //调用默认实现，这里我们只需要监控控件创建的耗时
                View view = getDelegate().createView(parent, name, context, attrs);
                LogUtils.i(name + " cost " + (System.currentTimeMillis() - time));
                return view;
            }

            @Override
            public View onCreateView(String name, Context context, AttributeSet attrs) {
                return null;
            }
        });

        super.onCreate(savedInstanceState)

    }
```

## 5.5 异步Inflate实战

### 背景

布局性能瓶颈点:

- XML 加载与解析是 IO 操作
- View 的创建采用的方法是反射

解决思路：

- 从根本解决问题，不使用反射、避免 IO
- 从侧面缓解：比如使用子线程 inflate 布局

### AsyncLayoutInflater 介绍

引入 AsyncLayoutInflater

```groovy
    implementation 'com.android.support:asynclayoutinflater:28.0.0'
```

使用方式：

```java
        new AsyncLayoutInflater(MainActivity.this).inflate(R.layout.activity_main, null, new AsyncLayoutInflater.OnInflateFinishedListener() {
            @Override
            public void onInflateFinished(@NonNull View view, int i, @Nullable ViewGroup viewGroup) {
                setContentView(view);
                mRecyclerView = findViewById(R.id.recycler_view);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                mRecyclerView.setAdapter(mNewsAdapter);
                mNewsAdapter.setOnFeedShowCallBack(MainActivity.this);
            }
        });
```

AsyncLayoutInflater 并没有保持 AppCompatActivity 内部的向下兼容的特性（比如将 TextView 替换为 AppCompatTextView），因此我们可以、参照 AsyncLayoutInflater 源码，重定义一个保持 AppCompatActivity 特性的 AsyncLayoutInflater。

## 5.6 布局加载优化实战

### Java 代码写布局

- 本质上解决就了性能问题
- 引入新问题：不便于开发，可维护性

### X2C 库

- 保留了 xml 优点，解决其性能问题
- 开发人员写 XML，加载 Java 代码
- 原理：APT 根据 xlm 生成 java 代码

X2C 库存在的问题

- 部分属性 Java 不支持
- 失去了系统的兼容性（AppCompat）

## 5.7 视图绘制优化实战

- 优化布局层级
- 避免过度绘制技巧

视图绘制流程：

- 测量，确定大小
- 布局，确定位置
- 绘制，绘制视图

性能瓶颈：

- 每个阶段都是遍历操作
- 有些情况会触发多次测量

准则：

- 减少 View 层级
- 层级尽量宽而浅

ConstraintLayout：

- 完全扁平化布局

技巧:

- 不嵌套使用 RelativeLayout
- 不在嵌套 LinearLayout 中使用 widget
- merge 属性，减少了一个层级，只能由于根 View

过度绘制：

- 一个像素最好只被绘制一次
- 条是 GPU 过度绘制

避免过度绘制:

- 去掉多余背景色，减少复杂 shape 使用
- 避免层级叠加
- 自定义 View 使用 clipRect 屏蔽被覆盖的 View 绘制

其他机巧

- Viewstub，延迟处初始化
- onDraw 中避免创建对象
- TextView 预渲染

## 5.8 布局优化模拟面试

1. 你在作布局优化过程中用到了哪些工具
   1. Choreographer（线上）
   2. AOP、Hook（线下）
   3. Systrace、LayoutInspector（线下）（线下）
2. 布局为什么会卡顿，你是如何优化的
   1. IO、反射、遍历、绘制过程
   2. 异步inflate，X2C，减少层级，重绘
   3. AOP 监控
3. 昨晚布局优化有哪些成功产出
   1. 建立了体系化监控手段：线上线下
   2. 指标：FPS、加载时间、布局层级
   3. 每次发版之前，对核心路径进行 Review
