package reactvice

import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.consumeEach
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit

/*
大多数的操作符都没有指定的线程（scheduler），它们一般在调用它们的线程工作。在 Rx 的线程 部分的例子中，通过 subscribe 操作符足见这点。
 */
private fun rangeWithIntervalRx(scheduler: Scheduler, time: Long, start: Int, count: Int): Flowable<Int> =
        Flowable.zip(
                Flowable.range(start, count),
                Flowable.interval(time, TimeUnit.MILLISECONDS, scheduler),
                BiFunction { x, _ -> x })

/*
现在的运行结果显示，它在 Rx 的计算线程池中执行，我们最开始用 Rx 的 subscribe 操作符那样：

注意，Unconfined 上下文要谨慎使用。虽然因为它局部栈容量的增加和线程调度的减少，在某些测试用例中可以提高整体性能，但是这在另一方面也产生了更深的栈，使得我们难以推断出代码的异步性。

如果一个协程向一个 channel 发送了一个数据，那么调用 send 的线程就会以 Unconfined 调度器开启协程来执行这段代码，
而原来调用 send 的协程就会挂起，直到 unconfined 的消费者协程遇到了下一个挂起点。
这和缺乏线程切换操作符的 Rx 里，执行在单线程的、每一步都加锁的 onNext 的执行很像。
这在 Rx 里是一种很常见的默认操作，因为操作符通常只做一些细小的工作，然后通过连接许多操作符来完成复杂的过程。
然而，这在协程中不是很通用，因为在一个协程中你可以完成任意复杂的操作。通常，你只有通过扇入扇出处理多个工作协程之间的复杂管道时才需要链接流。
 */
@ExperimentalCoroutinesApi
fun main() = runBlocking<Unit> {
    val job = launch(Dispatchers.Unconfined) { // launch new coroutine in Unconfined context (without its own thread pool)
        rangeWithIntervalRx(Schedulers.computation(), 100, 1, 3)
                .consumeEach { println("$it on thread ${Thread.currentThread().name}") }
    }
    job.join() // wait for our coroutine to complete
}