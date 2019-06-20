package reactvice

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.reactive.consumeEach
import kotlinx.coroutines.reactive.publish
import kotlinx.coroutines.runBlocking

/*
我们用 kotlinx-coroutines-reactive 模块的 publish 代替 kotlinx-coroutines-core 模块的 produce 重写，代码其实一样，只是 source 的类型从 ReceiveChannel 变成了 Publisher。

这个例子着重展示了 reactive stream 与 channel 最关键的不同之处。Reactive stream 是一个高阶函数的概念。
尽管 channel 也是一个元素流，但 reactive stream 定义了生成元素的方式。当订阅产生的时候，才会生成真正的流。
根据 Publish 实现的不同，每一个订阅者都会收到相同或者不同的元素流。

这个例子里使用的 publish 构造者会为每一个订阅启动一个全新的协程，而每调用一次 Publisher.consumeEach 就会产生一个新的订阅。
这次的代码里调用了两次，所以 "Begin" 也就打印了两次。

在 Rx 语言中这也叫做冷（cold）生产者。许多标准的 Rx 操作符也会生产这种“冷”流。我们可以从一个协程中去迭代它们，然后每个订阅都生产同样的元素流。
注意，我们同样可以使用 Rx 的 publish 操作符和 connect 方法来模拟 channel 的行为。
 */
@ExperimentalCoroutinesApi
fun main(args: Array<String>) = runBlocking<Unit> {
    // 创建一个 channel，每隔 200ms 生产一个从 1 到 3 的数
    val source = publish<Int>(coroutineContext) {
        //^^^^^^^  <--- 就是这儿的代码与先前不同
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

    // 再次打印 source 的数据
    println("Again:")
    source.consumeEach {
        // 消费数据
        println(it)
    }
}