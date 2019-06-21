package core.context_dispatchers

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

private fun log(msg: String) = println("[${Thread.currentThread().name}] $msg")
/*
协程日志会频繁记录的时候以及当你只是需要来自相同协程的关联日志记录， 自动分配 id 是非常棒的。
然而，当协程与执行一个明确的请求或与执行一些显式的后台任务有关的时候，出于调试的目的给它明确的命名是更好的做法。

以 -Dkotlinx.coroutines.debug 启动该程序
 */
fun main() = runBlocking(CoroutineName("main")) {
    //sampleStart
    log("Started main coroutine")
    // run two background value computations
    val v1 = async(CoroutineName("v1coroutine")) {
        delay(500)
        log("Computing v1")
        252
    }
    val v2 = async(CoroutineName("v2coroutine")) {
        delay(1000)
        log("Computing v2")
        6
    }
    log("The answer for v1 / v2 = ${v1.await() / v2.await()}")
//sampleEnd
}