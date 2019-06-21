package cn.kotliner.coroutine.async

import kotlin.coroutines.experimental.AbstractCoroutineContextElement
import kotlin.coroutines.experimental.CoroutineContext

/**
 * Created by benny on 5/29/17.
 */
class DownloadContext(val url: String): AbstractCoroutineContextElement(Key){
    companion object Key: CoroutineContext.Key<DownloadContext>
}