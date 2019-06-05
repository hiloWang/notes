package cn.kotliner.coroutine.async

import cn.kotliner.coroutine.common.HttpError
import cn.kotliner.coroutine.common.HttpException
import cn.kotliner.coroutine.common.HttpService
import cn.kotliner.coroutine.common.log
import cn.kotliner.coroutine.ui.LOGO_URL
import kotlin.coroutines.experimental.*

/**
 * Created by benny on 5/29/17.
 */
fun 我要开始协程啦(context: CoroutineContext = EmptyCoroutineContext, block: suspend () -> Unit) {
    block.startCoroutine(ContextContinuation( context + AsyncContext()))
}

suspend fun <T> 我要开始耗时操作了(block: CoroutineContext.() -> T)
        = suspendCoroutine<T> {
    continuation ->
    log("异步任务开始前")
    AsyncTask {
        try {
            continuation.resume(block(continuation.context))
        } catch(e: Exception) {
            continuation.resumeWithException(e)
        }
    }.execute()
}

fun 我要开始加载图片啦(url: String): ByteArray {
    log("异步任务开始前")
    log("耗时操作，下载图片")
    val responseBody = HttpService.service.getLogo(url).execute()
    if (responseBody.isSuccessful) {
        responseBody.body()?.byteStream()?.readBytes()?.let {
            return it
        }
        throw HttpException(HttpError.HTTP_ERROR_NO_DATA)
    } else {
        throw HttpException(responseBody.code())
    }
}