# 大厂资深面试官 带你破解Android高级面试——NOTE02

[大厂资深面试官 带你破解Android高级面试](https://coding.imooc.com/class/317.html)

---
## 5 Activity 相关

---
### 5.1 Activity 的启动流程

#### 考察什么？

- 是否熟悉 Activity 启动过程中与 AMS 的交互过程（高级）
- 是否熟悉 Binder 通信机制（高级）
- 是否了解插件化框架如何 Hook Activity 启动（高级）
- 阐述 Activity 专场动画的实现原理可加分（中级）
- 阐述 Activity 的窗苦现实流程可加分（高级）

#### 题目剖析

Activity 的启动流程

- 与 AMS 如何交互
- Activity 的参数和结果如何传递
- Activity 如何实例化
- Activity 生命周期如何流转
- Activity 窗苦如何展示

**Activity 跨进程启动**：

- 进程A：startActivity --> ActivityManagerProxy
- system_server 进程：AMS --> 解析 Activity 信息，处理启动参数，启动目标进程，绑定新进程
  - 从 zygote fork 新进程
- system_server 进程：AMS --> ApplicationThreadProxy --> scheduleLaunchActivity
- 目标进程 B：ApplicationThread 处理 AMS 调度。

**Activity 进程内启动**：

- 不需要 fork 新进程
- 插件化 hook 点：
  - startActivity --> (替换桩Activity) --> AMP
  - AMP --> scheduleLaunchActivity --> (替换回目标Activity) --> ApplicationThreadProxy

**Activity 的参数传递**：

- Binder 缓冲区大小限制
- 数据必须可以序列化

**Activity 实例化**：

- Instrumentation

**窗窗口如何显示**：

- newActivity
- ativity attach
- activity create
- activity start
- activity-restoreState
- activity-postCreate
- activity-resume
- ActivityThread.makeVisible

**转场动画**：

- 先记录位置信息、大小信息。
- 在新的界面，把位置信息和大小信息应用到新的 View 上。

### 5.2 如何跨 App 启动 Activity，有哪些注释事项

#### 考察什么？

- 是否了解如何启动外部应用的 Activity（初级）
- 是否了解如何防止自己的 Activity 被外部非正东启动（中级）
- 是否对拒绝服务漏洞有所了解（高级）
- 如何在开发是规避拒绝服务漏洞（高级）

#### 题目剖析

**何跨 App 启动 Activity方式**：

- 共享 uid 的 App：`startActivity(ComponentName)`
- `expodrted = true`
- 使用 intentFilter

**为允许外部启动的 Activity 加权限**

- manifest 中声明权限

**拒绝服务漏洞**：

- 攻击者 AActivity 攻击 BActivity
- AActivity 启动 BActivity 是，传递一个 Serializable，但是 BActivity 中没有对应的类。这时被启动 BActivity 去反序列化时，由于找不到对应的类，就会有异常，这就是拒绝服务漏洞。
- 解决方案是：`try-catch`
- 第三方库如果没有加 `try-catch`，我们得手动加上

---
### 5.3 如何解决 Activity 参数传递的类型安全以及接口复杂的问题？

#### 考察什么？

- 是否有代码优化与重构意识（高级）
- 是否对反射、注解处理器有所了解（高级）
- 是否具备有一定的框架设计能力（高级）

#### 题目剖析

- 类型安全:Bundle 的 K-V 不能在编译器保证类型
- 接口繁琐：启动 Activity 时参数和结果传递都依赖 Intent
- 等价的问法：设计一个框架，解决上述问题
- 面试不需要实现，大胆设想

**为什么 Activity 的参数存在类型安全问题**

- 使用 Intent 传递参数，类型安全需要人工保证

**解决方案 APT**：

- APT，生成模板代码
- 注入时机：ActivityLifecycleCallback
- onNewIntent 问题
- 支持 Fragment

**APT插件的开发注意事项**：

- 注意注解标注的类的继承关系
- 注意注解标志的类为内部类的情况
- 注解 Kotlin 与 Java 的类型映射问题
- 把握好代码生成和直接依赖的边界

**元编程**：

- APT：Dagger、ARouter
- Bytecode：RePlugin
- GFeneric
- Reflect
- Proxy：Retrofit

---
### 5.4 如何在代码的任意位置为当前 Activity 添加 View？

#### 考察什么？

- 如何在任意位置获取当前 Activity（中级）
- 是否对 Activity 的矿口有深入认识（中级）
- 前的内存泄露风险以及内存回收机制（高级）
- 是否能够深入需求评估技术方案的合理性（高级）

#### 题目剖析

`任意位置`添加 View

- 如何获取当前 Activity
- 如何在不影响正常 View 展示的情况下添加 View
- 既然能添加，也要能移除，如何移除？
- 这样做的目的是什么？添加全局的 View 是否更合适？

**如何获取当前 Activity**：

- ActivityLifecycleCallbacks
- 注意避免泄露

**内存回收机制**：

- GC Roots
- 四种引用

**添加 View**：

- `window.addContentView`
- `android.R.id.content`

**替代方案：添加全局的 View**：

- 明确需求选择合适的方案

---
### 5.5 如何实现类似微信右滑返回的效果？

#### 考察什么？

- 是否熟练掌握手势和动画的运用（中级）
- 是否了解窗口绘制的高级原理（高级）
- 是否对 Activity 的窗苦有深入认识（高级)

#### 题目剖析

- Activity 的侧滑返回
- Fragment 的侧滑返回

**Fragment 的侧滑返回**：

- 对 Fragment 的控制
- 不涉及 Window 的控制，只是 View 级别的操作
- 实现 View 跟随手势滑动移动的效果
- 实现手势结束后判断取消或返回执行归位动画

**Activity 的侧滑返回**：

- 要处理好 Widnow，设置透明的 Window
- Activity 联动，多 Task，截图占位

**透明的 Activity 对声明周期的影响**：

- 底部的 Activity 处理 Pause 状态，而不是 Stop 的状态。

**SDK 设计**：

- 通过组合而非继承实现功能
- 窗口透明：反射调用 Activity 的 comvertToTranslucent 方法，动态设置 Activity 的透明

---
## 6 Handler 相关

---
### 6.1 Android 中为什么非 UI 线程不能更新 UI？

#### 考察什么？

- 是否理解线程安全的概念（中级）
- 是否能理解 UI 线程的工作机制（高级）
- 是否熟悉 SurfaceVIew 实现高帧率的原理（高级）

#### 题目剖析

- UI 线程的工作机制
- 为什么 UI 设计成线程不安全的
- 非 UI 线程就一定不能更新 UI 吗？

**UI 线程是什么**：

- zygote --> app --> `ActivityThread.main` --> `Looper.loop`

**UI 线程如何工作**：

- Looper 就是引擎，app 的驱动力。
- 百变不离其宗：任何 UI 模型都是`生产者消费者模型`
  
**如何把 UI 线程设计成线程安全的**：

- 加锁

**为什么 UI 设计成线程不安全的**：

- UI 的可变性是高频的
- UI 对响应时间非常敏感，要求必须高效

**非 UI 线程就一定不能更新 UI 吗？**

- 在 View 树没有构建完成时
- post、postInvalidate 等方法
- SurfaceView 可以
- GLSurfaceView：GLThread
- TextureView

**SurfaceView**：

- lockCanvas
- draw
- unlockCanvasAndPost
- GLSurfaceView：GLThread
- TextureView

---
### 6.2 Handler 发送消息的 Delay 靠谱吗？

#### 考察什么？

- 是否清除 UI 时间相关的任务，如动画的设计实现原理（高级）
- 是否对 Looper 的消息机制有深刻理解（高级）
- 是否做过 UI 过度绘制或其他消息机制的优化（高级）

#### 题目剖析

- 大于 Handler Looper 的周期时基本可靠（>50ms）
- Looper 负载越高，任务越容易积压，进而导致卡顿
- 不要用 Handler 的 delay 作为计时的依据
- HandlerThread 的 delay 可以认为是可靠的

**MessageQueue 如何处理消息**：

- MessageQueue：enquequeMessage --> next
- Native：wake --> pollOnce --> epoll_wait

**队列优化**：

- 相同类型消息的取消
- 互斥消息的取消
- 消息复用：`Message.obtain()`
- idleHandler
- HandlerThread

---
### 6.3 主线程的 Looper 为什么不会导致应用 ANR ？

#### 考察什么？

- 是否了解 ANR 的产生条件
- 是否对 Android APP 的进程允许机制有深入理解（高级)
- 是否对 Looper 的消息机制有深刻理解（高级）
- 是否对 IO 多路复用有一定的认识（高级)

#### 题目剖析

- ANR 如何产生？
- Looper 工作机制？
- Looper 不会导致应用 ANR 的本质原因？
- Looper 为什么不会导致 CPU 占用率高？

**ANR 类型**

- Service Timeout
  - 前台服务 20s
  - 后台服务 200s
- Broadcast Timeout
  - 前台 10s
  - 后台 60s
- ContentProvider Timeout：10s
- InputDispatrching Timeout：5s

**Service Timeout 的产生**

- startServiceLocked
- startServiceInnerLocked
- birngUpServiceLocked
- realStartServiceLocked
- bumpServiceExecutingLocked
- `scheduleServiceTimeoutLocked` 买了一个定时炸弹，规定时间内没有拆除则会引爆
- 定时炸弹的移除：`serviceDoneExecutingLocked` 内部会 `removeMesage(ActivityManagerService.SERVICE_TIMEOUT_MSG)`

```java
private final void realStartServiceLocked(ServiceRecord r, ProcessRecord ap, boolean execInFg) throw RemoteExecetion{
    ...
    bumpServiceExecutingLocked(r,execInFg,"create");
    ...
    app.thread.scheduleCrateService(...)
    ...
    serviceDoneExecutingLocked(r, isDestroying, isDestroying)
    ...
}
```

**主线程究竟在干什么**？

- Looper 是一个进程上的概念
- ANR 只是 Looper 中的一小部分，只是对某一个环节中开发者占用时间的健康

**Looper 为什么不会导致 CPU 暂用率高**

- Looper 不会空转
- epool_wait 函数
- io 多路复用

---
### 6.4 如何自己实现一个简单的 Handler - Looper 框架？

#### 考察什么？

- 是否对 Looper 的消息机制有深刻的理解（高级）
- 是否对 Java 并发包中提供的队列有较为清晰的认识（高级）
- 是否能够允许所学知识设计出一套类似的框架（高级）

#### 题目剖析

- 简单：表面可以运用 Java 标准库当中的组件
- 覆盖关键路径即可，突出重点
- 分析 Android 为什么要单独实现一套
- 仍然着眼于阐述 Handler-Looper 的原理

**Handler 的核心能力**：

- 线程间通信
- 消息的延迟执行

**Looper 的核心能力**：

- 核心动力，转起来
- 死循环

**MessageQueue**：

- 持有消息
- 消息按时间排序
- 队列为空时阻塞读取
- 头阶段有延时可以定时阻塞

**Android 为什么不使用 DelayQueue**：

- 没有提供合适的 remove 机制
- 自己实现，更大的自由度，特别是与 native 层交互
- 有加锁机制，没有针对单线程做优化