package core.exception_supervision

import kotlinx.coroutines.*

/*
正如我们之前研究的那样，取消是一种双向机制，在协程的整个层次结构之间传播。但是如果需要单向取消怎么办？

此类需求的一个良好示例是可以在其作用域内定义任务的的 UI 组件。如果任何一个 UI 的子任务执行失败了，
它并不总是有必要取消（有效地杀死）整个 UI 组件， 但是如果 UI 组件被销毁了（并且它的任务也被取消了），
由于它的结果不再被需要了，它有必要使所有的子任务执行失败。

另一个例子是服务进程孵化了一些子任务并且需要 监督 它们的执行，追踪它们的故障并在这些子任务执行失败的时候重启。

SupervisorJob 可以被用于这些目的。它类似于常规的 Job，唯一的取消异常将只会向下传播。这是非常容易从示例中观察到的：
 */
fun main() = runBlocking {
    val supervisor = SupervisorJob()

    //使用 SupervisorJob 的作用是，子协程抛出异常不会导致父协程和其他兄弟协程停止。
    with(CoroutineScope(coroutineContext + supervisor)) {

        // launch the first child -- its exception is ignored for this example (don't do this in practice!)
        val firstChild = launch(CoroutineExceptionHandler { _, _ -> }) {
            println("First child is failing")
            throw AssertionError("First child is cancelled")
        }

        // launch the second child
        val secondChild = launch {
            firstChild.join()
            // Cancellation of the first child is not propagated to the second child
            println("First child is cancelled: ${firstChild.isCancelled}, but second one is still active")
            try {
                delay(Long.MAX_VALUE)
            } finally {
                // But cancellation of the supervisor is propagated
                println("Second child is cancelled because supervisor is cancelled")
            }
        }

        // wait until the first child fails & completes
        firstChild.join()
        println("Cancelling supervisor")
        supervisor.cancel()
        secondChild.join()
    }
}