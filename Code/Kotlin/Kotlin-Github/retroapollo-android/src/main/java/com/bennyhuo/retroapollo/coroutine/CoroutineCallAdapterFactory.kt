package com.bennyhuo.retroapollo.coroutine

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloCall.Callback
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.bennyhuo.retroapollo.CallAdapter
import kotlinx.coroutines.experimental.CompletableDeferred
import kotlinx.coroutines.experimental.Deferred
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class CoroutineCallAdapterFactory private constructor() : CallAdapter.Factory() {
    companion object {
        @JvmStatic
        @JvmName("create")
        operator fun invoke() = CoroutineCallAdapterFactory()
    }

    override fun get(returnType: Type): CallAdapter<*, *>? {
        if (Deferred::class.java != getRawType(returnType)) {
            return null
        }
        if (returnType !is ParameterizedType) {
            throw IllegalStateException(
                    "Deferred return type must be parameterized as Deferred<Foo> or Deferred<out Foo>")
        }
        val responseType = getParameterUpperBound(0, returnType)

        val rawDeferredType = getRawType(responseType)
        return if (rawDeferredType == Response::class.java) {
            if (responseType !is ParameterizedType) {
                throw IllegalStateException(
                        "Response must be parameterized as Response<Foo> or Response<out Foo>")
            }
            ResponseCallAdapter<Any>(getParameterUpperBound(0, responseType))
        } else {
            BodyCallAdapter<Any>(responseType)
        }
    }

    private class BodyCallAdapter<T>(
            private val responseType: Type
    ) : CallAdapter<T, Deferred<T>> {

        override fun responseType() = responseType

        override fun adapt(call: ApolloCall<T>): Deferred<T> {
            val deferred = CompletableDeferred<T>()

            deferred.invokeOnCompletion {
                if (deferred.isCancelled) {
                    call.cancel()
                }
            }

            call.enqueue(object : Callback<T>() {
                override fun onFailure(e: ApolloException) {
                    deferred.completeExceptionally(e)
                }

                override fun onResponse(response: Response<T>) {
                    if (response.data() == null) {
                        deferred.completeExceptionally(ApolloException(response.errors().toString()))
                    } else {
                        deferred.complete(response.data()!!)
                    }
                }
            })

            return deferred
        }
    }

    private class ResponseCallAdapter<T>(
            private val responseType: Type
    ) : CallAdapter<T, Deferred<Response<T>>> {

        override fun responseType() = responseType

        override fun adapt(call: ApolloCall<T>): Deferred<Response<T>> {
            val deferred = CompletableDeferred<Response<T>>()

            deferred.invokeOnCompletion {
                if (deferred.isCancelled) {
                    call.cancel()
                }
            }

            call.enqueue(object : Callback<T>() {
                override fun onFailure(t: ApolloException) {
                    deferred.completeExceptionally(t)
                }

                override fun onResponse(response: Response<T>) {
                    deferred.complete(response)
                }
            })

            return deferred
        }
    }
}
