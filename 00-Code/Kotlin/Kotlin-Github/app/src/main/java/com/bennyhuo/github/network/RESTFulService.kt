package com.bennyhuo.github.network

import com.bennyhuo.github.AppContext
import com.bennyhuo.github.common.ext.ensureDir
import com.bennyhuo.github.network.compat.enableTls12OnPreLollipop
import com.bennyhuo.github.network.interceptors.AcceptInterceptor
import com.bennyhuo.github.network.interceptors.AuthInterceptor
import com.bennyhuo.github.network.interceptors.CacheInterceptor
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory2
import retrofit2.converter.gson.GsonConverterFactory
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.io.File
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://api.github.com"

//通过一个 QueryParameter 让 CacheInterceptor 添加 no-cache
const val FORCE_NETWORK = "forceNetwork"

private val cacheFile by lazy {
    File(AppContext.cacheDir, "webServiceApi").apply { ensureDir() }
}

val retrofit by lazy {
    Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory2.createWithSchedulers(Schedulers.io(), AndroidSchedulers.mainThread()))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .cache(Cache(cacheFile, 1024 * 1024 * 1024))
                    .addInterceptor(AcceptInterceptor())
                    .addInterceptor(AuthInterceptor())
                    .addInterceptor(CacheInterceptor())
                    .addInterceptor(HttpLoggingInterceptor().setLevel(Level.BODY))
                    .enableTls12OnPreLollipop()
                    .build()
            )
            .baseUrl(BASE_URL)
            .build()
}
