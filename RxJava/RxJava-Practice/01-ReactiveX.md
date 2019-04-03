# Reactive X

---
## 1 Rx 简介

### RX 历史

1. 微软响应式扩展：ReactiveX 是 Reactive Extensions 的缩写，一般简写为 Rx，最初是 LINQ(Language Integrated Query) 的一个扩展，由微软的架构师 Erik Meijer 领导的团队开发，在 2012 年 11 月开源，Rx 是一个编程模型，目标是 **提供一致的编程接口**，帮助开发者更方便地 **处理异步数据流**，社区网站是 `reactivex.io`。
2. Netflix RxJava：Netflix 在 2012 年开始意识到他们的架构要满足他们庞大的用户群体已经变得步履维艰。因此他们决定重新设计架构来减少 REST 调用的次数。为了实现这一目标，他们决定尝试响应式，开始将 `.NET Rx` 迁移到 JVM 上面。2013 年 2月，Ben Christensen 和 Jafar Husain 发在 Netflix 技术博客的一篇文章第一次向世界展示了 RxJava。

Rx 模式支持响应式数据序列处理，主要的设计要点有：

  - 使用回调链分离时间/延迟：仅当数据可用时才会回调。
  - 分离线程模型：用 Observable / Stream 来处理同步或异步。
  - 控制错误链/终止：数据载荷信号以及错误与完成信号都传递给回调链。
  - 解决各种预定义 API 中多重分散-聚合和构造问题。

参考：

- [RX-从.NET到RxJava](https://rxjava.yuxingxin.com/chapter1/chapter1.html)
- [ReactiveX的历史](https://mcxiaoke.gitbooks.io/rxdocs/content/Intro.html)

### ReactiveX 定义

`reactivex.io`定义：ReactiveX 是一个使用 **可观察数据流** 进行 **异步编程** 的编程接口。ReactiveX 结合了 Observer模式、Iterator模式和函数式编程。ReactiveX 不仅仅是一个编程接口，它是一种编程思想的突破，它影响了许多其它的程序库和框架以及编程语言。

---
## 2 RxJava

RxJava 官方介绍：Reactive Extensions for the JVM , a library for composing asynchronous and event-based programs using observable sequences for the Java VM"，既 JVM 上的响应式扩展，一个在 Java VM 上使用`可观测的序列`来编写`异步的`、`基于事件`的程序的库。

### 观察者模式

观察者模式属于对象行为模式之一，也可叫做发布——订阅模式。它定义了一种以对多的依赖关系，让多个观察者（订阅者）同时观察（监听）一个被观察者（主题），当被观察者的状态发生变化时，会通知所有的观察者对象。

与传统的观察者模式不同的是，RxJava中的普通的Observable与Subscriber之间是一对一的订阅关系，也就是说，一般情况下多个Subscriber之间的数据是彼此独立的。而在多播的Observable中是一对多的关系。

在实际使用中，数据的传递一般分为两种方式：推模型和拉模型。

- **推模型**：目标对象向观察者推送目标的详细信息，不管观察者是否需要，推送的信息通常是目标对象的全部或者部分数据，相当于是在广播通信
- **拉模型**：目标对象在通知观察者的时候，只传递少量的数据信息，如果观察者需要更加具体的信息，由观察者主动到目标对象中获取，相当于是观察者从目标对象中拉取数据，一般这种模型的实现中，会把目标对象自身通过 update 方法传递给观察者，这样在观察者需要获取数据的时候，就可以通过这个引用来获取了。

与迭代器模式不同的地方在于，迭代器模式在事件处理上采用的是 “同步/拉式” 的方式，而 Observable 采用的是 “异步/推式” 的方式 ，对于 Subscriber(观察者)而言，这种方式会更加灵活。RxJava 使用推模型，当订阅发生后，Observable 主动把具体的数据发射给 Subscriber。

### 我的理解

ReactiveX 结合了 Observer模式、Iterator模式和函数式编程。

- 观察者模式：RxJava 扩展了观察者模式，通过使用可观察的对象序列流来表述一系列事件，订阅者进行观察并对序列流做出反应（或持久化或输出显示等等）。
- 迭代器模式：借鉴迭代器模式，对多个对象序列进行迭代输出，订阅者可以依次处理不同的对象序列。
- 事件流与函数式编程：事件从上下流动到下游，在这个过程中，我们使用 RxJava 定义的各种操作符（以函数式的方式）对事件进行变换、过滤、组合等操作。以把事件转换为我们想要的状态。
- Observable 与响应关系：它是一切事件源的抽象，通过订阅它，形成了下游观察者对上游事件序列变化的响应关系。这种响应式区别于传统直观的命令式、过程式风格。
- 统一的编程接口与异常处理机制：Observable 是所有事件的源，Subscriber（Observer）是最终的事件接收者，对象序列流从 Observable 发出，经过各种变化操作，把最终的结果推送到 Subscriber（事件序列流要么成功要么失败，结果总是会通知到 Subscriber）。就这么简单，基本上我们不需要为特定的场景定义特定的接口了。
- 多个事件源的组合：Observable 非常灵活，利用其提供的操作符可以轻易实现和其他 Observable 的复杂交换，比如串行、并行、相互等待。这比传统的基于异步回调的方式要强大太多。

RxJava 是 JVM 上响应式扩展的标准实现 。它提供了强大的函数式 API，同意了异步编程接口，响应式扩展是一种编程范式，编写代码时可以使用这种范式，也可以不使用，所以 RxJava 并不针对具体的场景，它有很多特点，比如 解决 callbackhell、优雅的线程切换、强大的操作符、链式编程等等，但这些都不是它的核心，也不是重点，最重要的是我们如何理解和掌握这种编程思想，在实际的业务场景中利用 RxJava 提供的 API 来简化我们遇到的问题。

---
## 3 还是不懂？

推荐阅读：

- [重新理解响应式编程](https://www.jianshu.com/p/c95e29854cb1)

这到底是什么系列

- 1 [观察者模式 – 响应式编程 [Android RxJava2]（这到底是什么）：第一部分](https://gold.xitu.io/entry/58ada9738fd9c5006704f5a1?utm_source=gold-miner&utm_medium=readme&utm_campaign=github) ([Zhiwei Yu（Zhiw）](https://github.com/Zhiw) 翻译)
- 2 [拉模式和推模式，命令式和响应式 – 响应式编程 [Android RxJava2]（这到底是什么）：第二部分](https://juejin.im/entry/58d78547a22b9d006465ca57/?utm_source=gold-miner&utm_medium=readme&utm_campaign=github)
- 3 [函数式接口、默认方法、纯函数、函数的副作用、高阶函数、可变的和不可变的、函数式编程和 Lambda 表达式 - 响应式编程 ［Android RxJava 2］（这到底是什么）第三部分](https://juejin.im/entry/591298eea0bb9f0058b35c7f/?utm_source=gold-miner&utm_medium=readme&utm_campaign=github)  ([XHShirley](https://github.com/XHShirley) 翻译)
- 4 [War against Learning Curve of Rx Java 2 + Java 8 Stream [ Android RxJava2 ] ( What the hell is this ) Part4](http://www.uwanttolearn.com/android/war-learning-curve-rx-java-2-java-8-stream-android-rxjava2-hell-part4/)
- 5 [开发者（也就是我）与Rx Observable 类的对话 [ Android RxJava2 ] ( 这到底是什么？) 第五部分](https://juejin.im/post/590ab4f7128fe10058f35119/?utm_source=gold-miner&utm_medium=readme&utm_campaign=github) ([stormrabbit](https://github.com/stormrabbit) 翻译)
- 6 [Continuation (Summer vs Winter Observable) of Dialogue between Rx Observable and a Developer (Me) [ Android RxJava2 ] ( What the hell is this ) Part6](http://www.uwanttolearn.com/android/continuation-summer-vs-winter-observable-dialogue-rx-observable-developer-android-rxjava2-hell-part6/)
- 7 [Continuation (Observable Marriage Proposal to Observer) of Dialogue between Rx Observable and a Developer (Me) [ Android RxJava2 ] ( What the hell is this ) Part7 ](http://www.uwanttolearn.com/android/continuation-observable-marriage-proposal-observer-dialogue-rx-observable-developer-android-rxjava2-hell-part7/)
- 8 [Confusion between Subject and Observable + Observer [ Android RxJava2 ] ( What the hell is this ) Part8](http://www.uwanttolearn.com/android/confusion-subject-observable-observer-android-rxjava2-hell-part8/)