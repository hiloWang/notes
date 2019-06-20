package reactvice

import kotlinx.coroutines.*
import kotlinx.coroutines.reactive.consumeEach
import kotlinx.coroutines.reactive.publish
import org.reactivestreams.Publisher
import kotlin.coroutines.CoroutineContext

/*
使用协程，至少有两种方法可以处理多个数据流的问题。其一，像上一个例子里展示的那样调用 select；其二就是启动多个协程。我们就用后者来实现一下 merge 操作符吧：
 */
@ExperimentalCoroutinesApi
fun <T> Publisher<Publisher<T>>.merge(context: CoroutineContext) = GlobalScope.publish<T>(context) {
    consumeEach { pub ->
        //遍历每一个发射的 Publisher
        // for each publisher received on the source channel
        launch {
            //启动一个协程，消费该 pub，再次发射该 pub 发射的数据到统一的下游，这里必须启动一个新的协程，否则会顺序的发送每一个 pub 所发射的数据。
            // launch a child coroutine
            pub.consumeEach { send(it) } // resend all element from this publisher
        }
    }
}

@ExperimentalCoroutinesApi
fun CoroutineScope.testPub() = publish<Publisher<Int>> {
    send(rangeWithInterval(250, 1, 4)) // number 1 at 250ms, 2 at 500ms, 3 at 750ms, 4 at 1000ms
    delay(100) // wait for 100 ms
    send(rangeWithInterval(500, 11, 3)) // number 11 at 600ms, 12 at 1100ms, 13 at 1600ms
    delay(1100) // wait for 1.1s - done in 1.2 sec after start
}

@ExperimentalCoroutinesApi
fun main() = runBlocking<Unit> {
    testPub().merge(coroutineContext).consumeEach { println(it) } // print the whole stream
}