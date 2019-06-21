package reactvice

import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.reactive.consumeEach
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit

private fun rangeWithIntervalRx(scheduler: Scheduler, time: Long, start: Int, count: Int): Flowable<Int> =
        Flowable.zip(
                Flowable.range(start, count),
                Flowable.interval(time, TimeUnit.MILLISECONDS, scheduler),
                BiFunction { x, _ -> x })

//一个协程总是在某个 context 中工作。比如，在主线程通过 runBlocking 开启一个协程，然后去迭代 Rx 版本的 rangeWithIntervalRx
fun main() = runBlocking<Unit> {
    /*
    答应结果为:
            1 on thread main
            2 on thread main
            3 on thread main
     */
    rangeWithIntervalRx(Schedulers.computation(), 100, 1, 3)
            .consumeEach { println("$it on thread ${Thread.currentThread().name}") }
}