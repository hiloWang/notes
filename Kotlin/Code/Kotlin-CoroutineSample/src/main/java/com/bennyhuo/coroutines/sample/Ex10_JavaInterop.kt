@file:JvmName("CoroutineInterop")

package com.bennyhuo.coroutines.sample

import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.future.future
import kotlinx.coroutines.experimental.runBlocking

private suspend fun loadAsync(): String {
    delay(1000L)
    return "Hello"
}

fun loadString() = runBlocking {
    async { loadAsync() }.await()
}

//需要 org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:0.22.5，Android 上面目前仍不方便使用
fun loadFuture() = future {
    async { loadAsync() }.await()
}