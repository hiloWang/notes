package reactvice

import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers

//在 Rx 里，你可以在调用链中使用特殊的操作符切换操作的线程
fun main() {
    Flowable.fromPublisher(rangeWithInterval(Dispatchers.Default, 100, 1, 3))
            .observeOn(Schedulers.computation())                           // <-- THIS LINE IS ADDED
            .subscribe { println("$it on thread ${Thread.currentThread().name}") }
    Thread.sleep(1000)
}