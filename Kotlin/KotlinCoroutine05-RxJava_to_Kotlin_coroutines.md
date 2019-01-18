# RxJava to Kotlin coroutines: Observing suspenders

原文链接：[RxJava to Kotlin coroutines](https://medium.com/androiddevelopers/rxjava-to-kotlin-coroutines-1204c896a700)

这篇文章总结了我如何重构一个使用 RxJava 的应用程序，具体来说，我将讨论将 Single/Maybe/Completable 源切换到协同程序。

## The app

首先，稍微介绍应用程序的架构方式。我的大部分业务逻辑都内置于称为`calls`的内容中：

```kotlin
interface Call<in Param, Output> {
    fun data(param: Param): Flowable<Output>
    fun refresh(param: Param): Completable
}
```

正如你所看到的，每一个 call 都有两个主要的责任：

1. 它的 `data()`方法，它暴露与调用相关的数据流。返回一个 Flowable，大部分时候，它只是一个从 Room 返回的一个 Flowable，然后一个 ViewModule 会订阅它并且传递数据到 UI 层，等。
2. 它的 `refresh()`方法，它触发一次数据刷新，大部分实现会从网络获取数据，转换数据实体并且把它们更新到 Room 数据库中，现在它返回一个 Completable，一个 ViewModule 会订阅它的 action。（Room 的更新会自动触发上面 data 返回的流）

## So where did I plan on fitting coroutines into this?

我的目标是使 `refresh()` 成为一个挂起函数：

```kotlin
interface Call<in Param, Output> {
    fun data(param: Param): Flowable<Output>
    suspend fun refresh(param: Param)
}
```

一旦我做了这个改变，所有的 call 实现开始抱怨，因为方法签名改变了，幸运的是  [**kotlinx-coroutines-rx2**](https://github.com/Kotlin/kotlinx.coroutines/tree/master/reactive/kotlinx-coroutines-rx2) 扩展为  RxJava 单一类型提供了一些扩展函数，允许我们 `await()`它们的完成，因此，我在每个实现 Rx 链的末尾快速粘贴了 `.await（）`代码，修复了构建。

> [**kotlinx-coroutines-rx2**](https://github.com/Kotlin/kotlinx.coroutines/tree/master/reactive/kotlinx-coroutines-rx2) 是 Kotlin Coroutines 对 RxJava2 的扩展。

现在我们非常粗糙地和愚蠢地使用 Kotlin Coroutines，但是这只是第一步。而且一切都可以工作。

下一步是开始转换 `refresh()` "coroutine aware" 下面的所有代码，并删除不需要它的 RxJava。在这一点上，你可能想知道我所说的 "coroutine aware" 是什么，好吧，这与线程有关。

## Threading

在 RxJava 中，我有用于不同类型任务的不同的 Scheduler ，这是通过 data class 来实现的，该 data class 被注入到将 Rx 运算符链接在一起的任何地方。

```kotlin
data class AppRxSchedulers(
    val database: Scheduler,
    val disk: Scheduler,
    val network: Scheduler,
    val main: Scheduler
)
//Dagger2 注解
@Singleton
@Provides
fun provideRxSchedulers() = AppRxSchedulers(
        database = Schedulers.single(),
        disk = Schedulers.io(),
        network = Schedulers.io(),
        main = AndroidSchedulers.mainThread()
)
```

在我看来最重要的一点是数据库的 Scheduler，这是因为我希望想要强制单线程读取，确保数据完整性并且不对 SQLite 加锁。

对于 Coroutines 我也想做同样的事，确保 RxJava 和 Coroutines 底层使用同样的线程池，使用 kotlinx-coroutines-rx2 扩展库，这相对容易实现。它给 Scheduler 添加了扩展函数将其包装为一个 CoroutineDispatcher，使用它我将我的 Sceduler 转换为 CoroutineDispatcher 并注入它们。

```kotlin
data class AppCoroutineDispatchers(
    val database: CoroutineDispatcher,
    val disk: CoroutineDispatcher,
    val network: CoroutineDispatcher,
    val main: CoroutineDispatcher
)

@Singleton
@Provides
fun provideDispatchers(schedulers: AppRxSchedulers) = 
    AppCoroutineDispatchers(
        database = schedulers.database.asCoroutineDispatcher(),
        disk = schedulers.disk.asCoroutineDispatcher(),
        network = schedulers.network.asCoroutineDispatcher(),
        main = UI//在kotlin 1.3 中应该是 Dispatchers.Main
    )
```

正如你所见到的，目前我是用 RxJava 的 Scheduler 作为源，在未来，我可能会交换它，以便调度程序来自调度程序。

## Changing threads

现在，我让我的 Scheduler 和 Dispatcher 共享同样的线程池，但是如何在我的操作中使用它们呢？ RxJava 使用它的 `subscribeOn()` 和 `observeOn()` 方法非常容易的将不同的线程观察者链在一起，这里是 `refresh()` 方法的一个实例，我用我的 network scheduler 从网络获取数据并且将它转换内部实体，然后使用 data scheduler 来存储结果。

```kotlin
override fun refresh(): Completable {
    return trakt.users().profile(UserSlug.ME).toRxSingle()
            .subscribeOn(schedulers.network)
            .map {
                TraktUser(username = it.username, name = it.name)
            }
            .observeOn(schedulers.database)
            .doOnSuccess {
                dao.insert(it)
            }
            .toCompletable()
}

override fun data(): Flowable<TraktUser> {
    return dao.getTraktUser()
            .subscribeOn(schedulers.database)
}
```

希望你能看到这是一个漂亮标准的 RxJava 风格代码，现在我需要将他转换为 coroutines。

## First attempt

当你读过 [coroutines guide](https://github.com/Kotlin/kotlinx.coroutines/blob/master/coroutines-guide.md) 后，你的脑海中大概有两个函数：`launch()` 和 `async()`，大概你也能猜到，我的第一次尝试集中在使用这些将事物链接在一起：

```kotlin
override suspend fun refresh(param: Unit) {
    // Fetch network response on network dispatcher
    // 使用 network dispatcher 执行获取网络响应
    val networkResponse = async(dispatchers.network) {
        trakt.users().profile(UserSlug.ME).execute().body()
    }.await() // await the result 等待结果
    // Map to our entity 转换为我们需要的实体
    val entity = TraktUser(
        username = networkResponse.username,
        name = networkResponse.name
    )
	// 使用 database dispatcher 执行保持到数据库中
    // Save to the database on the database dispatcher
    async(dispatchers.database) {
        dao.insert(entity)
    }.await()  // Wait for the insert to finish 等待插入完成
}
```

这实际上有效，但有点浪费。在这里，我们实际上开启了三个协程，1：network call；2：database call；3 主协程调用 `data()`（在ViewModel中）。

我相信你能想象到一个更加复杂的 Rx 链，它可以做像  `flatMap` 一个 Iterable 或其他疯狂的事情。当你并不总是需要它们时，你所创建的协同程序的数量会显着增加，上面方法中所作的每一个操作都是顺序执行的，所以它们并不需要去开启一个新的 coroutine，我们需要的是一种改变 dispatcher 的方法，并且幸运的是，coroutines 团队为我们提供了一个方法：`withContext`。

> withContext
>
> ```kotlin
> suspend fun <T> withContext(
>     context: CoroutineContext, 
>     block: suspend CoroutineScope.() -> T
> ): T (source)
> ```
>
> Calls the specified suspending block with a given coroutine context, suspends until it completes, and returns the result.
>
> This function immediately applies dispatcher from the new context, shifting execution of the block into the different thread inside the block, and back when it completes. The specified [context](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/with-context.html#kotlinx.coroutines$withContext(kotlin.coroutines.CoroutineContext,%20kotlin.SuspendFunction1((kotlinx.coroutines.CoroutineScope,%20kotlinx.coroutines.withContext.T)))/context) is added onto the current coroutine context for the execution of the block.

## Second attempt

我在 [coroutines guide](https://github.com/Kotlin/kotlinx.coroutines/blob/master/coroutines-guide.md#thread-confinement-fine-grained) 中的一个小代码示例中偶然发现了 [withContext()](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.experimental/with-context.html)，我的第一个尝试是使用 `withContext` 来替换 `subscribeOn()` and `observeOn()`，因为它完全符合我们的要求：

> 这个方法立即使用来自新的 context 上指定的 Dispatchr，将块的执行转移到块内的不同线程中，然后等待它完成后返回。

考虑到这一点，该示例变为：

```kotlin
override suspend fun refresh(param: Unit) {
    // Fetch network response on network dispatcher
    val networkResponse = withContext(dispatchers.network) {
        trakt.users().profile(UserSlug.ME).execute().body()
    }
    // Map to our entity
    val entity = TraktUser(
        username = networkResponse.username,
        name = networkResponse.name
    )

    // Save to the database on the database dispatcher
    withContext(dispatchers.database) {
        dao.insert(entity)
    }
}
```

可以看到，现在我们移除了 `async` 调用，这意味着我们没有创建新的 coroutines，我们只是把主协程的调度移动到我们规定的 dispatcher(thread) 中。

## But the docs say that coroutines are really lightweight. Why can’t I just async/launch?

coroutines 确实是非常轻量级的，但是创建它们依然需要一些消耗，你需要记住的是，运行我们程序的 Android ，是一个资源受限的系统，所以我们应该尽可能的来压缩我们的步骤，而使用 `withContext` 满足我们的需求，与使用 `async` 或 `launch` 创建新协程相比，它只是单个函数调用和最小对象分配。

还有一个事实是，异步和启动是针对异步的任务。大多数时候你有一个主要的异步任务，但是在其内部你将会调度子的异步任务，通过使用 `async` 或 `launch` 你将被迫使用 `await()` 和 `join()`，这是不必要的阅读复杂性（此时 kotlin coroutine 还没有提供结构化并发）。

另一方面，如果你的子任务是不相关的（与其他任务没有关联性），使它们与 `async` 同时运行是一种有效的方法。

## What about more complex Rx chains?

这是一个示例链，每当我看到它时都会混淆我：

```kotlin
override fun refresh(param: Unit): Completable {
    trakt.users().watchedShows(UserSlug.ME).toRxSingle()
            .subscribeOn(schedulers.network)
            .toFlowable()
            .flatMapIterable { it }
            .flatMapSingle {
                showFetcher.load(it)
            }
            .toList()
            .observeOn(schedulers.database)
            .doOnSuccess {
                databaseTransactionRunner.runInTransaction {
                    dao.deleteAll()
                    it.forEach { dao.insert(it) }
                }
            }
            .toCompletable()
}
```

当你分析它时，它实际上并不是那么复杂，而是我们上面使用过的例子。最大的区别在于它处理的是实体集合而不是单个实体。要做到这一点，使用 Flowable 的 `flatMapIterable()` 加载出每个项目对应的数据（fan out） ，然后使用 `toList` 组合所有的条目到一个 list 中，然后持久化到数据库。 

我实际上使用另一个类（showFetcher）为操作符提供 fan out 操作，在这案例中，它返回 Single，现在忽略它吧。

这里我们实际上描述的是一个并行的映射，`map()`的运行时并发的，JDK8 提供了一些类似 操作：`list.parallel().map(*/\* map function */*).collect(toList())`。

在 Kotlin 中没有使用 coroutines 的内建版本（我能找到的范围中），但幸运的是，它很容易被实现：

```kotlin
suspend fun <A, B> Collection<A>.parallelMap(
    context: CoroutineContext = DefaultDispatcher,
    block: suspend (A) -> B
): Collection<B> {
    return map {
        // Use async to start a coroutine for each item
        async(context) {
            block(it)
        }
    }.map {
        // We now have a map of Deferred<T> so we await() each            
        it.await()
    }
}
```

注意：虽然对我来说有用，但它也许不是一个完美的实现。

通过使用 `parallelMap`，我们的复杂的 Rx 链变成了下面的样子：

```kotlin
override suspend fun refresh(param: Unit) {
    val networkResponse = withContext(dispatchers.network) {
        trakt.users().watchedShows(UserSlug.ME).execute().body()
    }

    val shows = networkResponse.parallelMap {
        showFetcher.load(it)
    }

    // Now save the list to the database
    withContext(dispatchers.database) {
        databaseTransactionRunner.runInTransaction {
            dao.deleteAll()
            shows.forEach { dao.insert(it) }
        }
    }
}
```

希望你能看到现在阅读更加清晰，showFetcher 类仍然需要转换，但现在可以等待。如果你对此感兴趣，你可以查看 将 Rx 转换为 coroutines 的 [PR](https://github.com/chrisbanes/tivi/pull/135/) 。

## Next steps

希望您可以看到从 RxJava 的 Single / Maybe / Completable 切换到协同程序实际上相对容易。目前，我仍在使用 RxJava 来传输可观察数据，但我可能会转向 LiveData 或 coroutine channel。

## 补充说明

集成 kotlinx-coroutines-rx2：

```groovy
// https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-rx2
compile group: 'org.jetbrains.kotlinx', name: 'kotlinx-coroutines-rx2', version: '1.0.1'
```

