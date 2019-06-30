[国内Top团队大牛带你玩转Android性能分析与优化](https://coding.imooc.com/class/308.html)

# 12 App专项技术优化

## 12.1 列表页卡顿优化

- ViewHolder 复用
- 异步加载
- 减少层级，避免过度绘制、代码构建布局
- 滑动时取消图片加载
- TextView 预渲染
  - 展示类 StaticLayout 即可，性能优于 DynamicLayout
  - 移除创建 StaticLayout
  - facebook/TextLayoutBuilder
- 避免字符串拼接
- 复杂 Item 拆分

```java
public class CustomTextView extends View {

    private String mText = "我是StaticLayout显示出来的文本";
    private TextPaint mTextPaint;
    private StaticLayout mStaticLayout;

    public CustomTextView(Context context) {
        super(context);
        initLabelView();
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLabelView();
    }

    private void initLabelView() {
        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(16 * getResources().getDisplayMetrics().density);
        mTextPaint.setColor(Color.BLACK);
        final int width = (int) mTextPaint.measureText(mText);

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                mStaticLayout = new StaticLayout(mText, mTextPaint, (int) width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0, false);
                postInvalidate();
            }
        });
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mStaticLayout != null){
            canvas.save();
            canvas.translate(getPaddingLeft(), getPaddingTop());
            mStaticLayout.draw(canvas);
            canvas.restore();
        }
    }
}
```

## 12.2 存储优化

- 异步 IO
- SharePreferences 相关
  - 加载慢，初始化加载整个文件
  - 全量写入：单词修改也会导致全量写入
  - 卡顿：
    - Android 提供了异步 apply 机制，这种机制可能导致奔溃时数据丢失
    - 当应用收到系统广播，系统会强制将 SharePreferences 数据依据保存到本地磁盘，而主线程需要等待异步保存完毕，从而可能导致卡顿或ANR
    - SharePreferences 初衷用来存储数据量很小的数据
- 微信 MMVK 替换 SharePreferences
- 日志存储优化
  - 大量服务需要日志库支持
  - 日志库要求：不影响性能、日志不丢失、安全
  - 常规方案1：每产生一次日志，就写一次磁盘
  - 常规方案2：使用内存 buffer，buffer 满时写入磁盘
  - 推荐方案：mmap

## 12.3 WebView异常监控

- WebView 相关问题：性能与适配
- 其他方案：[VasSonic](https://github.com/Tencent/VasSonic)

如何判断 WebView 是否出现异常：监控 WebView 白屏，如果 WebView 出现异常，那么整个 WebView 界面肯定是白屏的，所以如果打开 webView 一段时间后，WebView 始终是全白的，那可以认为 WebView 出现了异常或兼容性问题。
