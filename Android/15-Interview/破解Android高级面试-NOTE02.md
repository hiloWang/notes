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

- [ ] todo

---
## 7 内存优化

- [ ] todo

---
## 8 插件化和热修复

- [ ] todo

---
## 9 优化相关

- [ ] todo

---
## 10 拆解需求设计架构

- [ ] todo

---
## 11 课程总结

- [ ] todo
