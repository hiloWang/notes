# [精通高级RxJava 2响应式编程思想](https://coding.imooc.com/class/158.html)

---
## 1 响应式编程思维

响应式编程是一种面向**数据流**和**变化传播**的编程范式。

- 数据流：只能以事先规定好的顺序被读取一次的数据的一个序列，即编程中的任意的对象以一定顺序组成的一个序列
- 变化传播：类似观察者模式，变化了就需要通知观察者
- 编程范式：比如经典的 OOP、AOP，响应式编程也是一种范式

---
## 2 RxJava 介绍

RxJava – Reactive Extensions for the JVM – a library for composing asynchronous and event-based programs using observable sequences for the Java VM.RxJava 是针对 JVM 的反应式扩展，它是 JVM 平台上使用的**可观察序列**，用于编写**异步**和**基于事件**的编程库。

关键词：

- 异步的：已简单的方式编写异步程序
- 回调：有异步就有回调
- 基于事件驱动：可观察的序列，当序列发生变化就会产生事件

---
## 3 RxJava 核心元素

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
- Subscription：订阅，和RxJava的所有不同，有用于主动拉取的 request 方法（主动拉取来处理背压）
- Emitter：同上
