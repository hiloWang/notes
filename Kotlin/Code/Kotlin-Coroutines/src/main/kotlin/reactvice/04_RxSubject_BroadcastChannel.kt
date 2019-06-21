package reactvice

import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.rx2.consumeEach

//RxJava 有个概念叫做 Subject，它会向它所有的订阅者广播数据。而协程里相对应的概念叫做 BroadcastChannel。
// Rx 中有着各种各样的 Subject，其中 BehaviorSubject 是用来管理状态的：
fun main1() {
    val subject = BehaviorSubject.create<String>()
    subject.onNext("one")
    subject.onNext("two") // updates the state of BehaviorSubject, "one" value is lost
    // now subscribe to this subject and print everything
    subject.subscribe(System.out::println)
    subject.onNext("three")
    subject.onNext("four")
}

//你也可以在协程中订阅 subject，就像其他任意一个 reactive stream 一样。
//在这里，我们使用 Unconfined 上下文，通过它启动的订阅协程表现方式与 Rx 中的订阅一样。这也意味着启动的这个协程也会在同样的线程中立即执行。
@ExperimentalCoroutinesApi
fun main2() = runBlocking<Unit> {
    val subject = BehaviorSubject.create<String>()
    subject.onNext("one")
    subject.onNext("two")
    // now launch a coroutine to print everything
    GlobalScope.launch(Dispatchers.Unconfined) { // launch coroutine in unconfined context
        subject.consumeEach { println(it) }
    }
    subject.onNext("three")
    subject.onNext("four")
}

//协程的优势在于很容易合并 UI 线程的更新操作。一个典型的 UI 程序不需要响应每一个状态变化，只有最近的状态才是有效的。
// 一旦 UI 线程空闲，一连串的状态更新只需要反映到 UI 上一次。下面的例子中，我们会使用主线程的上下文启动消费者协程来模拟这种情况，
// 可以使用 yield 函数来模拟一系列更新的中断，释放主线程：
fun main3() = runBlocking<Unit> {
    val subject = BehaviorSubject.create<String>()
    subject.onNext("one")
    subject.onNext("two")
    // now launch a coroutine to print the most recent update
    launch { // use the context of the main thread for a coroutine
        subject.consumeEach { println(it) }
    }
    subject.onNext("three")
    subject.onNext("four")
    yield() // yield the main thread to the launched coroutine <--- HERE
    subject.onComplete() // now complete subject's sequence to cancel consumer, too
}

//如果纯用协程来，对应的实现是 ConflatedBroadcastChannel，它通过协程的 channel 直接提供了相同的逻辑，不再需要 reactive steam 桥接。
//BroadcastChannel 的另一个实现是 ArrayBroadcastChannel ，只要相关的订阅打开着，他就会把每一个事件传递给每一个订阅者。他与 Rx 中的 PublishSubject 类似。
// ArrayBroadcastChannel 构造函数的 capacity 参数指定了缓冲区的大小，这个缓冲区放置的是生产者等待接收者接收时可以先发送的数据个数。
@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
fun main() = runBlocking<Unit> {
    val broadcast = ConflatedBroadcastChannel<String>()
    broadcast.offer("one")
    broadcast.offer("two")
    // now launch a coroutine to print the most recent update
    launch { // use the context of the main thread for a coroutine
        broadcast.consumeEach { println(it) }
    }
    broadcast.offer("three")
    broadcast.offer("four")
    yield() // yield the main thread to the launched coroutine
    broadcast.close() // now close broadcast channel to cancel consumer, too
}