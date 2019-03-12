## 1 PopupWindow介绍

使用PopupWindow可在屏幕上自由的添加子窗口，对与位置的控制比Dialog更加灵活。

## 2 使用方式

### 构建方法

```java
    // 创建一个空的PopupWindow
    PopupWindow ()
    // 创建一个PopupWindow，指定视图
    PopupWindow (View contentView)
    // 创建一个PopupWindow，指定宽高
    PopupWindow (int width,int height)
    // 创建一个PopupWindow，指定视图、宽高和是否可获取焦点
    PopupWindow (View contentView,
                  int width,
                  int height,
                  boolean focusable)
```

**显示PopuWindow必要的三个条件**

```java
    // 因为PopupWindow没有默认布局所以必须指定宽高
    void setHeight (int height)
    void setWidth (int width)
    // 需要显示的内容
    void setContentView (View contentView)
```

上面三个属性必需设置，否则无法显示 PopupWindow，示例：下面创建了一个包裹内容的PopupWindow。

```java
    PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
```

### 显示PopupWindow

```java
    //在指定View的下方显示
    void showAsDropDown (View anchor,int xoff, int yoff)
    //当前窗口(parent所属窗口，parent主要用于获取WindowToken)的任意位置(不包括状态栏)
    void showAtLocation (View parent,int xoff,int yoff,int gravity)
```

### 属性

```java
    // 设置是否可被点击
    void setTouchable (boolean touchable)
    //多点触控
    void setSplitTouchEnabled (boolean enabled)
    // CheekPress事件处理，默认为false, 即不忽略，当物体触摸在屏幕上的尺寸超过手指尺寸范围, 将被判定为CheekPress事件(脸颊点击).
    void setIgnoreCheekPress ()
    // 设置外部是否可被点击，如果为true点击PopupWindow外部区域可以取消PopupWindow
    void setOutsideTouchable (boolean touchable) 
    // Android5.0(API22)后添加的方法, 默认为true. 为true时将不会与导航栏重叠.
    void setAttachedInDecor (boolean enabled)
    //一般控件都不需要焦点. 但是输入框EditText需要先获取焦点才能输入. 最重要的是当PopupWindow可获取焦点时按下手机返回键将不会销毁当前
    //Activity而是关闭当前PopupWindow. 所以我们一般还是设置为true. 更加符合用户操作逻辑. 该方法为true时同时拥有setOutsideTouchable(true)的作用.
    void setFocusable (boolean focusable)
    //设置背景
    void setBackgroundDrawable (Drawable background)
    //该方法只在showAsDropDown()方法执行后才有效. 可以判断PopupWindow和附着View anchor谁的Y轴坐标小
    boolean isAboveAnchor ()
    //默认对齐方式从View anchor的左下角变成左上角
    void setOverlapAnchor (boolean overlapAnchor)
    //PopupWindow默认是不会超出屏幕边界的. 但是如果该方法为false时会采用精准位置, 能超出屏幕范围.
    void setClippingEnabled (boolean enabled)
    //设置显示动画
    void setAnimationStyle (int animationStyle)
```

如果在 android6.0 以下无法点击外部取消 Popupwindow. 可以通过设置背景来解决这个 Bug：

```java
popupWindow.setBackgroundDrawable(new BitmapDrawable());
```

### 更新

```java
    //该方法不能更新PopupWindow的宽高, 只能更新PopupWindow的状态. 例如更新Focusable和OutsideTouchable
    void update ()
    //更新PopupWindow的宽高
    void update (int width,int height)
```

## 3 PopupWidnow实现解析

PopupWidnow是一个子窗口，必需依附在一个父窗口上，而PopupWidnow内部的View并没有Window对象的包装，而是根据setContentView方法设置的View创建一个PopupDecorView，然后直通通过WindowManager添加到窗口系统中。

```java
        private PopupDecorView createDecorView(View contentView) {
            final ViewGroup.LayoutParams layoutParams = mContentView.getLayoutParams();
            final int height;
            if (layoutParams != null && layoutParams.height == WRAP_CONTENT) {
                height = WRAP_CONTENT;
            } else {
                height = MATCH_PARENT;
            }
    
            final PopupDecorView decorView = new PopupDecorView(mContext);
            decorView.addView(contentView, MATCH_PARENT, height);
            decorView.setClipChildren(false);
            decorView.setClipToPadding(false);
    
            return decorView;
        }
```

PopupWindow的大小也直接由其内部创建的`WindowManager.LayoutParams`决定。

## 引用

- [最详细的PopopWindow分析](https://juejin.im/post/58ed82c3a22b9d0063469e98)
- [浅谈PopupWindow在Android开发中的使用](http://www.jianshu.com/p/825d1cc9fa79)
