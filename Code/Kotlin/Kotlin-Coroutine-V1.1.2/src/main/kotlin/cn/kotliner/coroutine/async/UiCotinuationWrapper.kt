package cn.kotliner.coroutine.async

import javax.swing.SwingUtilities
import kotlin.coroutines.experimental.Continuation
import kotlin.coroutines.experimental.CoroutineContext
import kotlin.coroutines.experimental.EmptyCoroutineContext

/**
 * Created by benny on 5/29/17.
 */
class UiCotinuationWrapper<T>(val continuation: Continuation<T>): Continuation<T>{
    override val context = continuation.context

    override fun resume(value: T) {
        SwingUtilities.invokeLater { continuation.resume(value) }
    }

    override fun resumeWithException(exception: Throwable) {
        SwingUtilities.invokeLater { continuation.resumeWithException(exception) }
    }

}
