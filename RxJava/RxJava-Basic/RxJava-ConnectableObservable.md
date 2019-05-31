# 可连接的 Observable

可连接的 Observable（`connectable Observable`）与普通的 Observable 差不多，不过它并不会在被订阅时开始发射数据，而是直到使用了`connect`操作符时才会开始。用这个方法，你可以等待所有的观察者都订阅了 Observable 之后再开始发射数据。**使用`connect`可以把`冷的Observable`变成`热的Observable`**。

## `connectable Observable`与普通的 `Observable` 的区别：

`connectable Observable`是支持多订阅的，多订阅是指在发射的同一个数据系列中多个订阅者都可以接受到发射的数据。而普通的`Observable`每一次订阅都会产生一个新的数据序列，多个订阅者收到的数据是彼此独立没有关联的。

## 相关操作符

- ConnectableObservable.connect()
- Observable.publish()
- Observable.replay()
- ConnectableObservable.refCount()