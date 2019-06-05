package core.context_dispatchers

import kotlinx.coroutines.*

val threadLocal = ThreadLocal<String?>() // declare thread-local variable

/*
有时能够传递一些线程局部的数据很方便，但是，对于协程来说，它们不受任何特定线程的约束，所以很难手动的去实现它并且不写出大量的样板代码。

ThreadLocal 的  asContextElement 扩展函数在这里会充当救兵。它创建了额外的上下文元素， 且保留给定 ThreadLocal 的值，并在每次协程切换其上下文时恢复它。
 */
fun main() = runBlocking<Unit> {
    //sampleStart
    threadLocal.set("main")
    println("Pre-main, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
    val job = launch(Dispatchers.Default + threadLocal.asContextElement(value = "launch")) {
        println("Launch start, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
        yield()
        println("After yield, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
    }
    job.join()
    println("Post-main, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
//sampleEnd
}