package core.context_dispatchers


import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking


/*
协程可以在一个线程上挂起并恢复其它线程。 甚至一个单线程的调度器是非常难以弄清楚协程何时，在哪里，正在做什么的。
使用通常的方法来调试应用程序是让线程在每一个日志文件的日志声明中打印线程的名字。这种特性在日志框架中是普遍受支持的。
使用协程时，单独的线程名称不会给出很多上下文，所以 kotlinx.coroutines 包含了调试工具来让它更简单。

JVM启动参数：-Dkotlinx.coroutines.debug

打印结果：
    [main @coroutine#2] I'm computing a piece of the answer
    [main @coroutine#3] I'm computing another piece of the answer
    [main @coroutine#1] The answer is 42

这里有三个协程，runBlocking和两个async，它们都在 runBlocking 上下文中执行并且被限制在了主线程内。
 */
fun main() = runBlocking<Unit> {
    //sampleStart
    val a = async {
        log("I'm computing a piece of the answer")
        6
    }
    val b = async {
        log("I'm computing another piece of the answer")
        7
    }
    log("The answer is ${a.await() * b.await()}")
//sampleEnd
}

private fun log(msg: String) = println("[${Thread.currentThread().name}] $msg")
