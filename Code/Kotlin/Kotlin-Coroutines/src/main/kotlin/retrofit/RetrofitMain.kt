package retrofit

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


interface GitHub {
    @GET("/")
    fun contributorsAsync(): Deferred<Map<String, String>>
}

fun main() = runBlocking {

    println("Making GitHub API request")

    val okHttpClient = OkHttpClient
            .Builder()
            .build()

    val retrofit = Retrofit.Builder().apply {
        baseUrl("https://api.github.com")
        client(okHttpClient)
        addConverterFactory(GsonConverterFactory.create())
        addCallAdapterFactory(CoroutineCallAdapterFactory())
    }.build()

    val github = retrofit.create(GitHub::class.java)
    val result = github.contributorsAsync()

    launch {
        try {
            val contributors = result.await()
            println("contributors = $contributors")
        } finally {
            okHttpClient.dispatcher().executorService().shutdown()
        }
    }

    println("end...")
    delay(100)
    result.cancel()
}