# View的事件分发源码分析(5.0)

---
## 1 View的事件分发

前面已经大概的分析了2.3版本的源码，现在会对事件分发进行更加深入的学习。

在Android中使用MotionEvent来表示一个事件，当事件产生后，WMS会通过IPC把事件分发给当前处于活动的窗口，在应用层，最先获取事件的是ViewRoot中的W类，然后通过ViewRoot把事件传递给它内部的mView，然后是Activity。但是对于我们程序员而已，能开始操作事件的起始位置是Activity。所以姑且把Activity作为事件分发的起点。

---
## 2 事件分发的流程

能开始操作事件的起始位置是Activity中的dispatchTouchEvent方法：
```java
      public boolean dispatchTouchEvent(MotionEvent ev) {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            //这是一个空方法
                onUserInteraction();
            }
            if (getWindow().superDispatchTouchEvent(ev)) {
                return true;
            }
            return onTouchEvent(ev);
        }
```
可以看到它优先把时间传递给了它内部的Window，其实就是PhoneWindow了，而PhoneWindow的superDispatchTouchEvent方法只是简单的把事件传递给它的DecorView，这里就不贴源码了。大概流程如下：

`Activity--->Window--->View`

**如果想屏幕窗口中View的事件，可以从Activity中拦截**

---
## 3 与事件分发的相关的三个方法

- `dispatchTouchEvent (View,ViewGroup)` 传递事件
- `onInterceptouchEvent (ViewGroup)` 拦截事件，View不需要拦截事件
- `onTouchEvent (View,ViewGroup)` 处理事件
- 事件传递的返回值，ture表示拦截不继续、false表示不拦截，继续流程
- 事件处理的返回值，true表示处理了，false由上层View处理

---
## 4 View事件分发源码分析

在分析之前说明一下事件的分类，MotionEvent描述了用户的行为

*   ACTION_DOWN **手指按下**
*   ACTION_UP **手指抬起**
*   ACTION_MOVE **手指移动**
*   ACTION_POINTER_DOWN **多指触控时，另一个手指按下**
*   ACTION_POINTER_UP **多指触控时，有一个手指抬起**
*   ACTION_CANCEL **事件被取消**


在一个View树中，最先得到事件的肯定是根View，对于Activity来讲，就是其Window内部的DecorView了，事件通过dispatchTouchEvent方法传递到View中，而根View是ViewGroup类型的，但是ViewGroup的事件分发也依赖于View的事件分发，所以先来分析View的dispatchTouchEvent方法：

---
### 4.1 View.dispatchTouchEvent方法分析
```java
      public boolean dispatchTouchEvent(MotionEvent event) {
           ......
            //Dwon事件，表示一系列事件的开始，这里要取消嵌套滑动,mNestedScrollingParent置为null
            final int actionMasked = event.getActionMasked();
            if (actionMasked == MotionEvent.ACTION_DOWN) {
                // Defensive cleanup for new gesture
                stopNestedScroll();
            }
            //onFilterTouchEventForSecurity一般都成立
            if (onFilterTouchEventForSecurity(event)) {
                //noinspection SimplifiableIfStatement
                ListenerInfo li = mListenerInfo;
                /*
                首先mListenerInfo一般都不会null
                当mOnTouchListener不为null并且其onTouch方法返回true是，将会直接处理事件，
                那么onTouchEvent方法将不会被调用，这也是给我们一个机会在外部优先处理View的事件
                */
                if (li != null && li.mOnTouchListener != null
                        && (mViewFlags & ENABLED_MASK) == ENABLED
                        && li.mOnTouchListener.onTouch(this, event)) {
                    result = true;
                }
               //当上面不成立，并且onTouchEvent返回true时，表示事件被处理了
                if (!result && onTouchEvent(event)) {
                    result = true;
                }
            }
            .......
            //如果事件是UP或者Cancel，或者事件是Down并且事件没有被处理停止嵌套滑动
            if (actionMasked == MotionEvent.ACTION_UP ||
                    actionMasked == MotionEvent.ACTION_CANCEL ||
                    (actionMasked == MotionEvent.ACTION_DOWN && !result)) {
                stopNestedScroll();
            }
            return result;
        }
```

**分析View的dispatchTouchEvent方法，可知**：

View中如果设置了onTouchEventListener，并且我们在onTouch方法中返回true，View的内部将不会再获取事件，这是系统提供给我们的优先处理View事件的接口，如果上述不成立，才会走View内部的onTouchEvent方法，并返回onTouchEvent的返回值

接下来分析View.的onTouchEvent方法

#### View.onTouchEvent方法分析

```java
    public boolean onTouchEvent(MotionEvent event) {
            final float x = event.getX();
            final float y = event.getY();
            //View的内部标识，一般是View的状态
            final int viewFlags = mViewFlags;
            //事件类型
            final int action = event.getAction();
            //这里表示如果View是不可用的，即调用的setDisable(false),或者在xml中设置
            if ((viewFlags & ENABLED_MASK) == DISABLED) {
                if (action == MotionEvent.ACTION_UP && (mPrivateFlags & PFLAG_PRESSED) != 0) {/恢复按下的状态
                    setPressed(false);
                }
               //可以看出，只要View是可以点击或者可以长按的，事件还是会被处理，但是不会响应
               //View的click或者longClick事件
                return (((viewFlags & CLICKABLE) == CLICKABLE
                        || (viewFlags & LONG_CLICKABLE) == LONG_CLICKABLE)
                        || (viewFlags & CONTEXT_CLICKABLE) == CONTEXT_CLICKABLE);
            }
            //如果有mTouchDelegate，优先让其处理
            if (mTouchDelegate != null) {
                if (mTouchDelegate.onTouchEvent(event)) {
                    return true;
                }
            }
            //最后如果View是可以点击或者长按的，则会进入，且此代码块必返回true，表示事件使用被处理
            if (((viewFlags & CLICKABLE) == CLICKABLE ||
                    (viewFlags & LONG_CLICKABLE) == LONG_CLICKABLE) ||
                    (viewFlags & CONTEXT_CLICKABLE) == CONTEXT_CLICKABLE) {
                switch (action) {
                //抬起事件
                    case MotionEvent.ACTION_UP:
                    //按下了或者预按下了(不懂)
                        boolean prepressed = (mPrivateFlags & PFLAG_PREPRESSED) != 0;
                        //处于按下的状态
                        if ((mPrivateFlags & PFLAG_PRESSED) != 0 || prepressed) {
                            //获取焦点如果可以获取焦点并且触摸模式可以获取焦点，并且当前没有获取到焦点
                            boolean focusTaken = false;
                            if (isFocusable() && isFocusableInTouchMode() && !isFocused()) {
                            //请求焦点，如果已有返回false
                                focusTaken = requestFocus();
                            }
                            //跟涟漪有关
                            if (prepressed) {
                                // The button is being released before we actually
                                // showed it as pressed.  Make it show the pressed
                                // state now (before scheduling the click) to ensure
                                // the user sees it.
                                setPressed(true, x, y);
                           }
                            if (!mHasPerformedLongPress && !mIgnoreNextUpEvent) {
                                // This is a tap, so remove the longpress check
                                removeLongPressCallback();
    
                                // 这里很重要，如果View处于可获取焦点状态，但是他没有获取到焦点，那么第一次点击它时，不会调用它的click事件
                                if (!focusTaken) {
                                //下面是处理点击事件
                                    // Use a Runnable and post this rather than calling
                                    // performClick directly. This lets other visual state
                                    // of the view update before click actions start.
                                    if (mPerformClick == null) {
                                        mPerformClick = new PerformClick();
                                    }
                                    if (!post(mPerformClick)) {
                                        performClick();
                                    }
                                }
                            }
    
                            if (mUnsetPressedState == null) {
                                mUnsetPressedState = new UnsetPressedState();
                            }
                         //接下来的恢复按下状态
                            if (prepressed) {
                                postDelayed(mUnsetPressedState,
                                        ViewConfiguration.getPressedStateDuration());
                            } else if (!post(mUnsetPressedState)) {
                                // If the post failed, unpress right now
                                mUnsetPressedState.run();
                            }
    
                            removeTapCallback();
                        }
                        mIgnoreNextUpEvent = false;
                        break;
    
                    case MotionEvent.ACTION_DOWN:
                        mHasPerformedLongPress = false;
    
                        if (performButtonActionOnTouchDown(event)) {
                            break;
                        }
    
                        //一般返回ture
                        boolean isInScrollingContainer = isInScrollingContainer();
    
                        // For views inside a scrolling container, delay the pressed feedback for
                        // a short period in case this is a scroll.
                        if (isInScrollingContainer) {
                            mPrivateFlags |= PFLAG_PREPRESSED;
                            if (mPendingCheckForTap == null) {
                                mPendingCheckForTap = new CheckForTap();
                            }
                            mPendingCheckForTap.x = event.getX();
                            mPendingCheckForTap.y = event.getY();
                            postDelayed(mPendingCheckForTap, ViewConfiguration.getTapTimeout());
                        } else {
                            // Not inside a scrolling container, so show the feedback right away
                            setPressed(true, x, y);
                            checkForLongClick(0);
                        }
                        break;
    
                    case MotionEvent.ACTION_CANCEL:
                    //事件被取消，移除所有回调，恢复状态
                        setPressed(false);
                        removeTapCallback();
                        removeLongPressCallback();
                        mInContextButtonPress = false;
                        mHasPerformedLongPress = false;
                        mIgnoreNextUpEvent = false;
                        break;
    
                    case MotionEvent.ACTION_MOVE:
                        drawableHotspotChanged(x, y);
                        //事件移除边界，直接恢复状态，移除callBack，不会触发点击和长按事件
                        // Be lenient about moving outside of buttons
                        if (!pointInView(x, y, mTouchSlop)) {
                            // Outside button
                            removeTapCallback();
                            if ((mPrivateFlags & PFLAG_PRESSED) != 0) {
                                // Remove any future long press/tap checks
                                removeLongPressCallback();
    
                                setPressed(false);
                            }
                        }
                        break;
                }
              //只要可以点击或者长按，就消费事件
                return true;
            }
        //否则不消费事件
            return false;
        }
```

**分析View.的onTouchEvent事件可知：**

- View是否可用不影响事件的处理，只要View是可以点击的或者长按的，则View就会消费事件，但是如果View不可用，则不会触发点击或长按事件
- focus对View的点击事件有影响，View是isFocusable的并且isFocusableInTouchMode的并且当前没有获取到焦点，则先回请求焦点，此次点击不会响应click等事件

由于View没有子View，所以不需要拦截事件，没有拦截事件的方法。

分析完View的事件分发，再来分析ViewGroup的事件分发

### 4.2 ViewGroup的事件分发

ViewGroup继承自View，比View多了一个onInterceptTouchEvent，只重写了View的dispatchTouchEvent方法，所以默认ViewGroup的事件处理和View是一样的，只是改变了事件的分发逻辑，因为它有子View

#### ViewGroup的onInterceptTouchEvent

```java
      public boolean onInterceptTouchEvent(MotionEvent ev) {
            return false;
        }
```

可以看出默认的ViewGroup不会拦截事件

#### ViewGroup的dispatchTouchEvent方法

在了解dispatchTouchEvent方法前，先来了解一下TouchTarget，类似Handler中的Message的回收复用机制，用来记录处理事件的子view：

```java
        private static final class TouchTarget {
            private static final int MAX_RECYCLED = 32;
            private static final Object sRecycleLock = new Object[0];
            private static TouchTarget sRecycleBin; // 回收再利用的链表头
            private static int sRecycledCount;
    
            public static final int ALL_POINTER_IDS = -1; // all ones
    
            // 处理事件子view
            public View child;
            public int pointerIdBits;
            public TouchTarget next;//指向链表中的下一个
    
            private TouchTarget() {
            }
    
            // Message里也有类似的实现
            public static TouchTarget obtain(View child, int pointerIdBits) {
                final TouchTarget target;
                synchronized (sRecycleLock) {
                    if (sRecycleBin == null) { // 没有可以回收的目标，则new一个返回
                        target = new TouchTarget(); 
                    } else {
                        target = sRecycleBin; // 重用当前的sRecycleBin
                        sRecycleBin = target.next; // 更新sRecycleBin指向下一个
                         sRecycledCount--; // 重用了一个，可回收的减1
                        target.next = null; // 切断next指向
                    }
                }
                target.child = child; // 找到合适的target后，赋值
                target.pointerIdBits = pointerIdBits;
                return target;
            }
    
            public void recycle() { // 回收过程
                synchronized (sRecycleLock) {
                    if (sRecycledCount < MAX_RECYCLED) {//没有超过链表长度
                        next = sRecycleBin; // next指向旧的可回收的头
                        sRecycleBin = this; // update旧的头指向this，表示它自己现在是可回收的target（第一个）
                        sRecycledCount += 1; // 多了一个可回收的
                    } else {
                        next = null; // 没有next了
                    }
                    child = null; // 清空child字段
                }
            }
        }
```

**dispatchTouchEvent方法**

```java
     public boolean dispatchTouchEvent(MotionEvent ev) {
           ......
           //记录事件是否被处理
            boolean handled = false;
            if (onFilterTouchEventForSecurity(ev)) {//这里一般都成立
                final int action = ev.getAction();
                final int actionMasked = action & MotionEvent.ACTION_MASK;
    
                // 预处理刚开始的down事件
                if (actionMasked == MotionEvent.ACTION_DOWN) {
                   //一个新的系类事件开始，清除所有之前的状态和事的处理者
                    cancelAndClearTouchTargets(ev);
                    //清理所有状态，包括requestDisallowInterceptTouchEvent影响的标志位
                    resetTouchState();
                }
                //----第一段完
                
    
                //检查是否拦截事件，这里分两种情况：
                //1是对down事件的处理，判断是否要拦截事件,这是touchTarget是为null的，且requestDisallowInterceptTouchEvent是没有作用的
                //2是对其他事件的处理，这时已经不是down事件，但是可能已经找到处理事件的子view，在把接下来的事件传递给子view时，始终要判断自己是否需要拦截，这里requestDisallowInterceptTouchEvent是有用的
                final boolean intercepted;
                //如果是down事件，或者mFirstTouchTarget不为null。Down事件时mFirstTouchTarget肯定是为null的。
                if (actionMasked == MotionEvent.ACTION_DOWN
                        || mFirstTouchTarget != null) {
                        /*首先判断标志位disallowIntercept是否允许拦截事件，这个标志位可以通过requestDisallowInterceptTouchEvent改变，默认是false
                        */
                    final boolean disallowIntercept = (mGroupFlags & FLAG_DISALLOW_INTERCEPT) != 0;
                    //如果可以拦截事件
                    if (!disallowIntercept) {
                    //调用onInterceptTouchEvent方法
                        intercepted = onInterceptTouchEvent(ev);
                        ev.setAction(action); // restore action in case it was changed
                    } else {
                    //如果直接不允许拦截事件，就不拦截事件，可见标志位优先级大于拦截方法
                        intercepted = false;
                    }
                } else {
                  //如果不是down事件，而且没有找到可以处理事件的子view，以后直接自己处理事件
                    intercepted = true;
                }
    
                ......
                
                // 检查事件的取消
                final boolean canceled = resetCancelNextUpFlag(this)
                        || actionMasked == MotionEvent.ACTION_CANCEL;
    
                // 安卓3.0引入的拆分事件，-_-!
                final boolean split = (mGroupFlags & FLAG_SPLIT_MOTION_EVENTS) != 0;
                //记录事件处理者
                TouchTarget newTouchTarget = null;
                //是否开始分发事件到新的target
                boolean alreadyDispatchedToNewTouchTarget = false;
                //事件没有被取消，并且不拦截事件，那么找能处理事件的孩子
                if (!canceled && !intercepted) {
    
                    View childWithAccessibilityFocus = ev.isTargetAccessibilityFocus()
                            ? findChildWithAccessibilityFocus() : null;
    
                    if (actionMasked == MotionEvent.ACTION_DOWN//down事件
                            || (split && actionMasked == MotionEvent.ACTION_POINTER_DOWN)//接下来的down事件
                            || actionMasked == MotionEvent.ACTION_HOVER_MOVE) {
                            
                            //下面的逻辑到Dispatch to touch targets都在down事件内
                            
                        final int actionIndex = ev.getActionIndex(); //down事件始终是0
                        final int idBitsToAssign = split ? 1 << ev.getPointerId(actionIndex)
                                : TouchTarget.ALL_POINTER_IDS;
    
                        // Clean up earlier touch targets for this pointer id in case they
                        // have become out of sync.
                        removePointersFromTouchTargets(idBitsToAssign);
    
                        final int childrenCount = mChildrenCount;
                        //一般都成立
                        if (newTouchTarget == null && childrenCount != 0) {
                            final float x = ev.getX(actionIndex);
                            final float y = ev.getY(actionIndex);
                            // Find a child that can receive the event.
                            // Scan children from front to back.
                            final ArrayList<View> preorderedList = buildOrderedChildList();
                            final boolean customOrder = preorderedList == null
                                    && isChildrenDrawingOrderEnabled();
                            final View[] children = mChildren;
                            //从外到内找可以处理事件的子view
                            for (int i = childrenCount - 1; i >= 0; i--) {
                                final int childIndex = customOrder
                                        ? getChildDrawingOrder(childrenCount, i) : i;
                                final View child = (preorderedList == null)
                                        ? children[childIndex] : preorderedList.get(childIndex);
    
                                // safer given the timeframe.
                                if (childWithAccessibilityFocus != null) {
                                    if (childWithAccessibilityFocus != child) {
                                        continue;
                                    }
                                    childWithAccessibilityFocus = null;
                                    i = childrenCount - 1;
                                }
                                //如果不可以接收事件，跳出此次循环
                                if (!canViewReceivePointerEvents(child)
                                        || !isTransformedTouchPointInView(x, y, child, null)) {
                                    ev.setTargetAccessibilityFocus(false);
                                    continue;
                                }
                               //找到了一个可以接收事件的子view，这里是个检查newTouchTarget一般返回null
                                newTouchTarget = getTouchTarget(child);
                                if (newTouchTarget != null) {
                                    // Child is already receiving touch within its bounds.
                                    // Give it the new pointer in addition to the ones it is handling.
                                    newTouchTarget.pointerIdBits |= idBitsToAssign;
                                    break;
                                }
                            
                                resetCancelNextUpFlag(child);
                                //这里把事件交给可以接收事件的子view处理，如果子view在down事件中返回true，mFirstTouchTarget将会被赋值
                                if (dispatchTransformedTouchEvent(ev, false, child, idBitsToAssign)) {
                                    // Child wants to receive touch within its bounds.
                                    mLastTouchDownTime = ev.getDownTime();
                                    if (preorderedList != null) {
                                        
                                        for (int j = 0; j < childrenCount; j++) {
                                            if (children[childIndex] == mChildren[j]) {
                                                mLastTouchDownIndex = j;
                                                break;
                                            }
                                        }
                                    } else {
                                        mLastTouchDownIndex = childIndex;
                                    }
                                    mLastTouchDownX = ev.getX();
                                    mLastTouchDownY = ev.getY();
                                    //对mFirstTouchTarget将会被赋值
                                    newTouchTarget = addTouchTarget(child, idBitsToAssign);                    //标志已经分发事件到处理者。
                                    alreadyDispatchedToNewTouchTarget = true;
                                    break;
                                }
    
                                // The accessibility focus didn't handle the event, so clear
                                // the flag and do a normal dispatch to all children.
                                ev.setTargetAccessibilityFocus(false);
                            }
                            if (preorderedList != null) preorderedList.clear();
                        }
     // 将此事件交给child处理，有这种情况，一个手指按在了child1上，另一个手指按在了child2上，以此类推，这样TouchTarget的链就形成了
                        if (newTouchTarget == null && mFirstTouchTarget != null) {
                            // Did not find a child to receive the event.
                            // Assign the pointer to the least recently added target.
                            newTouchTarget = mFirstTouchTarget;
                            while (newTouchTarget.next != null) {
                                newTouchTarget = newTouchTarget.next;
                            }
                            newTouchTarget.pointerIdBits |= idBitsToAssign;
                        }
                    }
                }
    
        //第二段完 ，应该判断是否已经找到了事件处理者


                // Dispatch to touch targets.开始分发事件，这里是非down事件，不会走上面逻辑
                //这里有有子view可以处理事件
                if (mFirstTouchTarget == null) {
                //这里会自己处理事件
                    // No touch targets so treat this as an ordinary view.
                    handled = dispatchTransformedTouchEvent(ev, canceled, null,
                            TouchTarget.ALL_POINTER_IDS);
                } else {
                
                   //否则把事件交给子view处理 
                    TouchTarget predecessor = null;
                    TouchTarget target = mFirstTouchTarget;
                    while (target != null) {
                        final TouchTarget next = target.next;
                        if (alreadyDispatchedToNewTouchTarget && target == newTouchTarget) {
                            handled = true;
                        } else {
                        //这里还是会判断是否拦截事件
                            final boolean cancelChild = resetCancelNextUpFlag(target.child)
                                    || intercepted;
                            //继续分发，但是传入的参数cancelChild很重要
                            if (dispatchTransformedTouchEvent(ev, cancelChild,
                                    target.child, target.pointerIdBits)) {
                                handled = true;
                            }
                            //如果取消child的事件，事件交给处理链的下一个处理者，
                            //一般只有一个，这时候mFirstTouchTarget将=null
                            if (cancelChild) {
                                if (predecessor == null) {
                                    mFirstTouchTarget = next;
                                } else {
                                    predecessor.next = next;
                                }
                                target.recycle();
                                target = next;
                                continue;
                            }
                        }
                        predecessor = target;
                        target = next;
                    }
                }
    
        //第三段完
    
                //扫尾工作
                // Update list of touch targets for pointer up or cancel, if needed.
                if (canceled
                        || actionMasked == MotionEvent.ACTION_UP
                        || actionMasked == MotionEvent.ACTION_HOVER_MOVE) {
                        //手指抬起，重置状态，包括拦截标识
                    resetTouchState();
                } else if (split && actionMasked == MotionEvent.ACTION_POINTER_UP) {
                //另一个手指抬起，清除对应处理链
                    final int actionIndex = ev.getActionIndex();
                    final int idBitsToRemove = 1 << ev.getPointerId(actionIndex);
                    removePointersFromTouchTargets(idBitsToRemove);
                }
            }
    
            if (!handled && mInputEventConsistencyVerifier != null) {
                mInputEventConsistencyVerifier.onUnhandledEvent(ev, 1);
            }
            //返回结果
            return handled;
        }
```

再来看一下 **dispatchTransformedTouchEvent** ，分发事件的主要逻辑

```java
     private boolean dispatchTransformedTouchEvent(MotionEvent event, boolean cancel,
                View child, int desiredPointerIdBits) {
            final boolean handled;
            final int oldAction = event.getAction();
            //如果事件是cancel事件
            if (cancel || oldAction == MotionEvent.ACTION_CANCEL) {
                event.setAction(MotionEvent.ACTION_CANCEL);
                if (child == null) {
                   //子view是null，自己处理
                    handled = super.dispatchTouchEvent(event);
                } else {
                    //给子view一个cancel事件
                    handled = child.dispatchTouchEvent(event);
                }
                event.setAction(oldAction);
                return handled;
            }
    
            if (newPointerIdBits == 0) {
                return false;
            }
    
            final MotionEvent transformedEvent;//多点事件的处理
            if (newPointerIdBits == oldPointerIdBits) {
                if (child == null || child.hasIdentityMatrix()) {
                    if (child == null) {
                        handled = super.dispatchTouchEvent(event);
                    } else {
                        final float offsetX = mScrollX - child.mLeft;
                        final float offsetY = mScrollY - child.mTop;
                        event.offsetLocation(offsetX, offsetY);
                        handled = child.dispatchTouchEvent(event);
                        event.offsetLocation(-offsetX, -offsetY);
                    }
                    return handled;
                }
                transformedEvent = MotionEvent.obtain(event);
            } else {
                transformedEvent = event.split(newPointerIdBits);
            }
    
            // 一般从这里分发事件
            if (child == null) {//如果子view是null，自己处理
                handled = super.dispatchTouchEvent(transformedEvent);
            } else {//矫正位置后，交给ziview处理
                final float offsetX = mScrollX - child.mLeft;
                final float offsetY = mScrollY - child.mTop;
                transformedEvent.offsetLocation(offsetX, offsetY);
                if (! child.hasIdentityMatrix()) {
                    transformedEvent.transform(child.getInverseMatrix());
                }
                handled = child.dispatchTouchEvent(transformedEvent);
            }
    
            // Done.
            transformedEvent.recycle();
            //然会结果
            return handled;
        }
```

然后关于 **TouchTarget** 的一些操作了：

```java
       //清理拦截事件的标志位等
        private void resetTouchState() {
            clearTouchTargets();
            resetCancelNextUpFlag(this);
            mGroupFlags &= ~FLAG_DISALLOW_INTERCEPT;
        }
    
        /**
         * 清理取消接下来的事件的标志位
         */
        private static boolean resetCancelNextUpFlag(View view) {
            if ((view.mPrivateFlags & PFLAG_CANCEL_NEXT_UP_EVENT) != 0) {
                view.mPrivateFlags &= ~PFLAG_CANCEL_NEXT_UP_EVENT;
                return true;
            }
            return false;
        }
    
        /**
         *清理所有的事件处理者
         */
        private void clearTouchTargets() {
            TouchTarget target = mFirstTouchTarget;
            if (target != null) {
                do {
                    TouchTarget next = target.next;
                    target.recycle();
                    target = next;
                } while (target != null);
                mFirstTouchTarget = null;
            }
        }
    
        /**
         * 取消之前所有事件接收者的事件
         */
        private void cancelAndClearTouchTargets(MotionEvent event) {
            if (mFirstTouchTarget != null) {
                boolean syntheticEvent = false;
                if (event == null) {
                    final long now = SystemClock.uptimeMillis();
                    event = MotionEvent.obtain(now, now,
                            MotionEvent.ACTION_CANCEL, 0.0f, 0.0f, 0);
                    event.setSource(InputDevice.SOURCE_TOUCHSCREEN);
                    syntheticEvent = true;
                }
    
                for (TouchTarget target = mFirstTouchTarget; target != null; target = target.next) {
                    resetCancelNextUpFlag(target.child);
                    dispatchTransformedTouchEvent(event, true, target.child, target.pointerIdBits);
                }
                clearTouchTargets();
                if (syntheticEvent) {
                    event.recycle();
                }
            }
        }
    
        /**
         * 从事件处理链中获取一个处理者
         */
        private TouchTarget getTouchTarget(View child) {
            for (TouchTarget target = mFirstTouchTarget; target != null; target = target.next) {
                if (target.child == child) {
                    return target;
                }
            }
            return null;
        }
    
        /**
         * 把一个事件处理者添加到事件处理链中。
         */
        private TouchTarget addTouchTarget(View child, int pointerIdBits) {
            TouchTarget target = TouchTarget.obtain(child, pointerIdBits);
            target.next = mFirstTouchTarget;
            mFirstTouchTarget = target;
            return target;
        }
    
        // 从链表中删除某个特定的节点
        private void cancelTouchTarget(View view) {
            TouchTarget predecessor = null;
            TouchTarget target = mFirstTouchTarget;
            while (target != null) {
                final TouchTarget next = target.next;
                if (target.child == view) {
                    if (predecessor == null) {
                        mFirstTouchTarget = next;
                    } else {
                        predecessor.next = next;
                    }
                    target.recycle();
    
                    final long now = SystemClock.uptimeMillis();
                    MotionEvent event = MotionEvent.obtain(now, now,
                            MotionEvent.ACTION_CANCEL, 0.0f, 0.0f, 0);
                    event.setSource(InputDevice.SOURCE_TOUCHSCREEN);
                    view.dispatchTouchEvent(event);
                    event.recycle();
                    return;
                }
                predecessor = target;
                target = next;
            }
        }
```

然后的看一下 **requestDisallowInterceptTouchEvent** 方法

```java
      public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
     
            if (disallowIntercept == ((mGroupFlags & FLAG_DISALLOW_INTERCEPT) != 0)) {
                // We're already in this state, assume our ancestors are too
                return;
            }
     
            if (disallowIntercept) {
                mGroupFlags |= FLAG_DISALLOW_INTERCEPT;
            } else {
                mGroupFlags &= ~FLAG_DISALLOW_INTERCEPT;
            }
     
            // Pass it up to our parent
            if (mParent != null) {
                mParent.requestDisallowInterceptTouchEvent(disallowIntercept);
            }
        }
```

至此大概的流程我们分析完了，可以看到，虽然代码与2.3版本的相比是改变了不少，但是处理事件的主要流程并没有改变(怎么可能改变)，经过分析，我们可以得出很多的结论。
