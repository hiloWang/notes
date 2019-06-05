package core.concurrency

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

/*
协程可用多线程调度器（比如默认的 Dispatchers.Default）并发执行。这样就可以提出所有常见的并发问题。主要的问题是同步访问共享的可变状态。
协程领域对这个问题的一些解决方案类似于多线程领域中的解决方案， 但其它解决方案则是独一无二的。

常见的并发问题如下，不安全的累加一个数，得不到预期的结果
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

private var counter = 0

fun main() = runBlocking<Unit> {
    //sampleStart
    GlobalScope.massiveRun {
        counter++
    }
    println("Counter = $counter")
//sampleEnd
}