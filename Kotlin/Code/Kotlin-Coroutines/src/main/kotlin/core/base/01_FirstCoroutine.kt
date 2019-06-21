package core.base

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/*
通过 GlobalScope 的 launch 启动协程：

    本质上，协程是轻量级的线程。 它们在某些 CoroutineScope 上下文中与 launch 协程构建器 一起启动。在这里，我们在 GlobalScope 中启动一个新的协同程序，
    这意味着新协程的生命周期仅受整个应用程序的生命周期的限制。

    您可以使用 `Thread.sleep（...`）替换 `GlobalScope.launch {...} ` 和`thread{...}`以及`delay(...）`。试试吧。

    如果你使用 thread 替换 GlobalScope.launch，编译器将生成以下错误：Error: Kotlin: Suspend functions are only allowed to be called from a coroutine or another suspend function

    这是因为 delay 是一个特殊的挂起函数，它不会阻塞一个线程，但会挂起协同程序，它只能在协程中使用。

API：

    launch作用：启动一个新的协程并返回一个代表协程引用的Job对象，当前线程不会被阻塞。launch 使用的协程上下文继承自 CoroutineScope，如果没有指定，
                           那么 Dispatchers.Default 将会被使用。

    Job 是协程创建的后台任务的概念，它持有该协程的引用。Job 接口实际上继承自 CoroutineContext 类型。一个 Job 有如下三种状态：新建、活动中、结束

    Dispatchers.Default：它是一个协程上下文，同时也是一个协程调度器，用于协程调度程序的计算密集型任务。此时协程将会在 Default 提供的线程池中运行。

    delay作用：在给定的时间内延迟协程，此时该协程将让出执行权，仅用于协程中使用
 */
fun main() {
    GlobalScope.launch {
        // launch new coroutine in background and continue
        delay(1000L) // non-blocking delay for 1 second (default time unit is ms)
        println("World!") // print after delay
        println("CommonPool thread:" + Thread.currentThread())//Thread[ForkJoinPool.commonPool-worker-1,5,main]
    }
    println("Hello,") // main thread continues while coroutine is delayed
    Thread.sleep(2000L) // block main thread for 2 seconds to keep JVM alive，因为协程类似守护线程，主线程退出协程就自动退出了
}

