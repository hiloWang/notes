package core.exception_supervision


import kotlinx.coroutines.*

/*
对于作用域的并发，supervisorScope 可以被用来替代 coroutineScope 来实现相同的目的。它只会单向的传播并且当子任务自身执行失败的时候将它们全部取消。
它也会在所有的子任务执行结束前等待， 就像 coroutineScope 所做的那样。
 */
fun main() = runBlocking {

    try {
        supervisorScope {

            val child = launch {
                try {
                    println("Child is sleeping")
                    delay(Long.MAX_VALUE)
                } finally {
                    println("Child is cancelled")
                }
            }

            // Give our child a chance to execute and print using yield
            yield()
            println("Throwing exception from scope")
            throw AssertionError()
        }
    } catch (e: AssertionError) {
        println("Caught assertion error")
    }

    println("end...")

}