# KeyEvent事件分发机制(API25)

## 相关方法

- `ViewRootImpl中的dispatchInputEvent方法`
- `Activity.dispatchKeyEvent方法`
- `ViewGroup.dispatchKeyEvent方法`
- `View.dispatchKeyEvent方法`


## ViewRootImpl中的dispatchInputEvent方法

WMS中接受到消息后，会调用ViewRootImpl中的dispatchInputEvent方法，最终会把KeyEvent转发到ViewHierarchy中去。调用的是View的dispatchKeyEvent()方法。

ViewRootImpl中的View是PhoneWindow中的DecorView。然后查看DecorView中的dispatchKeyEvent()方法，发现该方法先把KeyEvent传递给了其PhoneWindow中的Callback的dispatchKeyEvent()方法，其实一般这个Callback就是PhoneWindow对应的Activity。然后查看Activity的dispatchKeyEvent()方法。

```java
    public boolean dispatchKeyEvent(KeyEvent event) {
        onUserInteraction();
        // Let action bars open menus in response to the menu key prioritized over
        // the window handling it
        final int keyCode = event.getKeyCode();
        if (keyCode == KeyEvent.KEYCODE_MENU && mActionBar != null && mActionBar.onMenuKeyEvent(event)) {
            return true;
        } else if (event.isCtrlPressed() &&  event.getUnicodeChar(event.getMetaState() & ~KeyEvent.META_CTRL_MASK) == '<') {
            // Capture the Control-< and send focus to the ActionBar
            final int action = event.getAction();
            if (action == KeyEvent.ACTION_DOWN) {
                final ActionBar actionBar = getActionBar();
                if (actionBar != null && actionBar.isShowing() && actionBar.requestFocus()) {
                    mEatKeyUpEvent = true;
                    return true;
                }
            } else if (action == KeyEvent.ACTION_UP && mEatKeyUpEvent) {
                mEatKeyUpEvent = false;
                return true;
            }
        }
        //出了上面对mActionBar的处理外，其实调用的还是Window的superDispatchKeyEvent方法，又把KeyEvent传递会PhoneWindow中
        Window win = getWindow();
        if (win.superDispatchKeyEvent(event)) {
            return true;
        }
        View decor = mDecor;
        if (decor == null) decor = win.getDecorView();
        return event.dispatch(this, decor != null ? decor.getKeyDispatcherState() : null, this);
    }
```

发现最后KeyEvent会传递到ViewHierarchy中取


ViewGroup.dispatchKeyEvent方法

```java
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (mInputEventConsistencyVerifier != null) {
            mInputEventConsistencyVerifier.onKeyEvent(event, 1);
        }
        //拥有焦点的ViewGroup以及具有了明确边界的范围（PFLAG_FOCUSED在view的setFrame时，会设置为true）
        //就就父View处理
        if ((mPrivateFlags & (PFLAG_FOCUSED | PFLAG_HAS_BOUNDS)) == (PFLAG_FOCUSED | PFLAG_HAS_BOUNDS)) {
            if (super.dispatchKeyEvent(event)) {
                return true;
            }
        } 
        //mFocused表示在ViewGroup中具有焦点的子view，就让该子view处理。
        else if (mFocused != null && (mFocused.mPrivateFlags & PFLAG_HAS_BOUNDS) == PFLAG_HAS_BOUNDS) {
            if (mFocused.dispatchKeyEvent(event)) {
                return true;
            }
        }
        //否则不处理KeyEvent事件
        if (mInputEventConsistencyVerifier != null) {
            mInputEventConsistencyVerifier.onUnhandledEvent(event, 1);
        }
        return false;
    }
```
ViewGroup中的处理逻辑为：ViewGroup重写了View的dispatchKeyEvent，如果有带有焦点的子view时，分发按键消息到该子view中去。没有，直接由父view分发。



## View.dispatchKeyEvent方法

首先，这肯定是一个拥有焦点的View。

```java
     public boolean dispatchKeyEvent(KeyEvent event) {
        if (mInputEventConsistencyVerifier != null) {
            mInputEventConsistencyVerifier.onKeyEvent(event, 0);
        }

        // Give any attached key listener a first crack at the event.
        //noinspection SimplifiableIfStatement
        ListenerInfo li = mListenerInfo;
        //如果设置了mOnKeyListener，并且该View时enable的，优先把KeyEvent传递给该View的mOnKeyListener。
        //如果mOnKeyListener的onKey方法返回true，就结束该KeyEvent的传递
        if (li != null && li.mOnKeyListener != null && (mViewFlags & ENABLED_MASK) == ENABLED && li.mOnKeyListener.onKey(this, event.getKeyCode(), event)) {
            return true;
        }
        //否则调用event的dispatch方法
        if (event.dispatch(this, mAttachInfo != null? mAttachInfo.mKeyDispatchState : null, this)) {
            return true;
        }

        if (mInputEventConsistencyVerifier != null) {
            mInputEventConsistencyVerifier.onUnhandledEvent(event, 0);
        }
        //否则返回false。
        return false;
    }
```

## 流程总结

1. DecorView -> Activity
2. Activity先让actionbar优先处理keyEvent，然后通过window处理，处理不了，到window上的DecorView处理。
3. DecorView -> ViewHierarchy
4. ViewGroup的dispatchKeyEvent
5. View的的dispatchKeyEvent（具有焦点的View）
6. DecorView的onKeyDown

Activity的dispatchKeyEvent，是用于处理KeyEvent相关，子类可以重写拦截所有的key event消息在分发到window这一层去的时候。
