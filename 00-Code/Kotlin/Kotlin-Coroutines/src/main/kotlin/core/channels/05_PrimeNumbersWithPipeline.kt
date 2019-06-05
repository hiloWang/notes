package core.channels

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.runBlocking


fun main() = runBlocking {
    //sampleStart
    //这里就像洋葱一样，一层包着一层。每个协程负责过滤指定给自己的素数。
    //现在我们开启了一个从2开始的数字流管道，从当前的通道中取一个素数， 并为每一个我们发现的素数启动一个流水线阶段：
    //numbersFrom(2) -> filter(2) -> filter(3) -> filter(5) -> filter(7) ……

    //下面的例子打印了前十个素数， 在主线程的上下文中运行整个管道。直到所有的协程在该主协程 runBlocking 的作用域中被启动完成。
    // 我们不必使用一个显式的列表来保存所有被我们已经启动的协程。 我们使用 cancelChildren 扩展函数在我们打印了前十个素数以后来取消所有的子协程。
    var cur = numbersFrom(2)
    for (i in 1..10) {
        val prime = cur.receive()
        println(prime)
        cur = filter(cur, prime)
    }
    coroutineContext.cancelChildren() // cancel all children to let main finish
//sampleEnd
}

//让我们来展示一个极端的例子——在协程中使用一个管道来生成素数。我们开启了一个数字的无限序列。
private fun CoroutineScope.numbersFrom(start: Int) = produce<Int> {
    var x = start
    while (true) {
        println("numbersFrom product number : ${x}")
        send(x++)
    } // infinite stream of integers from start
}

//在下面的管道阶段中过滤了来源于流中的数字，删除了所有可以被给定素数整除的数字，每次调用都会产生新的 ReceiveChannel
private fun CoroutineScope.filter(numbers: ReceiveChannel<Int>, prime: Int) = produce<Int> {
    for (x in numbers){
        println("${Thread.currentThread()} filter checking $x  or prime = $prime")
        if (x % prime != 0) send(x)
    }
}