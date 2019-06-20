package core.channels

import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull

/*
计时器通道是一种特别的会合通道，每次经过特定的延迟都会从该通道进行消费并产生 Unit。
虽然它看起来似乎没用，它被用来构建分段来创建复杂的基于时间的 produce 管道和进行窗口化操作以及其它时间相关的处理。
可以在 select 中使用计时器通道来进行“打勾”操作。

使用工厂方法 ticker 来创建这些通道。 为了表明不需要其它元素，请使用 ReceiveChannel.cancel 方法。

请注意，ticker 知道可能的消费者暂停，并且默认情况下会调整下一个生成的元素如果发生暂停则延迟，试图保持固定的生成元素率。
给可选的 mode 参数传入 TickerMode.FIXED_DELAY 可以保持固定元素之间的延迟。
 */
@ObsoleteCoroutinesApi
fun main() = runBlocking<Unit> {

    val tickerChannel = ticker(delayMillis = 100, initialDelayMillis = 0) // create ticker channel

    var nextElement = withTimeoutOrNull(1) { tickerChannel.receive() }
    println("Initial element is available immediately: $nextElement") // initial delay hasn't passed yet

    nextElement = withTimeoutOrNull(50) { tickerChannel.receive() } // all subsequent elements has 100ms delay
    println("Next element is not ready in 50 ms: $nextElement")

    nextElement = withTimeoutOrNull(60) { tickerChannel.receive() }
    println("Next element is ready in 100 ms: $nextElement")

    // Emulate large consumption delays
    println("Consumer pauses for 150ms")
    delay(150)

    // Next element is available immediately
    nextElement = withTimeoutOrNull(1) { tickerChannel.receive() }
    println("Next element is available immediately after large consumer delay: $nextElement")

    // Note that the pause between `receive` calls is taken into account and next element arrives faster
    nextElement = withTimeoutOrNull(60) { tickerChannel.receive() }
    println("Next element is ready in 50ms after consumer pause in 150ms: $nextElement")

    tickerChannel.cancel() // indicate that no more elements are needed
}