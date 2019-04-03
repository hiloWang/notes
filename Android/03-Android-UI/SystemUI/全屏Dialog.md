## 实现全屏的 Dialog

Dialog 和 Activity一样，内部都有一个窗口，用于控制视图的显示，默认情况下 Dialog 并不是全屏的，因为 Dialog 应用的 theme 中包含的 `android:windowIsFloating` 属性值为 false，当`windowIsFloating=true` 时，PhoneWindow 内的 ViewTree 测量的起始测量模式就是 `wrap_content` 的。

PhoneWindow 的源码中对 isFloating 的处理：

```java
     mIsFloating = a.getBoolean(com.android.internal.R.styleable.Window_windowIsFloating, false);
            //.......
            if (mIsFloating) {
                setLayout(WRAP_CONTENT, WRAP_CONTENT);
                setFlags(0, flagsToUpdate);
            } else {
                setFlags(FLAG_LAYOUT_IN_SCREEN|FLAG_LAYOUT_INSET_DECOR, flagsToUpdate);
            }

Window 中 setLayout 方法

    public void setLayout(int width, int height){
            final WindowManager.LayoutParams attrs = getAttributes();
            attrs.width = width;
            attrs.height = height;
            if (mCallback != null) {
                mCallback.onWindowAttributesChanged(attrs);
            }
    }
```

了解 ViewTree 绘制过程的话，在 ViewRootImpl 的 performTraversals 方法中会调用 getRootMeasureSpec 方法，为 ViewTree 的 width 和 height 生成起始的 mode 和 Size，getRootMeasureSpec 的 rootDimension 参数其实就是 Window 的 setLayout 方法设置的。

```java
     private int getRootMeasureSpec(int windowSize, int rootDimension) {
            int measureSpec;
            switch (rootDimension) {
    
            case ViewGroup.LayoutParams.MATCH_PARENT:
                // Window can't resize. Force root view to be windowSize.
                measureSpec = MeasureSpec.makeMeasureSpec(windowSize, MeasureSpec.EXACTLY);
                break;
            case ViewGroup.LayoutParams.WRAP_CONTENT:
                // Window can resize. Set max size for root view.
                measureSpec = MeasureSpec.makeMeasureSpec(windowSize, MeasureSpec.AT_MOST);
                break;
            default:
                // Window wants to be an exact size. Force root view to be that size.
                measureSpec = MeasureSpec.makeMeasureSpec(rootDimension, MeasureSpec.EXACTLY);
                break;
            }
            return measureSpec;
        }
```

那么使 Dialog 全屏的方法如下：

```java
    public class FullScrreenDialog extends Dialog {

        public FullScrreenDialog(Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //1:不需要title
            getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            //2:设置布局
            View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_full_screen, null);
            setContentView(view);
            //3:设置窗口的颜色为透明色
            getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));
            //4:把测量的参数设置为MATCH_PAREN
            getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        }
    }
```

需要注意的是步骤3和4一定要再步骤2之后设置，这是因为在调用` setContentView(view);`的逻辑中，会重新覆盖已经设置好的 LayoutParams 参数，导致 3 和 4 没有效果。同理在 DialogFragment 中，也需要注意，是在 onActivityCreate 之后再设置 Window 的参数，因为 DialogFragment 中是在 onActivityCreate 方法中调用Window的 `setContentView(view)`。当然如果 setContentView 中 inflation 的布局参数与期望设置的参数一致则可以省略Window的参数设置。

除了代码设置之外，我们还可以使用style设置

```java
    <style name="Dialog.FullScreen" parent="Theme.AppCompat.Dialog">
        /**为了兼容 AppTheme，不要使用 android:windowNoTitle */
        <item name="windowNoTitle">true</item>
        <item name="android:windowBackground">@color/transparent</item>
        <item name="android:windowIsFloating">false</item>
    </style>
```

以上三个步骤缺一不可，否则可能达不到理想的效果：

- 设置 windowIsFloating 为 flase，改变初始的测量模式。
- 覆盖 windowBackground，因为系统默认的 windowBackground 可能有 InsetDrawable 占据了一定的边缘空间。
- windowNoTitle，方 Window 选择不带 title 的根布局。


## 引用

- [三句代码创建全屏Dialog或者DialogFragment：带你从源码角度实现](https://juejin.im/post/58de0a9a44d904006d04cead)