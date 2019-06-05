package core.base

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/*

 */
fun main() = runBlocking {
    //sampleStart
    val job = GlobalScope.launch { // launch new coroutine and keep a reference to its Job
        delay(1000L)
        println("World!")
    }
    println("Hello,")
    //join作用：Suspends coroutine until this job is complete.此时当前协程会等到之前启动的协程运行完毕
    job.join() // wait until child coroutine completes
    println("ending--")
//sampleEnd
}