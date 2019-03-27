# android-Ultra-Pull-To-Refresh 分析

---
## 1 android-Ultra-Pull-To-Refresh

[android-Ultra-Pull-To-Refresh](https://github.com/liaohuqiu/android-Ultra-Pull-To-Refresh)(下面简称`uptr`) 是一个优秀的下拉刷新开源项目，使用起来非常的灵活与方便。`uptr`主要用于实现下拉刷新，而且只支持下拉刷新，它可以包含任何的子view，还可以自定义下拉头部，用起来非常简单，具体的用法可以自行查看项目的 sample。

---
## 2 设计思路

`uptr`使用一个继承ViewGroup的`PtrFrameLayout`来实现对任何view的下拉刷新，事件分发都由这个PtrFrameLayout来控制，比其他的重写ListView或者RecyclerView的实现方式要灵活很多，而且没有任何侵入性。

`uptr`定义了实现下拉刷新中的四种状态：

    PTR_STATUS_INIT = 1;//初始化
    PTR_STATUS_PREPARE = 2;//准备下拉
    PTR_STATUS_LOADING = 3;//loading状态
    PTR_STATUS_COMPLETE = 4;//刷新完成

对于下拉刷新功能定义了`PtrHandler`接口，`PtrHandler`中定义定义了两个方法，一个是用于检测content是否可以进行滑动的方法`checkCanDoRefresh`，一个用于回调给使用者的同时刷新的方法`onRefreshBegin`，对于`checkCanDoRefresh`已经提供了默认的实现，可以处理大部分情况下的contentView，而使用者又可以针对自己的业务需求来从新定义这个方法的行为，所以具有非常好的灵活性。

对于刷新过程中的header UI遍布，定义了`PtrUIHandler`接口，在下拉状态的变化过程中都会通过此接口把状态与下拉的偏移值回调给使用者，而使用者可以根据自己的需求定义header的样式与动画。

PtrFrameLayout重写了ViewGroup的`dispatchTouchEvent`方法来实现对事件分发的完全控制，而并不是通过重写`onInterceptTouchEvent`和`onTouchEvent`方法，因为系统原生的事件分发行为有这样一个问题，在一系列事件中如果容器已经拦截了事件就没有办法再把事件分发给子view了，获取如果子view在获取DOWN事件后调用`requestDisallowInterceptTouchEvent`方法，也可以让父view无法再拦截事件，这些行为对实现顺畅的下拉刷新都有一定影响。所以有时选择重写`dispatchTouchEvent`也是有必要的。

## 3 PtrHandler和PtrUIHandler

### 3.1 PtrHandler

```java
    public interface PtrHandler {
    
        public boolean checkCanDoRefresh(final PtrFrameLayout frame, final View content, final View header);
    
        public void onRefreshBegin(final PtrFrameLayout frame);
    }
```

`uptr`已经提供了对`checkCanDoRefresh`方法的默认实现，实现了类是`PtrDefaultHandler`，我们主要来看一下判断contentView是否可以向上滑动的方法：

```java
    public static boolean canChildScrollUp(View view) {
            if (android.os.Build.VERSION.SDK_INT < 14) {
                if (view instanceof AbsListView) {
                    final AbsListView absListView = (AbsListView) view;
                    return absListView.getChildCount() > 0
                            && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                            .getTop() < absListView.getPaddingTop());
                } else {
                    return view.getScrollY() > 0;
                }
            } else {
                Log.d(TAG,"view.canScrollVertically(-1)"+view.canScrollVertically(-1));
                Log.d(TAG,"view.canScrollVertically(1)"+view.canScrollVertically(1));
                return view.canScrollVertically(-1);
            }
        }
```

#### 在API14之前

使用getScrollY来判断滑动，如果`getScrollY() > 0`则contentView可以被向下拖动，但是对于AbsListView一类的控件，getScrollY()方法返回的都是0，所以采用下面方式来判断：

     absListView.getChildCount() > 0 （要求AbsListView有子view，没有子view当然无法滑动） 
     而且要求AbsListView的第一个可见的Item的positio>0或者第一个childView的top<absListView的paddingTop。

#### 在API14之后

直接使用` view.canScrollVertically(-1)`方法即可判断，传入负数表示判断是否可以被向下拖动，传入正数表示判断是否可以被向上拖动。

使用的方法很简单:

```java
    ptrFrame.setPtrHandler(new PtrDefaultHandler() {
                @Override
                public void onRefreshBegin(PtrFrameLayout frame) {
                    //do you refresh
                }
                
                @Override
                public boolean checkCanDoRefresh(PtrFrameLayout frame,
                        View content, View header) {
                     if(your comdition){
                          return true/false
                      }
                    return super.checkCanDoRefresh(frame, content, header);
                }
            });
```

### 3.2 PtrUIHandler

```java
    public interface PtrUIHandler {
        public void onUIReset(PtrFrameLayout frame);
    
        public void onUIRefreshPrepare(PtrFrameLayout frame);
    
        public void onUIRefreshBegin(PtrFrameLayout frame);
    
        public void onUIRefreshComplete(PtrFrameLayout frame);
    
        public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator);
    }
```

PtrUIHandler定义了无法回调方法，分别表示：

- onUIReset 刷新完毕，重置ui
- onUIRefreshPrepare 预备下拉刷新
- onUIRefreshBegin 触发了下拉刷新
- onUIRefreshComplete 下拉刷新完成
- onUIPositionChange 始终会回调的方法，传入了status，还可以通过ptrIndicator获取偏移量

PtrUIHandler的回调是通过PtrUIHandlerHolder执行的，PtrUIHandlerHolder内部维护了一个链表，记录了所有的PtrUIHandler，每次都会遍历链表分发状态

## 4 测量与布局

任何一个view的呈现都需要通过measure和layout，所以照着这个流程我们来分析`PtrFrameLayout`

- onFinishInflate
onFinishInflate表示当前layout已经从xml布局文件中被解析成对应的实例，`PtrFrameLayout`在这里确认子view。

分别是mHeaderView和mContent。逻辑很简单，这里就不再分析了。

- onMeasure
onMeasure主要对mHeaderView和mContent进行测量，`PtrFrameLayout`实现了自己的LayoutParams，如果我们也需要调用ViewGroup的`measureChildWithMargins`方法也需要实现自己的LayoutParams，PtrFrameLayout测量的逻辑也简单。

- onLayout

在onLayout时，`PtrFrameLayout`把HeaderView隐藏起来了，header的mTop计算如下：

```java
            // mHeaderView.getMeasuredHeight() + lp.topMargin+ lp.bottomMargin; 
            final int top = paddingTop + lp.topMargin + offsetX - mHeaderHeight;// =  - mHeaderView.getMeasuredHeight() - lp.bottomMargin + paddingTop
```

layout的逻辑也比较简单，不多说。

## 5 下拉刷新的实现

刚刚说了`PtrFrameLayout`重写了`disPtachTouchEvent`方法来实现对事件的完全控制。
而在整个下拉刷星过程中还有两个类非常重要，分别是`PtrIndicator`和`ScrollChecker`。

- PtrIndicator用于在下拉过程中的各种计算，包括手势拖动的偏移值，阻尼系数的技术，触发刷新的位置计算等等。

- ScrollChecker用于实现滑动，它本身实现了Runnable，这样他就可以被post到messageQueue中，而且 ScrollChecker还是用了Scroller用于实现对滑动的计算，它的主要实现方式是不断的post自己，然后再run方法中判断Scroller模拟的滑动是否完成，没有完成就不断的post自己，从而根据Scroller计算的滑动值来实现滑动，可见Scroller并不就只能和`computeScroll`一起使用。

### PtrIndicator

PtrIndicator的计算其实也是比较简单的，只是其内部的方法比较多，下面是我对PtrIndicator注释的源码。

```java
    public class PtrIndicator {
    
        private final static String TAG  = PtrIndicator.class.getName();
        
        public final static int POS_START = 0;//0起始位置
        protected int mOffsetToRefresh = 0;//触发刷新的高度 = headerHeight * mRatioOfHeaderHeightToRefresh
        private PointF mPtLastMove = new PointF();//触摸点的真实位置
        private float mOffsetX;//每一次手势的偏移值
        private float mOffsetY;//每一次手势的偏移值
        private int mCurrentPos = 0;//根据阻尼系数计算后的真实的偏移距离，
        private int mLastPos = 0;//上一次事件的mCurrentPos
        private int mHeaderHeight;//头部的高度
        private int mPressedPos = 0;//初始化mCurrentPos的值
    
        private float mRatioOfHeaderHeightToRefresh = 1.2f;//超过头部的多少认为是可以触发刷新的距离
        private float mResistance = 1.7f;//下拉过程中的阻尼系数
        private boolean mIsUnderTouch = false;//是否为手势拖动
        private int mOffsetToKeepHeaderWhileLoading = -1;
        // record the refresh complete position
        private int mRefreshCompleteY = 0;//记录刷新完成的位置
    
        //是否为手指触摸
        public boolean isUnderTouch() {
            return mIsUnderTouch;
        }
    
        //阻尼系数
        public float getResistance() {
            return mResistance;
        }
    
        
        public void setResistance(float resistance) {
            mResistance = resistance;
        }
    
        //释放拖动
        public void onRelease() {
            mIsUnderTouch = false;
        }
    
        //刷新完毕
        public void onUIRefreshComplete() {
            mRefreshCompleteY = mCurrentPos;
        }
    
        //超过了触发刷新的位置
        public boolean goDownCrossFinishPosition() {
            return mCurrentPos >= mRefreshCompleteY;
        }
    
        protected void processOnMove(float currentX, float currentY, float offsetX, float offsetY) {
            setOffset(offsetX, offsetY / mResistance);
        }
    
        public void setRatioOfHeaderHeightToRefresh(float ratio) {
            mRatioOfHeaderHeightToRefresh = ratio;
            mOffsetToRefresh = (int) (mHeaderHeight * ratio);
        }
    
        public float getRatioOfHeaderToHeightRefresh() {
            return mRatioOfHeaderHeightToRefresh;
        }
    
        public int getOffsetToRefresh() {
            return mOffsetToRefresh;
        }
    
        public void setOffsetToRefresh(int offset) {
            mRatioOfHeaderHeightToRefresh = mHeaderHeight * 1f / offset;
            mOffsetToRefresh = offset;
        }
    
        public void onPressDown(float x, float y) {
            mIsUnderTouch = true;
            mPressedPos = mCurrentPos;
            mPtLastMove.set(x, y);
        }
    
        public final void onMove(float x, float y) {
            float offsetX = x - mPtLastMove.x;
            float offsetY = (y - mPtLastMove.y);
            processOnMove(x, y, offsetX, offsetY);
            mPtLastMove.set(x, y);
        }
    
        protected void setOffset(float x, float y) {
            mOffsetX = x;
            mOffsetY = y;
        }
    
        public float getOffsetX() {
            return mOffsetX;
        }
    
        public float getOffsetY() {
            return mOffsetY;
        }
    
        public int getLastPosY() {
            return mLastPos;
        }
    
        public int getCurrentPosY() {
            return mCurrentPos;
        }
    
        /**
         * Update current position before update the UI
         */
        public final void setCurrentPos(int current) {
            mLastPos = mCurrentPos;
            mCurrentPos = current;
            onUpdatePos(current, mLastPos);
        }
    
        protected void onUpdatePos(int current, int last) {
    
        }
    
        public int getHeaderHeight() {
            return mHeaderHeight;
        }
    
        public void setHeaderHeight(int height) {
            mHeaderHeight = height;
            updateHeight();
        }
    
        protected void updateHeight() {
            mOffsetToRefresh = (int) (mRatioOfHeaderHeightToRefresh * mHeaderHeight);
        }
    
        public void convertFrom(PtrIndicator ptrSlider) {
            mCurrentPos = ptrSlider.mCurrentPos;
            mLastPos = ptrSlider.mLastPos;
            mHeaderHeight = ptrSlider.mHeaderHeight;
        }
    
        //已经产生偏移
        public boolean hasLeftStartPosition() {
            return mCurrentPos > POS_START;
        }
    
        //刚刚产生了偏移
        public boolean hasJustLeftStartPosition() {
            return mLastPos == POS_START && hasLeftStartPosition();
        }
    
        //刚刚回到起始位置
        public boolean hasJustBackToStartPosition() {
            return mLastPos != POS_START && isInStartPosition();
        }
    
        //超过了触发刷新的位置
        public boolean isOverOffsetToRefresh() {
            return mCurrentPos >= getOffsetToRefresh();
        }
    
        //是按下之后产生了移动
        public boolean hasMovedAfterPressedDown() {
            return mCurrentPos != mPressedPos;
        }
    
        //在起始的位置
        public boolean isInStartPosition() {
            return mCurrentPos == POS_START;
        }
    
        //刚刚从上往下拉，超过了触发刷新的位置
        public boolean crossRefreshLineFromTopToBottom() {
            return mLastPos < getOffsetToRefresh() && mCurrentPos >= getOffsetToRefresh();
        }
    
        //刚刚从上往下拉，超过了header的高度
        public boolean hasJustReachedHeaderHeightFromTopToBottom() {
            return mLastPos < mHeaderHeight && mCurrentPos >= mHeaderHeight;
        }
    
        //超过loading时保持header的偏移值，默认是header的高度
        public boolean isOverOffsetToKeepHeaderWhileLoading() {
            return mCurrentPos > getOffsetToKeepHeaderWhileLoading();
        }
    
        public void setOffsetToKeepHeaderWhileLoading(int offset) {
            mOffsetToKeepHeaderWhileLoading = offset;
        }
    
        public int getOffsetToKeepHeaderWhileLoading() {
            return mOffsetToKeepHeaderWhileLoading >= 0 ? mOffsetToKeepHeaderWhileLoading : mHeaderHeight;
        }
    
        //已经在将要到达的位置了
        public boolean isAlreadyHere(int to) {
            return mCurrentPos == to;
        }
        
       //获取偏移的百分数
        public float getLastPercent() {
            final float oldPercent = mHeaderHeight == 0 ? 0 : mLastPos * 1f / mHeaderHeight;
            return oldPercent;
        }
        //获取当前的偏移百分数
        public float getCurrentPercent() {
            final float currentPercent = mHeaderHeight == 0 ? 0 : mCurrentPos * 1f / mHeaderHeight;
            return currentPercent;
        }
    
        //将要达到的位置是否要小于0
        public boolean willOverTop(int to) {
            return to < POS_START;
        }
        
    }
```

### ScrollChecker

ScrollChecker的逻辑也比较简单，各个已经说了它的实现方式，关键的代码如下：

```java
    //开始一个滑动
    mScroller.startScroll(0, 0, 0, distance, duration);
                post(this);
    
    public void run() {
                boolean finish = !mScroller.computeScrollOffset()
                        || mScroller.isFinished();
                int curY = mScroller.getCurrY();
                //计算偏移值
                int deltaY = curY - mLastFlingY;
                            if (!finish) {
                    //计算这一次的curY，以便下一次计算
                    mLastFlingY = curY;
                    //movePos用于实现内容的移动
                    movePos(deltaY);
                    post(this);
                } else {
                    finish();
                }
            }
```

### PtrFrameLayout

```java
    public class PtrFrameLayout extends ViewGroup {
    
        // status enum
        public final static byte PTR_STATUS_INIT = 1;//初始化
        public final static byte PTR_STATUS_PREPARE = 2;//准备下拉
        public final static byte PTR_STATUS_LOADING = 3;//loading状态
        public final static byte PTR_STATUS_COMPLETE = 4;//刷新完成
    
        private static final boolean DEBUG_LAYOUT = true;
        private static final String TAG = PtrFrameLayout.class.getName();
        public static boolean DEBUG = false;
        private static int ID = 1;
    
        // auto refresh status
        private static byte FLAG_AUTO_REFRESH_AT_ONCE = 0x01;//
        private static byte FLAG_AUTO_REFRESH_BUT_LATER = 0x01 << 1;//
        private static byte FLAG_ENABLE_NEXT_PTR_AT_ONCE = 0x01 << 2;
        private static byte FLAG_PIN_CONTENT = 0x01 << 3;//下拉的时候保持content不动
    
        private static byte MASK_AUTO_REFRESH = 0x03;
    
        protected final String LOG_TAG = "ptr-frame-" + ++ID;
        protected View mContent;
        
        // optional config for define header and content in xml file xml中header和content的id
        private int mHeaderId = 0;
        private int mContainerId = 0;
        // config
        private int mDurationToClose = 200;//，回弹到刷新高度所用时间。
        private int mDurationToCloseHeader = 1000;//头部回弹时间
        private boolean mKeepHeaderWhenRefresh = true;//刷新时保持头部显示
        private boolean mPullToRefresh = false;//下拉过程中达到触发高度就立即刷新
        private View mHeaderView;
        private PtrUIHandlerHolder mPtrUIHandlerHolder = PtrUIHandlerHolder// PtrUIHandler的holder，内部是链表结构，用于回调UI刷新状态
                .create();
        private PtrHandler mPtrHandler;//刷新hanlder，两个功能，检查conent是否可以下拉和回调刷新
        // working parameters
        private ScrollChecker mScrollChecker;//用于实现滑动，Runnable+Scroller
        private int mPagingTouchSlop;//认为是触发滑动的距离
        private int mHeaderHeight;//头部高度
    
        private byte mStatus = PTR_STATUS_INIT;//当前状态
        private boolean mDisableWhenHorizontalMove = false;//当发生水平滑动时，禁止下拉，常见的就是viewpager作为列表的header时
        private int mFlag = 0x00;//内部各种设置标识，
    
        // disable when detect moving horizontally
        private boolean mPreventForHorizontal = false;//与水平滑动相关
    
        private MotionEvent mLastMoveEvent;//最后一次move事件
    
        private PtrUIHandlerHook mRefreshCompleteHook;//钩子
    
        private int mLoadingMinTime = 500;//loading的最小时间
        private long mLoadingStartTime = 0;//loading的开始时间
        
        private PtrIndicator mPtrIndicator;//这个类很重要，用于计算滑动距离和判断各种滑动状态
        
        private boolean mHasSendCancelEvent = false;//是否已经发送了一个cancel事件
    
        public PtrFrameLayout(Context context) {
            this(context, null);
        }
    
        public PtrFrameLayout(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }
    
        public PtrFrameLayout(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            //初始化PtrIndicator
            mPtrIndicator = new PtrIndicator();
    
            //获取属性
            TypedArray arr = context.obtainStyledAttributes(attrs,
                    R.styleable.PtrFrameLayout, 0, 0);
            if (arr != null) {
    
                mHeaderId = arr.getResourceId(
                        R.styleable.PtrFrameLayout_ptr_header, mHeaderId);
                mContainerId = arr.getResourceId(
                        R.styleable.PtrFrameLayout_ptr_content, mContainerId);
    
                mPtrIndicator.setResistance(arr.getFloat(
                        R.styleable.PtrFrameLayout_ptr_resistance,
                        mPtrIndicator.getResistance()));
    
                mDurationToClose = arr.getInt(
                        R.styleable.PtrFrameLayout_ptr_duration_to_close,
                        mDurationToClose);
                mDurationToCloseHeader = arr.getInt(
                        R.styleable.PtrFrameLayout_ptr_duration_to_close_header,
                        mDurationToCloseHeader);
    
                float ratio = mPtrIndicator.getRatioOfHeaderToHeightRefresh();
                ratio = arr
                        .getFloat(
                                R.styleable.PtrFrameLayout_ptr_ratio_of_header_height_to_refresh,
                                ratio);
                mPtrIndicator.setRatioOfHeaderHeightToRefresh(ratio);
    
                mKeepHeaderWhenRefresh = arr.getBoolean(
                        R.styleable.PtrFrameLayout_ptr_keep_header_when_refresh,
                        mKeepHeaderWhenRefresh);
    
                mPullToRefresh = arr.getBoolean(
                        R.styleable.PtrFrameLayout_ptr_pull_to_fresh,
                        mPullToRefresh);
                arr.recycle();
            }
            
            //创建一个ScrollChecker
            mScrollChecker = new ScrollChecker();
    
            final ViewConfiguration conf = ViewConfiguration.get(getContext());
            mPagingTouchSlop = conf.getScaledTouchSlop() * 2;
        }
    
        /**
         * 在onFinishInflate找到子header和content
         */
        @Override
        protected void onFinishInflate() {
            final int childCount = getChildCount();
            if (childCount > 2) {//不能超过两个孩子
                throw new IllegalStateException(
                        "PtrFrameLayout only can host 2 elements");
            } else if (childCount == 2) {
                if (mHeaderId != 0 && mHeaderView == null) {
                    mHeaderView = findViewById(mHeaderId);
                }
                if (mContainerId != 0 && mContent == null) {
                    mContent = findViewById(mContainerId);
                }
    
                // not specify header or content
                if (mContent == null || mHeaderView == null) {
    
                    View child1 = getChildAt(0);
                    View child2 = getChildAt(1);
                    if (child1 instanceof PtrUIHandler) {
                        mHeaderView = child1;
                        mContent = child2;
                    } else if (child2 instanceof PtrUIHandler) {
                        mHeaderView = child2;
                        mContent = child1;
                    } else {
                        // both are not specified
                        if (mContent == null && mHeaderView == null) {
                            mHeaderView = child1;
                            mContent = child2;
                        }
                        // only one is specified
                        else {
                            if (mHeaderView == null) {
                                mHeaderView = mContent == child1 ? child2 : child1;
                            } else {
                                mContent = mHeaderView == child1 ? child2 : child1;
                            }
                        }
                    }
                }
            } else if (childCount == 1) {
                mContent = getChildAt(0);
            } else {
                TextView errorView = new TextView(getContext());
                errorView.setClickable(true);
                errorView.setTextColor(0xffff6600);
                errorView.setGravity(Gravity.CENTER);
                errorView.setTextSize(20);
                errorView
                        .setText("The content view in PtrFrameLayout is empty. Do you forget to specify its id in xml layout file?");
                mContent = errorView;
                addView(mContent);
            }
            if (mHeaderView != null) {
                mHeaderView.bringToFront();//把header调整到z轴的顶部，秋百万真实贴心啊
            }
            super.onFinishInflate();
        }
    
        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            //从这里可见此lPtrLayout不支持wrap_content，事实上也没有必要支持
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    
            if (DEBUG && DEBUG_LAYOUT) {
                PtrCLog.d(
                        LOG_TAG,
                        "onMeasure frame: width: %s, height: %s, padding: %s %s %s %s",
                        getMeasuredHeight(), getMeasuredWidth(), getPaddingLeft(),
                        getPaddingRight(), getPaddingTop(), getPaddingBottom());
    
            }
    
            //测量头部
            if (mHeaderView != null) {
                measureChildWithMargins(mHeaderView, widthMeasureSpec, 0,
                        heightMeasureSpec, 0);
                MarginLayoutParams lp = (MarginLayoutParams) mHeaderView
                        .getLayoutParams();
                //计算header的高度
                mHeaderHeight = mHeaderView.getMeasuredHeight() + lp.topMargin
                        + lp.bottomMargin;
                //把高度设置为Indicator
                mPtrIndicator.setHeaderHeight(mHeaderHeight);
            }
           
            //测量content
            if (mContent != null) {
                measureContentView(mContent, widthMeasureSpec, heightMeasureSpec);
                if (DEBUG && DEBUG_LAYOUT) {
                    ViewGroup.MarginLayoutParams lp = (MarginLayoutParams) mContent
                            .getLayoutParams();
                    PtrCLog.d(
                            LOG_TAG,
                            "onMeasure content, width: %s, height: %s, margin: %s %s %s %s",
                            getMeasuredWidth(), getMeasuredHeight(), lp.leftMargin,
                            lp.topMargin, lp.rightMargin, lp.bottomMargin);
                    PtrCLog.d(LOG_TAG,
                            "onMeasure, currentPos: %s, lastPos: %s, top: %s",
                            mPtrIndicator.getCurrentPosY(),
                            mPtrIndicator.getLastPosY(), mContent.getTop());
                }
            }
        }
    
        /**
         * 测量content的逻辑，
         * @param child
         * @param parentWidthMeasureSpec
         * @param parentHeightMeasureSpec
         */
        private void measureContentView(View child, int parentWidthMeasureSpec,
                int parentHeightMeasureSpec) {
            final MarginLayoutParams lp = (MarginLayoutParams) child
                    .getLayoutParams();
                //其实这里的逻辑与measureChildWithMargins类似,只是高度没有考虑BottomMargin，
            final int childWidthMeasureSpec = getChildMeasureSpec(
                    parentWidthMeasureSpec, getPaddingLeft() + getPaddingRight()
                            + lp.leftMargin + lp.rightMargin, lp.width);
            
            final int childHeightMeasureSpec = getChildMeasureSpec(
                    parentHeightMeasureSpec, getPaddingTop() + getPaddingBottom()
                            + lp.topMargin, lp.height);
    
            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
        }
    
        @Override
        protected void onLayout(boolean flag, int i, int j, int k, int l) {
            layoutChildren();
        }
    
        //布局
        private void layoutChildren() {
            int offsetX = mPtrIndicator.getCurrentPosY();//一般offsetX = 0；
            int paddingLeft = getPaddingLeft();
            int paddingTop = getPaddingTop();
    
            //布局的时候会把header隐藏起来
            if (mHeaderView != null) {
                MarginLayoutParams lp = (MarginLayoutParams) mHeaderView
                        .getLayoutParams();
                final int left = paddingLeft + lp.leftMargin;
                // mHeaderView.getMeasuredHeight() + lp.topMargin+ lp.bottomMargin; 
                final int top = paddingTop + lp.topMargin + offsetX - mHeaderHeight;// =  - mHeaderView.getMeasuredHeight() - lp.bottomMargin + paddingTop
                final int right = left + mHeaderView.getMeasuredWidth();
                final int bottom = top + mHeaderView.getMeasuredHeight();
                mHeaderView.layout(left, top, right, bottom);
                if (DEBUG && DEBUG_LAYOUT) {
                    PtrCLog.d(LOG_TAG, "onLayout header: %s %s %s %s", left, top,
                            right, bottom);
                }
            }
            if (mContent != null) {
                if (isPinContent()) {
                    offsetX = 0;
                }
                MarginLayoutParams lp = (MarginLayoutParams) mContent
                        .getLayoutParams();
                final int left = paddingLeft + lp.leftMargin;
                final int top = paddingTop + lp.topMargin + offsetX;
                final int right = left + mContent.getMeasuredWidth();
                final int bottom = top + mContent.getMeasuredHeight();
                if (DEBUG && DEBUG_LAYOUT) {
                    PtrCLog.d(LOG_TAG, "onLayout content: %s %s %s %s", left, top,
                            right, bottom);
                }
                mContent.layout(left, top, right, bottom);
            }
        }
    
        //调用ViewGroup的dispatchTouchEvent
        public boolean dispatchTouchEventSupper(MotionEvent e) {
            return super.dispatchTouchEvent(e);
        }
    
        //因为是直接重写了dispatchTouchEvent方法，所以不需要依靠原来的拦截事件
        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
    
            return super.onInterceptTouchEvent(ev);
        }
      //因为是直接重写了dispatchTouchEvent方法，所以不需要依靠onTouchEvent处理事件
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            return super.onTouchEvent(event);
        }
    
        
        
        @Override
        public boolean dispatchTouchEvent(MotionEvent e) {
    //        mPtrIndicator.printInfo();
            
            //如果不可用，或者内容为null，不会有事件处理。
            if (!isEnabled() || mContent == null || mHeaderView == null) {
                return dispatchTouchEventSupper(e);
            }
            int action = e.getAction();//没有mask的事件类型，所以在多手指拖动的情况下会有跳跃的现象
            switch (action) {
            //抬起或者事件取消
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mPtrIndicator.onRelease();//告诉指示器，手指抬起
                if (mPtrIndicator.hasLeftStartPosition()) {//是否已经离开了起始位置，就是已经产出了滑动了，mCurrentPos > POS_START;
                    if (DEBUG) {
                        PtrCLog.d(LOG_TAG, "call onRelease when user release");
                    }
                    onRelease(false);
                    /*
                     * 如果按下的位置不等于当前偏移的位置 mCurrentPos != mPressedPos;为什么上面有了hasLeftStartPosition的判断，这里还需要判断呢？
                     */
                    if (mPtrIndicator.hasMovedAfterPressedDown()) {
                        sendCancelEvent();//给子view发送一个cancel事件，因为已经认为是一个滑动事件了。
                        return true;
                    }
                    return dispatchTouchEventSupper(e);
                } else {
                    return dispatchTouchEventSupper(e);
                }
            //手指按下
            case MotionEvent.ACTION_DOWN:
                mHasSendCancelEvent = false;//重置标识
                
                mPtrIndicator.onPressDown(e.getX(), e.getY());//通知指示器，按下了
    
                mScrollChecker.abortIfWorking();//如果之前有滑动，立马停止
    
                mPreventForHorizontal = false;//水平滑动
                // The cancel event will be sent once the position is moved.
                // So let the event pass to children.
                // fix #93, #102
                dispatchTouchEventSupper(e);//把down事件分发给子view
                
                return true;//down事件返回true表示下拉layout始终会处理事件
    
            case MotionEvent.ACTION_MOVE:
                mLastMoveEvent = e;//记录最后一次move事件，等下可能用到
                mPtrIndicator.onMove(e.getX(), e.getY());//把手势移动计算操作都交给指示器
                
                float offsetX = mPtrIndicator.getOffsetX();//得到偏移值  new- old
                float offsetY = mPtrIndicator.getOffsetY();
    
                if (mDisableWhenHorizontalMove//横向移动的处理
                        && !mPreventForHorizontal
                        && (Math.abs(offsetX) > mPagingTouchSlop && Math
                                .abs(offsetX) > Math.abs(offsetY))) {
                    if (mPtrIndicator.isInStartPosition()) {//没有发生垂直移动
                        mPreventForHorizontal = true;
                    }
                }
                if (mPreventForHorizontal) {//有横向滑动，就交给子view处理
                    return dispatchTouchEventSupper(e);
                }
    
                boolean moveDown = offsetY > 0;//是否是向下拖动
                boolean moveUp = !moveDown;//是否是向上拖动
                boolean canMoveUp = mPtrIndicator.hasLeftStartPosition();//是否已经离开了起始位置
    
                if (DEBUG) {
                    boolean canMoveDown = mPtrHandler != null
                            && mPtrHandler.checkCanDoRefresh(this, mContent,
                                    mHeaderView);
                    PtrCLog.v(
                            LOG_TAG,
                            "ACTION_MOVE: offsetY:%s, currentPos: %s, moveUp: %s, canMoveUp: %s, moveDown: %s: canMoveDown: %s",
                            offsetY, mPtrIndicator.getCurrentPosY(), moveUp,
                            canMoveUp, moveDown, canMoveDown);
                }
    
                // disable move when header not reach top 禁用移动，当header没有达到top位置
                //这里如果是向下滑动，并且content是可以向下滑动的，就让content自己滑动
                if (moveDown
                        && mPtrHandler != null
                        && !mPtrHandler.checkCanDoRefresh(this, mContent,
                                mHeaderView)) {
                    return dispatchTouchEventSupper(e);
                }
                //否则如果是向上滑动并且之前已经向下移动了一段距离(这时向上拖动就应该回到初始位置)
                //或者子view不同向下滑动了，就自己处理滑动
                if ((moveUp && canMoveUp) || moveDown) {
                    movePos(offsetY);
                    return true;
                }
                //否则就就交给子view滑动
            }
            return dispatchTouchEventSupper(e);
        }
    
        /**
         * if deltaY > 0, move the content down
         *      否则如果是向上滑动并且之前已经向下移动了一段距离
                或者子view不同向下滑动了，就自己处理滑动
         * @param deltaY
         */
        private void movePos(float deltaY) {
            // has reached the top
            if ((deltaY < 0 && mPtrIndicator.isInStartPosition())) {// meybe no run
                //如果已经把内容拖动到顶部就不用再往上拖动了，这是一个保护性措施
                if (DEBUG) {
                    PtrCLog.e(LOG_TAG, String.format("has reached the top"));
                }
                return;
            }
    
            int to = mPtrIndicator.getCurrentPosY() + (int) deltaY;//需要到达的位置
    
            // over top 如果将要到达的位置小于0
            if (mPtrIndicator.willOverTop(to)) {//meybe no run
                if (DEBUG) {
                    PtrCLog.e(LOG_TAG, String.format("over top"));
                }
                to = PtrIndicator.POS_START;//让其等于0
            }
    
            mPtrIndicator.setCurrentPos(to);//更新当前的mCurrentPos
            int change = to - mPtrIndicator.getLastPosY();//change其实就是需要移动的大小
            updatePos(change);
        }
    
        private void updatePos(int change) {
            if (change == 0) {
                return;
            }
    
            boolean isUnderTouch = mPtrIndicator.isUnderTouch();//手指是否还在拖动，没有抬起
    
            //如果是手指在拖动，并且已经产生移动了
            // once moved, cancel event will be sent to child
            if (isUnderTouch && !mHasSendCancelEvent
                    && mPtrIndicator.hasMovedAfterPressedDown()) {
                mHasSendCancelEvent = true;
                sendCancelEvent();
            }
    
            // leave initiated position or just refresh complete
            //开始下拉
            if ((mPtrIndicator.hasJustLeftStartPosition() && mStatus == PTR_STATUS_INIT)
                    || (mPtrIndicator.goDownCrossFinishPosition()//超过了触发刷新位置
                            && mStatus == PTR_STATUS_COMPLETE//刷新状态是完成刷新了
                            && isEnabledNextPtrAtOnce())) {//立即启动下一次刷新
    
                mStatus = PTR_STATUS_PREPARE;//更新状态
                mPtrUIHandlerHolder.onUIRefreshPrepare(this);//通知预备要刷新了
                if (DEBUG) {
                    PtrCLog.i(LOG_TAG,
                            "PtrUIHandler: onUIRefreshPrepare, mFlag %s", mFlag);
                }
            }
    
            // back to initiated position mLastPos != POS_START && mCurrentPos == POS_START;
             //刚刚回到起始位置，之前是下拉的，现在已经复位了
            if (mPtrIndicator.hasJustBackToStartPosition()) {
                tryToNotifyReset();//重置
                // recover event to children
                //模拟一个down事件，重新把事件分发给子view，因为接下来的事件又要交给子view处理了
                if (isUnderTouch) {
                    sendDownEvent();
                }
            }
    
            // Pull to Refresh
            if (mStatus == PTR_STATUS_PREPARE) {
                // reach fresh height while moving from top to bottom
                //从下往下拉，如果是超过了触发刷新的距离就立即出发刷新。
                if (isUnderTouch && !isAutoRefresh() && mPullToRefresh
                        && mPtrIndicator.crossRefreshLineFromTopToBottom()) {////刚刚从上往下拉，超过了触发刷新的位置
                    Log.e(TAG, "crossRefreshLineFromTopToBottom");
                    tryToPerformRefresh();
                }
                // reach header height while auto refresh
                if (performAutoRefreshButLater()
                        && mPtrIndicator//  刚刚从上往下拉，超过了header的高度
                                .hasJustReachedHeaderHeightFromTopToBottom()) {
                    Log.e(TAG, "performAutoRefreshButLater");
                    tryToPerformRefresh();
                }
            }
    
            if (DEBUG) {
                PtrCLog.v(
                        LOG_TAG,
                        "updatePos: change: %s, current: %s last: %s, top: %s, headerHeight: %s",
                        change, mPtrIndicator.getCurrentPosY(),
                        mPtrIndicator.getLastPosY(), mContent.getTop(),
                        mHeaderHeight);
            }
    
            mHeaderView.offsetTopAndBottom(change);
            if (!isPinContent()) {
                mContent.offsetTopAndBottom(change);
            }
            invalidate();
    
            if (mPtrUIHandlerHolder.hasHandler()) {
                mPtrUIHandlerHolder.onUIPositionChange(this, isUnderTouch, mStatus,
                        mPtrIndicator);
            }
            onPositionChange(isUnderTouch, mStatus, mPtrIndicator);
        }
    
        protected void onPositionChange(boolean isInTouching, byte status,
                PtrIndicator mPtrIndicator) {
        }
    
        @SuppressWarnings("unused")
        public int getHeaderHeight() {
            return mHeaderHeight;
        }
    
        private void onRelease(boolean stayForLoading) {
    
            tryToPerformRefresh();
    
            if (mStatus == PTR_STATUS_LOADING) {//已经触发了刷新
                // keep header for fresh
                if (mKeepHeaderWhenRefresh) {//loading时保持头部
                    // scroll header back
                    if (mPtrIndicator.isOverOffsetToKeepHeaderWhileLoading()
                            && !stayForLoading) {
                        mScrollChecker.tryToScrollTo(
                                mPtrIndicator.getOffsetToKeepHeaderWhileLoading(),//获取保持头部的高度
                                mDurationToClose);
                    } else {
                        // do nothing
                    }
                } else {//回到其实位置
                    tryScrollBackToTopWhileLoading();
                }
            } else {
                if (mStatus == PTR_STATUS_COMPLETE) {//已经是刷新完成状态
                    notifyUIRefreshComplete(false);
                } else {
                    tryScrollBackToTopAbortRefresh();//回到起始位置
                }
            }
        }
    
        /**
         * please DO REMEMBER resume the hook
         * 
         * @param hook
         */
    
        public void setRefreshCompleteHook(PtrUIHandlerHook hook) {
            mRefreshCompleteHook = hook;
            hook.setResumeAction(new Runnable() {
                @Override
                public void run() {
                    if (DEBUG) {
                        PtrCLog.d(LOG_TAG, "mRefreshCompleteHook resume.");
                    }
                    notifyUIRefreshComplete(true);
                }
            });
        }
    
        /**
         * Scroll back to to if is not under touch
         */
        private void tryScrollBackToTop() {
            if (!mPtrIndicator.isUnderTouch()) {
                mScrollChecker.tryToScrollTo(PtrIndicator.POS_START,
                        mDurationToCloseHeader);
            }
        }
    
        /**
         * just make easier to understand
         */
        private void tryScrollBackToTopWhileLoading() {
            tryScrollBackToTop();
        }
    
        /**
         * just make easier to understand
         */
        private void tryScrollBackToTopAfterComplete() {
            tryScrollBackToTop();
        }
    
        /**
         * just make easier to understand
         */
        private void tryScrollBackToTopAbortRefresh() {
            tryScrollBackToTop();
        }
    
        private boolean tryToPerformRefresh() {
            if (mStatus != PTR_STATUS_PREPARE) {
                return false;
            }
    
            //自动刷新和拖动刷新
            if ((mPtrIndicator.isOverOffsetToKeepHeaderWhileLoading() && isAutoRefresh())
                    || mPtrIndicator.isOverOffsetToRefresh()) {
                mStatus = PTR_STATUS_LOADING;
                performRefresh();
            }
            return false;
        }
    
        /**
         * 执行刷新
         */
        private void performRefresh() {
            mLoadingStartTime = System.currentTimeMillis();
            if (mPtrUIHandlerHolder.hasHandler()) {
                mPtrUIHandlerHolder.onUIRefreshBegin(this);
                if (DEBUG) {
                    PtrCLog.i(LOG_TAG, "PtrUIHandler: onUIRefreshBegin");
                }
            }
            if (mPtrHandler != null) {
                mPtrHandler.onRefreshBegin(this);
            }
        }
    
        /**
         * If at the top and not in loading, reset
         */
        private boolean tryToNotifyReset() {
            if ((mStatus == PTR_STATUS_COMPLETE || mStatus == PTR_STATUS_PREPARE)
                    && mPtrIndicator.isInStartPosition()) {
                if (mPtrUIHandlerHolder.hasHandler()) {
                    mPtrUIHandlerHolder.onUIReset(this);
                    if (DEBUG) {
                        PtrCLog.i(LOG_TAG, "PtrUIHandler: onUIReset");
                    }
                }
                mStatus = PTR_STATUS_INIT;
                clearFlag();
                return true;
            }
            return false;
        }
    
        protected void onPtrScrollAbort() {
            if (mPtrIndicator.hasLeftStartPosition() && isAutoRefresh()) {
                if (DEBUG) {
                    PtrCLog.d(LOG_TAG, "call onRelease after scroll abort");
                }
                onRelease(true);
            }
        }
    
        protected void onPtrScrollFinish() {
            if (mPtrIndicator.hasLeftStartPosition() && isAutoRefresh()) {
                if (DEBUG) {
                    PtrCLog.d(LOG_TAG, "call onRelease after scroll finish");
                }
                onRelease(true);
            }
        }
    
        /**
         * Detect whether is refreshing.
         * 
         * @return
         */
        public boolean isRefreshing() {
            return mStatus == PTR_STATUS_LOADING;
        }
    
        /**
         * Call this when data is loaded. The UI will perform complete at once or
         * after a delay, depends on the time elapsed is greater then
         * {@link #mLoadingMinTime} or not.
         */
        final public void refreshComplete() {
            if (DEBUG) {
                PtrCLog.i(LOG_TAG, "refreshComplete");
            }
    
            if (mRefreshCompleteHook != null) {
                mRefreshCompleteHook.reset();
            }
    
            int delay = (int) (mLoadingMinTime - (System.currentTimeMillis() - mLoadingStartTime));
            if (delay <= 0) {
                if (DEBUG) {
                    PtrCLog.d(LOG_TAG, "performRefreshComplete at once");
                }
                performRefreshComplete();
            } else {
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        performRefreshComplete();
                    }
                }, delay);
                if (DEBUG) {
                    PtrCLog.d(LOG_TAG, "performRefreshComplete after delay: %s",
                            delay);
                }
            }
        }
    
        /**
         * Do refresh complete work when time elapsed is greater than
         * {@link #mLoadingMinTime}
         */
        private void performRefreshComplete() {
            mStatus = PTR_STATUS_COMPLETE;
    
            // if is auto refresh do nothing, wait scroller stop
            if (mScrollChecker.mIsRunning && isAutoRefresh()) {
                // do nothing
                if (DEBUG) {
                    PtrCLog.d(
                            LOG_TAG,
                            "performRefreshComplete do nothing, scrolling: %s, auto refresh: %s",
                            mScrollChecker.mIsRunning, mFlag);
                }
                return;
            }
    
            notifyUIRefreshComplete(false);
        }
    
        /**
         * Do real refresh work. If there is a hook, execute the hook first.
         * 
         * @param ignoreHook
         */
        private void notifyUIRefreshComplete(boolean ignoreHook) {
            /**
             * After hook operation is done, {@link #notifyUIRefreshComplete} will
             * be call in resume action to ignore hook.
             */
            if (mPtrIndicator.hasLeftStartPosition() && !ignoreHook
                    && mRefreshCompleteHook != null) {
                if (DEBUG) {
                    PtrCLog.d(LOG_TAG,
                            "notifyUIRefreshComplete mRefreshCompleteHook run.");
                }
    
                mRefreshCompleteHook.takeOver();
                return;
            }
            if (mPtrUIHandlerHolder.hasHandler()) {
                if (DEBUG) {
                    PtrCLog.i(LOG_TAG, "PtrUIHandler: onUIRefreshComplete");
                }
                mPtrUIHandlerHolder.onUIRefreshComplete(this);
            }
            mPtrIndicator.onUIRefreshComplete();
            tryScrollBackToTopAfterComplete();
            tryToNotifyReset();
        }
    
        public void autoRefresh() {
            autoRefresh(true, mDurationToCloseHeader);
        }
    
        public void autoRefresh(boolean atOnce) {
            autoRefresh(atOnce, mDurationToCloseHeader);
        }
    
        private void clearFlag() {
            // remove auto fresh flag
            mFlag = mFlag & ~MASK_AUTO_REFRESH;
        }
    
        public void autoRefresh(boolean atOnce, int duration) {
    
            if (mStatus != PTR_STATUS_INIT) {
                return;
            }
    
            mFlag |= atOnce ? FLAG_AUTO_REFRESH_AT_ONCE
                    : FLAG_AUTO_REFRESH_BUT_LATER;
    
            mStatus = PTR_STATUS_PREPARE;
            if (mPtrUIHandlerHolder.hasHandler()) {
                mPtrUIHandlerHolder.onUIRefreshPrepare(this);
                if (DEBUG) {
                    PtrCLog.i(LOG_TAG,
                            "PtrUIHandler: onUIRefreshPrepare, mFlag %s", mFlag);
                }
            }
            mScrollChecker.tryToScrollTo(mPtrIndicator.getOffsetToRefresh(),
                    duration);
            if (atOnce) {
                mStatus = PTR_STATUS_LOADING;
                performRefresh();
            }
        }
    
        public boolean isAutoRefresh() {
            return (mFlag & MASK_AUTO_REFRESH) > 0;
        }
    
        private boolean performAutoRefreshButLater() {
            return (mFlag & MASK_AUTO_REFRESH) == FLAG_AUTO_REFRESH_BUT_LATER;
        }
    
        /**
         * If @param enable has been set to true. The user can perform next PTR at
         * once.
         * 
         * @param enable
         */
        public void setEnabledNextPtrAtOnce(boolean enable) {
            if (enable) {
                mFlag = mFlag | FLAG_ENABLE_NEXT_PTR_AT_ONCE;
            } else {
                mFlag = mFlag & ~FLAG_ENABLE_NEXT_PTR_AT_ONCE;
            }
        }
    
        public boolean isEnabledNextPtrAtOnce() {
            return (mFlag & FLAG_ENABLE_NEXT_PTR_AT_ONCE) > 0;
        }
    
        /**
         * The content view will now move when
         * 
         * @param pinContent
         *            set to true.
         * 
         * @param pinContent
         */
        public void setPinContent(boolean pinContent) {
            if (pinContent) {
                mFlag = mFlag | FLAG_PIN_CONTENT;
            } else {
                mFlag = mFlag & ~FLAG_PIN_CONTENT;
            }
        }
    
        public boolean isPinContent() {
            return (mFlag & FLAG_PIN_CONTENT) > 0;
        }
    
        /**
         * It's useful when working with viewpager.
         * 
         * @param disable
         */
        public void disableWhenHorizontalMove(boolean disable) {
            mDisableWhenHorizontalMove = disable;
        }
    
        /**
         * loading will last at least for so long
         * 
         * @param time
         */
        public void setLoadingMinTime(int time) {
            mLoadingMinTime = time;
        }
    
        /**
         * Not necessary any longer. Once moved, cancel event will be sent to child.
         * 
         * @param yes
         */
        @Deprecated
        public void setInterceptEventWhileWorking(boolean yes) {
        }
    
        @SuppressWarnings({ "unused" })
        public View getContentView() {
            return mContent;
        }
    
        public void setPtrHandler(PtrHandler ptrHandler) {
            mPtrHandler = ptrHandler;
        }
    
        public void addPtrUIHandler(PtrUIHandler ptrUIHandler) {
            PtrUIHandlerHolder.addHandler(mPtrUIHandlerHolder, ptrUIHandler);
        }
    
        @SuppressWarnings({ "unused" })
        public void removePtrUIHandler(PtrUIHandler ptrUIHandler) {
            mPtrUIHandlerHolder = PtrUIHandlerHolder.removeHandler(
                    mPtrUIHandlerHolder, ptrUIHandler);
        }
    
        public void setPtrIndicator(PtrIndicator slider) {
            if (mPtrIndicator != null && mPtrIndicator != slider) {
                slider.convertFrom(mPtrIndicator);
            }
            mPtrIndicator = slider;
        }
    
        @SuppressWarnings({ "unused" })
        public float getResistance() {
            return mPtrIndicator.getResistance();
        }
    
        public void setResistance(float resistance) {
            mPtrIndicator.setResistance(resistance);
        }
    
        @SuppressWarnings({ "unused" })
        public float getDurationToClose() {
            return mDurationToClose;
        }
    
        /**
         * The duration to return back to the refresh position
         * 
         * @param duration
         */
        public void setDurationToClose(int duration) {
            mDurationToClose = duration;
        }
    
        @SuppressWarnings({ "unused" })
        public long getDurationToCloseHeader() {
            return mDurationToCloseHeader;
        }
    
        /**
         * The duration to close time
         * 
         * @param duration
         */
        public void setDurationToCloseHeader(int duration) {
            mDurationToCloseHeader = duration;
        }
    
        public void setRatioOfHeaderHeightToRefresh(float ratio) {
            mPtrIndicator.setRatioOfHeaderHeightToRefresh(ratio);
        }
    
        public int getOffsetToRefresh() {
            return mPtrIndicator.getOffsetToRefresh();
        }
    
        @SuppressWarnings({ "unused" })
        public void setOffsetToRefresh(int offset) {
            mPtrIndicator.setOffsetToRefresh(offset);
        }
    
        @SuppressWarnings({ "unused" })
        public float getRatioOfHeaderToHeightRefresh() {
            return mPtrIndicator.getRatioOfHeaderToHeightRefresh();
        }
    
        @SuppressWarnings({ "unused" })
        public void setOffsetToKeepHeaderWhileLoading(int offset) {
            mPtrIndicator.setOffsetToKeepHeaderWhileLoading(offset);
        }
    
        @SuppressWarnings({ "unused" })
        public int getOffsetToKeepHeaderWhileLoading() {
            return mPtrIndicator.getOffsetToKeepHeaderWhileLoading();
        }
    
        @SuppressWarnings({ "unused" })
        public boolean isKeepHeaderWhenRefresh() {
            return mKeepHeaderWhenRefresh;
        }
    
        public void setKeepHeaderWhenRefresh(boolean keepOrNot) {
            mKeepHeaderWhenRefresh = keepOrNot;
        }
    
        public boolean isPullToRefresh() {
            return mPullToRefresh;
        }
    
        public void setPullToRefresh(boolean pullToRefresh) {
            mPullToRefresh = pullToRefresh;
        }
    
        @SuppressWarnings({ "unused" })
        public View getHeaderView() {
            return mHeaderView;
        }
    
        public void setHeaderView(View header) {
            if (mHeaderView != null && header != null && mHeaderView != header) {
                removeView(mHeaderView);
            }
            ViewGroup.LayoutParams lp = header.getLayoutParams();
            if (lp == null) {
                lp = new LayoutParams(-1, -2);
                header.setLayoutParams(lp);
            }
            mHeaderView = header;
            addView(header);
        }
    
        @Override
        protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
            return p instanceof LayoutParams;
        }
    
        @Override
        protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
            return new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);
        }
    
        @Override
        protected ViewGroup.LayoutParams generateLayoutParams(
                ViewGroup.LayoutParams p) {
            return new LayoutParams(p);
        }
    
        @Override
        public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
            return new LayoutParams(getContext(), attrs);
        }
    
        private void sendCancelEvent() {
            if (DEBUG) {
                PtrCLog.d(LOG_TAG, "send cancel event");
            }
            // The ScrollChecker will update position and lead to send cancel event
            // when mLastMoveEvent is null.
            // fix #104, #80, #92
            if (mLastMoveEvent == null) {
                return;
            }
            MotionEvent last = mLastMoveEvent;
            MotionEvent e = MotionEvent.obtain(last.getDownTime(),
                    last.getEventTime() + ViewConfiguration.getLongPressTimeout(),
                    MotionEvent.ACTION_CANCEL, last.getX(), last.getY(),
                    last.getMetaState());
            dispatchTouchEventSupper(e);
        }
    
        private void sendDownEvent() {
            if (DEBUG) {
                PtrCLog.d(LOG_TAG, "send down event");
            }
            final MotionEvent last = mLastMoveEvent;
            MotionEvent e = MotionEvent.obtain(last.getDownTime(),
                    last.getEventTime(), MotionEvent.ACTION_DOWN, last.getX(),
                    last.getY(), last.getMetaState());
            dispatchTouchEventSupper(e);
        }
    
        public static class LayoutParams extends MarginLayoutParams {
    
            public LayoutParams(Context c, AttributeSet attrs) {
                super(c, attrs);
            }
    
            public LayoutParams(int width, int height) {
                super(width, height);
            }
    
            @SuppressWarnings({ "unused" })
            public LayoutParams(MarginLayoutParams source) {
                super(source);
            }
    
            public LayoutParams(ViewGroup.LayoutParams source) {
                super(source);
            }
        }
    
        class ScrollChecker implements Runnable {
    
            private int mLastFlingY;
            private Scroller mScroller;
            private boolean mIsRunning = false;
            private int mStart;
            private int mTo;
    
            public ScrollChecker() {
                mScroller = new Scroller(getContext());
            }
    
            public void run() {
                boolean finish = !mScroller.computeScrollOffset()
                        || mScroller.isFinished();
                int curY = mScroller.getCurrY();
                int deltaY = curY - mLastFlingY;
                if (DEBUG) {
                    if (deltaY != 0) {
                        PtrCLog.v(
                                LOG_TAG,
                                "scroll: %s, start: %s, to: %s, currentPos: %s, current :%s, last: %s, delta: %s",
                                finish, mStart, mTo,
                                mPtrIndicator.getCurrentPosY(), curY, mLastFlingY,
                                deltaY);
                    }
                }
                if (!finish) {
                    mLastFlingY = curY;
                    movePos(deltaY);
                    post(this);
                } else {
                    finish();
                }
            }
    
            private void finish() {
                if (DEBUG) {
                    PtrCLog.v(LOG_TAG, "finish, currentPos:%s",
                            mPtrIndicator.getCurrentPosY());
                }
                reset();
                onPtrScrollFinish();
            }
    
            private void reset() {
                mIsRunning = false;
                mLastFlingY = 0;
                removeCallbacks(this);
            }
    
            public void abortIfWorking() {
                if (mIsRunning) {
                    if (!mScroller.isFinished()) {
                        mScroller.forceFinished(true);
                    }
                    onPtrScrollAbort();
                    reset();
                }
            }
    
            public void tryToScrollTo(int to, int duration) {
                if (mPtrIndicator.isAlreadyHere(to)) {
                    return;
                }
                mStart = mPtrIndicator.getCurrentPosY();
                mTo = to;
                int distance = to - mStart;
                if (DEBUG) {
                    PtrCLog.d(LOG_TAG,
                            "tryToScrollTo: start: %s, distance:%s, to:%s", mStart,
                            distance, to);
                }
                removeCallbacks(this);
    
                mLastFlingY = 0;
    
                // fix #47: Scroller should be reused,
                // https://github.com/liaohuqiu/android-Ultra-Pull-To-Refresh/issues/47
                if (!mScroller.isFinished()) {
                    mScroller.forceFinished(true);
                }
                mScroller.startScroll(0, 0, 0, distance, duration);
                post(this);
                mIsRunning = true;
            }
        }
    }
    

    
    public class PtrFrameLayout extends ViewGroup {
    
        // status enum
        public final static byte PTR_STATUS_INIT = 1;//初始化
        public final static byte PTR_STATUS_PREPARE = 2;//准备下拉
        public final static byte PTR_STATUS_LOADING = 3;//loading状态
        public final static byte PTR_STATUS_COMPLETE = 4;//刷新完成
    
        private static final boolean DEBUG_LAYOUT = true;
        private static final String TAG = PtrFrameLayout.class.getName();
        public static boolean DEBUG = false;
        private static int ID = 1;
    
        // auto refresh status
        private static byte FLAG_AUTO_REFRESH_AT_ONCE = 0x01;//
        private static byte FLAG_AUTO_REFRESH_BUT_LATER = 0x01 << 1;//
        private static byte FLAG_ENABLE_NEXT_PTR_AT_ONCE = 0x01 << 2;
        private static byte FLAG_PIN_CONTENT = 0x01 << 3;//下拉的时候保持content不动
    
        private static byte MASK_AUTO_REFRESH = 0x03;
    
        protected final String LOG_TAG = "ptr-frame-" + ++ID;
        protected View mContent;
        
        // optional config for define header and content in xml file xml中header和content的id
        private int mHeaderId = 0;
        private int mContainerId = 0;
        // config
        private int mDurationToClose = 200;//，回弹到刷新高度所用时间。
        private int mDurationToCloseHeader = 1000;//头部回弹时间
        private boolean mKeepHeaderWhenRefresh = true;//刷新时保持头部显示
        private boolean mPullToRefresh = false;//下拉过程中达到触发高度就立即刷新
        private View mHeaderView;
        private PtrUIHandlerHolder mPtrUIHandlerHolder = PtrUIHandlerHolder// PtrUIHandler的holder，内部是链表结构，用于回调UI刷新状态
                .create();
        private PtrHandler mPtrHandler;//刷新hanlder，两个功能，检查conent是否可以下拉和回调刷新
        // working parameters
        private ScrollChecker mScrollChecker;//用于实现滑动，Runnable+Scroller
        private int mPagingTouchSlop;//认为是触发滑动的距离
        private int mHeaderHeight;//头部高度
    
        private byte mStatus = PTR_STATUS_INIT;//当前状态
        private boolean mDisableWhenHorizontalMove = false;//当发生水平滑动时，禁止下拉，常见的就是viewpager作为列表的header时
        private int mFlag = 0x00;//内部各种设置标识，
    
        // disable when detect moving horizontally
        private boolean mPreventForHorizontal = false;//与水平滑动相关
    
        private MotionEvent mLastMoveEvent;//最后一次move事件
    
        private PtrUIHandlerHook mRefreshCompleteHook;//钩子
    
        private int mLoadingMinTime = 500;//loading的最小时间
        private long mLoadingStartTime = 0;//loading的开始时间
        
        private PtrIndicator mPtrIndicator;//这个类很重要，用于计算滑动距离和判断各种滑动状态
        
        private boolean mHasSendCancelEvent = false;//是否已经发送了一个cancel事件
    
        public PtrFrameLayout(Context context) {
            this(context, null);
        }
    
        public PtrFrameLayout(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }
    
        public PtrFrameLayout(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            //初始化PtrIndicator
            mPtrIndicator = new PtrIndicator();
    
            //获取属性
            TypedArray arr = context.obtainStyledAttributes(attrs,
                    R.styleable.PtrFrameLayout, 0, 0);
            if (arr != null) {
    
                mHeaderId = arr.getResourceId(
                        R.styleable.PtrFrameLayout_ptr_header, mHeaderId);
                mContainerId = arr.getResourceId(
                        R.styleable.PtrFrameLayout_ptr_content, mContainerId);
    
                mPtrIndicator.setResistance(arr.getFloat(
                        R.styleable.PtrFrameLayout_ptr_resistance,
                        mPtrIndicator.getResistance()));
    
                mDurationToClose = arr.getInt(
                        R.styleable.PtrFrameLayout_ptr_duration_to_close,
                        mDurationToClose);
                mDurationToCloseHeader = arr.getInt(
                        R.styleable.PtrFrameLayout_ptr_duration_to_close_header,
                        mDurationToCloseHeader);
    
                float ratio = mPtrIndicator.getRatioOfHeaderToHeightRefresh();
                ratio = arr
                        .getFloat(
                                R.styleable.PtrFrameLayout_ptr_ratio_of_header_height_to_refresh,
                                ratio);
                mPtrIndicator.setRatioOfHeaderHeightToRefresh(ratio);
    
                mKeepHeaderWhenRefresh = arr.getBoolean(
                        R.styleable.PtrFrameLayout_ptr_keep_header_when_refresh,
                        mKeepHeaderWhenRefresh);
    
                mPullToRefresh = arr.getBoolean(
                        R.styleable.PtrFrameLayout_ptr_pull_to_fresh,
                        mPullToRefresh);
                arr.recycle();
            }
            
            //创建一个ScrollChecker
            mScrollChecker = new ScrollChecker();
    
            final ViewConfiguration conf = ViewConfiguration.get(getContext());
            mPagingTouchSlop = conf.getScaledTouchSlop() * 2;
        }
    
        /**
         * 在onFinishInflate找到子header和content
         */
        @Override
        protected void onFinishInflate() {
            final int childCount = getChildCount();
            if (childCount > 2) {//不能超过两个孩子
                throw new IllegalStateException(
                        "PtrFrameLayout only can host 2 elements");
            } else if (childCount == 2) {
                if (mHeaderId != 0 && mHeaderView == null) {
                    mHeaderView = findViewById(mHeaderId);
                }
                if (mContainerId != 0 && mContent == null) {
                    mContent = findViewById(mContainerId);
                }
    
                // not specify header or content
                if (mContent == null || mHeaderView == null) {
    
                    View child1 = getChildAt(0);
                    View child2 = getChildAt(1);
                    if (child1 instanceof PtrUIHandler) {
                        mHeaderView = child1;
                        mContent = child2;
                    } else if (child2 instanceof PtrUIHandler) {
                        mHeaderView = child2;
                        mContent = child1;
                    } else {
                        // both are not specified
                        if (mContent == null && mHeaderView == null) {
                            mHeaderView = child1;
                            mContent = child2;
                        }
                        // only one is specified
                        else {
                            if (mHeaderView == null) {
                                mHeaderView = mContent == child1 ? child2 : child1;
                            } else {
                                mContent = mHeaderView == child1 ? child2 : child1;
                            }
                        }
                    }
                }
            } else if (childCount == 1) {
                mContent = getChildAt(0);
            } else {
                TextView errorView = new TextView(getContext());
                errorView.setClickable(true);
                errorView.setTextColor(0xffff6600);
                errorView.setGravity(Gravity.CENTER);
                errorView.setTextSize(20);
                errorView
                        .setText("The content view in PtrFrameLayout is empty. Do you forget to specify its id in xml layout file?");
                mContent = errorView;
                addView(mContent);
            }
            if (mHeaderView != null) {
                mHeaderView.bringToFront();//把header调整到z轴的顶部，秋百万真实贴心啊
            }
            super.onFinishInflate();
        }
    
        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            //从这里可见此lPtrLayout不支持wrap_content，事实上也没有必要支持
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    
            if (DEBUG && DEBUG_LAYOUT) {
                PtrCLog.d(
                        LOG_TAG,
                        "onMeasure frame: width: %s, height: %s, padding: %s %s %s %s",
                        getMeasuredHeight(), getMeasuredWidth(), getPaddingLeft(),
                        getPaddingRight(), getPaddingTop(), getPaddingBottom());
    
            }
    
            //测量头部
            if (mHeaderView != null) {
                measureChildWithMargins(mHeaderView, widthMeasureSpec, 0,
                        heightMeasureSpec, 0);
                MarginLayoutParams lp = (MarginLayoutParams) mHeaderView
                        .getLayoutParams();
                //计算header的高度
                mHeaderHeight = mHeaderView.getMeasuredHeight() + lp.topMargin
                        + lp.bottomMargin;
                //把高度设置为Indicator
                mPtrIndicator.setHeaderHeight(mHeaderHeight);
            }
           
            //测量content
            if (mContent != null) {
                measureContentView(mContent, widthMeasureSpec, heightMeasureSpec);
                if (DEBUG && DEBUG_LAYOUT) {
                    ViewGroup.MarginLayoutParams lp = (MarginLayoutParams) mContent
                            .getLayoutParams();
                    PtrCLog.d(
                            LOG_TAG,
                            "onMeasure content, width: %s, height: %s, margin: %s %s %s %s",
                            getMeasuredWidth(), getMeasuredHeight(), lp.leftMargin,
                            lp.topMargin, lp.rightMargin, lp.bottomMargin);
                    PtrCLog.d(LOG_TAG,
                            "onMeasure, currentPos: %s, lastPos: %s, top: %s",
                            mPtrIndicator.getCurrentPosY(),
                            mPtrIndicator.getLastPosY(), mContent.getTop());
                }
            }
        }
    
        /**
         * 测量content的逻辑，
         * @param child
         * @param parentWidthMeasureSpec
         * @param parentHeightMeasureSpec
         */
        private void measureContentView(View child, int parentWidthMeasureSpec,
                int parentHeightMeasureSpec) {
            final MarginLayoutParams lp = (MarginLayoutParams) child
                    .getLayoutParams();
                //其实这里的逻辑与measureChildWithMargins类似,只是高度没有考虑BottomMargin，
            final int childWidthMeasureSpec = getChildMeasureSpec(
                    parentWidthMeasureSpec, getPaddingLeft() + getPaddingRight()
                            + lp.leftMargin + lp.rightMargin, lp.width);
            
            final int childHeightMeasureSpec = getChildMeasureSpec(
                    parentHeightMeasureSpec, getPaddingTop() + getPaddingBottom()
                            + lp.topMargin, lp.height);
    
            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
        }
    
        @Override
        protected void onLayout(boolean flag, int i, int j, int k, int l) {
            layoutChildren();
        }
    
        //布局
        private void layoutChildren() {
            int offsetX = mPtrIndicator.getCurrentPosY();//一般offsetX = 0；
            int paddingLeft = getPaddingLeft();
            int paddingTop = getPaddingTop();
    
            //布局的时候会把header隐藏起来
            if (mHeaderView != null) {
                MarginLayoutParams lp = (MarginLayoutParams) mHeaderView
                        .getLayoutParams();
                final int left = paddingLeft + lp.leftMargin;
                // mHeaderView.getMeasuredHeight() + lp.topMargin+ lp.bottomMargin; 
                final int top = paddingTop + lp.topMargin + offsetX - mHeaderHeight;// =  - mHeaderView.getMeasuredHeight() - lp.bottomMargin + paddingTop
                final int right = left + mHeaderView.getMeasuredWidth();
                final int bottom = top + mHeaderView.getMeasuredHeight();
                mHeaderView.layout(left, top, right, bottom);
                if (DEBUG && DEBUG_LAYOUT) {
                    PtrCLog.d(LOG_TAG, "onLayout header: %s %s %s %s", left, top,
                            right, bottom);
                }
            }
            if (mContent != null) {
                if (isPinContent()) {
                    offsetX = 0;
                }
                MarginLayoutParams lp = (MarginLayoutParams) mContent
                        .getLayoutParams();
                final int left = paddingLeft + lp.leftMargin;
                final int top = paddingTop + lp.topMargin + offsetX;
                final int right = left + mContent.getMeasuredWidth();
                final int bottom = top + mContent.getMeasuredHeight();
                if (DEBUG && DEBUG_LAYOUT) {
                    PtrCLog.d(LOG_TAG, "onLayout content: %s %s %s %s", left, top,
                            right, bottom);
                }
                mContent.layout(left, top, right, bottom);
            }
        }
    
        //调用ViewGroup的dispatchTouchEvent
        public boolean dispatchTouchEventSupper(MotionEvent e) {
            return super.dispatchTouchEvent(e);
        }
    
        //因为是直接重写了dispatchTouchEvent方法，所以不需要依靠原来的拦截事件
        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
    
            return super.onInterceptTouchEvent(ev);
        }
      //因为是直接重写了dispatchTouchEvent方法，所以不需要依靠onTouchEvent处理事件
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            return super.onTouchEvent(event);
        }
    
        
        
        @Override
        public boolean dispatchTouchEvent(MotionEvent e) {
    //        mPtrIndicator.printInfo();
            
            //如果不可用，或者内容为null，不会有事件处理。
            if (!isEnabled() || mContent == null || mHeaderView == null) {
                return dispatchTouchEventSupper(e);
            }
            int action = e.getAction();//没有mask的事件类型，所以在多手指拖动的情况下会有跳跃的现象
            switch (action) {
            //抬起或者事件取消
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mPtrIndicator.onRelease();//告诉指示器，手指抬起
                if (mPtrIndicator.hasLeftStartPosition()) {//是否已经离开了起始位置，就是已经产出了滑动了，mCurrentPos > POS_START;
                    if (DEBUG) {
                        PtrCLog.d(LOG_TAG, "call onRelease when user release");
                    }
                    onRelease(false);
                    /*
                     * 如果按下的位置不等于当前偏移的位置 mCurrentPos != mPressedPos;为什么上面有了hasLeftStartPosition的判断，这里还需要判断呢？
                     */
                    if (mPtrIndicator.hasMovedAfterPressedDown()) {
                        sendCancelEvent();//给子view发送一个cancel事件，因为已经认为是一个滑动事件了。
                        return true;
                    }
                    return dispatchTouchEventSupper(e);
                } else {
                    return dispatchTouchEventSupper(e);
                }
            //手指按下
            case MotionEvent.ACTION_DOWN:
                mHasSendCancelEvent = false;//重置标识
                
                mPtrIndicator.onPressDown(e.getX(), e.getY());//通知指示器，按下了
    
                mScrollChecker.abortIfWorking();//如果之前有滑动，立马停止
    
                mPreventForHorizontal = false;//水平滑动
                // The cancel event will be sent once the position is moved.
                // So let the event pass to children.
                // fix #93, #102
                dispatchTouchEventSupper(e);//把down事件分发给子view
                
                return true;//down事件返回true表示下拉layout始终会处理事件
    
            case MotionEvent.ACTION_MOVE:
                mLastMoveEvent = e;//记录最后一次move事件，等下可能用到
                mPtrIndicator.onMove(e.getX(), e.getY());//把手势移动计算操作都交给指示器
                
                float offsetX = mPtrIndicator.getOffsetX();//得到偏移值  new- old
                float offsetY = mPtrIndicator.getOffsetY();
    
                if (mDisableWhenHorizontalMove//横向移动的处理
                        && !mPreventForHorizontal
                        && (Math.abs(offsetX) > mPagingTouchSlop && Math
                                .abs(offsetX) > Math.abs(offsetY))) {
                    if (mPtrIndicator.isInStartPosition()) {//没有发生垂直移动
                        mPreventForHorizontal = true;
                    }
                }
                if (mPreventForHorizontal) {//有横向滑动，就交给子view处理
                    return dispatchTouchEventSupper(e);
                }
    
                boolean moveDown = offsetY > 0;//是否是向下拖动
                boolean moveUp = !moveDown;//是否是向上拖动
                boolean canMoveUp = mPtrIndicator.hasLeftStartPosition();//是否已经离开了起始位置
    
                if (DEBUG) {
                    boolean canMoveDown = mPtrHandler != null
                            && mPtrHandler.checkCanDoRefresh(this, mContent,
                                    mHeaderView);
                    PtrCLog.v(
                            LOG_TAG,
                            "ACTION_MOVE: offsetY:%s, currentPos: %s, moveUp: %s, canMoveUp: %s, moveDown: %s: canMoveDown: %s",
                            offsetY, mPtrIndicator.getCurrentPosY(), moveUp,
                            canMoveUp, moveDown, canMoveDown);
                }
    
                // disable move when header not reach top 禁用移动，当header没有达到top位置
                //这里如果是向下滑动，并且content是可以向下滑动的，就让content自己滑动
                if (moveDown
                        && mPtrHandler != null
                        && !mPtrHandler.checkCanDoRefresh(this, mContent,
                                mHeaderView)) {
                    return dispatchTouchEventSupper(e);
                }
                //否则如果是向上滑动并且之前已经向下移动了一段距离(这时向上拖动就应该回到初始位置)
                //或者子view不同向下滑动了，就自己处理滑动
                if ((moveUp && canMoveUp) || moveDown) {
                    movePos(offsetY);
                    return true;
                }
                //否则就就交给子view滑动
            }
            return dispatchTouchEventSupper(e);
        }
    
        /**
         * if deltaY > 0, move the content down
         *      否则如果是向上滑动并且之前已经向下移动了一段距离
                或者子view不同向下滑动了，就自己处理滑动
         * @param deltaY
         */
        private void movePos(float deltaY) {
            // has reached the top
            if ((deltaY < 0 && mPtrIndicator.isInStartPosition())) {// meybe no run
                //如果已经把内容拖动到顶部就不用再往上拖动了，这是一个保护性措施
                if (DEBUG) {
                    PtrCLog.e(LOG_TAG, String.format("has reached the top"));
                }
                return;
            }
    
            int to = mPtrIndicator.getCurrentPosY() + (int) deltaY;//需要到达的位置
    
            // over top 如果将要到达的位置小于0
            if (mPtrIndicator.willOverTop(to)) {//meybe no run
                if (DEBUG) {
                    PtrCLog.e(LOG_TAG, String.format("over top"));
                }
                to = PtrIndicator.POS_START;//让其等于0
            }
    
            mPtrIndicator.setCurrentPos(to);//更新当前的mCurrentPos
            int change = to - mPtrIndicator.getLastPosY();//change其实就是需要移动的大小
            updatePos(change);
        }
    
        private void updatePos(int change) {
            if (change == 0) {
                return;
            }
    
            boolean isUnderTouch = mPtrIndicator.isUnderTouch();//手指是否还在拖动，没有抬起
    
            //如果是手指在拖动，并且已经产生移动了
            // once moved, cancel event will be sent to child
            if (isUnderTouch && !mHasSendCancelEvent
                    && mPtrIndicator.hasMovedAfterPressedDown()) {
                mHasSendCancelEvent = true;
                sendCancelEvent();
            }
    
            // leave initiated position or just refresh complete
            //开始下拉
            if ((mPtrIndicator.hasJustLeftStartPosition() && mStatus == PTR_STATUS_INIT)
                    || (mPtrIndicator.goDownCrossFinishPosition()//超过了触发刷新位置
                            && mStatus == PTR_STATUS_COMPLETE//刷新状态是完成刷新了
                            && isEnabledNextPtrAtOnce())) {//立即启动下一次刷新
    
                mStatus = PTR_STATUS_PREPARE;//更新状态
                mPtrUIHandlerHolder.onUIRefreshPrepare(this);//通知预备要刷新了
                if (DEBUG) {
                    PtrCLog.i(LOG_TAG,
                            "PtrUIHandler: onUIRefreshPrepare, mFlag %s", mFlag);
                }
            }
    
            // back to initiated position mLastPos != POS_START && mCurrentPos == POS_START;
             //刚刚回到起始位置，之前是下拉的，现在已经复位了
            if (mPtrIndicator.hasJustBackToStartPosition()) {
                tryToNotifyReset();//重置
                // recover event to children
                //模拟一个down事件，重新把事件分发给子view，因为接下来的事件又要交给子view处理了
                if (isUnderTouch) {
                    sendDownEvent();
                }
            }
    
            // Pull to Refresh
            if (mStatus == PTR_STATUS_PREPARE) {
                // reach fresh height while moving from top to bottom
                //从下往下拉，如果是超过了触发刷新的距离就立即出发刷新。
                if (isUnderTouch && !isAutoRefresh() && mPullToRefresh
                        && mPtrIndicator.crossRefreshLineFromTopToBottom()) {////刚刚从上往下拉，超过了触发刷新的位置
                    Log.e(TAG, "crossRefreshLineFromTopToBottom");
                    tryToPerformRefresh();
                }
                // reach header height while auto refresh
                if (performAutoRefreshButLater()
                        && mPtrIndicator//  刚刚从上往下拉，超过了header的高度
                                .hasJustReachedHeaderHeightFromTopToBottom()) {
                    Log.e(TAG, "performAutoRefreshButLater");
                    tryToPerformRefresh();
                }
            }
    
            if (DEBUG) {
                PtrCLog.v(
                        LOG_TAG,
                        "updatePos: change: %s, current: %s last: %s, top: %s, headerHeight: %s",
                        change, mPtrIndicator.getCurrentPosY(),
                        mPtrIndicator.getLastPosY(), mContent.getTop(),
                        mHeaderHeight);
            }
    
            mHeaderView.offsetTopAndBottom(change);
            if (!isPinContent()) {
                mContent.offsetTopAndBottom(change);
            }
            invalidate();
    
            if (mPtrUIHandlerHolder.hasHandler()) {
                mPtrUIHandlerHolder.onUIPositionChange(this, isUnderTouch, mStatus,
                        mPtrIndicator);
            }
            onPositionChange(isUnderTouch, mStatus, mPtrIndicator);
        }
    
        protected void onPositionChange(boolean isInTouching, byte status,
                PtrIndicator mPtrIndicator) {
        }
    
        @SuppressWarnings("unused")
        public int getHeaderHeight() {
            return mHeaderHeight;
        }
    
        private void onRelease(boolean stayForLoading) {
    
            tryToPerformRefresh();
    
            if (mStatus == PTR_STATUS_LOADING) {//已经触发了刷新
                // keep header for fresh
                if (mKeepHeaderWhenRefresh) {//loading时保持头部
                    // scroll header back
                    if (mPtrIndicator.isOverOffsetToKeepHeaderWhileLoading()
                            && !stayForLoading) {
                        mScrollChecker.tryToScrollTo(
                                mPtrIndicator.getOffsetToKeepHeaderWhileLoading(),//获取保持头部的高度
                                mDurationToClose);
                    } else {
                        // do nothing
                    }
                } else {//回到其实位置
                    tryScrollBackToTopWhileLoading();
                }
            } else {
                if (mStatus == PTR_STATUS_COMPLETE) {//已经是刷新完成状态
                    notifyUIRefreshComplete(false);
                } else {
                    tryScrollBackToTopAbortRefresh();//回到起始位置
                }
            }
        }
    
        /**
         * please DO REMEMBER resume the hook
         * 
         * @param hook
         */
    
        public void setRefreshCompleteHook(PtrUIHandlerHook hook) {
            mRefreshCompleteHook = hook;
            hook.setResumeAction(new Runnable() {
                @Override
                public void run() {
                    if (DEBUG) {
                        PtrCLog.d(LOG_TAG, "mRefreshCompleteHook resume.");
                    }
                    notifyUIRefreshComplete(true);
                }
            });
        }
    
        /**
         * Scroll back to to if is not under touch
         */
        private void tryScrollBackToTop() {
            if (!mPtrIndicator.isUnderTouch()) {
                mScrollChecker.tryToScrollTo(PtrIndicator.POS_START,
                        mDurationToCloseHeader);
            }
        }
    
        /**
         * just make easier to understand
         */
        private void tryScrollBackToTopWhileLoading() {
            tryScrollBackToTop();
        }
    
        /**
         * just make easier to understand
         */
        private void tryScrollBackToTopAfterComplete() {
            tryScrollBackToTop();
        }
    
        /**
         * just make easier to understand
         */
        private void tryScrollBackToTopAbortRefresh() {
            tryScrollBackToTop();
        }
    
        private boolean tryToPerformRefresh() {
            if (mStatus != PTR_STATUS_PREPARE) {
                return false;
            }
    
            //自动刷新和拖动刷新
            if ((mPtrIndicator.isOverOffsetToKeepHeaderWhileLoading() && isAutoRefresh())
                    || mPtrIndicator.isOverOffsetToRefresh()) {
                mStatus = PTR_STATUS_LOADING;
                performRefresh();
            }
            return false;
        }
    
        /**
         * 执行刷新
         */
        private void performRefresh() {
            mLoadingStartTime = System.currentTimeMillis();
            if (mPtrUIHandlerHolder.hasHandler()) {
                mPtrUIHandlerHolder.onUIRefreshBegin(this);
                if (DEBUG) {
                    PtrCLog.i(LOG_TAG, "PtrUIHandler: onUIRefreshBegin");
                }
            }
            if (mPtrHandler != null) {
                mPtrHandler.onRefreshBegin(this);
            }
        }
    
        /**
         * If at the top and not in loading, reset
         */
        private boolean tryToNotifyReset() {
            if ((mStatus == PTR_STATUS_COMPLETE || mStatus == PTR_STATUS_PREPARE)
                    && mPtrIndicator.isInStartPosition()) {
                if (mPtrUIHandlerHolder.hasHandler()) {
                    mPtrUIHandlerHolder.onUIReset(this);
                    if (DEBUG) {
                        PtrCLog.i(LOG_TAG, "PtrUIHandler: onUIReset");
                    }
                }
                mStatus = PTR_STATUS_INIT;
                clearFlag();
                return true;
            }
            return false;
        }
    
        protected void onPtrScrollAbort() {
            if (mPtrIndicator.hasLeftStartPosition() && isAutoRefresh()) {
                if (DEBUG) {
                    PtrCLog.d(LOG_TAG, "call onRelease after scroll abort");
                }
                onRelease(true);
            }
        }
    
        protected void onPtrScrollFinish() {
            if (mPtrIndicator.hasLeftStartPosition() && isAutoRefresh()) {
                if (DEBUG) {
                    PtrCLog.d(LOG_TAG, "call onRelease after scroll finish");
                }
                onRelease(true);
            }
        }
    
        /**
         * Detect whether is refreshing.
         * 
         * @return
         */
        public boolean isRefreshing() {
            return mStatus == PTR_STATUS_LOADING;
        }
    
        /**
         * Call this when data is loaded. The UI will perform complete at once or
         * after a delay, depends on the time elapsed is greater then
         * {@link #mLoadingMinTime} or not.
         */
        final public void refreshComplete() {
            if (DEBUG) {
                PtrCLog.i(LOG_TAG, "refreshComplete");
            }
    
            if (mRefreshCompleteHook != null) {
                mRefreshCompleteHook.reset();
            }
    
            int delay = (int) (mLoadingMinTime - (System.currentTimeMillis() - mLoadingStartTime));
            if (delay <= 0) {
                if (DEBUG) {
                    PtrCLog.d(LOG_TAG, "performRefreshComplete at once");
                }
                performRefreshComplete();
            } else {
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        performRefreshComplete();
                    }
                }, delay);
                if (DEBUG) {
                    PtrCLog.d(LOG_TAG, "performRefreshComplete after delay: %s",
                            delay);
                }
            }
        }
    
        /**
         * Do refresh complete work when time elapsed is greater than
         * {@link #mLoadingMinTime}
         */
        private void performRefreshComplete() {
            mStatus = PTR_STATUS_COMPLETE;
    
            // if is auto refresh do nothing, wait scroller stop
            if (mScrollChecker.mIsRunning && isAutoRefresh()) {
                // do nothing
                if (DEBUG) {
                    PtrCLog.d(
                            LOG_TAG,
                            "performRefreshComplete do nothing, scrolling: %s, auto refresh: %s",
                            mScrollChecker.mIsRunning, mFlag);
                }
                return;
            }
    
            notifyUIRefreshComplete(false);
        }
    
        /**
         * Do real refresh work. If there is a hook, execute the hook first.
         * 
         * @param ignoreHook
         */
        private void notifyUIRefreshComplete(boolean ignoreHook) {
            /**
             * After hook operation is done, {@link #notifyUIRefreshComplete} will
             * be call in resume action to ignore hook.
             */
            if (mPtrIndicator.hasLeftStartPosition() && !ignoreHook
                    && mRefreshCompleteHook != null) {
                if (DEBUG) {
                    PtrCLog.d(LOG_TAG,
                            "notifyUIRefreshComplete mRefreshCompleteHook run.");
                }
    
                mRefreshCompleteHook.takeOver();
                return;
            }
            if (mPtrUIHandlerHolder.hasHandler()) {
                if (DEBUG) {
                    PtrCLog.i(LOG_TAG, "PtrUIHandler: onUIRefreshComplete");
                }
                mPtrUIHandlerHolder.onUIRefreshComplete(this);
            }
            mPtrIndicator.onUIRefreshComplete();
            tryScrollBackToTopAfterComplete();
            tryToNotifyReset();
        }
    
        public void autoRefresh() {
            autoRefresh(true, mDurationToCloseHeader);
        }
    
        public void autoRefresh(boolean atOnce) {
            autoRefresh(atOnce, mDurationToCloseHeader);
        }
    
        private void clearFlag() {
            // remove auto fresh flag
            mFlag = mFlag & ~MASK_AUTO_REFRESH;
        }
    
        public void autoRefresh(boolean atOnce, int duration) {
    
            if (mStatus != PTR_STATUS_INIT) {
                return;
            }
    
            mFlag |= atOnce ? FLAG_AUTO_REFRESH_AT_ONCE
                    : FLAG_AUTO_REFRESH_BUT_LATER;
    
            mStatus = PTR_STATUS_PREPARE;
            if (mPtrUIHandlerHolder.hasHandler()) {
                mPtrUIHandlerHolder.onUIRefreshPrepare(this);
                if (DEBUG) {
                    PtrCLog.i(LOG_TAG,
                            "PtrUIHandler: onUIRefreshPrepare, mFlag %s", mFlag);
                }
            }
            mScrollChecker.tryToScrollTo(mPtrIndicator.getOffsetToRefresh(),
                    duration);
            if (atOnce) {
                mStatus = PTR_STATUS_LOADING;
                performRefresh();
            }
        }
    
        public boolean isAutoRefresh() {
            return (mFlag & MASK_AUTO_REFRESH) > 0;
        }
    
        private boolean performAutoRefreshButLater() {
            return (mFlag & MASK_AUTO_REFRESH) == FLAG_AUTO_REFRESH_BUT_LATER;
        }
    
        /**
         * If @param enable has been set to true. The user can perform next PTR at
         * once.
         * 
         * @param enable
         */
        public void setEnabledNextPtrAtOnce(boolean enable) {
            if (enable) {
                mFlag = mFlag | FLAG_ENABLE_NEXT_PTR_AT_ONCE;
            } else {
                mFlag = mFlag & ~FLAG_ENABLE_NEXT_PTR_AT_ONCE;
            }
        }
    
        public boolean isEnabledNextPtrAtOnce() {
            return (mFlag & FLAG_ENABLE_NEXT_PTR_AT_ONCE) > 0;
        }
    
        /**
         * The content view will now move when
         * 
         * @param pinContent
         *            set to true.
         * 
         * @param pinContent
         */
        public void setPinContent(boolean pinContent) {
            if (pinContent) {
                mFlag = mFlag | FLAG_PIN_CONTENT;
            } else {
                mFlag = mFlag & ~FLAG_PIN_CONTENT;
            }
        }
    
        public boolean isPinContent() {
            return (mFlag & FLAG_PIN_CONTENT) > 0;
        }
    
        /**
         * It's useful when working with viewpager.
         * 
         * @param disable
         */
        public void disableWhenHorizontalMove(boolean disable) {
            mDisableWhenHorizontalMove = disable;
        }
    
        /**
         * loading will last at least for so long
         * 
         * @param time
         */
        public void setLoadingMinTime(int time) {
            mLoadingMinTime = time;
        }
    
        /**
         * Not necessary any longer. Once moved, cancel event will be sent to child.
         * 
         * @param yes
         */
        @Deprecated
        public void setInterceptEventWhileWorking(boolean yes) {
        }
    
        @SuppressWarnings({ "unused" })
        public View getContentView() {
            return mContent;
        }
    
        public void setPtrHandler(PtrHandler ptrHandler) {
            mPtrHandler = ptrHandler;
        }
    
        public void addPtrUIHandler(PtrUIHandler ptrUIHandler) {
            PtrUIHandlerHolder.addHandler(mPtrUIHandlerHolder, ptrUIHandler);
        }
    
        @SuppressWarnings({ "unused" })
        public void removePtrUIHandler(PtrUIHandler ptrUIHandler) {
            mPtrUIHandlerHolder = PtrUIHandlerHolder.removeHandler(
                    mPtrUIHandlerHolder, ptrUIHandler);
        }
    
        public void setPtrIndicator(PtrIndicator slider) {
            if (mPtrIndicator != null && mPtrIndicator != slider) {
                slider.convertFrom(mPtrIndicator);
            }
            mPtrIndicator = slider;
        }
    
        @SuppressWarnings({ "unused" })
        public float getResistance() {
            return mPtrIndicator.getResistance();
        }
    
        public void setResistance(float resistance) {
            mPtrIndicator.setResistance(resistance);
        }
    
        @SuppressWarnings({ "unused" })
        public float getDurationToClose() {
            return mDurationToClose;
        }
    
        /**
         * The duration to return back to the refresh position
         * 
         * @param duration
         */
        public void setDurationToClose(int duration) {
            mDurationToClose = duration;
        }
    
        @SuppressWarnings({ "unused" })
        public long getDurationToCloseHeader() {
            return mDurationToCloseHeader;
        }
    
        /**
         * The duration to close time
         * 
         * @param duration
         */
        public void setDurationToCloseHeader(int duration) {
            mDurationToCloseHeader = duration;
        }
    
        public void setRatioOfHeaderHeightToRefresh(float ratio) {
            mPtrIndicator.setRatioOfHeaderHeightToRefresh(ratio);
        }
    
        public int getOffsetToRefresh() {
            return mPtrIndicator.getOffsetToRefresh();
        }
    
        @SuppressWarnings({ "unused" })
        public void setOffsetToRefresh(int offset) {
            mPtrIndicator.setOffsetToRefresh(offset);
        }
    
        @SuppressWarnings({ "unused" })
        public float getRatioOfHeaderToHeightRefresh() {
            return mPtrIndicator.getRatioOfHeaderToHeightRefresh();
        }
    
        @SuppressWarnings({ "unused" })
        public void setOffsetToKeepHeaderWhileLoading(int offset) {
            mPtrIndicator.setOffsetToKeepHeaderWhileLoading(offset);
        }
    
        @SuppressWarnings({ "unused" })
        public int getOffsetToKeepHeaderWhileLoading() {
            return mPtrIndicator.getOffsetToKeepHeaderWhileLoading();
        }
    
        @SuppressWarnings({ "unused" })
        public boolean isKeepHeaderWhenRefresh() {
            return mKeepHeaderWhenRefresh;
        }
    
        public void setKeepHeaderWhenRefresh(boolean keepOrNot) {
            mKeepHeaderWhenRefresh = keepOrNot;
        }
    
        public boolean isPullToRefresh() {
            return mPullToRefresh;
        }
    
        public void setPullToRefresh(boolean pullToRefresh) {
            mPullToRefresh = pullToRefresh;
        }
    
        @SuppressWarnings({ "unused" })
        public View getHeaderView() {
            return mHeaderView;
        }
    
        public void setHeaderView(View header) {
            if (mHeaderView != null && header != null && mHeaderView != header) {
                removeView(mHeaderView);
            }
            ViewGroup.LayoutParams lp = header.getLayoutParams();
            if (lp == null) {
                lp = new LayoutParams(-1, -2);
                header.setLayoutParams(lp);
            }
            mHeaderView = header;
            addView(header);
        }
    
        @Override
        protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
            return p instanceof LayoutParams;
        }
    
        @Override
        protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
            return new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);
        }
    
        @Override
        protected ViewGroup.LayoutParams generateLayoutParams(
                ViewGroup.LayoutParams p) {
            return new LayoutParams(p);
        }
    
        @Override
        public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
            return new LayoutParams(getContext(), attrs);
        }
    
        private void sendCancelEvent() {
            if (DEBUG) {
                PtrCLog.d(LOG_TAG, "send cancel event");
            }
            // The ScrollChecker will update position and lead to send cancel event
            // when mLastMoveEvent is null.
            // fix #104, #80, #92
            if (mLastMoveEvent == null) {
                return;
            }
            MotionEvent last = mLastMoveEvent;
            MotionEvent e = MotionEvent.obtain(last.getDownTime(),
                    last.getEventTime() + ViewConfiguration.getLongPressTimeout(),
                    MotionEvent.ACTION_CANCEL, last.getX(), last.getY(),
                    last.getMetaState());
            dispatchTouchEventSupper(e);
        }
    
        private void sendDownEvent() {
            if (DEBUG) {
                PtrCLog.d(LOG_TAG, "send down event");
            }
            final MotionEvent last = mLastMoveEvent;
            MotionEvent e = MotionEvent.obtain(last.getDownTime(),
                    last.getEventTime(), MotionEvent.ACTION_DOWN, last.getX(),
                    last.getY(), last.getMetaState());
            dispatchTouchEventSupper(e);
        }
    
        public static class LayoutParams extends MarginLayoutParams {
    
            public LayoutParams(Context c, AttributeSet attrs) {
                super(c, attrs);
            }
    
            public LayoutParams(int width, int height) {
                super(width, height);
            }
    
            @SuppressWarnings({ "unused" })
            public LayoutParams(MarginLayoutParams source) {
                super(source);
            }
    
            public LayoutParams(ViewGroup.LayoutParams source) {
                super(source);
            }
        }
    
        class ScrollChecker implements Runnable {
    
            private int mLastFlingY;
            private Scroller mScroller;
            private boolean mIsRunning = false;
            private int mStart;
            private int mTo;
    
            public ScrollChecker() {
                mScroller = new Scroller(getContext());
            }
    
            public void run() {
                boolean finish = !mScroller.computeScrollOffset()
                        || mScroller.isFinished();
                int curY = mScroller.getCurrY();
                int deltaY = curY - mLastFlingY;
                if (DEBUG) {
                    if (deltaY != 0) {
                        PtrCLog.v(
                                LOG_TAG,
                                "scroll: %s, start: %s, to: %s, currentPos: %s, current :%s, last: %s, delta: %s",
                                finish, mStart, mTo,
                                mPtrIndicator.getCurrentPosY(), curY, mLastFlingY,
                                deltaY);
                    }
                }
                if (!finish) {
                    mLastFlingY = curY;
                    movePos(deltaY);
                    post(this);
                } else {
                    finish();
                }
            }
    
            private void finish() {
                if (DEBUG) {
                    PtrCLog.v(LOG_TAG, "finish, currentPos:%s",
                            mPtrIndicator.getCurrentPosY());
                }
                reset();
                onPtrScrollFinish();
            }
    
            private void reset() {
                mIsRunning = false;
                mLastFlingY = 0;
                removeCallbacks(this);
            }
    
            public void abortIfWorking() {
                if (mIsRunning) {
                    if (!mScroller.isFinished()) {
                        mScroller.forceFinished(true);
                    }
                    onPtrScrollAbort();
                    reset();
                }
            }
    
            public void tryToScrollTo(int to, int duration) {
                if (mPtrIndicator.isAlreadyHere(to)) {
                    return;
                }
                mStart = mPtrIndicator.getCurrentPosY();
                mTo = to;
                int distance = to - mStart;
                if (DEBUG) {
                    PtrCLog.d(LOG_TAG,
                            "tryToScrollTo: start: %s, distance:%s, to:%s", mStart,
                            distance, to);
                }
                removeCallbacks(this);
    
                mLastFlingY = 0;
    
                // fix #47: Scroller should be reused,
                // https://github.com/liaohuqiu/android-Ultra-Pull-To-Refresh/issues/47
                if (!mScroller.isFinished()) {
                    mScroller.forceFinished(true);
                }
                mScroller.startScroll(0, 0, 0, distance, duration);
                post(this);
                mIsRunning = true;
            }
        }
    }
```