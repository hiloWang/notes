package cn.kotliner.coroutine.common

/**
 * Created by benny on 5/20/17.
 */
import okhttp3.ResponseBody


/**
 * Created by benny on 5/20/17.
 */
object HttpService {

    val service by lazy{
        val retrofit = retrofit2.Retrofit.Builder()
                .baseUrl("http://www.imooc.com")
                .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                .build()

        retrofit.create(cn.kotliner.coroutine.common.Service::class.java)
    }

}

interface Service{

    @retrofit2.http.GET
    fun getLogo(@retrofit2.http.Url fileUrl: String): retrofit2.Call<ResponseBody>

}