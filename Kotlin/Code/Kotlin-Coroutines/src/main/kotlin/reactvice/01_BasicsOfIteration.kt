package reactvice

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

//----------------------------------------------------------------------------------------------------------
//01-04：这部分概述了响应式数据流与基于协程的 channel 之间的不同点。
//----------------------------------------------------------------------------------------------------------

/*
Channel 在某种程度上类似 reactive stream 中的这几个类：
    Reactive stream Publisher；
    Rx Java 1.x Observable；
    Rx Java 2.x Flowable，它实现了 Publisher 。

它们都描述了一个异步的元素（数据）流，可能无限，可能有限，而且都支持背压。不过，用 Rx 的术语来讲， Channel 表现得更像是一个热数据流。
生产协程将元素发送到 channel ，消费协程从其中接收。每调用一次 receive 就会消耗 channel 上的一个数据。看下面的例子。

这里开始后只会打印一次。当执行了 produce 函数之后，就会开启一个协程用于发送数据流，它生产的所有数据将会由扩展函数 ReceiveChannel.consumeEach 消费，
并且没有办法从这个 channel 再次接收数据。当生产者协程关闭后，再试图从它获取数据，什么也无法得到。
 */
@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
fun main(args: Array<String>) = runBlocking<Unit> {

    // 创建一个 channel，每隔 200ms 生产一个从 1 到 3 的数
    val source = produce<Int>(coroutineContext) {
        println("Begin") // 记录协程的开始
        for (x in 1..3) {
            delay(200) // 等待 200ms
            send(x) // 向 channel 发送数字
        }
    }

    // 打印 source 的数据
    println("Elements:")
    source.consumeEach {
        // 消费数据
        println(it)
    }

    // 再次打印 source 的数据，接收不到数据了。
    println("Again:")
    source.consumeEach {
        // 消费数据
        println(it)
    }

}