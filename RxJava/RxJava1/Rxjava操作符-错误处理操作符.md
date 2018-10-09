#  RxJava操作符——错误处理操作符

## onErrorReturn

`onErrorReturn` 让Observable遇到错误时发射一个特殊的项并且正常终止。`onErrorReturn`可以用于提前处理错误。

## onErrorResumeNext

`onErrorResumeNext` 让Observable在遇到错误时开始发射第二个Observable的数据序列。

## onExceptionResumeNext

`onExceptionResumeNext` 和`onErrorResumeNext`类似，`onExceptionResumeNext`方法返回一个镜像原有Observable行为的新Observable，也使用一个备用的Observable，不同的是，如果`onError`收到的`Throwable`不是一个`Exception`，它会将错误传递给观察者的`onError`方法，不会使用备用的Observable。

## retry

`retry`操作符不会将原始Observable的`onError`通知传递给观察者，它会订阅这个Observable，再给它一次机会无错误地完成它的数据序列。`retry`总是传递`onNext`通知给观察者，由于重新订阅，可能会造成数据项重复

`retry`还有一个重载，接受一个int类型的参数，表示重试多少次。

**`retry`操作符默认在`trampoline`调度器上执行。**


## retryWhen

`retryWhen`和`retry`类似，区别是，`retryWhen`将`onError`中的`Throwable`传递给一个函数，这个函数产生另一个Observable，`retryWhen`观察它的结果再决定是不是要重新订阅原始的Observable。如果这个Observable发射了一项数据，它就重新订阅，如果这个Observable发射的是`onError`通知，它就将这个通知传递给观察者然后终止。**`retryWhen`默认在`trampoline`调度器上执行**

使用`retryWhen`可以在一些需要token的接口访问中起到很大的作用，比如当token失效时，重新获取token之后，再次调用需要token的接口。**需要注意的是，重试只应用在调用链上，对于类似Observable.just(A)操作符创建的数据源，`retryWhen`是无法导致just的重新调用，数据A将会被缓存。**



















