package core.concurrency

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

/*
在实践中，线程限制是在大段代码中执行的，例如：状态更新类业务逻辑中大部分都是限于单线程中。
下面的示例演示了这种情况， 在单线程上下文中运行每个协程。 这里我们使用 CoroutineScope() 函数来切换协程上下文为 CoroutineScope：
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

//构建一个单线程协程调度器
private val counterContext = newSingleThreadContext("CounterContext")
private var counter = 0

fun main() = runBlocking<Unit> {
    //sampleStart
    CoroutineScope(counterContext).massiveRun {
        // run each coroutine in the single-threaded context
        counter++
    }
    println("Counter = $counter")
//sampleEnd
}