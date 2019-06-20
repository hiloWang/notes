package core.base

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/*
让我们在 launch { …… } 中提取代码块并分离到另一个函数中。当你在这段代码上展示“提取函数”函数的时候，你得到了一个新的函数并用 suspend 修饰。
这是你的第一个 挂起函数 。挂起函数可以像一个普通的函数一样使用内部协程，但是它们拥有一些额外的特性，反过来说， 使用其它的挂起函数，
比如这个示例中的 delay，可以使协程暂停执行。
 */
fun main() = runBlocking {
    launch { doWorld() }
    println("Hello,")
}

// this is your first suspending function
suspend fun doWorld() {
    delay(1000L)
    println("World!")
}