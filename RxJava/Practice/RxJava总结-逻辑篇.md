# RxJava使用总结


---
## 1 map与flatMap使用场景

- 场景1：**一对多**的变换
- 场景2：一个接口的请求依赖另一个API请求返回的数据

### 1.1 Map

1. `map`只用与**同步的**，**一对一的**数据转换
2. `map`接收一个`Func1`函数对象，我们可以继承`Func1`加上一些业务逻辑，封装共性的业务处理

所以一般的**一对一没有异步嵌套需求**就该使用map。

### 1.2 FlatMap

`flatMap`可用于`嵌套的异步操作`和`一对多`的变化操作

1. `flatMap`可以用于**一对多**的变换。
2. `flatMap`可以嵌套异步操作，无论是多个还是单个，这是`map`无法做到的，而且`flatMap`的一个重载可以使用一个int类型的参数设置同时执行flatMap的最大数量

嵌套异步操作可以用**多个接口需要顺序调用**的场景，比如县获取token，才能访问下一个接口。另外需要注意的是当同时发出多个嵌套的异步操作时， **`flatMap`不保证异步的响应顺序。**

---
## 2 flatMap与concatMap

`flatMap`与`concatMap`的使用非常相似，但是除此之外`concatMap`还保证了**多个**嵌套的异步操作按照之前发出异常操作的顺序发射异步操作的结果。所以对于**多个嵌套的需要保证结果按顺序发射的操作**可以使用`concatMap`，其他则可以使用flatMap

---
## 3 zip

有一些业务场景，在同一个界面中需要同时调用多个接口，而且又需要保证这多个接口都响应后才对显示界面，这时我们可以使用zip操作符

---
## 4 repeatWhen

使用RepeatWhen实现轮询

## 5 retryWhen

情怯接口的token失效后，先更新toekn，再自动重试的逻辑

## 6 concat+first

先取 memory 中的数据，如果有，就取出，然后停止检索队列；没有就取 disk 的数据，有就取出，然后停止检索队列；最后才是网络请求，first() 和 takeFirst() 区别在于，如果没有符合的数据源，first() 会抛 NoSuchElementException 异常

```java
            Observable<Data> memory = Observable.just(new Data());
            Observable<Data> disk = Observable.just(new Data());
            Observable<Data> network = Observable.just(new Data());
    
            Observable<Data> source = Observable
                    .concat(memory, disk, network)
                    .first();
```

参考：[loading-data-from-multiple-sources-with-rxjava](http://blog.danlew.net/2015/06/22/loading-data-from-multiple-sources-with-rxjava/)

---
## 7 defer

使用defer用于延迟创建Observable，不要在创建操作符just、from等中直接调用长耗时的方法获取数据，在创建Observable时就是长耗时的，正确的做法是使用defer。

---
## 8 BehaviorSubject

BehaviorSubject的特性就是当有一个新的订阅者订阅它时，如果它之前发射过数据，它会把之前最后发射的一项数据发射个现在的订阅者，也就是说BehaviorSubject会缓存他最近一次发射的数据，使用BehaviorSubject这个特性实现内存缓存

---
## 9 Replay

### 9.1 使用replay缓存发射过的数据，当屏幕旋转Activity重建时，把之前的数据恢复过来

其实是利用Fragmen的retain功能，保留ConnectableObservable的实例，但是setRetainInstace(true)只会在Activity因为配置重建(reCreate)时才会保留Fragment的实例，但是真实的情况下要复杂的多，所以一般不会这样使用Replay。

### 9.2 多个界面从同一个数据源获取数据

有这样的情况，一个接口返回的数据是多个界面需要的，比如ViewPager的多个列表Pager或从同一个接口获取不同类型的数据来展示，而多个界面可能通过出发请求数据的方法，为了只让请求数据的方法只调用一次，可以使用ConnectableObservable来缓存数据。这是Observable多播的一种应用。使用连接类操作符把普通的Observable变成多播的ConnectableObservable，多个界面从同一个ConnectableObservable根据条件过滤获取自己的数据。

---
##  10 autoConnect有指定数量的订阅者订阅后才开始执行

如果明确的知道会有多少个订阅者，并且需要当所有的订阅者都订阅时才开始请求数据，可以使用autoConnection，autoConnection的作用是：**返回一个Observable，当有规定数量的Subscribers订阅它时自动连接这个ConnectableObservable**。

---
## 11 使用 Completable 或 Single

对于有些只有成功或者失败的操作，或者调用方只关心成功或者失败的操作，一个个使用 Completable，而不是 Flowable 或 Observable。

---
## 12 concatMap + reduct

对于一个列表页，我们已经加载了 3 页，如果这时候我们去下拉刷新，一般的做法是重新加载第三页，但是更好的体验也许是自动拉去前 3 页，这是我们可以使用 concatMap + reduct 来实现。

```kotlin
    interface DataProvider<DataType> {
        fun getData(page: Int): Observable<GitHubPaging<DataType>>
    }

    fun loadFromFirst(pageCount: Int = currentPage) =
            //加载三次
            Observable.range(1, pageCount)
                    //同时去加载，但是按顺序返回
                    .concatMap {
                        getData(it)
                    }
                    .doOnError {
                        logger.error("loadFromFirst, pageCount=$pageCount", it)
                    }
                    //叠加到一个容器中
                    .reduce { acc, page ->
                        acc.mergeData(page)
                    }
                    .doOnNext {
                        //定义一个data来处理这些数据
                        data.clear()
                        data.mergeData(it)
                    }
```






