package core.cancel_timeouts

import kotlinx.coroutines.*

/*
在前一个例子中任何尝试在 finally 块中调用挂起函数的行为都会抛出 CancellationException，因为这里持续运行的代码是可以被取消的。
通常，这并不是一个问题，所有良好的关闭操作（关闭一个文件、取消一个任务、或是关闭任何一种通信通道）通常都是非阻塞的，并且不会调用任何挂起函数。
然而，在真实的案例中，当你需要挂起一个被取消的协程，你可以将相应的代码包装在 withContext(NonCancellable) {……} 中，
并使用 withContext 函数以及 NonCancellable 上下文，见如下示例所示：
 */
fun main() = runBlocking {
    //sampleStart
    val job = launch {
        try {
            repeat(1000) { i ->
                println("I'm sleeping $i ...")
                delay(500L)
            }
        } finally {
            //在罕见的情况下，需要在取消的协程中挂起，可以使用 withContext(NonCancellable) {...}，
            //run作用：Calls the specified suspending block with a given coroutine context, suspends until it completes, and returns the result.

            withContext(NonCancellable) {
                println(Thread.currentThread())
                println("I'm running finally")
                //如果不使用 withContext(NonCancellable) {}，则下面两行代码不会执行
                delay(1000L)
                println("And I've just delayed for 1 sec because I'm non-cancellable")
            }
        }
    }
    delay(1300L) // delay a bit
    println("main: I'm tired of waiting!")
    job.cancelAndJoin() // cancels the job and waits for its completion
    println("main: Now I can quit.")
//sampleEnd
}