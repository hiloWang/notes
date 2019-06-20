package core.context_dispatchers

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/*
假如，我们写了一个 Android 应用程序并在上下文中启动了多个协程来为 Android activity 进行异步操作来拉取以及更新数据，或作动画等。
当 activity 被销毁的时候这些协程必须被取消以防止内存泄漏。

1：我们可以通过创建一个 Job 的实例来管理协程的生命周期，并让它与我们的 activity 的生命周期相关联。
当一个 activity 被创建的时候一个任务（job）实例被使用 Job() 工厂函数创建，并且当这个 activity 被销毁的时候它也被取消，

2：我们也可以在 Actvity 类中实现 CoroutineScope 接口。我们只需提供一个覆盖的 CoroutineScope.coroutineContext 属性来在作用域中为协程指定上下文。
 */
private class Activity : CoroutineScope {

    lateinit var job: Job

    fun create() {
        job = Job()
    }

    fun destroy() {
        job.cancel()
    }
    // to be continued ...

    // class Activity continues
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + job
    // to be continued ...

    // class Activity continues
    fun doSomething() {
        // launch ten coroutines for a demo, each working for a different time
        repeat(10) { i ->
            launch {
                delay((i + 1) * 200L) // variable delay 200ms, 400ms, ... etc
                println("Coroutine $i is done")
            }
        }
    }
} // class Activity ends

fun main() = runBlocking<Unit> {
    //sampleStart
    val activity = Activity()
    activity.create() // create an activity
    activity.doSomething() // run test function
    println("Launched coroutines")
    delay(500L) // delay for half a second
    println("Destroying activity!")
    activity.destroy() // cancels all coroutines
    delay(1000) // visually confirm that they don't work
//sampleEnd
}