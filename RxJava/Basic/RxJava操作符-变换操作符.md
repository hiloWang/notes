# RxJava操作符——变换操作符

## map

map操作符对原Observable发生的每一个数据应用一个变换函数转换成一个新的数据，然后再发射

## flatMap

`flatMap`操作符使用一个指定的函数对原始Observable发射的每一项数据执行变换操作，这个函数返回一个本身也发射数据的Observable，然后`flatMap`合并这些Observables发射的数据，最后将合并后的结果当做它自己的数据序列发射。

 `flatMap()` 和 `map()` 有一个相同点：它也是把传入的参数转化之后返回另一个对象。但需要注意，和 `map()` 不同的是，`flatMap()` 中返回的是个 `Observable` 对象，并且这个 `Observable` 对象并不是被直接发送到了 `Subscriber` 的回调方法中。

由于可以在嵌套的 `Observable` 中添加异步代码，`flatMap()` 也常用于嵌套的异步操作，例如嵌套的网络请求。而`map`仅仅只是对数据进行转换，且转换都是同步都是一个串行的过程，`map`无法用于实现嵌套的有返回值的异步操作。

flatMap还有一个常用的重载操作符有一个接受额外的`int`参数的一个变体。这个参数设置`flatMap`从原来的Observable映射Observables的**最大同时订阅数**。当达到这个限制时，它会等待其中一个终止然后再订阅另一个。当变换后的Observable是异步任务是，理由这个操作符可以控制任务最大并发数。

需要注意的是`flatMap()`**不保证对原有数据添加异步操作转换后**，生成新的Observable，最后合并这些Observable的顺序一致性！顺序取决于异步操作的耗时。

## flatMapIterable

flatMapIterable这个变体成对的打包数据，然后生成Iterable而不是原始数据和生成的Observables，但是处理方式是相同的。

## concatMap

concatMap功能与flatMap类似，只是concatMap保证转换后发射的数据是按顺序的，具体可以阅读此文章[concatMap与flatMap的比较](http://www.jianshu.com/p/6d16805537ef)

## switchMap

switchMap功能与flatMap类似，但是有一点当原始Observable发射一个新的数据（Observable）时，它将取消订阅并停止监视产生执之前那个数据的Observable，只监视当前这一个。

## buffer

buffer定期收集Observable的数据放进一个数据包裹，然后发射这些数据包裹，而不是一次发射一个值。

注意：如果原来的Observable发射了一个`onError`通知，`Buffer`会立即传递这个通知，而不是首先发射缓存的数据，即使在这之前缓存中包含了原始Observable发射的数据

buffer的某些重载默认情况下会使用`computation`调度器。

buffer有很多重载形式,比如:

- buffer(int count) 每次收集count个数据后进行发生
- buffer(count, skip) 从原始Observable的第一项数据开始创建新的缓存，此后每当收到`skip`项数据，用`count`项数据填充缓存：开头的一项和后续的`count-1`项
- buffer(boundary) `buffer(boundary)`监视一个名叫`boundary`的Observable，每当这个Observable发射了一个值，它就创建一个新的`List`开始收集来自原始Observable的数据并发射原来的`List`

## scan

`scan`操作符对原始Observable发射的第一项数据应用一个函数，然后将那个函数的结果作为自己的第一项数据发射。它将函数的结果同第二项数据一起填充给这个函数来产生它自己的第二项数据。它持续进行这个过程来产生剩余的数据序列。这个操作符在某些情况下被叫做`accumulator`。scan可用于累加操作。

##  groupBy

groupBy用于对Observable发射的数据序列进行分组。


## window

RxJava的window()函数和buffer()很像，但是它发射的Observable而不是列表，我们订阅后在onNext中接受的是Obserable。

## cast

cast用于类型转换
