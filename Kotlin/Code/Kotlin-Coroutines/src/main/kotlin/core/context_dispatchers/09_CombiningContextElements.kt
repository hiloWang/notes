package core.context_dispatchers


/*
上下文组合：有时候我们需要为一个协程上下文定义多个元素，我们可以使用 + 操作符来实现，举个例子，我们可以同时明确地指定启动协程的 Dispatcher 和 name.

以 -Dkotlinx.coroutines.debug 启动该程序
 */
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking<Unit> {
    //sampleStart
    launch(Dispatchers.Default + CoroutineName("test")) {
        println("I'm working in thread ${Thread.currentThread().name}")
    }
//sampleEnd
}