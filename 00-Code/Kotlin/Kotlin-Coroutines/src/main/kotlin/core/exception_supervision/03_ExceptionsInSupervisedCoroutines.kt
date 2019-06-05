package core.exception_supervision


import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope

/*
常规的任务和监督任务之间的另一个重要区别是异常处理。 每一个子任务应该通过异常处理机制处理自身的异常。 这种差异来自于子任务的执行失败不会传播给它的父任务的事实。
 */
fun main() = runBlocking {

    val handler = CoroutineExceptionHandler { _, exception ->
        println("Caught $exception")
    }

    supervisorScope {
        val child = launch(handler) {
            println("Child throws an exception")
            throw AssertionError()
        }
        println("Scope is completing")
    }

    println("Scope is completed")
}