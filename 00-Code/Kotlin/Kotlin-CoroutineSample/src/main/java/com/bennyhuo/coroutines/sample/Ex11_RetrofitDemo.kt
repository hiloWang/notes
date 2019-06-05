package com.bennyhuo.coroutines.sample

import com.bennyhuo.coroutines.utils.log
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import kotlinx.coroutines.experimental.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import kotlin.coroutines.experimental.suspendCoroutine

//region common
private val gitHubServiceApi by lazy {
    val retrofit = retrofit2.Retrofit.Builder()
            .baseUrl("https://api.github.com")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

    retrofit.create(GitHubServiceApi::class.java)
}

interface GitHubServiceApi {
    @GET("users/{login}")
    fun getUserCallback(@Path("login") login: String): Call<User>

    @GET("users/{login}")
    fun getUserCoroutine(@Path("login") login: String): Deferred<User>
}

data class User(val id: String, val name: String, val url: String)

fun showUser(user: User) {
    println(user)
}

fun showError(t: Throwable) {
    t.printStackTrace()
}
//endregion

fun main(args: Array<String>) = runBlocking {
    //useCallback()
    //wrappedInSuspendFunction()
    //useCoroutine()
    //useTraditionalForLoop()
    //useExtensionForEach()
    //timeCost()
}

fun useCallback() {

    gitHubServiceApi.getUserCallback("bennyhuo").enqueue(object : Callback<User> {
        override fun onFailure(call: Call<User>, t: Throwable) {
            showError(t)
        }

        override fun onResponse(call: Call<User>, response: Response<User>) {
            response.body()?.let(::showUser) ?: showError(NullPointerException())
        }
    })
}

suspend fun wrappedInSuspendFunction() {
    launch {
        try {
            showUser(async { getUser("bennyhuo") }.await())
        } catch (e: Exception) {
            showError(e)
        }
    }.join()
}

suspend fun getUser(login: String) = suspendCoroutine<User> { continuation ->
    gitHubServiceApi.getUserCallback(login).enqueue(object : Callback<User> {
        override fun onFailure(call: Call<User>, t: Throwable) {
            continuation.resumeWithException(t)
        }

        override fun onResponse(call: Call<User>, response: Response<User>) {
            response.body()?.let(continuation::resume) ?: continuation.resumeWithException(NullPointerException())
        }
    })
}

suspend fun useCoroutine() {
    launch {
        try {
            showUser(gitHubServiceApi.getUserCoroutine("bennyhuo").await())
        } catch (e: Exception) {
            showError(e)
        }
    }.join()
}

suspend fun useTraditionalForLoop() {
    launch {
        for (login in listOf("JakeWharton", "abreslav", "yole", "elizarov")) {
            try {
                showUser(gitHubServiceApi.getUserCoroutine(login).await())
            } catch (e: Exception) {
                showError(e)
            }
            delay(1000)
        }
    }.join()
}

suspend fun useExtensionForEach() {
    launch {
        listOf("JakeWharton", "abreslav", "yole", "elizarov")
                .forEach {
                    try {
                        showUser(gitHubServiceApi.getUserCoroutine(it).await())
                    } catch (e: Exception) {
                        showError(e)
                    }
                    delay(1000)
                }
    }.join()
}


suspend fun timeCost() {
    launch {
        cost {
            listOf("JakeWharton", "abreslav", "yole", "elizarov")
                    .forEach {
                        cost {
                            try {
                                showUser(gitHubServiceApi.getUserCoroutine(it).await())
                            } catch (e: Exception) {
                                showError(e)
                            }
                            delay(1000)
                        }
                    }
        }
    }.join()
}

inline fun <T> cost(block: () -> T): T {
    val start = System.currentTimeMillis()
    val result = block()
    val cost = System.currentTimeMillis() - start
    log("Time Cost: ${cost}ms")
    return result
}