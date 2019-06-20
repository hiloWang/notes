package core.exception_handing

import kotlinx.coroutines.*

/*
如果协程遇到除 CancellationException 以外的异常，它将取消具有该异常的父协程。 这种行为不能被覆盖，
且它被用来提供一个稳定的协程层次结构来进行结构化并发而无需依赖 CoroutineExceptionHandler 的实现。
且当所有的子协程被终止的时候，原本的异常被父协程所处理。

这也是为什么，在这个例子中，CoroutineExceptionHandler 总是被设置在由 GlobalScope 启动的协程中。
将异常处理者设置在 runBlocking 主作用域内启动的协程中是没有意义的，尽管子协程已经设置了异常处理者，
但是主协程也总是会被取消的（因为遇到除 CancellationException 以外的异常）。
 */
fun main() = runBlocking {
    //sampleStart
    val handler = CoroutineExceptionHandler { _, exception ->
        println("Caught $exception")
    }

    val job = GlobalScope.launch(handler) {
        launch {
            // the first child
            try {
                delay(Long.MAX_VALUE)
            } finally {
                withContext(NonCancellable) {
                    println("Children are cancelled, but exception is not handled until all children terminate")
                    delay(100)
                    println("The first child finished its non cancellable block")
                }
            }
        }

        launch {
            // the second child
            delay(10)
            println("Second child throws an exception")
            //将会导致父协程被取消
            throw ArithmeticException()
        }
    }

    job.join()
//sampleEnd
}
