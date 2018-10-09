# Rxjava操作符——辅助与变换操作符

##  delay

delay延迟一段指定的时间再发射来自Observable的发射物

`delay`还有另外一个实现，接受一个Observable，当这个Observable正常结束时，`delay`产生的Observable才发射数据，这个`delay`不在任何调度器上执行。

`delay`默认在`computation`调度器上执行

## delaySubscription

delaySubscription可以延迟订阅原始Observable。

**`delaySubscription`默认在`computation`**

## doOnComolete/doOnError/doOnUnsubscribe

doOnComolete/doOnError/doOnUnsubscribe分别标识在Observable完成/错误/取消订阅时调用。在哪个线程`unsubscribe`，`doOnUnsubscribe`就可能在那个线程被调用

## doOnEach/doOnNext

doOnEach/doOnNext产生的Observable每发射一项数据`doOnEach/doOnNext`就会调用它一次

- doOnEach的参数是`notification`，可以被再次订阅
- doOnNext的参数就是被发射的数据。

## doOnTerminate/doAfterTerminate

doOnTerminate/doAfterTerminate表示无论Observable是否正常终止，他们都会被调用。

## doOnSubscribe

doOnSubscribe是非常特殊的操作符，当观察者订阅它生成的Observable它就会被调用。

## materialize

- `materialize`将来自原始Observable的通知转换为`Notification`对象，然后它返回的Observable会发射这些数据。
- `dematerialize`操作符是`materialize`的逆向过程，它将`Materialize`转换的结果还原成它原本的形式。


一个合法的有限的Obversable将调用它的观察者的`onNext`方法零次或多次，然后调用观察者的`onCompleted`或`onError`正好一次。`materialize`操作符将这一系列调用，包括原来的`onNext`通知和终止通知`onCompleted`或`onError`都转换为一个Observable发射的数据序列。

## serialize

serializee强制一个Observable连续调用并保证行为正确

一个Observable可以异步调用它的观察者的方法，可能是从不同的线程调用。这可能会让Observable行为不正确，它可能会在某一个`onNext`调用之前尝试调用`onCompleted`或`onError`方法，或者从两个不同的线程同时调用`onNext`方法。使用`Serialize`操作符，你可以纠正这个Observable的行为，保证它的行为是正确的且是同步的。

RxJava中的实现是`serialize`，它默认不在任何特定的调度器上执行。

## timeInterval

timeInterval将一个发射数据的Observable转换为发射那些数据发射时间间隔的Observable

`timeInterval`操作符拦截原始Observable发射的数据项，替换为发射表示相邻发射物时间间隔的对象
这个操作符将原始Observable转换为另一个Obserervable，发射一个标志替换前者的数据项，这个标志表示前者的两个连续发射物之间流逝的时间长度。新的Observable的第一个发射物表示的是在观察者订阅原始Observable到原始Observable发射它的第一项数据之间流逝的时间长度。不存在与原始Observable发射最后一项数据和发射`onCompleted`通知之间时长对应的发射物。

## timeTamp

timeTamp将一个发射T类型数据的Observable转换为一个发射类型为`Timestamped<T>`的数据的Observable，每一项都包含数据的原始发射时间

## using

using创建一个只在Observable生命周期内存在的一次性资源

## nest

`nest`操作符有一个特殊的用途：将一个Observable转换为一个发射这个Observable的Observable。

## to系列操作符

to系列操作符将Observable转换为另一个对象或数据结构

ReactiveX的很多语言特定实现都有一种操作符可以将Observable或者Observable发射的数据序列转换为另一个对象或数据结构。`它们中的一些会阻塞直到Observable终止`，然后生成一个等价的对象或数据结构；另一些返回一个发射那个对象或数据结构的Observable。

在某些ReactiveX实现中，还有一个操作符用于将Observable转换成阻塞式的。一个阻塞式的Ogbservable在普通的Observable的基础上增加了几个方法，用于操作Observable发射的数据项。

BlockingObservable的方法不是将一个Observable变换为另一个，也不是过滤Observables，`它们会打断Observable的调用链，会阻塞等待直到Observable发射了想要的数据`，然后返回这个数据（而不是一个Observable）。

`toBlocking`把普通的`Observable`转换成阻塞的`BlockingObservable`, 其有可能用于测试和演示目的是有用的，但通常不适合生产应用程序。

### getIterator

`getIterator`操作符只能用于`BlockingObservable`的子类

可以使用这两个操作符：`BlockingObservable.from`或`the Observable.toBlocking`。
这个操作符将Observable转换为一个`Iterator`，可以通过它迭代原始Observable发射的数据集。

### toFuture

`toFuture`操作符也是只能用于`BlockingObservable`
这个操作符将Observable转换为一个返回单个数据项的`Future`，如果原始Observable发射多个数据项，`Future`会收到一个`IllegalArgumentException`；如果原始Observable没有发射任何数据，`Future`会收到一个`NoSuchElementException`。

如果想将发射多个数据项的Observable转换为`Future`，可以这样用：`myObservable.toList().toBlocking().toFuture()`。

### toIterable

`toIterable`操作符也是只能用于`BlockingObservable`。这个操作符将Observable转换为一个`Iterable`，你可以通过它迭代原始Observable发射的数据集。

### toList/toMap/toSortList/toMultiMap

用于合并数据序列
















