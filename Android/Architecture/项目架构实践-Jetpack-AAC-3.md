# [android-architecture-components](https://github.com/googlesamples/android-architecture-components) 分析

---
## 1 BasicSample

演示如何使用SQLite数据库和Room保存数据。

### 1.1 Room

- Database注解 将一个类标记为RoomDatabase
- Dao注解 用于生成Dao对象
- Entity注解 表示数据库中的一张表
- TypeConverter注解 用于添加类型转功能
- DataBase的查询默认在ArchTaskExecutor提供的执行中执行，可以通过ArchTaskExecutor的setDelegate来设置其他的执行器。

### 1.2 Repository层如何返回数据

```java
public class DataRepository {

    private static DataRepository sInstance;

    private final AppDatabase mDatabase;
    private MediatorLiveData<List<ProductEntity>> mObservableProducts;

    private DataRepository(final AppDatabase database) {
        mDatabase = database;

           //1 中间人LiveData用于连接另外两个LiveData
        mObservableProducts = new MediatorLiveData<>();

         //2 MediatorLiveData可以观察一个LiveData并对齐变化做出反应
         //初始化Repository时直接异步加载数据库中的数据，使用MediatorLiveData连接
        mObservableProducts.addSource(mDatabase.productDao().loadAllProducts(),
                productEntities -> {
                    if (mDatabase.getDatabaseCreated().getValue() != null) {
                        mObservableProducts.postValue(productEntities);
                    }
                });
    }

    public static DataRepository getInstance(final AppDatabase database) {
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    sInstance = new DataRepository(database);
                }
            }
        }
        return sInstance;
    }

    public LiveData<List<ProductEntity>> getProducts() {
        return mObservableProducts;
    }

    public LiveData<ProductEntity> loadProduct(final int productId) {
        return mDatabase.productDao().loadProduct(productId);
    }

    public LiveData<List<CommentEntity>> loadComments(final int productId) {
        return mDatabase.commentDao().loadComments(productId);
    }
}
```

MediatorLiveData是LiveData子类，它可以观察其他LiveData对象并对它们的OnChanged事件做出反应，使用MediatorLiveData连接其他数据源，从而Repository可以直接同步返回可观察的LiveData，避免复杂的异步回调。

### 1.3 ViewModel如何连接UI和Repository

```java
public class ProductViewModel extends AndroidViewModel {

    //1 声明类型为：LiveData
    private final LiveData<ProductEntity> mObservableProduct;

    //2 直接初始化
    public ProductViewModel(@NonNull Application application, DataRepository repository,
            final int productId) {
        super(application);
        mProductId = productId;

        mObservableProduct = repository.loadProduct(mProductId);
    }

    //3 直接返回给UI
    public LiveData<ProductEntity> getObservableProduct() {
        return mObservableProduct;
    }

}
```

### 1.4 总结

- 数据是一次性从数据库加载的
- 列表没有任何交互

---
## 2 BasicRxJavaSample

演示Room如何配合RxJava 2来使用

### 2.1 Room

Room中定义的Dao可以直接返回Flowable，当然这需要Room的RxJava2扩展支持。

```
@Dao
public interface UserDao {
    //Insert操作在调用线程执行。
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);

    @Query("SELECT * FROM Users LIMIT 1")
    Flowable<User> getUser();
}
```

### 2.2 View中使用Completable类封装一次用户数据的更新

```java
//ViewModel
    public Completable updateUserName(final String userName) {
        return Completable.fromAction(() -> {

           //更新还是插入
            mUser = mUser == null
                    ? new User(userName)
                    : new User(mUser.getId(), userName);

            mDataSource.insertOrUpdateUser(mUser);
        });
    }

//Activity
mViewModel.updateUserName(userName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> mUpdateButton.setEnabled(true),
                        throwable -> Log.e(TAG, "Unable to update username", throwable))
```

ViewModel中通过Completable的fromAction优雅的封装了一个插入操作，然后返回给UI订阅。


---
## 3 PagingSample(Kotlin)-从数据库加载数据

演示如何使用Paging library 和 Room来加载数据。

###  3.1 使用Room 支持 PagingLibrary

使用Room，Dao中查询数据API的返回值可以定义为 `DataSource.Factory` 类型。Room会自动根据声明自动创建 DataSource.Factory 实例。

```java
    /**
     Room知道如何创建一个DataSource的Factory，从LivePagedListProvider可以得到一个LiveData并通过ViewModel将它提供给UI。

     这里的DataSource.Factory创建的是PositionalDataSource，而使用Int是因为它是PositionalDataSource的Key类型，Room使用LIMIT / OFFSET SQL关键字来通过PositionalDataSource来分页大型查询。
     */
    @Query("SELECT * FROM Cheese ORDER BY name COLLATE NOCASE ASC")
    fun allCheesesByName(): DataSource.Factory<Int, Cheese>
```

相关类

- `DataSource`：表示一个数据源
- `DataSource.Factory<Int, Cheese>`：用于创建数据源
- `PagedListAdapter`：RecyclerView的适配器，用于展示PagedList数据
- `LivePagedListBuilder`：用于创建一个PagedList的容器：LiveData

### 3.2  DataSource

DataSource用于将快照数据(一个列表数据的快照，里面可能有空的占位数据)页面加载到PagedList中的基类，PagedList中可以随着加载更多数据而增长，但加载的PagedList是无法更新的(即不能插入或删除新的条目)，如果底层数据集被修改，则必须创建一个新的 `PagedList / DataSource` 对来表示新数据。PagedList可以响应加载提示并从其绑定的DataSource中查询数据。当RecyclerView滚动时，`PagedListAdapter`会自动调用PagedList的 `loadAround（int)`来加载更多的内容。

### 3.3 显示PagedList数据

#### 数据库dao层返回`DataSource.Factory`

```
    @Query("SELECT * FROM Cheese ORDER BY name COLLATE NOCASE ASC")
    fun allCheesesByName(): DataSource.Factory<Int, Cheese>
```

#### ViewModel创建`LiveData<PagedList>`

ViewModel中通过Dao返回的`DataSource.Factory`创建一个PagedList容器：`LiveData<PagedList>`

```java
class CheeseViewModel(app: Application) : AndroidViewModel(app) {
    val dao = CheeseDb.get(app).cheeseDao()

    companion object {
        private const val PAGE_SIZE = 30
        private const val ENABLE_PLACEHOLDERS = true
    }

    val allCheeses = LivePagedListBuilder(dao.allCheesesByName(), PagedList.Config.Builder()
                    .setPageSize(PAGE_SIZE)
                    .setEnablePlaceholders(ENABLE_PLACEHOLDERS)
                    .build()).build()

    fun insert(text: CharSequence) = ioThread {
        dao.insert(Cheese(id = 0, name = text.toString()))
    }

    fun remove(cheese: Cheese) = ioThread {
        dao.delete(cheese)
    }
}
```

- PAGE_SIZE：一次加载多少条数据，在设置PageSize时，最好选择让Item至少填充一个屏幕
- ENABLE_PLACEHOLDERS：是否使用占位符，如果启用了占位符，分页列表将返回完整大小，但是有些Item会使用Null值占位符， 如果占位符被禁用，不适用nul值占位，而是按需加载，但是自动会加载更多的页面数据，当新的page数据被加载，滚动条将会抖动，所以如果禁用了占位符，应该禁用滚动条。

假设PAGE_SIZE=30，下面分别是使用占位符和不适用占位符的日志：

```
//ENABLE_PLACEHOLDERS = false
 size = 90 nonullSize = 90 nullSize = 0
onInserted position = 90, count = 30
onInserted position = 120, count = 30
onInserted position = 150, count = 30
onInserted position = 180, count = 30
onInserted position = 210, count = 30
onInserted position = 240, count = 30
onInserted position = 270, count = 30
onInserted position = 300, count = 30

//ENABLE_PLACEHOLDERS = true
size = 657  nonullSize = 90 nullSize = 567
 onChanged position = 90, count = 30
 onChanged position = 120, count = 30
 onChanged position = 150, count = 30
 onChanged position = 180, count = 30
 onChanged position = 210, count = 30
 onChanged position = 240, count = 30
 onChanged position = 270, count = 30
 onChanged position = 300, count = 30
```

从日志就可以看出使用使用占位符和不适用占位符的区别。

#### UI层示PagedList数据

UI层使用PagedListAdapter展示PagedList数据

```java
//适配器
class CheeseAdapter : PagedListAdapter<Cheese, CheeseViewHolder>(diffCallback) {

    override fun onBindViewHolder(holder: CheeseViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheeseViewHolder =
            CheeseViewHolder(parent)

    companion object {

        private val diffCallback = object : DiffUtil.ItemCallback<Cheese>() {
            override fun areItemsTheSame(oldItem: Cheese, newItem: Cheese): Boolean =
                    oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Cheese, newItem: Cheese): Boolean =
                    oldItem == newItem
        }
    }
}

//观察LiveData<PagedList>，然后提交PagedList。当数据库更新时，将会创建新的DataSource和PagedList，并通过LiveData触发UI的更新
viewModel.allCheeses.observe(this, Observer {
      printPageInfo(it)
      adapter.submitList(it)
})
```

### 3.4 总结

- 从数据库进行分页加载使用 PositionalDataSource
- PagedList是不可变的，底层数据的更像、添加、删除都将导致PagedList从新创建


---
## 4 PagingNetworkSample

PagingNetworkSample演示了不同情景的列表数据加载方式：

- DB+Network
- Netwrok Only，根据Item加载
- Netwrok Only，根据page加载

### 4.1 API

PagingNetworkSample中的API根据Key来加载上一页和下一页的数据，比如请求第一页时，在响应头中会返回用于请求下一页的数据key。API中的接口根据主题和key来加载数据。

```java
interface RedditApi {

    @GET("/r/{subreddit}/hot.json")
    fun getTop(@Path("subreddit") subreddit: String, @Query("limit") limit: Int): Call<ListingResponse>

    @GET("/r/{subreddit}/hot.json")
    fun getTopAfter(@Path("subreddit") subreddit: String, @Query("after") after: String, @Query("limit") limit: Int): Call<ListingResponse>

    @GET("/r/{subreddit}/hot.json")
    fun getTopBefore(@Path("subreddit") subreddit: String, @Query("before") before: String, @Query("limit") limit: Int): Call<ListingResponse>

    class ListingResponse(val data: ListingData)

    //ListingData 保存数据和加载上一下、下一页的key
    class ListingData(
            val children: List<RedditChildrenResponse>,
            val after: String?,
            val before: String?
    )

    data class RedditChildrenResponse(val data: RedditPost)

}
```

### 4.2  数据实体封装

Repository层返回的数据都是包装过的数据，因为不仅仅要处理数据的展示，还要处理好网络请求时的状态转换。所以通过一个包装对象把数据实体和各种请求状态封装在一起，然后由Repository统一组合返回是一个不错的实践。

```
//表示状态
enum class Status {
    RUNNING,
    SUCCESS,
    FAILED
}

@Suppress("DataClassPrivateConstructor")
data class NetworkState private constructor(
        val status: Status,
        val msg: String? = null) {
    companion object {
        val LOADED = NetworkState(Status.SUCCESS)
        val LOADING = NetworkState(Status.RUNNING)
        fun error(msg: String?) = NetworkState(Status.FAILED, msg)
    }
}

//用于封装各种状态和数据实体的清单
data class Listing<T>(
        // the LiveData of paged lists for the UI to observe
        val pagedList: LiveData<PagedList<T>>,
        // represents the network request status to show to the user
        val networkState: LiveData<NetworkState>,
        // represents the refresh status to show to the user. Separate from networkState, this
        // value is importantly only when refresh is requested.
        val refreshState: LiveData<NetworkState>,
        // refreshes the whole data and fetches it from scratch.
        val refresh: () -> Unit,
        // retries any failed requests.
        val retry: () -> Unit)
```

### 4.3 ViewModel

由于 `Listing<T>` 封了请求的数据和状态，Repository只需要返回Listing实例即可，然后交由 ViewModel 层处理。ViewModel转换和分离Listing中的数据与状态，并包装为LiveData提供给UI层。

```java
class SubRedditViewModel(private val repository: RedditPostRepository) : ViewModel() {

    private val subredditName = MutableLiveData<String>()

    //根据subredditName的变化来触发repository的加载行为，返回的是LiveData<Listing<RedditPost>>类型，这正是上面提到的包装类
    private val repoResult = map(subredditName, {
        //postsOfSubreddit返回的是一个Listing<RedditPost>
        //通过map转换后，保证为LiveData<Listing<RedditPost>>
        //每次subredditName发生变化，触发postsOfSubreddit方法产生新的Listing<RedditPost>
        repository.postsOfSubreddit(it, 30)
    })

    //获取repoResult中的实体
    val posts = switchMap(repoResult, { it.pagedList })!!
    //获取repoResult中的网络状态
    val networkState = switchMap(repoResult, { it.networkState })!!
    //获取repoResult中的刷新状态
    val refreshState = switchMap(repoResult, { it.refreshState })!!

     //由UI层调用，刷新
    fun refresh() {
        repoResult.value?.refresh?.invoke()
    }

    //由UI层设置搜索不同的主题，以此触发新内容的加载
    fun showSubreddit(subreddit: String): Boolean {
        if (subredditName.value == subreddit) {
            return false
        }
        subredditName.value = subreddit
        return true
    }

    //由UI层调用，重试
    fun retry() {
        val listing = repoResult?.value
        listing?.retry?.invoke()
    }

    fun currentSubreddit(): String? = subredditName.value
}
```

### 4.4 UI处理数据与状态

UI层从ViewModel获取观察的对象，处理数据、网络状态的变化。

```java
class RedditActivity : AppCompatActivity() {

     ...省略一些非核心代码

     private fun initAdapter() {
        val glide = GlideApp.with(this)
        val adapter = PostsAdapter(glide) {
            model.retry()
        }
        list.adapter = adapter
        //观察数据列表的变化
        model.posts.observe(this, Observer<PagedList<RedditPost>> {
            adapter.submitList(it)
        })
        //观察网络状态的变化，这里让Adapter负责在类别尾部展示Loading的Item
        model.networkState.observe(this, Observer {
            adapter.setNetworkState(it)
        })

        //不需要主动的调用加载更多，PagedListAdapter和PagedList内部会自动触发
     }



     private fun initSwipeToRefresh() {
         //观察刷新状态
        model.refreshState.observe(this, Observer {
            swipe_refresh.isRefreshing = it == NetworkState.LOADING
        })
        //触发刷新
        swipe_refresh.setOnRefreshListener {
            model.refresh()
        }
    }

    //告知ViewModel搜索的主题发生变化了
    private fun updatedSubredditFromInput() {
        input.text.trim().toString().let {
            if (it.isNotEmpty()) {
                //重新刷新
                if (model.showSubreddit(it)) {
                    //清空旧的数据
                    list.scrollToPosition(0)
                    (list.adapter as? PostsAdapter)?.submitList(null)
                }
            }
        }
    }

}
```

### 4.5 Repository

该示例有有三种不同列表加载场景，所有三种不同的实现，有一个工厂方法用于根据不同场景来创建不同的Repository：

```java
   //仓库接口
   interface RedditPostRepository {
      fun postsOfSubreddit(subReddit: String, pageSize: Int): Listing<RedditPost>
   }

    override fun getRepository(type: RedditPostRepository.Type): RedditPostRepository {
        return when (type) {
            RedditPostRepository.Type.IN_MEMORY_BY_ITEM -> InMemoryByItemRepository(
                    redditApi = getRedditApi(),
                    networkExecutor = getNetworkExecutor())
            RedditPostRepository.Type.IN_MEMORY_BY_PAGE -> InMemoryByPageKeyRepository(
                    redditApi = getRedditApi(),
                    networkExecutor = getNetworkExecutor())
            RedditPostRepository.Type.DB -> DbRedditPostRepository(
                    db = db,
                    redditApi = getRedditApi(),
                    ioExecutor = getDiskIOExecutor())
        }
    }
```

---
#### 4.5.1  DB(Room) + Newwork

>以下分析数据库通常Room

该模式的仓库使用 `PagedList.BoundaryCallback` 监听数据库何时数据不足。然后从网络中获取更多项目并将其插入数据库。

```java
class SubredditBoundaryCallback(
        private val subredditName: String,    //搜索的主题名
        private val webservice: RedditApi,    //API
        private val handleResponse: (String, RedditApi.ListingResponse?) -> Unit,//结果网络结果处理器
        private val ioExecutor: Executor,    //io执行器
        private val networkPageSize: Int)     //pageSize
    : PagedList.BoundaryCallback<RedditPost>() {

    //PagingRequestHelper用于BoundaryCallbacks和DataSources的帮助类，用于帮助跟踪网络请求。
    //它被设计为支持3种类型的请求，INITIAL，BEFORE和AFTER，并通过调用
    //runIfNotRunning（PagingRequestHelper.RequestType，PagingRequestHelper.Request）
    //为每个请求运行1个请求。它跟踪每个PagingRequestHelper.RequestType的PagingRequestHelper.Status和一个错误。
    val helper = PagingRequestHelper(ioExecutor)

    //网络状态
    val networkState = helper.createStatusLiveData()

    /**
     * Database returned 0 items. We should query the backend for more items.
     * 当数据库返回0个Item时，需要通过从后台查询更多的数据
     */
    @MainThread
    override fun onZeroItemsLoaded() {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) {
            webservice.getTop(
                    subreddit = subredditName,
                    limit = networkPageSize)
                    .enqueue(createWebserviceCallback(it))
        }
    }

    /**
     * 当PagedList结尾处的项目已加载并且在其prefetchDistance中发生访问时调用。
     * 并不一定就是在PagedList的尾部，也可能是在PagedList的头部。
     * 当列表的长度很长时，PagedList中远离显示位置的部分会使用null占位
     */
    @MainThread
    override fun onItemAtEndLoaded(itemAtEnd: RedditPost) {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) {
            webservice.getTopAfter(
                    subreddit = subredditName,
                    after = itemAtEnd.name,
                    limit = networkPageSize)
                    .enqueue(createWebserviceCallback(it))
        }
    }

    /**
     * every time it gets new items, boundary callback simply inserts them into the database and
     * paging library takes care of refreshing the list if necessary.
     * 每次获取新项目时，boundary callback 都会将它们插入数据库，并且在需要时分页库负责刷新列表。
     */
    private fun insertItemsIntoDb(response: Response<RedditApi.ListingResponse>,it: PagingRequestHelper.Request.Callback) {
        ioExecutor.execute {
            handleResponse(subredditName, response.body())
            it.recordSuccess()
        }
    }

    override fun onItemAtFrontLoaded(itemAtFront: RedditPost) {
        // ignored, since we only ever append to what's in the DB
        // 不需要处理
    }

    //用于处理网络结果
    private fun createWebserviceCallback(it: PagingRequestHelper.Request.Callback)
            : Callback<RedditApi.ListingResponse> {

        return object : Callback<RedditApi.ListingResponse> {
            //调用PagingRequestHelper的回调，告知请求失败
            override fun onFailure(  call: Call<RedditApi.ListingResponse>,t: Throwable) {
                it.recordFailure(t)
            }

            //结果正常，保存到数据库中
            override fun onResponse(call: Call<RedditApi.ListingResponse>,response: Response<RedditApi.ListingResponse>) {
                insertItemsIntoDb(response, it)
            }
        }
    }
}
```

DbRedditPostRepository的核心实现：

```java
class DbRedditPostRepository(

        val db: RedditDb,//数据库实例
        private val redditApi: RedditApi,//API
        private val ioExecutor: Executor,//执行器
        private val networkPageSize: Int = DEFAULT_NETWORK_PAGE_SIZE) //page size

        : RedditPostRepository {

    companion object {
        private const val DEFAULT_NETWORK_PAGE_SIZE = 10
    }


    /**
     * 根据给定的subreddit返回一个Listing
     */
    @MainThread
    override fun postsOfSubreddit(subReddit: String, pageSize: Int): Listing<RedditPost> {

        // 创建一个边界回调，它将观察用户何时到达列表边缘并用额外数据更新数据库。
        val boundaryCallback = SubredditBoundaryCallback(
                webservice = redditApi,
                subredditName = subReddit,
                handleResponse = this::insertResultIntoDb,//insertResultIntoDb方法用于将网络数据插入到数据库
                ioExecutor = ioExecutor,
                networkPageSize = networkPageSize)

        // create a data source factory from Room
        val dataSourceFactory = db.posts().postsBySubreddit(subReddit)
        val builder = LivePagedListBuilder(dataSourceFactory, pageSize)
                .setBoundaryCallback(boundaryCallback)

        //使用MutableLiveData触发刷新请求，最终调用刷新方法并获取新的实时数据。用户的每个刷新请求都变成新的调度refreshTrigger中的数据
        val refreshTrigger = MutableLiveData<Unit>()
        val refreshState = Transformations.switchMap(refreshTrigger, {
            refresh(subReddit)//refresh用于响应UI的刷新操作
        })

        return Listing(
                pagedList = builder.build(),
                networkState = boundaryCallback.networkState,
                retry = {
                    boundaryCallback.helper.retryAllFailed()
                },
                refresh = {
                    refreshTrigger.value = null
                },
                refreshState = refreshState
        )
    }

    //将响应插入到数据库中，同时将位置索引分配给项目。
    private fun insertResultIntoDb(subredditName: String, body: RedditApi.ListingResponse?) {
        body!!.data.children.let { posts ->
            db.runInTransaction {
                val start = db.posts().getNextIndexInSubreddit(subredditName)
                val items = posts.mapIndexed { index, child ->
                    child.data.indexInResponse = start + index
                    child.data
                }
                db.posts().insert(items)
            }
        }
    }


    /**
     * 当调用刷新时，我们只需运行新的网络请求，并在到达时清除数据库表并在事务中插入所有新项。由于PagedList已经使用数据库绑定的数据源，因此在数据库事务完成后它将自动更新。
     */
    @MainThread
    private fun refresh(subredditName: String): LiveData<NetworkState> {
        val networkState = MutableLiveData<NetworkState>()
        networkState.value = NetworkState.LOADING//修改状态

        redditApi.getTop(subredditName, networkPageSize).enqueue(
                object : Callback<RedditApi.ListingResponse> {
                    override fun onFailure(call: Call<RedditApi.ListingResponse>, t: Throwable) {
                        // 修改状态为error
                        networkState.value = NetworkState.error(t.message)
                    }

                    override fun onResponse( call: Call<RedditApi.ListingResponse>, response: Response<RedditApi.ListingResponse>) {
                        ioExecutor.execute {
                            db.runInTransaction {
                                db.posts().deleteBySubreddit(subredditName)
                                insertResultIntoDb(subredditName, response.body())
                            }
                            // since we are in bg thread now, post the result.
                            networkState.postValue(NetworkState.LOADED)
                        }
                    }
                }
        )
        return networkState
    }

}
```

##### 总结

`SubredditBoundaryCallback` 用于绑定到一个 `PagedList`，PagedListAdapter 从 PagedList 中获取数据展示，并且会触发 PagedList 从其绑定的 DataSource 中加载数据，当PagedList无法从对应的DataSource中加载到更多数据时，会触发 SubredditBoundaryCallback 的回回调方法：

- onZeroItemsLoaded：如果 PagedList 没有返回任何数据(数据库为null)，则 SubredditBoundaryCallback 的 `onZeroItemsLoaded` 方法被调用，此时应该从网络加载数据，并保存数据库中
- onItemAtEndLoaded：当PagedList结尾处的项目已加载并且在其prefetchDistance中发生访问时调用。并不一定就是在PagedList的尾部，也可能是在PagedList的头部。 当列表的长度很长时，PagedList中远离显示位置的部分会使用null占位

PagingRequestHelper 是一个帮助类用于BoundaryCallbacks和DataSources的帮助类，用于帮助跟踪网络请求。它被设计为支持3种类型的请求，INITIAL，BEFORE和AFTER，并通过调用 `runIfNotRunning（PagingRequestHelper.RequestType，PagingRequestHelper.Request`， 为每个请求运行1个请求。它跟踪每个 `PagingRequestHelper.RequestType` 的 `PagingRequestHelper.Status` 和一个错误。

##### 存在的问题

- 数据新鲜度问题：`SubredditBoundaryCallback` 只有在数据库没有数据时才会调用相关回调，如何在查询数据库时同时从网络加载数据，保证数据的新鲜度？
- 如果Item存在交互如何处理，比如删除一个条目，或者点赞、评论之类的操作。

---
#### 4.5.2 只从网络加载数据(Network Only)

两个示例：

- PageKeyedSubredditDataSource 根据key加载
- ItemKeyedSubredditDataSource 根据列表Item加载

如果只从网络加载数据，则应该实现自己的 DataSource，比如继承 PageKeyedDataSource ，因为失去了数据库的更新通知功能，必须在 PageKeyedDataSource 中实现初始化加载和加载更多的方法，并且显式返回加载后的列表。PagedList会自动处理新加载的数据(因为一般 PagedList 和 PagedListAdapter 一起使用，而在初始化 PagedListAdapter 时就需要为提供 DiffUtil 的Callback)。

当从PagedList获取数据时，PagedList 会调用与之绑定的 PageKeyedDataSource 的下列方法：

- loadInitial
- loadBefore
- loadAfter

通过实现上面方法，就可以让 PageKeyedDataSource 从网络源源不断的(只要网络中有数据)加载数据，PageKeyedDataSource加载到的数据会自动更新到对应的PagedList中。这不像 `DB+Network` 模式，`DB+Network` 模式的数据源是数据库，数据库的数据是有限的，当数据库中没有更多的数据后，会触发 `BoundaryCallback` 中相关方法的调用，然后在BoundaryCallback的回调中从网络加载数据更新数据到数据库，这样数据库因为数据有更新，会重新创建一个新的 `PagedList/DataSource`对。而在`Network Only`模式中，DataSource的数据直接来源于网络，这样就不需要 `BoundaryCallback` 监听PagedList 加载不到更多数据的情况。

```java
class PageKeyedSubredditDataSource(
        private val redditApi: RedditApi,
        private val subredditName: String,
        private val retryExecutor: Executor) : PageKeyedDataSource<String, RedditPost>() {

    //重试
    private var retry: (() -> Any)? = null

    //网络状态
    val networkState = MutableLiveData<NetworkState>()

    //初始化加载
    val initialLoad = MutableLiveData<NetworkState>()

    fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.let {
            retryExecutor.execute {
                it.invoke()
            }
        }
    }

    override fun loadBefore(
            params: LoadParams<String>,
            callback: LoadCallback<String, RedditPost>) {
    }

    //加载后面的数据
    //运行在Executor线程，这里使用Retorfit的异步调用是说明异步也是可以的
    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, RedditPost>) {
        networkState.postValue(NetworkState.LOADING)
        redditApi.getTopAfter(subreddit = subredditName,
                after = params.key,
                limit = params.requestedLoadSize).enqueue(
                object : retrofit2.Callback<RedditApi.ListingResponse> {
                    override fun onFailure(call: Call<RedditApi.ListingResponse>, t: Throwable) {
                        retry = {
                            loadAfter(params, callback)
                        }
                        networkState.postValue(NetworkState.error(t.message ?: "unknown err"))
                    }

                    override fun onResponse(
                            call: Call<RedditApi.ListingResponse>,
                            response: Response<RedditApi.ListingResponse>) {
                        if (response.isSuccessful) {
                            val data = response.body()?.data
                            val items = data?.children?.map { it.data } ?: emptyList()
                            retry = null
                            callback.onResult(items, data?.after)
                            networkState.postValue(NetworkState.LOADED)
                        } else {
                            retry = {
                                loadAfter(params, callback)
                            }
                            networkState.postValue(
                                    NetworkState.error("error code: ${response.code()}"))
                        }
                    }
                }
        )
    }

    //初始化加载，运行在Executor线程
    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<String, RedditPost>) {
        val request = redditApi.getTop(  subreddit = subredditName, limit = params.requestedLoadSize)

        networkState.postValue(NetworkState.LOADING)
        initialLoad.postValue(NetworkState.LOADING)

        // triggered by a refresh, we better execute sync
        try {
            val response = request.execute()
            val data = response.body()?.data
            val items = data?.children?.map { it.data } ?: emptyList()
            retry = null
            networkState.postValue(NetworkState.LOADED)
            initialLoad.postValue(NetworkState.LOADED)
            callback.onResult(items, data?.before, data?.after)
        } catch (ioException: IOException) {
            retry = {
                loadInitial(params, callback)
            }
            val error = NetworkState.error(ioException.message ?: "unknown error")
            networkState.postValue(error)
            initialLoad.postValue(error)
        }
    }
}
```

Repository：

```java
class SubRedditDataSourceFactory(
        private val redditApi: RedditApi,
        private val subredditName: String,
        private val retryExecutor: Executor) : DataSource.Factory<String, RedditPost>() {
    val sourceLiveData = MutableLiveData<PageKeyedSubredditDataSource>()
    override fun create(): DataSource<String, RedditPost> {
        val source = PageKeyedSubredditDataSource(redditApi, subredditName, retryExecutor)
        sourceLiveData.postValue(source)
        return source
    }
}


class InMemoryByPageKeyRepository(private val redditApi: RedditApi,
                                  private val networkExecutor: Executor) : RedditPostRepository {
    @MainThread
    override fun postsOfSubreddit(subReddit: String, pageSize: Int): Listing<RedditPost> {
        val sourceFactory = SubRedditDataSourceFactory(redditApi, subReddit, networkExecutor)

        val livePagedList = LivePagedListBuilder(sourceFactory, pageSize)
                // provide custom executor for network requests, otherwise it will default to
                // Arch Components' IO pool which is also used for disk access
                .setFetchExecutor(networkExecutor)
                .build()

        val refreshState = Transformations.switchMap(sourceFactory.sourceLiveData) {
            it.initialLoad
        }

        return Listing(
                pagedList = livePagedList,
                networkState = Transformations.switchMap(sourceFactory.sourceLiveData, {
                    it.networkState
                }),
                retry = {
                    sourceFactory.sourceLiveData.value?.retryAllFailed()
                },
                refresh = {
                    sourceFactory.sourceLiveData.value?.invalidate()
                },
                refreshState = refreshState
        )
    }
}
```

##### 总结

- 只从网络加载数据则实现自己的 DataSrouce 和 DataSourceFactory
- 如果Item是可删除的，如何处理，PagedList 不可变的列表，不支持删除、插入操作，难道又要重新刷一遍网络？

---
## 5 GithubBrowserSample

仅对search模块进行分析

### 5.1 单一数据源

- 使用数据库作为上层单一数据源
- 网络加载的数据保存到数据库，数据库会自动触发上层

### 5.2 封装流程与暴露网络状态

- 使用NetworkBoundResource封装流程
- 使用Resource封装状态：Success、Error、Loading

### 5.3 Repository实现

```java
class RepoRepository{

    //加载下一页    
    fun searchNextPage(query: String): LiveData<Resource<Boolean>> {
        //该任务用于加载下一页，保存到数据库
        val fetchNextSearchPageTask = FetchNextSearchPageTask(
            query = query,
            githubService = githubService,
            db = db
        )
        appExecutors.networkIO().execute(fetchNextSearchPageTask)
        //这个liveData不是加载的数据，而是加载更多的状态
        return fetchNextSearchPageTask.liveData
    }


    //初始加载、刷新
    fun search(query: String): LiveData<Resource<List<Repo>>> {
        return object : NetworkBoundResource<List<Repo>, RepoSearchResponse>(appExecutors) {

            override fun saveCallResult(item: RepoSearchResponse) {
                val repoIds = item.items.map { it.id }
                val repoSearchResult = RepoSearchResult(
                    query = query,
                    repoIds = repoIds,
                    totalCount = item.total,
                    next = item.nextPage
                )
                db.beginTransaction()
                try {
                    repoDao.insertRepos(item.items)
                    repoDao.insert(repoSearchResult)
                    db.setTransactionSuccessful()
                } finally {
                    db.endTransaction()
                }
            }

            override fun shouldFetch(data: List<Repo>?) = data == null

            override fun loadFromDb(): LiveData<List<Repo>> {
                return Transformations.switchMap(repoDao.search(query)) { searchData ->
                    if (searchData == null) {
                        AbsentLiveData.create()
                    } else {
                        repoDao.loadOrdered(searchData.repoIds)
                    }
                }
            }

            override fun createCall() = githubService.searchRepos(query)

            override fun processResponse(response: ApiSuccessResponse<RepoSearchResponse>)
                    : RepoSearchResponse {
                val body = response.body
                body.nextPage = response.nextPage
                return body
            }
        }.asLiveData()
    }

}
```

### 5.4 ViewModel

```java
class SearchViewModel @Inject constructor(repoRepository: RepoRepository) : ViewModel() {

    //搜索的内容
    private val query = MutableLiveData<String>()

    //加一页加载处理器
    private val nextPageHandler = NextPageHandler(repoRepository)

    ////加载的结果
    //results直接连接着数据库，一旦数据库有更新，则results将会收到新的结果
    val results: LiveData<Resource<List<Repo>>> = Transformations
        .switchMap(query) { search ->
            if (search.isNullOrBlank()) {
                AbsentLiveData.create()
            } else {
                repoRepository.search(search)
            }
        }

    //加载更多的状态
    val loadMoreStatus: LiveData<LoadMoreState>
        get() = nextPageHandler.loadMoreState

    //触发新的搜索
    fun setQuery(originalInput: String) {
        val input = originalInput.toLowerCase(Locale.getDefault()).trim()
        if (input == query.value) {
            return
        }
        nextPageHandler.reset()
        query.value = input
    }

    //加载下一页
    fun loadNextPage() {
        query.value?.let {
            if (it.isNotBlank()) {
                nextPageHandler.queryNextPage(it)
            }
        }
    }

    //直接刷新
    fun refresh() {
        query.value?.let {
            query.value = it
        }
    }

    //封装加载更多的状态
    class LoadMoreState(val isRunning: Boolean, val errorMessage: String?) {
        private var handledError = false

        val errorMessageIfNotHandled: String?
            get() {
                if (handledError) {
                    return null
                }
                handledError = true
                return errorMessage
            }
    }

    class NextPageHandler(private val repository: RepoRepository) : Observer<Resource<Boolean>> {
             主要就要调用RepoRepository加载下一页，然后通过loadMoreStatus分发加载更多的状态
             ...
    }
}
```


### 5.5 UI

```java
 class SearchFragment : Fragment(), Injectable {


     ...去掉非关键代码

    private fun initRecyclerView() {

        //加载更多
        binding.repoList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastPosition = layoutManager.findLastVisibleItemPosition()
                if (lastPosition == adapter.itemCount - 1) {
                    searchViewModel.loadNextPage()
                }
            }
        })

        //观察列表
        searchViewModel.results.observe(this, Observer { result ->
            binding.searchResource = result
            binding.resultCount = result?.data?.size ?: 0
            adapter.submitList(result?.data)
            binding.executePendingBindings()
        })

        //加载更多状态
        searchViewModel.loadMoreStatus.observe(this, Observer { loadingMore ->
            if (loadingMore == null) {
                binding.loadingMore = false
            } else {
                binding.loadingMore = loadingMore.isRunning
                val error = loadingMore.errorMessageIfNotHandled
                if (error != null) {
                    Snackbar.make(binding.loadMoreBar, error, Snackbar.LENGTH_LONG).show()
                }
            }
            binding.executePendingBindings()
        })
    }

 }
```

### 5.6 问题

- 没有配合Paging使用，即没有对内容进行分页，数据库中的数据会直接全部被查询出来。如果数据量非常大，很浪费内存
- 如果从网络加载后的数据是一定不变的，可以使用 PagingNetworkSample 中 DB+Network 的示例

---
## 其他

- PersistenceContentProviderSample：演示ContentProvider如何使用Room来暴露数据
- PersistenceMigrationsSample ：演示如何从SQLite迁移到Room
- BasicRxJavaSampleKotlin：演示如何与Kotlin配合使用

