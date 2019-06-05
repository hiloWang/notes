package core.exception_handing

import kotlinx.coroutines.*

/*
但是如果不想将所有的异常打印在控制台中呢？
    CoroutineExceptionHandler 上下文元素被用来将通用的 catch 代码块用于在协程中自定义日志记录或异常处理。 它和使用 Thread.uncaughtExceptionHandler 很相似。
 */
fun main() = runBlocking {
    //sampleStart
    val handler = CoroutineExceptionHandler { _, exception ->
        println("Caught $exception")
    }
    val job = GlobalScope.launch(handler) {
        throw AssertionError()
    }
    val deferred = GlobalScope.async(handler) {
        throw ArithmeticException() // Nothing will be printed, relying on user to call deferred.await()
    }
    joinAll(job, deferred)
//sampleEnd
}