package core.concurrency

import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

/*
限制线程 是解决共享可变状态问题的一种方案：对特定共享状态的所有访问权都限制在单个线程中。
它通常应用于 UI 程序中：所有 UI 状态都局限于单个事件分发线程或应用主线程中。这在协程中很容易实现，通过使用一个单线程上下文：

但是这段代码运行非常缓慢，因为它进行了 细粒度 的线程限制。每个增量操作都得使用 withContext 块从多线程 Dispatchers.Default 上下文切换到单线程上下文。
 */
private suspend fun CoroutineScope.massiveRun(action: suspend () -> Unit) {
    val n = 100  // number of coroutines to launch
    val k = 1000 // times an action is repeated by each coroutine
    val time = measureTimeMillis {
        val jobs = List(n) {
            launch {
                repeat(k) { action() }
            }
        }
        jobs.forEach { it.join() }
    }
    println("Completed ${n * k} actions in $time ms")
}

private val counterContext = newSingleThreadContext("CounterContext")
private var counter = 0

fun main() = runBlocking<Unit> {
    //sampleStart
    GlobalScope.massiveRun {
        // 使用 DefaultDispatcher 运行每个协程
        // run each coroutine with DefaultDispatcher
        withContext(counterContext) {
            // 但是把每个递增操作都限制在此单线程上下文中
            // but confine each increment to the single-threaded context
            counter++
        }
    }
    println("Counter = $counter")
//sampleEnd
}