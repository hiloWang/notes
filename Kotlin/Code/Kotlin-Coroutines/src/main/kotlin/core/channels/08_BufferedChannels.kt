package core.channels

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/*
到目前为止展示的通道都是没有缓冲区的。无缓冲的通道在发送者和接收者相遇时传输元素。
如果发送先被调用，则它将被挂起直到接收被调用， 如果接收先被调用，它将被挂起直到发送被调用。

Channel() 工厂函数与 produce 建造器通过一个可选的参数 capacity 来指定 缓冲区大小 。
缓冲允许发送者在被挂起前发送多个元素， 就像 BlockingQueue 有指定的容量一样，当缓冲区被占满的时候将会引起阻塞。
 */
fun main() = runBlocking<Unit> {
    //sampleStart
    val channel = Channel<Int>(4) // create buffered channel
    val sender = launch { // launch sender coroutine
        repeat(10) {
            println("Sending $it") // print before sending each element
            channel.send(it) // will suspend when buffer is full
        }
    }
    // don't receive anything... just wait....
    delay(1000)
    sender.cancel() // cancel sender coroutine
//sampleEnd
}