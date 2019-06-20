package core.exception_handing

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield

/*
取消与异常紧密相关。协程内部使用 CancellationException 来进行取消，这个异常会被所有的处理者忽略，
所以那些可以被 catch 代码块捕获的异常仅仅应该被用来作为额外调试信息的资源。

当一个协程在没有任何理由的情况下使用 Job.cancel 取消的时候，它会被终止，但是它不会取消它的父协程。 无理由取消是父协程取消其子协程而非取消其自身的机制。
 */
fun main() = runBlocking {
    //sampleStart
    val job = launch {
        val child = launch {
            try {
                delay(Long.MAX_VALUE)
            } finally {
                println("Child is cancelled")
            }
        }

        yield()
        println("Cancelling child")
        child.cancel()
        child.join()
        yield()
        println("Parent is not cancelled")
    }

    job.join()
//sampleEnd
}