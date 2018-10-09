## 1 数据源

- **Observable**
默认的Observable
- **Single**
Single只会发射一个数据
- **Completable**
Completable只会发射成功或者错误，不会发射任何数据。
- **Subject**
订阅者和发布者的桥梁
- **ConnectableObservable**
可连接的Observable，一般的Observable都是单播的，而ConnectableObservable是多播的

## 2 四个部分

- 事件源：Observable
- 事件处理：Observer，Subscriber
- 中间变换：Operator
- 线程调度：Scheduler

## 3 一些总结

1. 数据源有很多类型，在Observable，Completable，Single中选择最合适的
2. flatMap嵌套异步的时候，注意处理好并发数量
3. Subject可以当作Observable和Subscriber之间的桥梁。
4. observable与observer之间的通信包括：`OnNext，OnCompleted，OnError，OnSubscribe`；observer与observable之间的通信包括：`Subscribe，Unsubscribe，Request`。
5. observable可能会触发零次或多次OnNext，每次发出一个item，最终可能会有一次OnCompleted或OnError，这两者不可能都被触发，这两者之一一旦触发，就再也不会有任何事件了；
6. Observable终止（释放资源）之前，必须向observer发出`OnCompleted/OnError`通知；
7. 当Observable发出`OnCompleted/OnError`之后，subscription就结束了，Observer无需再Unsubscribe；
8. Observer发出Unsubscribe后，Observable会尝试停止向Observer发出事件，但是并不保证！
9. 使用observeOn时，加上delayError能延迟错误的发射，参考[RxJava/issues/3857](https://github.com/ReactiveX/RxJava/issues/3857)
10. 处理好背压
11. 在Observable中开启新的线程进行操作，一旦操作不当很有可能造成内存泄漏哦。也就是说 **线程切换的事情交给RxJava去做。**