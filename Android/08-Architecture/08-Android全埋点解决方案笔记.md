# [《Android全埋点解决方案》](https://book.douban.com/subject/33400077/)

选择买点方案需要考虑的几点：

- 效率：不影响现有业务。
- 兼容性：Android SDK版本、Java 和 Kotlin、Lamdba、DataBinding、Fragment等等都需要考虑。
- 扩展性：业务的不断迭代，全自动采集和精细化采集控制力度的要求。

Android全埋点 SensorsAnalytics SDK：

- [sensorsdata/sa-sdk-android](https://github.com/sensorsdata/sa-sdk-android)

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

**动态权限申请导致的统计不准确问题处理**：在申请权限时，系统会开启新的对话框类型的 Activity 处理权限申请，无论是用户解决还是允许，对话框消失后，申请界面对应的 Activity 都会再次执行 onResume 方法，这会导致统计不准确(多统计了一次 PV)。解决方案是，在弹框之前先过滤掉当前 Activity 的 PV 统计（以当前 Activity 的 Class 标识），待权限申请结束后，再次恢复当前 Activity 的 PV 统计。

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

### `$AppClick` 事件介绍

`$AppClick` 事件即 View 的点击事件，目前对与 `$AppClick` 事件自动化埋点方案整体上可以分为动态方法和静态方案，两种方案各有优缺点：

- 动态方法：实现相对简单，但对效率有影响，兼容性也是需要考虑的问题。
- 静态方案：由于是静态织入埋点代码，其效率较高，更容易扩展，兼容性也较好。

### 代理 `View.OnClickListener`

动态埋点方式之一 **代理 View.OnClickListener** 的思路是：当一个界面的布局初始化完后，遍历 View 树，对于该 View 树中的每个 View ，如果被设置了 `View.OnClickListener`，则通过反射获取其 `View.OnClickListener` 替换为我们自定义的 WrapperOnClickListener，WrapperOnClickListener 的主要功能是统计 Click 事件，然后调用原来的 `View.OnClickListener`。

技术要点：

- 使用 `Application.registerActivityLifecycleCallbacks()` 监听每个界面的打开。
- 对每个界面遍历的时机：`ViewTreeObserver.OnGlobalLayoutListener`，每当 View 树发生布局变化时，我们都遍历一次 View 树，代理每个 View 的 `View.OnClickListener`。
- 遍历的根节点：DecorView，这是每个 View 树的根 View，最佳选择。
- 扩展支持所有的控件：
  - 有些控件的交互不是通过 `View.OnClickListener` 的，比如 RatingBar、RadioGroup 等等，这些都需要做特殊处理。
  - 获取对应控件的内存，比如 TextView 极其子类的文本内容。
  - 根据资源 id 获取资源名称：`Resource.getResourceEntryName()`

具体实现方案参考 [AutoTrackAppClick1](https://github.com/wangzhzh/AutoTrackAppClick1)。也可以参考 [AutoTrace](https://github.com/fengcunhan/AutoTrace/)。

代理 View.OnClickListener 的缺点

- 使用了反射替换 View.OnClickListener，性能可能会有影响。
- 每一次布局更改，都需要动态遍历 View 树。
- 无法直接支持浮动在 Activity 之上的 View 的点击，比如 Dialog、PopupWindow 等。

## 4 `$AppClick` 全埋点方案 2：代理 `Window.Callback`

该方案整体上与代理 `View.OnClickListener` 相差不大，区别在于这里我们代理的是 `Window.Callback`，每个 Activity 都对应一个 Window，Window 中通过 Callback 用于监听各种事件（Activity 是默认的实现者），比如 TouchEvent，View 树中的所有触摸事件都会先回调到 `Window.Callback`，我们可以利用相同的手段，包装一下 `Window.Callback`，然后在 dispatchTouchEvent 回调用统计事件，当收到一个 ACTION_UP 事件后，则初步认为是一个点击事件结束，然后尝试去统计。

具体实现方案参考 [AutoTrackAppClick2](https://github.com/wangzhzh/AutoTrackAppClick2)。

代理 `Window.Callback` 的缺点

- 每一次点击事件，都需要动态遍历 View 树，找到对应的 View，性能影响较大。
- 无法直接支持浮动在 Activity 之上的 View 的点击，比如 Dialog、PopupWindow 等。

## 5 `$AppClick` 全埋点方案 3：代理 `View.AccessibilityDelegate`

### Accessibility 介绍

关于 accessibility 具体参考[accessibility](https://developer.android.com/guide/topics/ui/accessibility)。

### 方案

利用 accessibility，我们也可以检测到用户的点击事件，下面是 View 的 performClick 方法实现，我们知道最终 View 的 OnClick 都是通过这个方法触发的，可以看到该方法最后一行代码 `sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_CLICKED);` 发送了一个 AccessibilityEvent，最终方法会调用到 mAccessibilityDelegate 的 sendAccessibilityEvent 方法，而这里的 mAccessibilityDelegate 就是我们需要 Hook 的地方。

```java
    public boolean performClick() {
        final boolean result;
        final ListenerInfo li = mListenerInfo;
        if (li != null && li.mOnClickListener != null) {
            playSoundEffect(SoundEffectConstants.CLICK);
            li.mOnClickListener.onClick(this);
            result = true;
        } else {
            result = false;
        }

        sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_CLICKED);
        return result;
    }

    public void sendAccessibilityEvent(int eventType) {
        if (mAccessibilityDelegate != null) {
            mAccessibilityDelegate.sendAccessibilityEvent(this, eventType);
        } else {
            sendAccessibilityEventInternal(eventType);
        }
    }
```

该方案整体上与代理 `View.OnClickListener` 相差不大，区别在于这里我们代理的是 `mAccessibilityDelegate`，而且代理 `mAccessibilityDelegate`  只能用来帮助我们统计点击事件的，向 RatingBar 之类的特殊控件，还是需要采取特殊处理。

具体实现参考 [AutoTrackAppClick3](https://github.com/wangzhzh/AutoTrackAppClick3)。

该方案的缺点：

- 使用了反射去 hook mAccessibilityDelegate，效率可能有影响，还可能会引发兼容性问题。
- 无法支持浮动在 Activity 之上的 View 的点击，比如 Dialog、PopupWindow 等。

## 6 `$AppClick` 全埋点方案 4：透明层

该方案整体上与代理 `View.OnClickListener` 相差不大，区别在于我们不再去代理任何 Listener，而是在每个 Activity 启动后在 DecorView 的最上层加一个透明的 View，该 View 的唯一功能就是检测手势事件进行统计。需要注意的是，务必保证我们添加的透明层是在 DecorView 的最上层。

具体实现参考 [AutoTrackAppClick4](https://github.com/wangzhzh/AutoTrackAppClick4)。

该方案的缺点：

- 无法支持浮动在 Activity 之上的 View 的点击，比如 Dialog、PopupWindow 等。
- 每一次采集，都需要去遍历整个 View 树找到手势事件对应的 View。效率较低。

至此，静态采集方案介绍完毕。
