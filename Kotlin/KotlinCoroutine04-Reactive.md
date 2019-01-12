# 协程的 Reactive 扩展

## 1 Module kotlinx-coroutines-reactive

Module kotlinx-coroutines-reactive 提供的协程构建器 `publish` 返回一个 Publisher。同时为 Publisher 提供了许有有用的扩展方法：

- Publisher.awaitFirst	
- Publisher.awaitFirstOrDefault
- Publisher.awaitFirstOrElse
- Publisher.awaitFirstOrNull	
- Publisher.awaitLast 
- Publisher.awaitSingle
- Publisher.openSubscription，订阅该发布者，返回一个 ReceiveChannel。

同时通过 ReceiveChannel.asPublisher 可以把 ReceiveChannel 转换为 asPublisher。

具体参考[kotlinx-coroutines-reactive](https://github.com/hltj/kotlinx.coroutines-cn/blob/master/reactive/kotlinx-coroutines-reactive/README.md)

## 2 Module kotlinx-coroutines-rx2

Module kotlinx-coroutines-reactive 提供了以下协程构建器：

- rxCompletable
- rxMaybe
- rxSingle
- rxObservable
- rxFlowable

同时为 Rx2 中的各种 source 提供了许有有用的扩展方法，这些扩展与 Module kotlinx-coroutines-reactive 中的类似。同时 Rx2 中的各种 source 也支持可协程中的相关元素进行转换，比如：

- Job.asCompletable：将 job 转换为热的 completable
- Deferred.asSingle：将 Deferred 转换为热的 Single
- ReceiveChannel.asObservable：将 ReceiveChannel 转换为热的 Observable
- Scheduler.asCoroutineDispatcher：调度器也可以转换为协程中的 CoroutineDispatcher

具体参考[kotlinx-coroutines-rx2](https://github.com/hltj/kotlinx.coroutines-cn/blob/master/reactive/kotlinx-coroutines-rx2/README.md)