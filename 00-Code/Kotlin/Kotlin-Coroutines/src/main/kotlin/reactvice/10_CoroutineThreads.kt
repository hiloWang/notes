package reactvice

import io.reactivex.Flowable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.reactive.publish
import kotlin.coroutines.CoroutineContext


//在协程的世界，Schedulers.computation() 可以粗略地与 Dispatchers.Default 相对应，所以上一个例子可以这样改：
fun rangeWithInterval(context: CoroutineContext, time: Long, start: Int, count: Int) = GlobalScope.publish<Int>(context) {
    for (x in start until start + count) {
        delay(time) // wait before sending each number
        send(x)
    }
}

//这里的 subscribe 并没有绑定自己的 scheduler，所以它会在 publisher 相同的线程里执行，也就是这个例子里的 Dispatchers.Default 。
fun main() {
    Flowable.fromPublisher(rangeWithInterval(Dispatchers.Default, 100, 1, 3))
            .subscribe { println("$it on thread ${Thread.currentThread().name}") }
    Thread.sleep(1000)
}