package core.cancel_timeouts


/*
在小应用程序中，从“main”方法返回可能像是一个好主意，可以让所有协程程序隐式终止。
但是一个长时间运行的程序，需要细粒度的控制协程，launch函数返回一个可用于取消正在运行的协程的作业
 */
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    //sampleStart
    val job = launch {
        repeat(1000) { i ->
            println("I'm sleeping $i ...")
            delay(500L)
        }
    }
    delay(1300L) // delay a bit
    println("main: I'm tired of waiting!")
    job.cancel() // cancels the job
    job.join() // waits for job's completion
    println("main: Now I can quit.")
//sampleEnd
}

/*
I'm sleeping 0 ...
I'm sleeping 1 ...
I'm sleeping 2 ...
main: I'm tired of waiting!
main: Now I can quit.
 */
