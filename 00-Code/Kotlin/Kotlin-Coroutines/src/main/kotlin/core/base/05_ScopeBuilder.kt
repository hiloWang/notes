package core.base

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/*
除了由不同的构建器提供协程作用域之外，还可以使用 coroutineScope 构建器声明自己的作用域。它会创建新的协程作用域并且在所有已启动子协程执行完毕之前不会结束。
runBlocking 与 coroutineScope 的主要区别在于后者在等待所有子协程执行完毕时不会阻塞当前线程。

打印结果：
    Task from coroutine scope
    Task from runBlocking
    Task from nested launch
    Coroutine scope is over
*/
fun main() = runBlocking {
    // this: CoroutineScope

    launch {
        delay(200L)
        println("Task from runBlocking")
    }

    //使用 coroutineScope 创建一个新的 CoroutineScope
    coroutineScope {
        // Creates a new coroutine scope
        launch {
            delay(500L)
            println("Task from nested launch")
        }

        delay(100L)
        println("Task from coroutine scope") // This line will be printed before nested launch
    }

    println("Coroutine scope is over") // This line is not printed until nested launch completes
}