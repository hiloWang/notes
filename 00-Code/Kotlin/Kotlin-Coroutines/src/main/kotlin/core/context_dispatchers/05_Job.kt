package core.context_dispatchers


/*
1. Job是其context的一部分。协程可以使用context[Job]表达式获取job

2. When context of a coroutine is used to launch another coroutine,
the Job of the new coroutine becomes a child of the parent coroutine's job.
When the parent coroutine is cancelled, all its children are recursively cancelled, too.

协程的 Job 是它上下文中的一部分。协程可以在它所属的上下文中使用 coroutineContext[Job] 表达式来取回它。

CoroutineScope 中的 isActive 只是 coroutineContext[Job]?.isActive == true 的一种方便的快捷方式。
 */
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.runBlocking

fun main() = runBlocking<Unit> {
    //sampleStart
    println("My job is ${coroutineContext[Job]}, isActive = $isActive")
//sampleEnd
}