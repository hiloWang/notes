[国内Top团队大牛带你玩转Android性能分析与优化](https://coding.imooc.com/class/308.html)

# 4 App内存优化

>本章从原理开始讲起，首先介绍Java及Android的内存管理机制，接下来手把手带领大家进行内存抖动、泄露的解决实战，同时通过ArtHook的方式优雅解检测出App所有不合理的图片。

---
## 4.2 内存优化介绍及工具选择

### 内存是个大问题但是缺乏关注

- 表现比较隐秘
- Java 自带了内存回收机制
- 压死骆驼的往往是最后一个根稻草

### 内存问题

- 内存抖动：图形分析工具呈现锯齿状，GC 导致卡顿
- 内存泄露：可用内存减少，频繁 GC
- OOM：应用奔溃

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
## 4.6 全面理解MAT

- [ ] todo

---
## 4.7 ARTHook优雅检测不合理图片

- [ ] todo

---
## 4.8 线上内存监控方案

- [ ] todo

---
## 4.9 内存优化技巧总结

- [ ] todo

---
## 4.10 内存优化模拟面试

- [ ] todo