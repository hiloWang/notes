package com.bennyhuo.coroutines.sample

import com.bennyhuo.coroutines.utils.log
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.runBlocking

fun main(args: Array<String>) = runBlocking {
    log(-1)
    val result = async {
        log(1)
        loadForResult().also {
            log(2)
        }
    }
    log(-2)
    log(result.await())
    log(-3)
}

suspend fun loadForResult(): String {
    delay(1000L)
    return "HelloWorld"
}