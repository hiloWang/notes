# RxJava操作符——连接操作

可连接的Observable（`connectable Observable`）与普通的Observable差不多，不过它并不会在被订阅时开始发射数据，而是直到使用了`connect`操作符时才会开始。用这个方法，你可以等待所有的观察者都订阅了Observable之后再开始发射数据。**使用`connect`可以把`冷的Observable`变成`热的Observable`**。

**这里要区分`connectable Observable`与普通的`Observable`的区别：`connectable Observable`是支持多订阅的，多订阅是指在发射的同一个数据系列中多个订阅者都可以接受到发射的数据。
而普通的`Observable`每一次订阅都会产生一个新的数据序列，多个订阅者收到的数据是彼此独立没有关联的。**


## publish

publish可把一个普通的操作符变为一个`ConnectableObservable`，有一个变体接受一个函数作为参数。这个函数用原始Observable发射的数据作为参数，产生一个新的数据作为`ConnectableObservable`给发射，替换原位置的数据项。实质是在前面的基础上添加一个`Map`操作

## repaly

repaly也是把一个普通的操作符变为一个`ConnectableObservable`，并且保证所有的观察者收到相同的数据序列，即使它们在Observable开始发射数据之后才订阅，当一个后来的订阅者开始订阅这个正在发射数据的`ConnectableObservable`时，`ConnectableObservable`会一次性把之前的数据发射给这个后来的订阅者，然后再发射现在的数据。

## connect

调用`ConnectableObservable`的`connect`方法会让它后面的Observable开始给发射数据给订阅者。

`connect`方法返回一个`Subscription`对象，可以调用它的`unsubscribe`方法让Observable停止发射数据给观察者。

即使没有任何订阅者订阅它，你也可以使用`connect`方法让一个Observable开始发射数据（或者开始生成待发射的数据）。这样**你可以将一个"冷"的Observable变为"热"的**。

## refCount

refCount让一个可连接的Observable行为像普通的Observable

`refCount`操作符把从一个可连接的Observable连接和断开的过程自动化了。它操作一个可连接的Observable，返回一个普通的Observable。当第一个订阅者订阅这个Observable时，`refCount`连接到下层的可连接Observable。`refCount`跟踪有多少个观察者订阅它，直到最后一个观察者完成才断开与下层可连接Observable的连接。

## share

`share == publish()+refCount()`


















