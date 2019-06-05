package com.bennyhuo.coroutines.sample

import com.bennyhuo.coroutines.utils.log
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch

fun main(args: Array<String>) {
    log(1)
    launch {
        log(-1)
        delay(1000L)
        log(-2)
    }
    log(2)
    Thread.sleep(5000L)
    log(3)
}