package core.channels

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.runBlocking



fun main() = runBlocking {
    //sampleStart
    val numbers = produceNumbers() // produces integers from 1 and on
    val squares = square(numbers) // squares integers
    for (i in 1..5) println(squares.receive()) // print first five
    println("Done!") // we are done
    coroutineContext.cancelChildren() // cancel children coroutines
//sampleEnd
}

//管道是一种一个协程在流中开始生产可能无穷多个元素的模式：
//所有创建了协程的函数被定义在了 CoroutineScope 的扩展上， 所以我们可以依靠结构化并发来确保没有常驻在我们的应用程序中的全局协程。
private fun CoroutineScope.produceNumbers() = produce<Int> {
    var x = 1
    while (true) send(x++) // infinite stream of integers starting from 1
}

//并且另一个或多个协程开始消费这些流，做一些操作，并生产了一些额外的结果。 在下面的例子中，对这些数字仅仅做了平方操作：
private fun CoroutineScope.square(numbers: ReceiveChannel<Int>): ReceiveChannel<Int> = produce {
    for (x in numbers) send(x * x)
}