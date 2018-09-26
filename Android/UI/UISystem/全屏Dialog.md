# 全屏的Dialog

Dialog和Activity一样，内部都有一个窗口，用于控制视图的显示，默认情况下，Dialog并不是全屏的，因为Dialog的应用的theme中包含的`android:windowIsFloating`属性为false，当`windowIsFloating=true`时，PhoneWindow内的ViewTree测量的起始测量模式就是`wrap_content`的

PhoneWindow的源码中对isFloating的处理

```java
     mIsFloating = a.getBoolean(com.android.internal.R.styleable.Window_windowIsFloating, false);
            //.......
            if (mIsFloating) {
                setLayout(WRAP_CONTENT, WRAP_CONTENT);
                setFlags(0, flagsToUpdate);
            } else {
                setFlags(FLAG_LAYOUT_IN_SCREEN|FLAG_LAYOUT_INSET_DECOR, flagsToUpdate);
            }

Window中setLayout方法

    public void setLayout(int width, int height){
            final WindowManager.LayoutParams attrs = getAttributes();
            attrs.width = width;
            attrs.height = height;
            if (mCallback != null) {
                mCallback.onWindowAttributesChanged(attrs);
            }
    }
```

再ViewRootImpl的performTraversals方法中调用getRootMeasureSpec方法，根据生成起始的测量模式：

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

那么使Dialog全屏的方法总结如下：

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

需要注意的是步骤3和4一定要再步骤2之后设置，这是因为在调用` setContentView(view);`的逻辑中，会重新覆盖已经设置好的LayoutParams参数，导致3和4没有效果。同理在DialogFragment中，也需要注意，实在onActivityCreate之后再设置Window的参数，因为DialogFragment中是在onActivityCreate方法中调用Window的`setContentView(view)`。
>当然如果setContentView中inflation的布局参数与期望设置的参数一致则可以省略Window的参数设置

除了代码设置之外，我们还可以使用style设置

```java
    <style name="Dialog.FullScreen" parent="Theme.AppCompat.Dialog">
        <item name="android:windowNoTitle">true</item>
        <item name="android:windowBackground">@color/transparent</item>
        <item name="android:windowIsFloating">false</item>
    </style>
```

这样的效果也是一样的。同样对于Dialog样式的Activity，上面代码同样使用，毕竟内部都是一样的PhoneWindow。


# 引用

- [三句代码创建全屏Dialog或者DialogFragment：带你从源码角度实现](https://juejin.im/post/58de0a9a44d904006d04cead)