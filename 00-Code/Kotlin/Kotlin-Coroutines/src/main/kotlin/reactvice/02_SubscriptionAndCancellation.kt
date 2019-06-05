package reactvice

import io.reactivex.Flowable
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.consume
import kotlinx.coroutines.reactive.consumeEach
import kotlinx.coroutines.reactive.openSubscription
import kotlinx.coroutines.runBlocking

/*
上一篇使用了 source.consumeEach { ... } 来开启一个订阅，以接收来自 source 的数据。如果我们需要更精细地控制来自 channel 的数据，
可以使用 Publisher.openSubscription ，如下所示。

 调用 openSubscription 之后，我们应该 close 对应的订阅以取消订阅。但在这里，我们并没有显示地调用 close，而是使用了 Kotlin 标准库中的 consume 函数。
 注册 doFinally 监听打印 "Finally" 来确认它的确关闭了。
 */
@ObsoleteCoroutinesApi
fun main(args: Array<String>) = runBlocking<Unit> {

    val source = Flowable.range(1, 5) // 1 到 5
            .doOnSubscribe { println("OnSubscribe") } // 加上一些拦截
            .doOnComplete { println("OnComplete") }
            .doFinally { println("Finally") }         // ... 处理完成之后

    var cnt = 0

    source.openSubscription().consume {
        // 打开 source 的 channel
        for (x in this) { // 迭代 channel 以接收元素
            println(x)
            if (++cnt >= 3) break // 打印 3 个元素后结束循环
        }
        // Note: `consume` cancels the channel when this block of code is complete
    }


    //如果迭代的之间是发送者发送的数据，也不需要显示地调用 close，因为 consumeEach 已经帮你完成了：
    //注意，"Finally" 打印在尾元素 "5" 之前。这个例子里面的 main 函数通过 runBlocking 开启一个协程，这个主协程通过 source.consumeEach { ... } 接收 channel 的数据，
    // 当它等待数据源的数据时就会挂起。 在接收 Flowable.range(1, 5) 发射的最后一项数据时，主协程恢复，并在调度后的某个时间点打印该元素，但数据源会立刻结束并打印 "Finally"。
    println("---------------------consumeEach")
    source.consumeEach {
        println(it)
    }

}