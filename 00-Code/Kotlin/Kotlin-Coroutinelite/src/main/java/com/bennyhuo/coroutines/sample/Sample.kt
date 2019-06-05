package com.bennyhuo.coroutines.sample

import com.bennyhuo.coroutines.lite.delay
import com.bennyhuo.coroutines.lite.launch
import com.bennyhuo.coroutines.lite.runBlocking
import com.bennyhuo.coroutines.utils.log

/*
BlockingQueueDispatcher->dispatch->EventTask=Function0<kotlin.Unit> 471910020////startCoroutine
BlockingQueueDispatcher->dispatch->isCompleted = false
CommonPoolDispatcher-->block = Function0<kotlin.Unit> 1418481495

sample->join->com.bennyhuo.coroutines.lite.StandaloneCoroutine@87aac27//join 1

BlockingQueueDispatcher->dispatch->isCompleted = false
23:54:00:917 [CommonPool] -1
23:54:00:918 [CommonPool] -2
23:54:00:918 [CommonPool] -3
23:54:00:918 [CommonPool] -4
AbstractCoroutine com.bennyhuo.coroutines.lite.StandaloneCoroutine@87aac27 ->resume->value = kotlin.Unit//launch 执行完毕，使用runBlocking的Coroutine重新调度runBlocking

joinSuspend continuation = kotlin.coroutines.experimental.SafeContinuation@2a0b1bff

BlockingQueueDispatcher->dispatch->EventTask=Function0<kotlin.Unit> 120398637
BlockingQueueDispatcher->dispatch->get=Function0<kotlin.Unit> 120398637/执行拦截到的方法，执行完毕后( println("sample->end")段) BlockingCoroutine 状态为完成

AbstractCoroutine join ret=kotlin.Unit    //join 1 end

sample->end
AbstractCoroutine com.bennyhuo.coroutines.lite.BlockingCoroutine@119d7047 ->resume->value = kotlin.Unit
 */

fun main(args: Array<String>) = runBlocking {
   val join = launch {
        log(-1)
      /*  val result = async {
            log(1)
            delay(100)
            log(2)
            loadForResult().also {
                log(3)
            }
        }*/
        log(-2)
//        delay(200)
        log(-3)
//        log(result.await())
        log(-4)
    }
    println("sample->join->$join")
    join.join()
    println("sample->end")
}

suspend fun loadForResult(): String {
    delay(1000L)
    return "HelloWorld"
}