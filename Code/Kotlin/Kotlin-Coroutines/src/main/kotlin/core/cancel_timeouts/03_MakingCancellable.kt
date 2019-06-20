package core.cancel_timeouts


/*
有两种方法可以让计算代码变得可以被取消。
1. 定期调用挂起函数来检查取消，yield是一个不错的选择
2. 在协程内明确地检查自己的取消状态
 */
import kotlinx.coroutines.*

fun main() = runBlocking {
    //sampleStart
    val startTime = System.currentTimeMillis()
    val job = launch(Dispatchers.Default) {
        var nextPrintTime = startTime
        var i = 0
        while (isActive) { // cancellable computation loop
            // print a message twice a second
            if (System.currentTimeMillis() >= nextPrintTime) {
                println("I'm sleeping ${i++} ...")
                nextPrintTime += 500L
            }
        }
    }
    delay(1300L) // delay a bit
    println("main: I'm tired of waiting!")
    job.cancelAndJoin() // cancels the job and waits for its completion
    println("main: Now I can quit.")
//sampleEnd
}