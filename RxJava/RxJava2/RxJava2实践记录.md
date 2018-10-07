# RxJava 实践记录

---
## 避免使用 过度RxBus

为什么要避免使用 EventBus/RxBus，EventBus/RxBus 很容易被滥用，看似解耦了，但是增加了程序的不确定性，过多的事件订阅与传递容易造成程序的混乱。

- 使用 EventBus/RxBus 来初始化某些数据。这样数据的初始化是不确定的，容易造成 NPE
- 过多的EventBus的代码会造成代码结构混乱，难以测试和追踪

可以使用 RxBus，但是进建议在模块内使用，而且是经过包装的类型（比如 OrderEvemt），不要直接使用 RxBus 类型。

---
## 关于  subscribeActual

```
JakeWharton: 
subscribeActual can only be implemented and called by Observable subclasses. You should never need to refer to it externally.

Your best bet is to not try and replicate an event bus with Rx. It's a fundamentally different architecture over which events flow. Consumers of events should directly reference their event sources for subscribing and then use the returned subscription/disposable in order to unhook.

For general Rx usage questions please use the RxJava mailing list or StackOverflow and the 'rxjava' tag.
```
subscribeActual 只能由可观察的子类实现和调用。你不应该在外部引用它。最好的办法是不要尝试使用 Rx 复制事件总线。它是一个从根本上不同的架构和事件流。事件的使用者应该直接引用它们的事件源进行订阅, 然后使用返回的订阅/一次性来解除。


---
## 避免过过多的 operator

几乎所有的operator都会给你生成一个新的Observable。

---
## 创建异步的 Observable

类似 `Observable.just()` 等操作都是同步调用的，很明显，这不适合用来创建异步的Observable，可以使用 `defer()/fromCallable()` 操作符。

