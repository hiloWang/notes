# 1 RxJava 概念全⾯介绍

---
## 1.1 Rx 的含义

Rx 即 Reactiva Extension

- Reactiva：即 Reactive Programming，是相对于观察者模式的扩展
- Extension

### Reactive Programming vs 观察者模式

观察者模式：观察者对被观察者监听，当有变化时⽴立即通知

![](index_files/zhihu-01)

Reactive Programming：当数据发⽣生变化时，所有对它有依赖的位置⽴立即相应地发⽣生反应

![](index_files/zhihu-02)

观察者模式关于点在于通知，而Reactive Programming的关注点在于对统计做出响应

### Extension

Extension 即扩展，在Reactive Programming的基础上进一步拓展：

- 不不仅⽀支持事件序列列，还⽀支持数据流
- 通过操作符对事件对象进⾏行行中间处理理
- 便便捷的线程操作

事件序列列 / 数据流的区别:

- 事件：不不可预知、动态的 -> 离散
  - ⽤用户点击
  - 服务器器推送
  - HTTP / HTTPS ⽹网络请求
- 数据流：现成的、静态的 -> 连续
  - 处理理静态数据（字符串串、Bitmap）
  - 逐⾏行行读取本地⽂文件

```java
//事件
 RxView.clicks(view)
       .subscribe(new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) {
                // 处理理点击事件......
            }
});

//数据流
Bitmap[] bitmaps = ...;
Observable.fromArray(bitmaps)
.subscribe(new Consumer<Bitmap>() {
    @Override
    public void accept(Bitmap bitmap) {
        // 加⽔水印
        addWatermark(bitmap);
    }
});
```

## 1.2 操作符

对事件对象进⾏行行中间处理理，优势：

- 将操作拆散，简化代码逻辑 -> 代码对复杂逻辑的承受能⼒力力增强
- 把耗时操作放在后台线程，不不卡界⾯面

## 1.3 线程操作

- subscribeOn()
- observeOn()

## 1.4 Backpressure

- 听不不懂的含义：上游⽣生产速度⼤大于下游处理理速度，导致下游处理理不不及的状况，被称为 Backpressure <- 来⾃自⼯工程概念 back pressure（背压）

对于 Backpressure：

- 问题 1：听起来好像很常⻅见？ -> 并不不常⻅见，确切说是⾮非常罕⻅见
- 问题 2：如果遇到该怎么办呢？ -> 丢弃
- 根本问题：什什么时候⽤用 Observable，什什么时候⽤用 Flowable？

听得懂的含义：对于可丢弃的事件，上游⽣生产过快导致事件堆积，当堆积到超出 buffer 上限，就叫做 Backpressure 出现。

处理理⽅方案：

- 1.丢弃新事件（具体的丢弃⽅方案可能有细分，但原则是丢弃）
- 2.不不丢弃，继续堆积（忽略 Backpressure，等价于⽤用 Observable，不推荐）

适合⽀支持 Backpressure 的情况：

- 在线直播视频流
- Log
- ⽤用户请求数超限丢弃（服务端）
- 关键点：事件的产⽣生不不可控、可丢弃

## 1.5 编程范式概念

- Reactive Programming？-> 数据的改变会⽴立即传达到依赖它的位置
- Functional Programming？-> ⼀一种⽤用于计算的编程范式，RxJava中有体现
- Functional Reactive Programming？-> 与 RxJava 没有关联

---
# 2 Rx 在 Zhihu 的历史

## 2.1 避免 Undeliverable Exception

```java
//没有错误处理者，会抛出Undeliverable Exception
Observable.create(emitter -> {
try {
    doSomething();
} catch (Exception e) {
    emitter.onError(e);
}})
.subscribe(rlt -> {        // ...    });

//订阅结束后发射error会抛出Undeliverable Exception，使用tryOnError避免Undeliverable Exception(内部判断订阅是否已经结束)
Observable.create(emitter -> {
try {
    doSomething();
} catch (Exception e) {
    if (!emitter.isDisposed()) {
        emitter.onError(e);
    }
    // emitter.tryOnError(e);
}})
.subscribe(
    rlt -> {
        // ...
    },
    throwable -> {
        // 处理错误
    }
);
```

## 2.2 关于 Dispose

- dispose() 方法和 Thread.interrupt() 方法很类似
  - 只起到通知作用
  - 已经被 dispose() 的流，不能再次被 dispose()
  - 异步任务内，应当通过 isDisposed() 方法判断是否要提前终止任务
- Create 操作符创建的异步任务，在被 dispose() 时，其实内部调用了 Thread.interrupt()
- 通过 isDisposed() 判断是否应该提前结束任务，从而节省 CPU 计算资源