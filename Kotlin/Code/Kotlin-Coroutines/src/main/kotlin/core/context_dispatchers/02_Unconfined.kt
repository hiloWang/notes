package core.context_dispatchers

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/*
Unconfined 协程调度在调用者线程中启动协程，但直到第一个暂停点，暂停后，它运行在恢复它的线程中。Unconfined调度程序适用于不消耗CPU时间，
也不更新任何局限于特定线程的共享数据（如UI）。通过CoroutineScope接口在任何协程块中可以访问coroutineContext属性，
coroutineContext是对这个特定协程的上下文的引用，比如此处是runBlocking的默认上下文父上下文是可以被继承。

runBlocking 协程的默认调度器，被限制在调用它的线程，因此承袭它在限制有可预测的 FIFO 调度的线程的执行上是非常有效果的。

 */
fun main() = runBlocking<Unit> {
    //sampleStart
    launch(Dispatchers.Unconfined) { // not confined -- will work with main thread
        println("Unconfined      : I'm working in thread ${Thread.currentThread().name}")
        delay(500)
        println("Unconfined      : After delay in thread ${Thread.currentThread().name}")// DefaultExecutor，DefaultExecutor监视delay的时间，然后在规定的时间后恢复协程。
    }
    launch { // context of the parent, main runBlocking coroutine
        println("main runBlocking: I'm working in thread ${Thread.currentThread().name}")
        delay(1000)
        println("main runBlocking: After delay in thread ${Thread.currentThread().name}")
    }
//sampleEnd
}