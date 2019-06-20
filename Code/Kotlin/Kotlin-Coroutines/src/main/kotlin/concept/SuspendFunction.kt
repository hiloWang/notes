package concept

import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Unconfined

/** 理解Suspend函数 */
@ExperimentalCoroutinesApi
fun main() {
    //asyncCallbackSample() //传统异步回调方式
    //asyncReturnSample1() // 协程异步返回方式1
    //asyncReturnSample2() // 协程异步返回方式2
    coroutinesSwitch() // 感受协程中暂停方法的多入口，每次暂停到恢复都可以切换到不同的调度线程
}

/**执行耗时操作*/
private fun queryDatabase(): String {
    Thread.sleep(2000)
    return "Data"
}

//====================================================================
//  感受暂停方法
//====================================================================
/**
 * 执行顺序为：
 *      main: 1
 *      main: 2
 *      main: 3
 *      kotlinx.coroutines.DefaultExecutor: 4
 *      main: 5
 */
@ExperimentalCoroutinesApi
private fun coroutinesSwitch() {
    // 1. 程序开始
    println("${Thread.currentThread().name}: 1")

    // 2. 启动一个协程, 并立即启动，Unconfined意思是在当前线程(主线程)运行协程
    GlobalScope.launch(Unconfined) {

        // 3. 本协程在主线程上直接开始执行了第一步
        println("${Thread.currentThread().name}: 2")

        /* 4. 本协程的第二步调用了一个suspend方法, 调用之后,
         * 本协程就放弃执行权
         *
         * delay被调用的时候, 在内部创建了一个计时器, 并设了个callback.
         * 1秒后计时器到期, 就会调用刚设置的callback.
         * 在callback里面, 会调用系统的接口来恢复协程.
         * 协程在计时器线程上恢复执行了. (不是主线程, 跟Unconfined有关)
         */
        delay(1000L)  // 过1秒后, 计时器线程会resume协程

        // 7. 计时器线程恢复了协程,此时已经运行在计时器线程了
        println("${Thread.currentThread().name}: 4")//kotlinx.coroutines.DefaultExecutor
    }

    // 5. 刚那个的协程不要我(主线程)干活了, 所以我继续之前的执行
    println("${Thread.currentThread().name}: 3")

    // 6. (主线程)睡2秒钟
    Thread.sleep(2000L)

    // 8. 我(主线程)睡完后继续执行
    println("${Thread.currentThread().name}: 5")
}

//====================================================================
//  协程的同步返回
//====================================================================
fun asyncReturnSample1() = runBlocking {
    //1 在主线程启动协程
    println("1 主线程开启协程")
    println("2 在主线程协程中开启新的协程异步查询数据")
    //2 开启异步协程去查询数据
    val deferred = GlobalScope.async(Dispatchers.Default) { queryDatabase() }
    println("3 主携程继续执行......")
    //3 获取数据
    val data = deferred.await()
    println("4 得到数据回到主线程协程$data")
}

private fun asyncReturnSample2() = runBlocking {
    //1 在主线程启动协程
    println("1 主线程开启协程")
    println("2 在主线程协程中开启新的协程异步查询数据")
    //2 开启异步协程去查询数据
    val deferred = GlobalScope.async { queryDatabase() }
    println("3 主携程继续执行.....1.")
    //3 获取数据
    launch(coroutineContext) {
        val data = deferred.await()
        println("5 得到数据回到主线程协程$data")
    }
    println("4 主携程继续执行.....2.")
}

//====================================================================
//  传统方式的异步回调
//====================================================================
private fun asyncCallbackSample() {
    asyncQuery {
        println(it)
    }
}

private fun asyncQuery(callback: (String) -> Unit) {
    Thread {
        val queryDatabase = queryDatabase()
        callback.invoke(queryDatabase)
    }.start()
}
