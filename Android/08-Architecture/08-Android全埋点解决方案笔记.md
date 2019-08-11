# [《Android全埋点解决方案》](https://book.douban.com/subject/33400077/)

选择买点方案需要考虑的几点：

- 效率：不影响现有业务。
- 兼容性：Android SDK版本、Java 和 Kotlin、Lamdba、DataBinding、Fragment等等都需要考虑。
- 扩展性：业务的不断迭代，全自动采集和精细化采集控制力度的要求。

## 1 `$AppViewScreen` 全埋点方案

`$AppViewScreen` 事件，即页面浏览事件，在 Android 中，页面流浏览事件就是指切换不同的 Activity/Fragment。

**关键技术**：

- Application.registerActivityLifecycleCallbacks()
- 在 ActivityLifeCycleCallback 的 onResume 中统计页面打开

**采集粒度**：

- 类名
- 触发时间
- 页面 Title
- ...等

**细节完善**：在申请权限时，系统会开启新的对话框类型的 Activity 处理权限申请，无论是用户解决还是允许，对话框消失后，申请界面对应的 Activity 都会再次执行 onResume 方法，这会导致统计不准确(多统计了一次 PV)。解决方案是，在弹框之前先过滤掉当前 Activity 的 PV 统计（以当前 Activity 的 Class 标识），待权限申请结束后，再次恢复当前 Activity 的 PV 统计。

## 2 `$AppStart、$AppEnd` 全埋点方案

`$AppStart、$AppEnd` 归根结底是判断当前 App 是处于前台还是后台，对此 Android 系统本身没有提供任何 API。但我们可以通过间接的方式来实现 App 前台切换的判断与监听。

[AndroidProcess](https://github.com/wenmingvs/AndroidProcess) 项目对各种间接方案做了解析与对比。目前比较理想的方案是基于对 ActivityLifecycleCallbacks 的封装。

监听和判断当前 App 是处于前台还是后台，有以下几个问题需要关注：

- 应用程序如果有多个经常如何判断？
- 应用程序如果发生崩溃如何判断？
- 应用程序如果被强杀了如何判断？

**应用程序如果有多个经常如何判断**？该问题归根到底其实是多进程如何通信的问题。可以使用 ContentProvider + SharedPreferences 方案，ContentProvider 还提供了用于监听数据变化的 ContentObserver。

**应用程序如果发生崩溃或被强杀了如何判断**？对此可以引入 Session 概念，对于一个应用程序，如果其页面推出，且在 30s 内没有新的页面打开，则可以认为其已经处于后台了（此时触发一次 AppEnd 事件），当一个页面显示出来，如果与上一个页面的退出时间的间隔超过 30s，可以认为该程序重新处于了前台（触发一个 AppStart 事件），对于页面的打开我们则可用通过注册 ActivityLifecycleCallbacks 回调来判断。

需要注意的点：

- Activity 之间可能是多进程的，前后台事件存储和 30s 倒计时交互也应该是支持多进程共享的。
- 如果程序发生崩溃或被强杀了，则在下一次启动时应该补发一次 AppEnd 事件，这是为了记录事件的完整性，因为程序发生崩溃或被强杀了是无法触发 AppEnd 事件的。具体的做法是，在页面启动时（onStart），判断上一个页面退出时间间隔是否超过 30s，如果超过则去检查之前是否记录了 AppEnd 事件，如果没有则补发一次 AppEnd 事件，然后再走正常流程触发 AppStart 和 AppViewScreen 事件。

具体实现方案参考 [AutoTrackAppStartAppEnd](https://github.com/wangzhzh/AutoTrackAppStartAppEnd)。

## 3 `$AppClick` 全埋点方案 1：代理 `View.OnClickListener`

`$AppClick` 事件即 View 的点击事件，目前对与 `$AppClick` 事件自动化埋点方案整体上可以分为动态方法和静态方案。

- 动态方法：实现相对简单。
- 静态方案：由于是静态织入埋点代码，其效率较高，更容易扩展，兼容性也较好。

动态埋点方式之一 **代理 View.OnClickListener** 的思路：当一个界面的布局初始化完后，遍历 View 树，如果某个 View 被设置了 View.OnClickListener，则通过反射获取其 View.OnClickListener 替换为我们自定义的 WrapperOnClickListener，WrapperOnClickListener 的主要功能是通知 Click 事件，然后调用原来的 View.OnClickListener。

技术要点：

- 遍历的时机：ViewTreeObserver.OnGlobalLayoutListener，每当 View 树发生布局变化时，我们都遍历一次 View 树，代理每个 View 的 View.OnClickListener。
- 遍历的根节点：DecorView，这是每个 View 树的根 View，最佳选择。
- 扩展支持所有的控件：
  - 有些控件的交互不是通过 View.OnClickListener 的，比如 RatingBar、RadioGroup 等等，这些都需要做特殊处理。
  - 获取对应控件的内存，比如 TextView 极其子类的文本内容。
  - 根据资源 id 获取资源名称：`Resource.getResourceEntryName()`

具体实现方案参考 [AutoTrackAppClick1](https://github.com/wangzhzh/AutoTrackAppClick1)。也可以参考 [AutoTrace](https://github.com/fengcunhan/AutoTrace/)。

代理 View.OnClickListener 的缺点

- 使用了反射替换 View.OnClickListener，性能可能会有影响。
- 每一次布局更改，都需要动态遍历 View 树。
- 无法直接支持浮动在 Activity 之上的 View 的点击，比如 Dialog、PopupWindow 等。

## 4 `$AppClick` 全埋点方案 2：代理 `Window.Callback`

- [ ] todo
