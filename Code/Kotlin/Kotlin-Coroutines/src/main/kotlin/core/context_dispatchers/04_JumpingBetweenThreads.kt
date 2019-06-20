package core.context_dispatchers

import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

private fun log(msg: String) = println("[${Thread.currentThread().name}] $msg")

/*
这个示例演示了一些新技术。其中一个使用 runBlocking 来显式指定了一个上下文，并且另一个使用 withContext 函数来改变协程的上下文，而仍然驻留在相同的协程中。

JVM启动参数：-Dkotlinx.coroutines.debug

输出如下：
    [Ctx1 @coroutine#1] Started in ctx1
    [Ctx2 @coroutine#1] Working in ctx2
    [Ctx1 @coroutine#1] Back to ctx1


关于 use 函数：当我们不再需要某个在 newSingleThreadContext 中创建的线程的时候， 它使用了 Kotlin 标准库中的 use 函数来自动释放该线程。
 */
fun main() {
//sampleStart
    newSingleThreadContext("Ctx1").use { ctx1 ->
        newSingleThreadContext("Ctx2").use { ctx2 ->
            //runBlocking 显式指定了一个上下文
            runBlocking(ctx1) {
                log("Started in ctx1")
                //withContext 改变协程的上下文
                withContext(ctx2) {
                    log("Working in ctx2")
                }
                log("Back to ctx1")
            }
        }
    }
//sampleEnd
}