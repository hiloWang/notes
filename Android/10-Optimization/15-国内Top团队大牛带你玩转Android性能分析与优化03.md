[国内Top团队大牛带你玩转Android性能分析与优化](https://coding.imooc.com/class/308.html)

# 4 App内存优化

>本章从原理开始讲起，首先介绍Java及Android的内存管理机制，接下来手把手带领大家进行内存抖动、泄露的解决实战，同时通过ArtHook的方式优雅解检测出App所有不合理的图片。

---
## 4.2 内存优化介绍及工具选择

### 内存是个大问题但是缺乏关注

- 表现比较隐秘，一般难以察觉。
- Java 自带了内存回收机制，很多人没有优化意识。
- 压死骆驼的往往是最后一个根稻草：OOM。

### 内存问题

- 内存抖动：图形分析工具呈现锯齿状，GC 导致卡顿。
- 内存泄露：可用内存减少，频繁 GC。
- OOM：应用奔溃。

### 内存分析工具

- Memory Profiler
- Memory Analyzer
- LeakCanary

### Memory Profiler

- 实时图标展示内存使用量
- 表现直观
- 可识别内存泄露和抖动等
- 捕获堆转储
  - ShadowSize 自己的内存大小
  - RetailersSize 受该对象支配的对象的内存大小
  - Bitmap 对象可实时展示该对象存储的图片
- 强制 GC 跟
- 踪内存分配能力：Record

Memory Profiler 只能在线下使用。

### Memory Analyzer

- 强大的 Java Heap 分析工具，查找内存泄露以及内存占用
- 生成整体报告，分析问题等
- 线下深入使用，可结合 Memory Profiler 一起使用

### LeakCanary

自动化内存泄露检测工具，使用比较简单。

---
## 4.3 Android内存管理机制

- Java 内存管理机制
- Android 内存管理机

### Java 内存管理

内存区域划分：

- 方法区
- 虚拟机栈
- 本地方法栈
- 堆（重点关注）
- 程序计数器（方法执行到第几行）

内存回收算法：

- 标记回收算法（效率不高，需要对所有块进行遍历和标记，易产生内存碎片）
- 复制回收算法（需要腾出一半内存，效率较高，只需要遍历一半的内存区域）
- 标记整理算法（先对对象进行标记，所有存活对象往一边进行移动，然后清理剩余内存）
- 分代回收算法（结合多种算法优势）
  - 新生代：对象存活率较低，使用复制回收算法
  - 老年代：大量对象长久存活，使用标记整理算法

### Android 内存管理

- 内存弹性分配，分配值与最大值受到具体设备的影响
- OOM 场景：
  - 内存真正不足（整个系统都没有内存了）
  - 可用内存不足
- Dalvik 与 ART 区别
  - Dalvik 仅固定一种回收算法（随着系统烧录到硬件）
  - ART 回收算法可运行期选择
- Low Memory killer：内存不足时，对进程进行回收
  - 进程分类：前台，可见，服务，后台，空进程
  - 回收收益：300M 还是 30M

---
##  4.4 内存抖动解决实战

### 内存抖动定义

- 内存频繁分配和回收导致内存不稳定
- 表现：频繁GC，内存曲线呈锯齿状
- 危害：导致卡顿，导致 OOM（频繁创建对象，导致内存不足及碎片）

### 使用 Memory Profiler 初步排除内存抖动

模拟内存抖动：

```java
    private static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // 创造内存抖动
            for (int index = 0; index <= 100; index++){
                String arg[] = new String[100000];
            }
            mHandler.sendEmptyMessageDelayed(0,30);
        }
    };
```

- 发生内存抖动，Memory Profiler 的内存图形是锯齿状的。
- 使用 Record 可以追踪内存分配，然后分析是哪个对象哪个步骤导致了内存抖动，我们可以使用 AllocationStack 工具定义到源码。
- 可以结合 CPU Profiler 分析排除。

---
## 4.5 内存泄露解决实战

### 内存泄露的定义

- 内存中存在已经没有用的对象
- 表现：内存抖动，可用内存逐渐减少

### 使用 Memory Analyzer 分析内存泄露

注意:Android 中的堆转储文件需要转换为 MAT 可以识别的格式，命令为 `hprof-conv 源路径 输出路径`

模拟内存泄露

```java
public class MemoryLeakActivity extends AppCompatActivity implements CallBack{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memoryleak);
        ImageView imageView = findViewById(R.id.iv_memoryleak);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.splash);
        imageView.setImageBitmap(bitmap);
        //持久存储器
        CallBackManager.addCallBack(this);
    }

}
```

发生内存泄露时，Memory Profiler 的内存图形是呈阶梯上升的，此时可以使用 MAT 分析堆转储文件，排查内存泄露。

- 发生内存泄露，一般是反复打开关闭了一个界面，或者反复执行了某一个操作。
- 因此可以在 MAT 的 Histogram 模块中搜索操作时当前界面的类名。
- 操作步骤：Histogram --> ListObject --> With incomeing references --> Path To GC Roots

---
## 4.6 全面理解 MAT

- Histogram(直方图)，基于类分析
  - with outgoing references 引用了那些对象
  - with incoming references 被哪些对象引用
- dominator 基于实例分析
- OQL 对象查询语言，语法类似数据库
- Thread Overview 线程概览
- Top consumer 占用内存较大的对象
- Leak Suspects(嫌疑犯)，自动分析的可能的内存泄露

---
## 4.7 ARTHook优雅检测不合理图片

### Bitmap 内存模型

- API10 之前，Bitmap 自身在 Dalvik Heap 中，像素在 Native 中。（Native回收时机不确定）
- API10 之后，像素也被放在了 Dalvik Heap 中。
- API26 之后，像素被放到了 Native 中，Heap 中的对象回收后，可以及时通知 Native 回收内存

### 获取 Bitmap 占用内存

- getByteCount 运行时
- `宽 * 高 * 一个像素占用内存值`（考虑缩放）

#### 优化方式

图片对内存优化至关重要，很多时候图片大小大于控件大小，只需要按需现实。我们可以使用某种方式实时对比图片大小和控件大小，当图片大小大远远大于控件大小，则打印警告，实现方式：

- 常规方式，自定义控件，不通用，侵入性强。
- ARTHook：使用运行时 hook，Epic 是一个虚拟机层面、以 Java Method 为粒度的运行时 hook 框架
- 支持 Android4.0-9.0

[Epic](https://github.com/tiann/epic) 的使用

- hook 哪个方法：`ImageView.setImageBitmap()`

核心代码

```java
public class ImageHook extends XC_MethodHook {

    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        super.afterHookedMethod(param);
        // 实现我们的逻辑
        ImageView imageView = (ImageView) param.thisObject;
        checkBitmap(imageView,((ImageView) param.thisObject).getDrawable());
    }

    private static void checkBitmap(Object thiz, Drawable drawable) {
        if (drawable instanceof BitmapDrawable && thiz instanceof View) {
            final Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            if (bitmap != null) {
                final View view = (View) thiz;
                int width = view.getWidth();
                int height = view.getHeight();
                if (width > 0 && height > 0) {
                    // 图标宽高都大于view带下的2倍以上，则警告
                    if (bitmap.getWidth() >= (width << 1) && bitmap.getHeight() >= (height << 1)) {
                        warn(bitmap.getWidth(), bitmap.getHeight(), width, height, new RuntimeException("Bitmap size too large"));
                    }
                } else {
                    final Throwable stackTrace = new RuntimeException();
                    view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                            int w = view.getWidth();
                            int h = view.getHeight();
                            if (w > 0 && h > 0) {
                                if (bitmap.getWidth() >= (w << 1) && bitmap.getHeight() >= (h << 1)) {
                                    warn(bitmap.getWidth(), bitmap.getHeight(), w, h, stackTrace);
                                }
                                view.getViewTreeObserver().removeOnPreDrawListener(this);
                            }
                            return true;
                        }
                    });
                }
            }
        }
    }


    private static void warn(int bitmapWidth, int bitmapHeight, int viewWidth, int viewHeight, Throwable t) {
        String warnInfo = new StringBuilder("Bitmap size too large: ")
                .append("\n real size: (").append(bitmapWidth).append(',').append(bitmapHeight).append(')')
                .append("\n desired size: (").append(viewWidth).append(',').append(viewHeight).append(')')
                .append("\n call stack trace: \n").append(Log.getStackTraceString(t)).append('\n')
                .toString();

        LogUtils.i(warnInfo);
    }

}

//在 Application.onCreate 中初始化
public class AppContext extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
            DexposedBridge.hookAllConstructors(ImageView.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                DexposedBridge.findAndHookMethod(ImageView.class, "setImageBitmap", Bitmap.class, new ImageHook());
            }
        });
    }

}
```

---
## 4.8 线上内存监控方案

- 常规方案
- LeakCanary 定制
- 线上监控完整方案

### 常规方案实现 1

设定场景线上 Dump：`Debug.dumpHprofData()`，流程如下：

1. 触发时机：超过最大内存的 80%
2. 内存 Dump 到文件
3. 回传到服务器
4. MAT 手动分析

方案特点:

- Dump 文件太大，和对象数正相关，可对文件进行裁剪。
- 上传失败率高，分析困难。
- 配合一定策略，有一定效果。

### 常规方案实现 2

实现方式：

- 将 LeakCanary 带到线上
- 在代码中预设泄露怀疑点
- 发现泄露回传分析结果

方案特点:

- 不适合所有场景，必须预设怀疑点
- 分析比较耗时，自身可能触发 OOM（LeakCanary自身缺陷）

理解 LeakCanary 原理

1. 监控控件生命周期，onDestory 添加 RefWatcher 检测
2. 二次确认，对象是否被回收，如果没有则断定发生内存泄露
3. 分析泄露，找引用链
4. LeakCanary 分为分析和监控两大组件

LeakCanary 定制（修改源码）：

- 预设怀疑点 --> 自动找怀疑点（谁的内存占用大，就怀疑谁）
- 分析泄露链路慢：分析每一个对象 --> 只分析 Retain Size 大的对象
- 分析导致 OOM： 将内存堆栈中的对象映射文件全部加载到内存当中 --> 内存堆栈中的对象进行裁剪，不全部加载到内存，只记录对象数量和内存占用

### 线上监控完整方案

1. 监控常规指标：待机内存、重点模块内存、OOM 率
2. 整体及重点模块：GC 次数，GC 时间
3. 对 LeakCanary 进行定制，线上自动化分析

---
## 4.9 内存优化技巧总结

- 优化大方向
  - 内存泄露
  - 内存抖动
  - Bitmap
- 细节
  - LargeHeap 属性，如果大家都开启了，那我们也开启吧
  - onTrimMemory 时，强制调整到主机面（牺牲一些用户替换）
  - 使用优化过的集合
  - 慎用 SharedPreference，第一次加载 SharedPreference 时，会将所有数据加载到内存中
  - 业务架构合理设计，比如需要选择省市区时，按需加载，不一次性全部加载

---
## 4.10 内存优化模拟面试

1. 你们优化内存的过程是如何的？
   1. 分析现状，确认问题
   2. 针对性优化：要举例子，比如如何解决内存抖动
   3. 效率提升，ARTHook，写规范文档
2. 你做了内存优化最大的感受是什么
   1. 磨刀不误砍柴工，学好工具，再应用
   2. 技术优化必须结合业务代码
   3. 系统化完善解决方案
3. 如何检测所有不合理的地方
   1. ART HOOK
   2. 重点强调区别，传统方案向 HOOK 方案的演进
