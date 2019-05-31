# 响应式流规范

Reactive Streams 的目的是为具有非阻塞背压的异步流的处理提供标准

---
## 1 目标，设计和范围

处理数据流 - 尤其是体积未预定的“实时”数据 - 需要在异步系统中特别小心。最突出的问题是需要小心地控制资源消耗，以便 fast data source 不会压倒流目标，异步是为了实现计算资源的并行使用，使一台计算机的多个CPU或网络上的主机进行协作。

Reactive Streams的主要目标是管理`跨异步边界的流数据交换`，即将元素传递给另一个线程或线程池，同时确保接收方不会被迫缓冲任意数量的数据。换一种说法，背压是该模型的一个组成部分，以便允许在线程之间协作的队列有界。如果背压信号是同步的，那么异步处理的好处将被否定（参见[Reactive Manifesto](http://reactivemanifesto.org/)），因此，需要注意强制实施 Reactive Streams 实现的所有方面的完全非阻塞和异步行为。

本说明书的目的是允许创建许多符合要求的实现，由于遵守规则，它们将能够平滑地互操作，在流应用程序的整个处理图中保留上述优点和特征。

应当注意，本说明书未涵盖流操作（transformation, splitting, merging, 等）的精确性质，Reactive Streams 仅关注于调解不同 API 组件之间的数据流。在他们的开发中，已经注意确保可以表达所有组合流的基本方式。

总之，Reactive Streams 是JVM 的面向流的库的标准和规范：

- 处理潜在无限数量的元素
- 按顺序
- 在组件之间异步传递元素
- 具有强制性非阻塞背压


Reactive Streams规范包含以下部分：

- API指定实现Reactive Streams的类型，并实现不同实现之间的互操作性。
- 技术兼容性工具包（TCK）是用于实现一致性测试的标准测试套件。

实现可以自由地实现规范未涵盖的其他功能，只要它们符合API要求并通过TCK中的测试即可。

---
## 2 API 组件

API由Reactive Stream实现提供的以下组件组成：

1. Publisher 发布者
2. Subscriber 订阅则
3. Subscription 订阅
4. Processor 处理器

Publisher 是可能无限数量的有序元素的提供者，根据其订阅者的要求发布它们。响应对 `Publisher.subscribe(Subscriber)` 的调用，Subscriber 上的方法的可能调用序列由以下协议给出：

```java
onSubscribe onNext* (onError | onComplete)?
```

这意味着 onSubscribe 始终会发出信号，接下来是一个可能无限数量的 `onNext` 信号（Subscriber 所要求的），如果出现故障则接着是 `onError` 信号，当没有更多元素可用时，接下来是一个 `onComplete` 信号，只要订阅未被取消，就是这样的。

以下规范使用来自 `ietf.org/rfc/rfc2119.txt`

---
## 3 词汇表

属于 | 定义
--- | ---
Signal | 作为名词：onSubscribe，onNext，onComplete，onError，request（n）或cancel方法之一。<br/>作为动词： calling/invoking a signal.
Demand（需求） | 作为名词，Publisher 尚未交付（履行）Subscriber 请求的元素的聚合数量。<br/>作为动词：请求（request）更多元素的行为。
Synchronous(ly) | 在调用线程执行
Return normally（正常返回） | 只返回声明类型的值给调用者。向订阅服务器发出故障信号的唯一合法方法是通过onError 方法。
Responsivity（响应度） | 准备/响应能力。在本文件中用于表明不同组件不应影响彼此的响应能力。
Non-obstructing（非阻断） | 描述在调用线程上尽可能快速执行的方法的质量。这意味着，例如，避免繁重的计算和其他会阻碍调用者执行线程的事情。
Terminal state（终止状态） | 对于 Publisher：当发出 onComplete 或 onError 信号时。对于 Subscriber：当收到 onComplete 或 onError 时。
NOP | 对调用线程没有可检测影响的执行，并且可以安全地被调用任意次。
External synchronization（外部同步） | 在本规范中定义的构造之外实现的线程间安全协作，使用诸如但不限于atomics, monitors, 和 locks。
Thread-safe（线程安全） | 可以同步或异步安全地调用，无需外部同步以确保程序正确性。

---
## 4 规范

### Publisher

```java
public interface Publisher<T> {
    public void subscribe(Subscriber<? super T> s);
}
```

### Subscriber

```java
public interface Subscriber<T> {
    public void onSubscribe(Subscription s);
    public void onNext(T t);
    public void onError(Throwable t);
    public void onComplete();
}
```

### Subscription

```java
public interface Subscription {
    public void request(long n);
    public void cancel();
}
```

### Processor

```java
public interface Processor<T, R> extends Subscriber<T>, Publisher<R> {
}
```

---
## 5 异步与同步处理

- [ ] todo

---
## 6 Subscriber controlled queue bounds

- [ ] todo