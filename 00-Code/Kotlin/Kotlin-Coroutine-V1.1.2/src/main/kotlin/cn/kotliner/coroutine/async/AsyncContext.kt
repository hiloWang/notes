package cn.kotliner.coroutine.async

import kotlin.coroutines.experimental.AbstractCoroutineContextElement
import kotlin.coroutines.experimental.Continuation
import kotlin.coroutines.experimental.ContinuationInterceptor

/**
 * Created by benny on 5/29/17.
 */
class AsyncContext: AbstractCoroutineContextElement(ContinuationInterceptor), ContinuationInterceptor{

    override fun <T> interceptContinuation(continuation: Continuation<T>): Continuation<T> {
        return UiCotinuationWrapper(continuation.context.fold(continuation){
            continuation, element ->
            if(element != this && element is ContinuationInterceptor){
                element.interceptContinuation(continuation)
            }else continuation
        })
    }

}