package core.exception_handing

import kotlinx.coroutines.*
import java.io.IOException

/*
如果一个协程的多个子协程抛出异常将会发生什么？ 通常的规则是 “第一个异常赢得了胜利”，所以第一个被抛出的异常将会暴露给处理者。
但也许这会是异常丢失的原因，比如说一个协程在 finally 块中抛出了一个异常。 这时，多余的异常将会被压制。

其中一个解决方法是分别抛出异常， 但是接下来 Deferred.await 应该有相同的机制来避免行为不一致并且会导致协程的实现细节（是否已将其部分工作委托给子协程） 泄漏到异常处理者中。
注意，这个机制当前只能在 Java 1.7 以上的版本中使用。 在 JS 和原生环境下暂时会受到限制，但将来会被修复。

 */
fun main() = runBlocking {
    val handler = CoroutineExceptionHandler { _, exception ->
        println("Caught $exception with suppressed ${exception.suppressed?.contentToString()}")
    }

    val job = GlobalScope.launch(handler) {
        launch {
            try {
                delay(Long.MAX_VALUE)
            } finally {
                throw ArithmeticException()
            }
        }
        launch {
            delay(100)
            throw IOException()
        }
        delay(Long.MAX_VALUE)
    }
    job.join()
}