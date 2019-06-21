package reactvice

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consume
import kotlinx.coroutines.reactive.consumeEach
import kotlinx.coroutines.reactive.openSubscription
import kotlinx.coroutines.reactive.publish
import kotlinx.coroutines.selects.whileSelect
import org.reactivestreams.Publisher
import kotlin.coroutines.CoroutineContext

/*
下面来实现一下我们自己的 takeUntil 操作符。这个其实非常棘手，因为我们要追踪管理两个流。在第二个流结束或者发送数据之前，我们才去接收源流的数据。
不过，协程中我们可以使用 select 表达式来辅助实现：
 */
@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
fun <T, U> Publisher<T>.takeUntil(context: CoroutineContext, other: Publisher<U>) = GlobalScope.publish<T>(context) {
    this@takeUntil.openSubscription().consume {
        // explicitly open channel to Publisher<T>
        val current = this
        other.openSubscription().consume {
            // explicitly open channel to Publisher<U>
            val other = this

            // Loops while [select] expression returns `true`.
            //这儿使用的 whileSelect 是 while(select{...}) {} 循环的一个缩写，Kotlin 的 consume 表达式会在 channel 退出的时候关闭它，也就是从相应的 publisher 那儿取消订阅。
            whileSelect {
                other.onReceive { false }          // bail out on any received element from `other`
                current.onReceive { send(it); true }  // resend element from this channel and continue
            }

        }
    }
}

//为了测试，下面的函数组合了 range 和 interval ，它用了 publish 协程构造器（它的纯 Rx 实现会在稍后说明）：
@ExperimentalCoroutinesApi
fun CoroutineScope.rangeWithInterval(time: Long, start: Int, count: Int) = publish<Int> {
    for (x in start until start + count) {
        delay(time) // wait before sending each number
        send(x)
    }
}

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
fun main() = runBlocking<Unit> {
    val slowNums = rangeWithInterval(200, 1, 10)         // numbers with 200ms interval
    val stop = rangeWithInterval(500, 1, 10)             // the first one after 500ms
    slowNums.takeUntil(coroutineContext, stop).consumeEach { println(it) } // let's test it
}