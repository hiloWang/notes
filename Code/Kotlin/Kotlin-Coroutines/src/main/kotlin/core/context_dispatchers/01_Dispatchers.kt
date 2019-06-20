package core.context_dispatchers


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking


/*
Dispatchers(调度器) and threads：Coroutine context 包含一个Dispatchers，它确定对应协程用于执行的线程或线程池。
Dispatchers 可以将协程执行限制在一个特定的线程上，将其调度到线程池，或者让其无限制地运行。

协程上下文包括了一个 协程调度器 （请参见 CoroutineDispatcher），它确定了相应的协程在执行时使用一个或多个线程。
协程调度器可以将协程的执行局限在指定的线程中，调度它运行在线程池中或让它不受限的运行。

当调用 launch { …… } 时不传参数，它从启动了它的 CoroutineScope 中承袭了上下文（以及调度器）。在这个案例中，它从 main 线程中的 runBlocking 主协程承袭了上下文。

newSingleThreadContext 为协程的运行启动了一个新的线程。 一个专用的线程是一种非常昂贵的资源。 在真实的应用程序中两者都必须被释放，
当不再需要的时候，使用 close 函数，或存储在一个顶级变量中使它在整个应用程序中被重用。

默认调度器，当协程在 GlobalScope 中启动的时候被使用， 它代表 Dispatchers.Default 使用了共享的后台线程池，
所以 GlobalScope.launch { …… } 也可以使用相同的调度器—— launch(Dispatchers.Default) { …… }。
 */
fun main() = runBlocking<Unit> {
    //sampleStart
    launch {
        // context of the parent, main runBlocking coroutine
        println("main runBlocking      : I'm working in thread ${Thread.currentThread().name}")
    }
    launch(Dispatchers.Unconfined) {
        // not confined -- will work with main thread
        println("Unconfined            : I'm working in thread ${Thread.currentThread().name}")
    }
    launch(Dispatchers.Default) {
        // will get dispatched to DefaultDispatcher
        println("Default               : I'm working in thread ${Thread.currentThread().name}")
    }
    launch(newSingleThreadContext("MyOwnThread")) {
        // will get its own new thread
        println("newSingleThreadContext: I'm working in thread ${Thread.currentThread().name}")
    }
//sampleEnd
}