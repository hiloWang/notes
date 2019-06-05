package com.bennyhuo.experimental.coroutines

import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.CoroutineStart.DEFAULT
import kotlinx.coroutines.experimental.android.UI

fun launchUI(start: CoroutineStart = DEFAULT, parent: Job? = null, block: suspend CoroutineScope.()->Unit)
        = launch(UI,  start, parent, block)

suspend fun <T> Deferred<T>.awaitOrError(): Result<T>{
    return try {
        Result.of(await())
    }catch(e: Exception){
        Result.of(e)
    }
}