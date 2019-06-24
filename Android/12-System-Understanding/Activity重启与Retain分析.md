# Activity的重启与Fragment、Loader 的 Retain 分析

---
## 1 Activity的重启

一般系统销毁一个Activity是因为我们退出了当前的界面或者手动调用了finish方法(其实都是一个意思)，但是除此之外Activity也有一些其他的原因会被销毁，比如配置改变，内存不足等情况，与主动退出Activity不同的是，我们有机会在Activity被销毁时保存一些数据，以便重启Activity时可以恢复上一次的状态，如下面表格所示列举了Activity重启的原因和保存状态的一些行为：


| 情景  |  对话框 | Activity、View、Fragment  |Fragment(setRetain(true)|Loader|静态变量|
| ------------ | ------------ | ------------ |------------ | ------------ | ------------ |
| 配置改变  | 重置  | 保存/恢复  |不变| 不变|不变|
| Activity重启  |  重置 | 保存/恢复|  保存/恢复| 重置 | 不变
| 进程重启  |重置   |保存/恢复    |保存/恢复|重置 |重置|

- 配置改变很简单，比如说常见的屏幕方向改变，语言的改变等。
- Activity重启可能是因为在开发者选项中设置了不保留活动，或者是因为内存不足的情况，比如在A界面打开B界面，然后内存不足的情况下可能A会被暂时销毁，当退出B界面时，A节目就会被重建。
- 进程重启，内存不足的情况下我们的进程可能会被暂时销毁或者发生crash时进程重启。

一个好的应用在配置发生变化时需要尽可能的保持用户之前操作的状态，安卓系统也提供了一些用于保存状态的方法。比如 Activity、Fragment、View 都有对应的 onSaveInstance，onRestoreInstance 之类的方法，对于配置改变可以使用 retained Fragment 或者 Loader 来保证某些对象不被销毁。

想要运用好这些方法，最好是能理解其原理，所以下面针对 Fragment 和 Loader 展开学习，了解系统是如何帮助我们保存活动的运行状态的。

---
## 2 Activity的 recreate 方法与状态保存

### 2.1 Activity的销毁与状态保存

Activity有一个方法 recreate ，用于重启自身，其实因为配置改变而导致 Activity 重启也是调用的这个方法，调用此方法，我们来看一下这个方法：

```java
       public void recreate() {
            if (mParent != null) {
                throw new IllegalStateException("Can only be called on top-level activity");
            }
            if (Looper.myLooper() != mMainThread.getLooper()) {
                throw new IllegalStateException("Must be called from main thread");
            }
            mMainThread.requestRelaunchActivity(mToken, null, null, 0, false, null, null, false);
        }
```

直接是调用了ActivitThread的requestRelaunchActivity方法，重启Activity，截取了一些关键的代码：

```java
    if (target == null) {
        target = new ActivityClientRecord();
        target.token = token;
        target.pendingResults = pendingResults;
        target.pendingIntents = pendingNewIntents;
        if (!fromServer) {
            ActivityClientRecord existing = mActivities.get(token);
            if (existing != null) {
                target.startsNotResumed = existing.paused;
                target.overrideConfig = existing.overrideConfig;
            }
            target.onlyLocalRequest = true;
        }
        mRelaunchingActivities.add(target);
        sendMessage(H.RELAUNCH_ACTIVITY, target);
    }
```

这里通过Handler发送了一个消息RELAUNCH_ACTIVITY，继续跟踪：

```java
    case RELAUNCH_ACTIVITY: {
            Trace.traceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, "activityRestart");
            ActivityClientRecord r = (ActivityClientRecord)msg.obj;
            handleRelaunchActivity(r);
            Trace.traceEnd(Trace.TRACE_TAG_ACTIVITY_MANAGER);
    }
```
    
然后是 handleRelaunchActivity(ActivityClientRecord tmp) 方法，这个方法有点长，只看关键部分

```java
    处理配置的变化
    // If there was a pending configuration change, execute it first.
            if (changedConfig != null) {
                mCurDefaultDisplayDpi = changedConfig.densityDpi;
                updateDefaultDensity();
                handleConfigurationChanged(changedConfig, null);
            }
    //从mActivities获取记录这个Activity的信息
       ActivityClientRecord r = mActivities.get(tmp.token);
            if (DEBUG_CONFIGURATION) Slog.v(TAG, "Handling relaunch of " + r);
            if (r == null) {
                return;
            }
    
            r.activity.mConfigChangeFlags |= configChanges;
            r.onlyLocalRequest = tmp.onlyLocalRequest;
            Intent currentIntent = r.activity.mIntent;
    //这一句代码很关键，表示activity是因为配置改变而重启的
            r.activity.mChangingConfigurations = true;
    //首先先暂停Activity
            // Need to ensure state is saved.
            if (!r.paused) {
                performPauseActivity(r.token, false, r.isPreHoneycomb());
            }
    //调用Activity的OnSaveInstanceState方法，让其保存状态
            if (r.state == null && !r.stopped && !r.isPreHoneycomb()) {
                callCallActivityOnSaveInstanceState(r);
            }
    //然后是destoryActivity
       handleDestroyActivity(r.token, false, configChanges, true);
    //最后清空一些资源的引用，然后重启Activity
     r.activity = null;
            r.window = null;
            r.hideForNow = false;
            r.nextIdle = null;
            // Merge any pending results and pending intents; don't just replace them
            if (tmp.pendingResults != null) {
                if (r.pendingResults == null) {
                    r.pendingResults = tmp.pendingResults;
                } else {
                    r.pendingResults.addAll(tmp.pendingResults);
                }
            }
            if (tmp.pendingIntents != null) {
                if (r.pendingIntents == null) {
                    r.pendingIntents = tmp.pendingIntents;
                } else {
                    r.pendingIntents.addAll(tmp.pendingIntents);
                }
            }
            r.startsNotResumed = tmp.startsNotResumed;
            r.overrideConfig = tmp.overrideConfig;
    
            handleLaunchActivity(r, currentIntent);
```

这里的 `handleDestroyActivity(r.token, false, configChanges, true)` 方法很关键，最后一个参数为true，然后handleDestoryActivity直接调用了performDestoryActivity来看一下这个方法:

```java
      private ActivityClientRecord performDestroyActivity(IBinder token, boolean finishing,
                int configChanges, boolean getNonConfigInstance) {
    //getNonConfigInstance = true
    //根据Activity的token获取对应的ActivityClientRecord
    
            ActivityClientRecord r = mActivities.get(token);
             ......
                r.activity.mConfigChangeFlags |= configChanges;
              //这里保存了actiivty的retainNonConfigurationInstances，
                if (getNonConfigInstance) {
                    try {
                        r.lastNonConfigurationInstances
                                = r.activity.retainNonConfigurationInstances();
                    } catch (Exception e) {
                        if (!mInstrumentation.onException(r.activity, e)) {
                            throw new RuntimeException(
                                    "Unable to retain activity "
                                    + r.intent.getComponent().toShortString()
                                    + ": " + e.toString(), e);
                        }
                    }
                }
            .... 最后调用AMS销毁了Activity
            return r;
        }
```

这里很重要的一点是如果为 `getNonConfigInstance = true` ，会保存 actiivty 的 retainNonConfigurationInstances。

### 2.2 状态的恢复

然后再回过头来看 handleLaunchActivity 方法，`handleLaunchActivity(r, currentIntent);`方法中的 r 参数就是刚刚 performDestoryActivity 方法返回的参数，在 performDestoryActivity 中会创建 Activity 对象，调用 Actiity 的 attach 方法，把这个 r 与对应的lastNonConfigurationInstances 参数传入：

```java
       activity.attach(appContext, this, getInstrumentation(), r.token,
                            r.ident, app, r.intent, r.activityInfo, title, r.parent,
                            r.embeddedID, r.lastNonConfigurationInstances, config,
                            r.referrer, r.voiceInteractor);

    
                    if (customIntent != null) {
                        activity.mIntent = customIntent;
                    }
                    r.lastNonConfigurationInstances = null;
                    activity.mStartedActivity = false;
                    int theme = r.activityInfo.getThemeResource();
                    if (theme != 0) {
                        activity.setTheme(theme);
                    }
    
                    activity.mCalled = false;
                    if (r.isPersistable()) {
                        mInstrumentation.callActivityOnCreate(activity, r.state, r.persistentState);
                    } else {
                //调用Activity的onCreate方法，把r.state传入，这里就是保存的状态
                        mInstrumentation.callActivityOnCreate(activity, r.state);
                    }
```

在Activity的attach方法里会为把lastNonConfigurationInstances赋值给mLastNonConfigurationInstances。

接下来看Activity的onCreate方法：

```java
      protected void onCreate(@Nullable Bundle savedInstanceState) {
            if (DEBUG_LIFECYCLE) Slog.v(TAG, "onCreate " + this + ": " + savedInstanceState);
            if (mLastNonConfigurationInstances != null) {
               //这里与mLastNonConfigurationInstances.loaders有关 mFragments.restoreLoaderNonConfig(mLastNonConfigurationInstances.loaders);
            }
            if (mActivityInfo.parentActivityName != null) {
                if (mActionBar == null) {
                    mEnableDefaultActionBarUp = true;
                } else {
                    mActionBar.setDefaultDisplayHomeAsUpEnabled(true);
                }
            }
            if (savedInstanceState != null) {
                Parcelable p = savedInstanceState.getParcelable(FRAGMENTS_TAG);
    //这里与Fragment有关
                mFragments.restoreAllState(p, mLastNonConfigurationInstances != null
                        ? mLastNonConfigurationInstances.fragments : null);
            }
            mFragments.dispatchCreate();
            getApplication().dispatchActivityCreated(this, savedInstanceState);
            if (mVoiceInteractor != null) {
                mVoiceInteractor.attachActivity(this);
            }
            mCalled = true;
        }
```

上面代码有两个关键的地方：

- mFragments.restoreLoaderNonConfig(mLastNonConfigurationInstances.loaders);
- mFragments.restoreAllState(p, mLastNonConfigurationInstances != null ? mLastNonConfigurationInstances.fragments : null);


先来看一下 `mFragments.restoreLoaderNonConfig(mLastNonConfigurationInstances.loaders)`，最终调用的是 FragmentHostCallback 的restoreLoaderNonConfig 方法：

```java
        void restoreLoaderNonConfig(ArrayMap<String, LoaderManager> loaderManagers) {
            mAllLoaderManagers = loaderManagers;
        }
```

把loaderManagers赋值给了mAllLoaderManagers


然后是restoreAllState方法：

```java
    //在FragmentController中
    public void restoreAllState(Parcelable state, List<Fragment> nonConfigList) {
            mHost.mFragmentManager.restoreAllState(state, nonConfigList);
    }
```

调用的是FragmentManager的restoreAllState方法，这个方法非常长，反正就是用于恢复各种状态

```java
    void restoreAllState(Parcelable state, List<Fragment> nonConfig) {
            // If there is no saved state at all, then there can not be
            // any nonConfig fragments either, so that is that.
            if (state == null) return;
            FragmentManagerState fms = (FragmentManagerState)state;
            if (fms.mActive == null) return;
            
            // First re-attach any non-config instances we are retaining back
            // to their saved state, so we don't try to instantiate them again.
            if (nonConfig != null) {
                for (int i=0; i<nonConfig.size(); i++) {
                    Fragment f = nonConfig.get(i);
                    if (DEBUG) Log.v(TAG, "restoreAllState: re-attaching retained " + f);
                    FragmentState fs = fms.mActive[f.mIndex];
                    fs.mInstance = f;
                    f.mSavedViewState = null;
                    f.mBackStackNesting = 0;
                    f.mInLayout = false;
                    f.mAdded = false;
                    f.mTarget = null;
                    if (fs.mSavedFragmentState != null) {
                        fs.mSavedFragmentState.setClassLoader(mHost.getContext().getClassLoader());
                        f.mSavedViewState = fs.mSavedFragmentState.getSparseParcelableArray(
                                FragmentManagerImpl.VIEW_STATE_TAG);
                        f.mSavedFragmentState = fs.mSavedFragmentState;
                    }
                }
            }
             ......
```

而 nonConfig 就是存储的调用了 `setRetainInstance(ture)` 的fangmeng的集合。到这里我们可以大概的知道数据与状态是怎么恢复的了。


### 2.3 如何收集需要保存的对象

那么当Activity重启时，又是如何收集需要保持的状态的呢？从retainNonConfigurationInstances方法入手，各个在销毁Activity的时候，调用的就是这个方法来获取需要保持的对象的：

```java
        NonConfigurationInstances retainNonConfigurationInstances() {
            Object activity = onRetainNonConfigurationInstance();
            HashMap<String, Object> children = onRetainNonConfigurationChildInstances();
            List<Fragment> fragments = mFragments.retainNonConfig();
            ArrayMap<String, LoaderManager> loaders = mFragments.retainLoaderNonConfig();
            if (activity == null && children == null && fragments == null && loaders == null
                    && mVoiceInteractor == null) {
                return null;
            }
    
            NonConfigurationInstances nci = new NonConfigurationInstances();
            nci.activity = activity;
            nci.children = children;
            nci.fragments = fragments;
            nci.loaders = loaders;
            if (mVoiceInteractor != null) {
                mVoiceInteractor.retainInstance();
                nci.voiceInteractor = mVoiceInteractor;
            }
            return nci;
        }
```
    
retainNonConfig 用于保存 `setRetainInstance(true)` 的 Fragment

```java
    //在FragmentImpl中 
    ArrayList<Fragment> retainNonConfig() {
            ArrayList<Fragment> fragments = null;
            //mActive就是用来存放setRetainInstance(true)的fragment
            if (mActive != null) {
                for (int i=0; i<mActive.size(); i++) {
                    Fragment f = mActive.get(i);
                    if (f != null && f.mRetainInstance) {
                        if (fragments == null) {
                            fragments = new ArrayList<Fragment>();
                        }
                        fragments.add(f);
                        f.mRetaining = true;
                        f.mTargetIndex = f.mTarget != null ? f.mTarget.mIndex : -1;
                        if (DEBUG) Log.v(TAG, "retainNonConfig: keeping retained " + f);
                    }
                }
            }
            return fragments;
        }
```

retainLoaderNonConfig 用于保存所有的 LoaderManager:

```java
    //在FragmentHostCallback中
    ArrayMap<String, LoaderManager> retainLoaderNonConfig() {
            boolean retainLoaders = false;
            if (mAllLoaderManagers != null) {
                // prune out any loader managers that were already stopped and so
                // have nothing useful to retain.
                final int N = mAllLoaderManagers.size();
                LoaderManagerImpl loaders[] = new LoaderManagerImpl[N];
                for (int i=N-1; i>=0; i--) {
                    loaders[i] = (LoaderManagerImpl) mAllLoaderManagers.valueAt(i);
                }
                for (int i=0; i<N; i++) {
                    LoaderManagerImpl lm = loaders[i];
                    if (lm.mRetaining) {
                        retainLoaders = true;
                    } else {
                        lm.doDestroy();
                        mAllLoaderManagers.remove(lm.mWho);
                    }
                }
            }
    
            if (retainLoaders) {
                return mAllLoaderManagers;
            }
            return null;
        }
```


## 总结

刚刚分析了在调用Activity的recreate时的流程，简要的说明了loader或fragment是如何被保存的，这只是Activity重启的一种情况，还有其他情况没有分析，但是需要注意的是只有被relaunch的activity在destroy时才会在ActivityThread代码中被调用retainNonConfig去通知Activity返回需要保存实例，其他的destroy不会。而其他方式只能通过`Bundle savedInstanceState`保存一些状态，这时候状态是通过序列化保存的，而对象是不可能保留的。所以在开发中需要根据各种不同的情况对状态进行处理，而利用 loader 或者 `fragment(setRetainIntance(true)` 来保存对象其实是非常薄弱的，不能太过于依赖这个东西。




