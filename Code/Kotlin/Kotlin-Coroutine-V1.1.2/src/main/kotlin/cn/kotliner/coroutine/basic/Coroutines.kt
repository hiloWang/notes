package cn.kotliner.coroutine.basic

import cn.kotliner.coroutine.common.HttpError
import cn.kotliner.coroutine.common.HttpException
import cn.kotliner.coroutine.common.HttpService
import cn.kotliner.coroutine.common.log
import cn.kotliner.coroutine.ui.LOGO_URL
import kotlin.coroutines.experimental.startCoroutine
import kotlin.coroutines.experimental.suspendCoroutine

/**
 * Created by benny on 5/29/17.
 */
fun 我要开始协程啦(block: suspend ()-> Unit){
    block.startCoroutine(BaseContinuation())
}

suspend fun 我要开始加载图片啦(url: String) = suspendCoroutine<ByteArray> {
    continuation ->
    log("耗时操作，下载图片")
    try {
        val responseBody = HttpService.service.getLogo(url).execute()
        if(responseBody.isSuccessful){
            responseBody.body()?.byteStream()?.readBytes()?.let(continuation::resume)
        }else{
            continuation.resumeWithException(HttpException(responseBody.code()))
        }
    } catch(e: Exception) {
        continuation.resumeWithException(e)
    }
}