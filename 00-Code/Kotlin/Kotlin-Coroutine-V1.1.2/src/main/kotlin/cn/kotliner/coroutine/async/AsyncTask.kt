package cn.kotliner.coroutine.async

import java.util.concurrent.Executors

/**
 * Created by benny on 5/29/17.
 */

private val pool by lazy {
    Executors.newCachedThreadPool()
}

class AsyncTask(val block: ()-> Unit){
    fun execute() = pool.execute(block)
}