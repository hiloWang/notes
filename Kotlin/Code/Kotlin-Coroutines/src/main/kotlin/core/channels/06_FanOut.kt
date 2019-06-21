package core.channels

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/*
多个协程也许会接收相同的管道，在它们之间进行分布式工作。 让我们启动一个定期产生整数的生产者协程 （每秒十个数字）：
 */
fun main() = runBlocking<Unit> {
    //sampleStart
    val producer = produceNumbers()
    //接下来我们可以得到几个处理者协程。在这个示例中，它们只是打印它们的 id 和接收到的数字：
    repeat(5) {
        //启动五个协程
        launchProcessor(it, producer)
    }
    delay(950)

    //注意，取消生产者协程并关闭它的通道，因此通过正在执行的处理者协程通道来终止迭代。
    producer.cancel() // cancel producer coroutine and thus kill them all
//sampleEnd
}

private fun CoroutineScope.produceNumbers() = produce<Int> {
    var x = 1 // start from 1
    while (true) {
        send(x++) // produce next
        delay(100) // wait 0.1s
    }
}

private fun CoroutineScope.launchProcessor(id: Int, channel: ReceiveChannel<Int>) = launch {
    //注意我们如何使用 for 循环显式迭代通道以在 launchProcessor 代码中执行扇出。 与 consumeEach 不同，这个 for 循环是安全完美地使用多个协程的。
    // 如果其中一个处理者协程执行失败，其它的处理器协程仍然会继续处理通道，而通过 consumeEach 编写的处理器始终在正常或非正常完成时消耗（取消）底层通道。
    for (msg in channel) {
        println("Processor #$id received $msg")
    }
}