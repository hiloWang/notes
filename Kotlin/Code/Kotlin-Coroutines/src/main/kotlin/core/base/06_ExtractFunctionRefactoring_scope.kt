package core.base

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/*
但是如果提取函数包含了一个调用当前作用域的协程构建器？ 在这个示例中仅仅使用 suspend 来修饰提取出来的函数是不够的。
在 CoroutineScope 调用 doWorld 方法是一种解决方案，但它并非总是适用，因为它不会使API看起来更清晰。
惯用的解决方法是使 CoroutineScope 在一个类中作为一个属性并包含一个目标函数， 或者使它外部的类实现 CoroutineScope 接口。
作为最后的手段，CoroutineScope(coroutineContext) 也是可以使用的，但是这样的结构是不安全的， 因为你将无法在这个作用域内控制方法的执行。只有私有的API可以使用这样的写法。
 */
fun main(args: Array<String>) = runBlocking {
    launchDoWorld()
    println("Hello,")
}

// this is your first suspending function
suspend fun launchDoWorld() = coroutineScope {
    launch {
        println("World!")
    }
}