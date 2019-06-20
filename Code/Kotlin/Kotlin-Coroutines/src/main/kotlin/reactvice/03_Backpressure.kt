package reactvice

import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.rx2.rxFlowable

/*
背压是 reactive stream 最有趣也最复杂的部分。协程的挂起特性给予了解决背压问题的天然解决方案。

在 Rx Java 2.x 中能够处理背压的类叫做 Flowable。下面，我们使用 kotlinx-coroutines-rx2 模块中的 rxFlowable 来定义一个 flowable，用它发送 1 到 3 三个整数。
它会在调用 send 函数之后打印以下消息内容，以便于观察它的处理过程。

整数在主线程的环境中生成，然后通过 observeOn 操作符切换到其它线程订阅，缓冲大小是 1.订阅操作很慢，处理每一项都要花费 500 毫秒（通过 Thread.sleep 模拟）。

通过结果我们可以看出，生产者协程将第一个元素放入缓冲区，然后在试着发送另一个的时候挂起了。只有在消费者消耗处理完第一个元素之后，生产者才会继续发送第二个。
 */
@ExperimentalCoroutinesApi
fun main() = runBlocking<Unit> {
    // coroutine -- fast producer of elements in the context of the main thread
    val source = rxFlowable {
        for (x in 1..3) {
            send(x) // this is a suspending function
            println("Sent $x") // print after successfully sent item
        }
    }

    // subscribe on another thread with a slow subscriber using Rx
    source
            .observeOn(Schedulers.io(), false, 1) // specify buffer size of 1 item
            .doOnComplete { println("Complete") }
            .subscribe { x ->
                Thread.sleep(500) // 500ms to process each item
                println("Processed $x")
            }

    delay(2000) // suspend the main thread for a few seconds
}