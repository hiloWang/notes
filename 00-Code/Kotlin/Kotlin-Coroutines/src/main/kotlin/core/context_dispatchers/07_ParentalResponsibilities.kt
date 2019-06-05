package core.context_dispatchers

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/*
一个父协程总是等待所有的子协程执行结束。父协程并不显式的跟踪所有子协程的启动以及不必使用 Job.join 在最后的时候等待它们：
 */
fun main() = runBlocking<Unit> {
    //sampleStart
    // launch a coroutine to process some kind of incoming request
    val request = launch {
        repeat(3) { i -> // launch a few children jobs
            launch  {
                delay((i + 1) * 200L) // variable delay 200ms, 400ms, 600ms
                println("Coroutine $i is done")
            }
        }
        println("request: I'm done and I don't explicitly join my children that are still active")
    }
    //这里加上 join，那么父协程将会在此次等待，所有的字协程完成之后，才会接着执行 join  之后的代码
    request.join() // wait for completion of the request, including all its children，等待请求的完成，包括其所有子协程
    //而如果不使用 join，父协程将会执行完自己所有的代码后再等待所有的子协程
    println("Now processing of the request is complete")
//sampleEnd
}