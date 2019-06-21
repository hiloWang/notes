package core.cancel_timeouts


import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout

//在实践中绝大多数取消一个协程的理由是它有可能超时。 当你手动追踪一个相关 Job 的引用并启动了一个单独的协程在延迟后取消追踪，
// 这里已经准备好使用 withTimeout 函数来做这件事。
fun main() = runBlocking {
    //withTimeout作用：Runs a given suspending block of code inside a coroutine with a specified timeout and throws
    // CancellationException if timeout was exceeded.
    //给定时间被协程没有执行完毕，将会抛出异常
    //sampleStart
    withTimeout(1300L) {
        repeat(1000) { i ->
            println("I'm sleeping $i ...")
            delay(500L)
        }
    }

    //由于抛出了异常，下面语句不会执行
    println("end")
//sampleEnd
}