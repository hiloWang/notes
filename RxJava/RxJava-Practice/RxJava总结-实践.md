# RxJava总结-实践

## 响应式编程思维

响应式编程是一种面向**数据流**和**变化传播**的编程范式。

- 数据流：只能以事先规定好的顺序被读取一次的数据的一个序列，即编程中的任意的对象以一定顺序组成的一个序列
- 变化传播：类似观察者模式，变化了就需要通知观察者
- 编程范式：比如经典的 OOP、AOP，响应式编程也是一种范式

RxJava – Reactive Extensions for the JVM – a library for composing asynchronous and event-based programs using observable sequences for the Java VM.RxJava 是针对 JVM 的反应式扩展，它是 JVM 平台上使用的**可观察序列**，用于编写 **异步** 和 **基于事件** 的编程库。

---
## RxJava 核心元素

RxJava 1

- Observable 可观测的序列
- Observer 观察者
- Subscription 描述订阅关系
- OnSubscribe 订阅时要执行的行为，用于生产数据序列、触发事件
- Subscriber 订阅者

背压

- 异步环境下产生的问题
- 发送和处理的速度不统一，一般是生产者的速度快于消费者的速度
- 是一种流速控制的解决策略

RxJava 2 Observable

- Observable：被观察者，不支持背压
- Observer：观察者
- Disposable：和 RxJava1 中的 Subscription 类似，表示订阅关系，可以获取订阅状态和取消订阅
- Emitter：一个发射数据的接口，和 Observer 的方法类似
- ObservableOnSubscribe：当订阅时会触发此接口的调用，实际作用是向观察者发射数据

RxJava 2 Flowable（支持背压）

- Flowable：被观察者
- FlowableOnSubscribe：当订阅时会触发此接口的调用，在 Flowable 内部，实际作用是向观察者发射数据
- Subscriber：一个单独的接口，和Observer类似，用于订阅 Flowable
- Subscription：订阅，和 RxJava 的所有不同，有用于主动拉取的 request 方法（主动拉取来处理背压）
- Emitter：同上

其他数据源：

- ConnectableObservable：可连接的 Observable，一般的 Observable 都是单播的，而ConnectableObservable是多播的。
- Subject：订阅者和发布者的桥梁。
- Single：Single只会发射一个数据。
- Completable：Single 只会发射成功或者错误，不会发射任何数据。
- Processor：RxJava2 中添加了 Processor，订阅者和发布者的桥梁。
- Maybe：RxJava2 中添加的，可能发射一个数据也可能不发射数据。

---
## 避免过度使用 RxBus

为什么要避免使用 EventBus/RxBus，EventBus/RxBus 很容易被滥用，看似解耦了，但是增加了程序的不确定性，过多的事件订阅与传递容易造成程序的混乱。

- 使用 EventBus/RxBus 来初始化某些数据。这样数据的初始化是不确定的，容易造成 NPE
- 过多的EventBus的代码会造成代码结构混乱，难以测试和追踪

可以使用 RxBus，但是进建议在模块内使用，而且是经过包装的类型（比如 OrderEvemt），不要直接使用 RxBus 类型。

---
##  要点

1. 数据源有很多类型，在 Observable、Completable、Single、Maybe 中选择最合适的。
2. flatMap 嵌套异步的时候，注意处理好并发数量。
3. Subject 可以当作 Observable 和 Subscriber 之间的桥梁。
4. observable 与observer 之间的通信包括：`OnNext，OnCompleted，OnError，OnSubscribe`；observer 与 observable之间的通信包括：`Subscribe，Unsubscribe，Request`。
5. observable 可能会触发零次或多次 OnNext，每次发出一个 item，最终可能会有一次 OnCompleted 或 OnError，这两者不可能都被触发，这两者之一一旦触发，就再也不会有任何事件了。
6. Observable终止（释放资源）之前，必须向 observer 发出 `OnCompleted/OnError` 通知。
7. 当Observable发出 `OnCompleted/OnError`之后，subscription 就结束了，Observer 无需再 Unsubscribe。
8. Observer 发出 Unsubscribe 后，Observable 会尝试停止向 Observer 发出事件，但是并不保证。
9. 使用 observeOn 时，加上 delayError 能延迟错误的发射，参考[RxJava/issues/3857](https://github.com/ReactiveX/RxJava/issues/3857)，delayError 在其他一些操作符也有用到。
10. 处理好背压
11. 在 Observable 中开启新的线程进行操作，一旦操作不当很有可能造成内存泄漏哦。也就是说 **线程切换的事情交给RxJava去做。**
12. 避免过过多的 operator，几乎所有的 operator 都会给你生成一个新的 Observable。
13. 创建异步的 Observable，类似 `Observable.just()` 等操作都是同步调用的，很明显这不适合用来创建异步的 Observable，这时可以使用 `defer()/fromCallable()` 等操作符。

---
## 关于 RxJava2 的 subscribeActual

> JakeWharton:
> subscribeActual can only be implemented and called by Observable subclasses. You should never need to refer to it externally.
>
> Your best bet is to not try and replicate an event bus with Rx. It's a fundamentally different architecture over which events flow. Consumers of events should directly reference their event sources for subscribing and then use the returned subscription/disposable in order to unhook.
>
> For general Rx usage questions please use the RxJava mailing list or StackOverflow and the 'rxjava' tag.

subscribeActual 只能由可观察的子类实现和调用。你不应该在外部引用它。最好的办法是不要尝试使用 Rx 复制事件总线。它是一个从根本上不同的架构和事件流。事件的使用者应该直接引用它们的事件源进行订阅, 然后使用返回的订阅/一次性来解除。