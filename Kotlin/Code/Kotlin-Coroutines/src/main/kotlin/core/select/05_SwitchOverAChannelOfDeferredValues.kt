package core.select

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.selects.select

/*
我们现在来编写一个通道生产者函数，它消费一个产生延迟字符串的通道，并等待每个接收的延迟值，但它只在下一个延迟值到达或者通道关闭之前处于运行状态。
此示例将 onReceiveOrNull 和 onAwait 子句放在同一个 select 中：
 */
@ExperimentalCoroutinesApi
private fun CoroutineScope.switchMapDeferreds(input: ReceiveChannel<Deferred<String>>) = produce<String> {
    var current = input.receive() // start with first received deferred value 从第一个接收到的延迟值开始
    while (isActive) { // loop while not cancelled/closed
        val next = select<Deferred<String>?> {
            // return next deferred value from this select or null
            input.onReceiveOrNull { update ->
                println("onReceiveOrNull $update")
                update // replaces next value to wait
            }
            current.onAwait { value ->
                send(value) // send value that current deferred has produced 发送当前延迟生成的值
                input.receiveOrNull() // and use the next deferred from the input channel 然后使用从输入通道得到的下一个延迟值
            }
        }
        if (next == null) {
            println("Channel was closed")
            break // out of loop
        } else {
            current = next
        }
    }
}

private fun CoroutineScope.asyncString(str: String, time: Long) = async {
    delay(time)
    str
}

fun main() = runBlocking<Unit> {
    //sampleStart
    val chan = Channel<Deferred<String>>() // the channel for test
    launch {
        // launch printing coroutine
        for (s in switchMapDeferreds(chan))
            println(s) // print each received string
    }
    chan.send(asyncString("BEGIN", 100))
    delay(200) // enough time for "BEGIN" to be produced
    chan.send(asyncString("Slow", 500))
    delay(100) // not enough time to produce slow
    chan.send(asyncString("Replace", 100))
    delay(500) // give it time before the last one
    chan.send(asyncString("END", 500))
    delay(1000) // give it time to process
    chan.close() // close the channel ...
    delay(500) // and wait some time to let it finish
//sampleEnd
}