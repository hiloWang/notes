package cn.kotliner.coroutine.async

import kotlin.coroutines.experimental.Continuation
import kotlin.coroutines.experimental.CoroutineContext
import kotlin.coroutines.experimental.EmptyCoroutineContext

/**
 * Created by benny on 5/29/17.
 */
class BaseContinuation: Continuation<Unit> {
    override val context: CoroutineContext = EmptyCoroutineContext

    override fun resume(value: Unit) {

    }

    override fun resumeWithException(exception: Throwable) {

    }

}