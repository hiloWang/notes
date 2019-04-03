# 管理系统UI

---
## 1 SystemBar

系统栏是专用于显示通知的屏幕区域，包括状态栏和导航栏：

- StatusBar 状态栏
- NavigationBar 导航栏

而 StatusBar 跟 NavigationBar 统称 SystemBar，

管理系统UI主要包括两个方面：

1. 控制它们的显示与隐藏，
2. 控制它们的透明与否及背景。

通过隐藏 SystemBar 可以达到全屏等的效果。

---
## 2 SystemBar 相关 API

### 4.0之前设置全屏的方式

- 使用全屏的主题
- `getWindow().addFlag(WindowManager.LayoutParams.FLAG_FULLSCREEN);`


示例代码：
```java
     private void setFullscreen(boolean on) {
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_FULLSCREEN;
            if (on) {
                winParams.flags |= bits;
            } else {
                winParams.flags &= ~bits;
            }
            win.setAttributes(winParams);
        }
```
4.0 之前采用的全屏方式是无法隐藏 NavigationBar 的，因为 NavigationBar 是在 4.0 以后才引入。使用这种方式设置全屏的特点是，**离开了 App 后(按HOME返回桌面)再进入 App 时，依然处于全屏模式，只能清除掉全屏标志位才能退出全屏。**

除此之外通过以下 Flag 也可以让使 Activity 的布局使用整个屏幕，但是 **状态栏会显示到 Activity 上方并遮盖部分布局**：

```java
    //下面 flag 的 api 等级为 1
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
```

- [FLAG_LAYOUT_IN_SCREEN](https://developer.android.com/reference/android/view/WindowManager.LayoutParams.html#FLAG_LAYOUT_IN_SCREEN) 将窗口放置在整个屏幕中，忽略边界周围的装饰(例如状态栏)
- [FLAG_LAYOUT_NO_LIMITS](https://developer.android.com/reference/android/view/WindowManager.LayoutParams.html#FLAG_LAYOUT_NO_LIMITS): 允许窗口扩展到屏幕外

### 4.0 之后 SystemBar 相关 API 的一些变化

- 在 3.0(API 11) 中，添加了一个重要的方法：`setSystemUiVisibility(int)`，用于控制包括 StatusBar 在内的一些窗口装饰元素的显示
- 在 4.0(API 14) 中，Andorid 引入了 NavigationBar，并添加了一个 Flag：`SYSTEM_UI_FLAG_HIDE_NAVIGATION` 用于控制 Navigatoin Bar 的显示。

另外还引入一些 Flag 用于调整 SystemBar：

- `View.SYSTEM_UI_FLAG_LOW_PROFILE` api 14 引入：不会使Status Bar和Navigation Bar消失，而是会使它们变暗，降低它们对视觉的干扰，使用户可以专注于应用的内容，但仍可响应用户的交互，当和它们的交互发生时，会退出Low Profile的状态。
- `View.SYSTEM_UI_FLAG_FULLSCREEN`  api 16 引入：控制 StatusBar 的显示
- `View.SYSTEM_UI_FLAG_HIDE_NAVIGATION` api 14 引入：控制 NavigationBar 的显示
- `View.SYSTEM_UI_FLAG_IMMERSIVE`   api 19 引入：用于设置沉浸模式
- `View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY`   api 19 引入：用于设置粘性沉浸模式
- `View.SYSTEM_UI_FLAG_LAYOUT_STABLE`   api 16 引入：固定布局的大小，不因 SystemBar 的显示与隐藏而发生变化
- `View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN`   api 16 引入：让布局 UI 延伸到状态栏下方，状态栏显示在布局的上方，并挡住了部分布局
- `View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION`  api 16 引入：让布局 UI 延伸到导航栏下方，导航栏显示在布局的上方，并挡住了部分布局

这些 Flag 使用的方法就是调用 View 的 setSystemUiVisibility 方法，多个Flag可以使用 `|` 进行位操作来组合。

通过 `SYSTEM_UI_FLAG_FULLSCREEN` 和 `SYSTEM_UI_FLAG_HIDE_NAVIGATION` 标志就可以让 StatusBar 和 NavigationBar 隐藏掉。但是这些都是很重要的功能，所以只要用户与 UI 发生交换就会是这些 Flag 失效，主要表现为：

- `View.SYSTEM_UI_FLAG_HIDE_NAVIGATION` 只要用户触碰了 UI，此标志就会失效
- `View.SYSTEM_UI_FLAG_FULLSCREEN 和 View.SYSTEM_UI_FLAG_LOW_PROFILE` 当用户在状态栏位置从上往下滑动时，就会使这两个 Flag 失效

上述情况适合视频播放的场景，但对于其他一些场景可能就不适合了，比如读书软件，用户需要随时滑动屏幕。

所以，在4.4(API 19)中引入了沉浸模式 `View.SYSTEM_UI_FLAG_IMMERSIVE` 和 `View.SYSTEM_UI_FLAG_IMMERSIVE_STICK`。

- 在 IMMERSIVE 模式中，用户的普通交互并不会使系统退出 IMMERSIVE 模式，如果要退出 IMMERSIVE 模式，需要在屏幕的顶部或底部向内滑动。这可以使用户专注于内容，但退出方式并不像 LEAN BACK 模式那么明显，所以在第一次进入 IMMERSIVE 时，系统会弹出一个 UI 提醒退出的方法。`SYSTEM_UI_FLAG_IMMERSIVE、SYSTEM_UI_FLAG_IMMERSIVE_STICK` 需要和 `SYSTEM_UI_FLAG_FULLSCREEN`、`SYSTEM_UI_FLAG_HIDE_NAVIGATION` 一起使用。
- IMMERSIVE_STICKY 和 IMMERSIVE 的区别是，在 IMMERSIVE 中，用户从屏幕顶部或底部向内滑动时会退出 IMMERSIVE 模式，需要手动控制再次进入IMMERSIVE 模式，而在 IMMERSIVE_STICKY 模式中，同样的操作只会使系统以半透明方法显示 System UI 方便用户操作，并会在一段时间后自动隐藏，此时并不会引起 `onSystemUiVibilityChanged` 的调用。

### 控制 SystemUI 的透明状态与边衬区域

除了控制 System UI 的显示和隐藏外，还可以使它们变成透明的。

在4.4(API 19)中引入了 `WindowManager.LayoutParams.FLAG_TRANSUCENT_STATUS` 和 `WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION` 用于控制 System UI 变透明，这两个 Flag 分别对应于 `windowTranslucentStatus` 和 `windowTranslucentNavigation` 两个 attr，并同时提供了相应的 Theme（这些 Theme 都没有 ActionBar），当使用这两个 Flag 时

- `SYSTEM_UI_FLAG_LAYOUT_STABLE`
- `SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN`
- `SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION`

三个 Flag 会被自动添加。

### 监听 SystemBar 的变化

通常情况下我们需要能够控制 SystemBar 的显示与隐藏，这个时候就需要监听 SystemBar 的状态。
通过 **OnSystemUiVisibilityChangeListener** 就可以对 SystemBar 的状态进行监听。
```java
    protected void onCreate(Bundle savedInstance) {
        mDecorView.setOnSystemUiVisibilityChangeListener(new OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == 0) {
                // SystemBar 处于显示状态
                } else {
                // SystemBar 处于隐藏状态
                }
            }
    });
```
与此方法有关的Flag是：

*   FULLSCREEN（4）
*   HIDE_NAVIGATION（2）
*   LOW_PROFILE（1）

`onSystemUiVisibilityChange(int visibility)` 中的 `visibility` 是 `LOW_PROFILE、FULLSCREEN` 跟 `HIDE_NAVIGATION` 这三个值的和。

---
## 3 隐藏 ActionBar

ActionBar 本身就有显示与隐藏的功能：
```java
    getActionBar().hide();
    getActionBar().show();
```
**一般我们使用的AppCompatActivity，所以应该使用`getSupportActionBar()`**

---
## 参考资料

- [Managing the System UI](https://developer.android.com/training/system-ui/index.html)
- [与 Status Bar 和 Navigation Bar 相关的一些东西](http://angeldevil.me/2014/09/02/About-Status-Bar-and-Navigation-Bar/)
- [WindowManager.LayoutParams官方文档](https://developer.android.com/reference/android/view/WindowManager.LayoutParams.html)
- APIDemo-SystemUIModes