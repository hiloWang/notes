package core.base

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


/*
runBlocking 连接协程与非协程世界，如果我们希望直接在主线程上面创建协程，那么可以使用runBlocking，runBlocking作用:
    1. 作为一个适配器，用于启动顶级主协程
    2. 运行一个新的协程并且阻塞当前线程直到协程执行完毕， 当前线程可中断。
    3. runBlocking适用于主函数或者测试中，
    4. 如果这个阻塞的线程被中断, 内部的协程将会被取消，runBlocking 调用抛出[InterruptedException].
    5. 使用runBlocking启动协程，会阻塞当前的协程，所以不要在协程中再使用runBlocking启动协程，因为这样没有什么意义
 */
fun main() {
    GlobalScope.launch { // launch new coroutine in background and continue
        delay(1000L)
        println("World!")
    }
    println("Hello,") // main thread continues here immediately
    runBlocking {     // but this expression blocks the main thread
        delay(2000L)  // ... while we delay for 2 seconds to keep JVM alive
    }
}

/*
fun main() = runBlocking<Unit> { // start main coroutine
    GlobalScope.launch { // launch new coroutine in background and continue
        delay(1000L)
        println("World!")
    }
    println("Hello,") // main coroutine continues here immediately
    delay(2000L)      // delaying for 2 seconds to keep JVM alive
}
 */

// 下面的运行结果是，World! Hello, 首先runBlocking启动协程执行，然后主线程被阻塞，等待协程执行完毕后主线程继续执行。
/*
fun main(args: Array<String>) {
    runBlocking {
        delay(1000L)
        println("World!")
    }
    println("Hello,") // main coroutine continues while child is delayed 当子协程延迟时主协程继续
}*/
