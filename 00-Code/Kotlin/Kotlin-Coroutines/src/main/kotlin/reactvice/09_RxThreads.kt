package reactvice

import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

//----------------------------------------------------------------------------------------------------------
//09-13：上一部分操作符相关的例子中有许多指定的 CoroutineContext 参数，它大概与 Rx 中的 Scheduler 相对应。
//----------------------------------------------------------------------------------------------------------

/*
下面的例子介绍了 Rx 里线程管理的基础，这儿的 rangeWithIntervalRx 是使用 Rx 的 zip、range和interval 操作符实现的：
 */
private fun rangeWithIntervalRx(scheduler: Scheduler, time: Long, start: Int, count: Int): Flowable<Int> =
        Flowable.zip(
                Flowable.range(start, count),
                Flowable.interval(time, TimeUnit.MILLISECONDS, scheduler), BiFunction { x, _ -> x })

//我们把 Schedulers.computation() 传给了 rangeWithIntervalRx 操作符，然后它就会在 Rx 的计算线程池中运行。输出如下：
fun main() {
    rangeWithIntervalRx(Schedulers.computation(), 100, 1, 3)
            .subscribe { println("$it on thread ${Thread.currentThread().name}") }
    Thread.sleep(1000)
}