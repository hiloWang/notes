package com.bennyhuo.coroutines.sample

import com.bennyhuo.coroutines.utils.log
import kotlinx.coroutines.experimental.*

private val threadPoolDispatcher = newSingleThreadContext("MyThreadPool")

fun main(args: Array<String>) = runBlocking(DefaultDispatcher) {
    log(-1)
    val job = launch(threadPoolDispatcher) {
        log(1)
        delay(1000L)
        log(2)
    }
    log(-2)
    job.join()
    log(-3)
}