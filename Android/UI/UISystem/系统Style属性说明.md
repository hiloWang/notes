# 1 Window 属性

|  属性 | 说明  |
| ------------ | ------------ |
|`android:windowNoTitle` | 设置有没有标题栏true或者false|
|`android:windowFullScreen`  |  设置全屏true或者false |
|`android:windowIsFloating`|设置是否浮现在activity之上true或者false（窗体是否浮在下层之上），当windowIsFloating为true时，window默认为包裹内容的|
|`android:windowIsTranslucent`|设置window是否为透明，需要swipteDismiss时要设置|
|`android:background` |表示ViewTree中View的默认背景颜色|
|`android:windowBackground` |设置window的背景颜色，**注意background和windowBackground的区别，windowBackground是表示窗口的背景色，只作用与DecorView，而background作用于ViewTree中所有的View，当需要显示一个透明背景的窗口时，需要把两个都设置为透明色**|
|`android:windowContentOverlay`|在窗口的内容区域的前景放置重叠Drawable，通常是标题下面的阴影。|
|`android:backgroundDimEnabled` | 是否允许对话框的背景变暗？如果允许背景就变暗了（用在浮动窗口）|
|`android:backgroundDimAmount`|变暗的程度|
|`android:windowActionModeOverlay`|ActionMode显示在顶栏，而不是屏幕中的菜单。告诉appcompat你有个toolbar在屏幕顶上，它应该把ActionMode画到toolbar上面。|
|`android:windowDrawsSystemBarBackgrounds`|需要绘制系统操作栏背景|
| `android:windowFrame`|Dialog 是否有边框|
|`windowOverscan`| 是否要求窗体铺满整屏幕|
|`windowShowWallpaper`|是否显示壁纸|
|`android:windowAnimationStyle` |窗体切换时的动画样式|
| `windowCloseOnTouchOutside` |是否再点击外部可关闭|
| `windowContentOverlay`|（设置窗体内容背景）|



## 一般自定义Dialog的theme

```xml
     <style name="CommonFragmentDialog">
            <item name="android:windowAnimationStyle">@style/bottom_in_style</item>
            <item name="android:windowContentOverlay">@null</item>
            <item name="android:backgroundDimEnabled">true</item>
            <item name="android:backgroundDimAmount">0.4</item>
            <item name="android:windowIsFloating">true</item>
            <item name="android:windowNoTitle">true</item>
        </style>
```

另如果需要窗口全透明

```xml
        <style name="TransparentDialog" parent="CommonFragmentDialog">
            <item name="android:background">@android:color/transparent</item>
            <item name="android:windowBackground">@android:color/transparent</item>
            <item name="android:windowIsTranslucent">true</item>
        </style>
```

更多可以参考[官方文档](http://developer.android.com/intl/zh-cn/reference/android/view/Window.html)

#  2 Theme属性

```
      android:theme="@android:style/Theme.Dialog" // 将一个Activity显示为对话框模式
      android:theme="@android:style/Theme.NoTitleBar" // 不显示应用程序标题栏
      android:theme="@android:style/Theme.NoTitleBar.Fullscreen" // 不显示应用程序标题栏，并全屏
      android:theme="@android:style/Theme.Light" // 背景为白色
      android:theme="@android:style/Theme.Light.NoTitleBar" // 白色背景并无标题栏
      android:theme="@android:style/Theme.Light.NoTitleBar.Fullscreen" // 白色背景，无标题栏，全屏
      android:theme="@android:style/Theme.Black" // 背景黑色
      android:theme="@android:style/Theme.Black.NoTitleBar" // 黑色背景并无标题栏
      android:theme="@android:style/Theme.Black.NoTitleBar.Fullscreen" // 黑色背景，无标题栏，全屏
      android:theme="@android:style/Theme.Wallpaper" // 用[系统](http://www.2cto.com/os/)桌面为应用程序背景
      android:theme="@android:style/Theme.Wallpaper.NoTitleBar" // 用系统桌面为应用程序背景，且无标题栏
      android:theme="@android:style/Theme.Wallpaper.NoTitleBar.Fullscreen" // 用系统桌面为应用程序背景，无标题栏，全屏
      android:theme="@android:style/Translucent" // 半透明效果
      android:theme="@android:style/Theme.Translucent.NoTitleBar" // 半透明并无标题栏
      android:theme="@android:style/Theme.Translucent.NoTitleBar.Fullscreen" // 半透明效果，无标题栏，全屏
```

# 3 Menu

```
    android:icon
    引用一个要用作项目图标的 Drawable 类。
    android:title
    引用一个要用作项目标题的字符串。
    android:showAsAction
    指定此项应作为操作项目显示在操作栏中的时间和方式。
```

toolbar右边的三个点是 操作栏右侧的操作溢出菜单，与手机物理菜单按键作用一样
需要支持快速访问的重要操作，可以在相应的 `<item>` 中添加 `android:showAsAction=”ifRoom”` ，从而将此项提升到操作栏中。`</item>`


`android：showAsAction`总共有五个属性:

*   never：永远不会显示。只会在溢出列表中显示。
*   ifRoom：会显示在Item中，但是如果已经有4个或者4个以上的Item时会隐藏在溢出列表中。
*   always：无论是否溢出，总会显示。
*   withText：Title会显示。
*   collapseActionView：可拓展的Item。


# 4 引用

[系统R.attr.html说明](https://developer.android.com/reference/android/R.attr.html)