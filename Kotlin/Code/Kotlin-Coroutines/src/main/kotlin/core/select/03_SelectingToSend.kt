package core.select

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.selects.select


/*
Select 表达式具有 onSend 子句，可以很好的与选择的偏向特性结合使用。
 */
@ObsoleteCoroutinesApi
fun main() = runBlocking<Unit> {
    //消费者将会非常缓慢，每个数值处理需要250毫秒：
    //sampleStart
    val side = Channel<Int>() // allocate side channel
    launch {
        // this is a very fast consumer for the side channel
        side.consumeEach { println("Side channel has $it") }
    }
    produceNumbers(side).consumeEach {
        println("Consuming $it")
        delay(250) // let us digest the consumed number properly, do not hurry； 不要着急，让我们正确消化消耗被发送来的数字。
    }
    println("Done consuming")
    coroutineContext.cancelChildren()
//sampleEnd
}

//编写一个整数生成器的示例，当主通道上的消费者无法跟上它时，它会将值发送到 side 通道上
@ExperimentalCoroutinesApi
private fun CoroutineScope.produceNumbers(side: SendChannel<Int>) = produce<Int> {
    for (num in 1..10) { // produce 10 numbers from 1 to 10
        delay(100) // every 100 ms
        select<Unit> {
            onSend(num) {} // Send to the primary channel；发送到主通道。
            side.onSend(num) {
            } // or to the side channel；或者发送到 side 通道。
        }
    }
}
