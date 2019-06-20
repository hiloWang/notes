package com.bennyhuo.coroutines.sample

import com.bennyhuo.coroutines.utils.log
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking

fun main(args: Array<String>) = runBlocking {
    log(1)
    val job = launch {
        log(-1)
        delay(1000L)
        log(-2)
    }
    log(2)
    job.join()
    log(3)
}