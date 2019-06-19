# Loader 机制

---
## 1 Loader介绍

Loader是Android3.0提供的新的异步api，Loader可用于进行异步操作，其实它内部采用的也是类似AsycnTask的异步任务机制，那么既然有了AsyncTask为什么还要有Loader呢？因为AysncTask有一个缺点，当Activity或者Fragment因为为屏幕旋转等因素被销毁重建后，如果处理不当就会造成任务和数据丢失，每次重建都要重新启动一个AsyncTask去执行异步任务。而使用Loader却可以解决这样的问题，Loader是Android框架中提供的在手机状态改变时不会被销毁的工具。Loader的生命周期是是由系统控制的，只有在向Loader请求数据的Activity/Fragment被永久销毁时才会被清除，所以通过Loader可以很好的解决因配置改变让Activity/Fragment销毁重建导致的任务或数据丢失等问题。

下面对Loader进行学习。

---
## 2 Loader的使用

通过几个例子来学习Loader的使用是最后的方式，不过需要先了解一下 Loader 相关的几个类。

- **Loader**：抽象出来的异步数据加载类
- **AsyncTaskLoader**：Loader的直接子类，完成了Loader的大部分逻辑，继承AsyncTaskLoader可以轻易实现异步数据加载
- **CursorLoader**：是AsyncTaskLoader的子类。它可以查询ContentResolver然后返回一个Cursor，同时CursorLoader内包含ContentObserver对象来监听Cursor数据的变化。
    - LoaderManager Loader管理器的抽象
    - LoaderManagerImpl LoaderManager的实现者
    - LoaderManager.LoaderCallbacks 提供LoaderManager与Fragment或Activity交互的回调，LoaderCallbacks有以下三个方法：
        1.  onCreateLoader()：根据所给出的ID，初始化并返回一个新的加载器。
        2.  onLoadFinished()：当一个先前被创建的加载器完成了它的加载过程时被调用。
        3.  onLoaderReset()：当一个先前被创建的加载器被重置或者Activity或Fragment完全销毁时被调用，然后使加载器的数据无效。
 

---
### 2.1 示例，通过Loader获取系统联系人列表

例子很简单，其实是 ApiDemo 中的一个例子，主要的代码都在这里：

```java

    //创建一个适配器，由于使用的Cursor，所以这里使用ListView与SimpleCursorAdapter
      mAdapter = new SimpleCursorAdapter(this,
                    android.R.layout.simple_list_item_2, null,
                    new String[]{ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.CONTACT_STATUS},
                    new int[]{android.R.id.text1, android.R.id.text2}, 0);
    
     //然后再onCreate方法中通过Activity的getLoaderManager创建一个Loader，这里的this实现了LoaderCallbacks
    
     getLoaderManager().initLoader(1, null, this);
    
            
        // 联系人查询项
        static final String[] CONTACTS_SUMMARY_PROJECTION = new String[]{
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.CONTACT_STATUS,
                ContactsContract.Contacts.CONTACT_PRESENCE,
                ContactsContract.Contacts.PHOTO_ID,
                ContactsContract.Contacts.LOOKUP_KEY,
        };
    
         //实现的回调
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            Log.d(TAG, "onCreateLoader() called with: " + "id = [" + id + "], args = [" + args + "]");
    
            Uri baseUri  = ContactsContract.Contacts.CONTENT_URI;
         
            //查询的条件语句，即sql
            String select = "((" + ContactsContract.Contacts.DISPLAY_NAME + " NOTNULL) AND ("
                    + ContactsContract.Contacts.HAS_PHONE_NUMBER + "=1) AND ("
                    + ContactsContract.Contacts.DISPLAY_NAME + " != '' ))";
    
            //返回一个Loader
            return new CursorLoader(
                    LoaderContactActivity.this,
                    baseUri,
                    CONTACTS_SUMMARY_PROJECTION,
                    select,
                    null,
                    ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC");
    
        }
    
        //当CursorLoader查询完数据之后
        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
               mAdapter.swapCursor(data);
        }
    
         //当调用getLoaderManager().restartLoader(0, null, this);或者Activity/Fragmen完全销毁，我们应该在这里清除我们显示的数据。
        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            Log.d(TAG, "onLoaderReset() called with: " + "loader = [" + loader + "]");
            mAdapter.swapCursor(null);
        }

```

说明：

- id是Loader的唯一表示， 我们应对根据这个唯一的标识创建自己的Loader。
- Buldle是创建Loader的参数，没有参数传null即可
- onCreateLoader方法接受initLoader时传递的参数，我们需要在这里创建我们的Loader。
- onLoadFinished表示数据已经加载完毕
- onLoaderReset表示数据被重置，或者Activity/Fragment完全销毁

是不是感觉 Loader 的使用还是很简单的，现在来测试一下屏幕旋转对 Loader 的影响。

在第一次进入屏幕时打印一个getLoaderManager和LoaderCallbacks的三个方法：

```
     D/LoaderContactActivity: onCreateLoader() called with: id = [1], args = [null]
    
     D/LoaderContactActivity: getLoaderManager():LoaderManager{b33365 in HostCallbacks{b13d83a}}
    
     D/LoaderContactActivity: onLoadFinished() called with: loader = [CursorLoader{8573e06 id=1}], data = [android.content.ContentResolver$CursorWrapperInner@af6cc7]
```

然后旋转屏幕：

```
     getLoaderManager():LoaderManager{b33365 in HostCallbacks{a0715b7}}
     onLoadFinished() called with: loader = [CursorLoader{8573e06 id=1}], data = [android.content.ContentResolver$CursorWrapperInner@af6cc7]
```

可以看到虽然LoaderManager重建创建了，但是onCreateLoader并没有再次调用，而是直接调用了onLoadFinished，并且返回了内存地址一样的CursorLoader和数据源Cursor，当我们主动退出Activity后，onLoaderReset()才会被调用。现在可以感觉到Loader的强大之处了，而且CursorLoader可以监听数据源的变化，比如这里的联系人列表，当有联系人信息被修改时，Cursor会自定监听并同步到列表。

---
## 3 Loader的部分源码解析

要了解 loader 是怎么运作的，只能从其源码了解，所以接下来对Loader相关源码进行分析：

## 3.1 LoaderManager的创建

获取LoaderManager的代码在Activity中：

```java
        FragmentController mFragments = FragmentController.createController(new HostCallbacks());
        public LoaderManager getLoaderManager() {
            return mFragments.getLoaderManager();
        }
```

可以看到通过mFragments获取到了LoaderManager，而mFragments是由FragmentController创建的，其实mFragments最终是调用的它内部的mHost的getLoaderManager()方法，也就是这里传入的HostCallbacks。而HostCallbacks的继承链如下：

```
FragmentContainer：定义Fragment的容器的的抽象
       |
FragmentHostCallback：定义了Fragment所依附对象(Activity)的，作为宿主的Activity需要实现FragmentHostCallback相关方法
       |
HostCallbacks
```
而主要的逻辑实现都在FragmentHostCallback里面，可以看一下：

```java
    public abstract class FragmentHostCallback<E> extends FragmentContainer {
        private final Activity mActivity;
        final Context mContext;
        private final Handler mHandler;
        final int mWindowAnimations;
        final FragmentManagerImpl mFragmentManager = new FragmentManagerImpl();
        private ArrayMap<String, LoaderManager> mAllLoaderManagers;
        private LoaderManagerImpl mLoaderManager;
        private boolean mCheckedForLoaderManager;
        private boolean mLoadersStarted;
    .......}
```

包括fragmentManager，LoaderManager都在FragmentHostCallback被创建。而LoaderManager的实现者就是LoaderManagerImpl了，看一下mLoaderManager是怎么创建的

```java
    LoaderManagerImpl getLoaderManagerImpl() {
            if (mLoaderManager != null) {
                return mLoaderManager;
            }
            mCheckedForLoaderManager = true;
            mLoaderManager = getLoaderManager("(root)", mLoadersStarted, true /*create*/);
            return mLoaderManager;
        }
        
        LoaderManagerImpl getLoaderManager(String who, boolean started, boolean create) {
            if (mAllLoaderManagers == null) {
                mAllLoaderManagers = new ArrayMap<String, LoaderManager>();
            }
            LoaderManagerImpl lm = (LoaderManagerImpl) mAllLoaderManagers.get(who);
            if (lm == null) {
                if (create) {
                    lm = new LoaderManagerImpl(who, this, started);
                    mAllLoaderManagers.put(who, lm);
                }
            } else {
                lm.updateHostController(this);
            }
            return lm;
        }
```

这里有一个容器ArrayMap用于存储LoaderManager，而从FragmentHostCallback获取LoaderManager是传入的"(root)"，如果容器中没有根据"(root)"找到才会创建。


### 3.2 LoaderManger的运行

```java
        LoaderManagerImpl(String who, FragmentHostCallback host, boolean started) {
            mWho = who;
            mHost = host;
            mStarted = started;
        }
```

LoaderManagerImpl的创建需要三层参数，who是LoaderManagerImpl的标识，可能属于Activity也可能是Fragment的，mStarted表示是否已经开始，当Activity执行onStart方法时，mStarted被置为true

```java
        @CallSuper
        protected void onStart() {
            mCalled = true;
            mFragments.doLoaderStart();
            getApplication().dispatchActivityStarted(this);
        }
```

而doLoaderStart会执行下面逻辑：

```java
        void doLoaderStart() {
            if (mLoadersStarted) {
                return;
            }
            mLoadersStarted = true;
    
            if (mLoaderManager != null) {
                mLoaderManager.doStart();
            } else if (!mCheckedForLoaderManager) {
                mLoaderManager = getLoaderManager("(root)", mLoadersStarted, false);
            }
            mCheckedForLoaderManager = true;
        }
```

这里又调用了LoaderManagerImpl的doStart方法

```java
        final SparseArray<LoaderInfo> mLoaders = new SparseArray<LoaderInfo>(0);

        void doStart() {
            if (DEBUG) Log.v(TAG, "Starting in " + this);
            if (mStarted) {
                RuntimeException e = new RuntimeException("here");
                e.fillInStackTrace();
                Log.w(TAG, "Called doStart when already started: " + this, e);
                return;
            }
            
            mStarted = true;
    
            // 在这里调用所有Loader的start方法，
            // Let the existing loaders know that we want to be notified when a load is complete
            for (int i = mLoaders.size()-1; i >= 0; i--) {
                mLoaders.valueAt(i).start();
            }
        }
```

而mLoaders就是一个存储LoaderInfo的SparseArray，LoaderInfo用于描述一个Loader的所有信息：

```java
           final class LoaderInfo implements Loader.OnLoadCompleteListener<Object>,
                Loader.OnLoadCanceledListener<Object> {
            final int mId;//唯一id
            final Bundle mArgs;//参数
            LoaderManager.LoaderCallbacks<Object> mCallbacks;//回调
            Loader<Object> mLoader;//加载器
            boolean mHaveData;
            boolean mDeliveredData;//是否发送了数据
            Object mData;//数据
            boolean mStarted;//是否已经开始
            boolean mRetaining;//正在保留对象
            boolean mRetainingStarted;//
            boolean mReportNextStart;
            boolean mDestroyed;//被销毁了
            boolean mListenerRegistered;
    
            LoaderInfo mPendingLoader;
            
            public LoaderInfo(int id, Bundle args, LoaderManager.LoaderCallbacks<Object> callbacks) {
                mId = id;
                mArgs = args;
                mCallbacks = callbacks;
            }
            //......
        }
```

首先看到构造方法就很熟悉，就是我们在initLoader是传入的参数，其次LoaderInfo的诸多变量都是用来记录Loader的状态的。看一下Loader的start方法：

```java
    void start() {
        if (mRetaining && mRetainingStarted) {
            // 如果是正在执行保存状态，直接mStarted置为true
            mStarted = true;
            return;
        }
        //如果正在执行保存状态，没有必要执行，直接return
        if (mStarted) {
            // If loader already started, don't restart.
            return;
        }

        mStarted = true;
        
        if (mLoader == null && mCallbacks != null) {
            mLoader = mCallbacks.onCreateLoader(mId, mArgs);//这里调用了我们实现的回调，用于创建一个Loader
        }
        if (mLoader != null) {//这里需要Loader不是静态的内部类哦！
            if (mLoader.getClass().isMemberClass()
                    && !Modifier.isStatic(mLoader.getClass().getModifiers())) {
                throw new IllegalArgumentException(
                        "Object returned from onCreateLoader must not be a non-static inner member class: "
                        + mLoader);
            }
            if (!mListenerRegistered) {//注册监听
                mLoader.registerListener(mId, this);
                mLoader.registerOnLoadCanceledListener(this);
                mListenerRegistered = true;
            }
            mLoader.startLoading();//调用Loader的startLoading方法
        }
    }
```

mRetaining、mRetainingStarted是当Activity因配置改变而recreate时才会被为true，稍后会分析，这里先看 mLoader.startLoading()方法

```java
       //startLoading即开始加载数据了
       public final void startLoading() {
            mStarted = true;
            mReset = false;
            mAbandoned = false;
            onStartLoading();
        }
      
        /**
         *onStartLoading是一个空方法，子类必须实现这个方法来加载数据
         */
        protected void onStartLoading() {
        }
```

我们可以参考一下Cursor的实现方式：

```java
        protected void onStartLoading() {
            //已经加载到数据则提供数据
            if (mCursor != null) {
                deliverResult(mCursor);
            }
            //否则如果数据改变了或者没有加载到数据则调用forceLoad。
            if (takeContentChanged() || mCursor == null) {
                forceLoad();
            }
        }
```

forceLoad会调用onForceLoad方法,而AsyncTaskLoader已经实现了这个方法：

```java
      //这里真正开始创建LoadTask，来执行异步任务。
       @Override
        protected void onForceLoad() {
            super.onForceLoad();
            cancelLoad();
            mTask = new LoadTask();
            if (DEBUG) Log.v(TAG, "Preparing load: mTask=" + mTask);
            executePendingTask();
        }
```

LoadTask继承自已经叫ModernAsyncTask的类，他的大部分逻辑都来自AsycnTask，为什么不直接用AsyncTask呢？ 以为AsycnTask的行为在各个版本的表现不一致。然后executePendingTask会导致异步任务的执行，调用到这个方法：

```java
      protected D doInBackground(Void... params) {
                if (DEBUG) Log.v(TAG, this + " >>> doInBackground");
                try {
                    D data = AsyncTaskLoader.this.onLoadInBackground();
                    return data;
                } catch (OperationCanceledException ex) {
                    if (!isCancelled()) {
                        throw ex;
                    }
                    return null;
                }
            }
```

onLoadInBackground方法会调用LoadInBackground方法，也就是AsycnTaskLoader的子类需要执行的方法。当加载完数据后，LoadTask的onPostExecute方法就会被调用，然后通知AsyncTaskLoader完成数据交付：

```java
       void dispatchOnLoadComplete(LoadTask task, D data) {
            if (mTask != task) {//如果任务已经改变，就不再交付数据了
                if (DEBUG) Log.v(TAG, "Load complete of old task, trying to cancel");
                dispatchOnCancelled(task, data);
            } else {
                if (isAbandoned()) {
                    // This cursor has been abandoned; just cancel the new data.
                    onCanceled(data);
                } else {
                    commitContentChanged();
                    mLastLoadCompleteTime = SystemClock.uptimeMillis();
                    mTask = null;
                    if (DEBUG) Log.v(TAG, "Delivering result");
                    deliverResult(data);
                }
            }
        }

         public void deliverResult(D data) {
            if (mListener != null) {
                mListener.onLoadComplete(this, data);
            }
        }
```

而mListener是由LoaderInfo实现的，最终会调用callOnLoadFinisd方法，把加载到的数据传递给调用者：

```java
     void callOnLoadFinished(Loader<Object> loader, Object data) {
                if (mCallbacks != null) {
                    String lastBecause = null;
                    if (mHost != null) {
                        lastBecause = mHost.mFragmentManager.mNoTransactionsBecause;
                        mHost.mFragmentManager.mNoTransactionsBecause = "onLoadFinished";
                    }
                    try {
                        if (DEBUG) Log.v(TAG, "  onLoadFinished in " + loader + ": "
                                + loader.dataToString(data));
                        mCallbacks.onLoadFinished(loader, data);
                    } finally {
                        if (mHost != null) {
                            mHost.mFragmentManager.mNoTransactionsBecause = lastBecause;
                        }
                    }
                    mDeliveredData = true;
                }
            }
```

大概的流程就是这样了，还有一些地方需要补充的

- 调用LoadManager的restartLoader会导致Loader重新加载数据
- 调用Loader的onContentChanged也会导致数据的重新加载
- CursorLoader可以监听数据的改变是因为内部使用了ContentObserver，继承AsyncTaskLoader 的话，我们需要自己实现内容的监听


LoaderManager的生命周期与Activity的生命周期绑定其流程为：Activity-->HostCallbacks-->FragmentHostCallbacks，不仅LoaderManager，Fragment的生命周期都是通过FragmentHostCallbacks进行分发的。

### 3.3 实现一个自定义的Loader

刚刚分析了Loader机制的部分源码，现在来实现一个自己的Loader，这个Loader用于查询系统所以安装了的App，以列表的形式展示。

实现自定义的Loader需要实现的一些方法：

- loadInBackground用于执行异步任务
- onStartLoading 这里判断如果已经加载过数据则提交数据，如果数据为null或者内容已经改变了，需要调用forceLoad
- deliverResult 这个方法用于提交数据，但是在提交数据前我们还需要检查Loader是否已经reset了，如果是重置了则释放已经加载到的数据(比如关闭cursor)，如果是started的状态，则真的提交数据
- onStopLoading 在onStopLoading调用cancelLoad
- onCanceled 在这里释放资源
- onRest 在这里释放资源和做一些收尾工作，比如取消关闭的注册，取消数据的监听等


下面是一个AppListLoader的实现：

```java
    public class AppLoader extends AsyncTaskLoader<List<AppEntity>> {
        private static final String TAG = AppLoader.class.getSimpleName();
        private PackageManager mPackageManager;
        private final AppComparator mAppComparator = new AppComparator();
        private List<AppEntity> mApps;
        private PackageIntentReceiver mPackageReceiver;
    
        public AppLoader(Context context) {
            super(context);
            mPackageManager = context.getPackageManager();
        }
    
        @Override
        public List<AppEntity> loadInBackground() {
    
            return loadAppList();
        }
    
        private List<AppEntity> loadAppList() {
            List<ApplicationInfo> installedApplications = mPackageManager.getInstalledApplications(
                    PackageManager.GET_UNINSTALLED_PACKAGES);
            if (Checker.isEmpty(installedApplications)) {
                installedApplications = Collections.emptyList();
            }
    
            List<AppEntity> appEntities = new ArrayList<>();
            AppEntity entry;
            for (ApplicationInfo app : installedApplications) {
                entry = new AppEntity(app);
                entry.initInfos(getContext(), mPackageManager);
                appEntities.add(entry);
            }
            Collections.sort(appEntities, mAppComparator);
            return appEntities;
    
        }
    
    
        private class AppComparator implements Comparator<AppEntity> {
            @Override
            public int compare(AppEntity lhs, AppEntity rhs) {
                if (lhs == rhs) {
                    return 0;
                }
                return lhs.getAppName().compareTo(rhs.getAppName());
            }
        }
    
    
        @Override
        public void deliverResult(List<AppEntity> data) {
            if (isReset()) {
                if (!Checker.isEmpty(mApps)) {
                    onReleaseResources(data);
                }
            }
            List<AppEntity> oldData = data;
            mApps = data;
            if (isStarted()) {
                super.deliverResult(mApps);
            }
            if (!Checker.isEmpty(oldData)) {
                onReleaseResources(oldData);
            }
        }
    
        protected void onReleaseResources(List<AppEntity> apps) {
            // For a simple List<> there is nothing to do.  For something
            // like a Cursor, we would close it here.
        }
    
        @Override
        protected void onStartLoading() {
            if (!Checker.isEmpty(mApps)) {
                deliverResult(mApps);
            }
    
            if (mPackageReceiver == null)
                mPackageReceiver = new PackageIntentReceiver(this);
    
    
            if (takeContentChanged() || Checker.isEmpty(mApps)) {
                forceLoad();
            }
        }
    
        /**
         * Handles a request to stop the Loader.
         */
        @Override
        protected void onStopLoading() {
            // Attempt to cancel the current load task if possible.
            cancelLoad();
        }
    
        /**
         * Handles a request to cancel a load.
         */
        @Override
        public void onCanceled(List<AppEntity> apps) {
            super.onCanceled(apps);
    
            // At this point we can release the resources associated with 'apps'
            // if needed.
            onReleaseResources(apps);
        }
    
    
        @Override
        protected void onReset() {
            stopLoading();
    
            if (mPackageReceiver != null) {
                getContext().unregisterReceiver(mPackageReceiver);
                mPackageReceiver = null;
            }
    
            if (!Checker.isEmpty(mApps)) {
                onReleaseResources(mApps);
                mApps = null;
            }
        }
    }
```

用于实现App卸载安装的广播接收者：

```java
    public class PackageIntentReceiver extends BroadcastReceiver {
        final AppLoader mLoader;
        private static final String TAG = PackageIntentReceiver.class.getSimpleName();
    
        public PackageIntentReceiver(AppLoader loader) {
            mLoader = loader;
            IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
            filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
            filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
            filter.addDataScheme("package");
            mLoader.getContext().registerReceiver(this, filter);
            // Register for events related to sdcard installation.
            IntentFilter sdFilter = new IntentFilter();
            sdFilter.addAction(IntentCompat.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE);
            sdFilter.addAction(IntentCompat.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE);
            Log.d(TAG, "PackageIntentReceiver() called with: " + "loader = [" + loader + "]");
            mLoader.getContext().registerReceiver(this, sdFilter);
        }
    
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive() called with: " + "context = [" + context + "], intent = [" + intent + "]");
            // 这里很重要哦，调用mLoader告知数据源改变了
            mLoader.onContentChanged();
        }
    }
```
