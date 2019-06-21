package core.concurrency

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.atomic.AtomicInteger
import kotlin.system.measureTimeMillis

/*
一种对线程、协程都有效的常规解决方法，就是使用线程安全（也称为同步的、 可线性化、原子）的数据结构，它为需要在共享状态上执行的相应操作提供所有必需的同步处理。
在简单的计数器场景中，我们可以使用具有 incrementAndGet 原子操作的 AtomicInteger 类：

这是针对此类特定问题的最快解决方案。它适用于普通计数器、集合、队列和其他标准数据结构以及它们的基本操作。
然而，它并不容易被扩展来应对复杂状态、或一些没有现成的线程安全实现的复杂操作。
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

private var counter = AtomicInteger()

fun main() = runBlocking<Unit> {
    //sampleStart
    GlobalScope.massiveRun {
        counter.incrementAndGet()
    }
    println("Counter = ${counter.get()}")
//sampleEnd
}