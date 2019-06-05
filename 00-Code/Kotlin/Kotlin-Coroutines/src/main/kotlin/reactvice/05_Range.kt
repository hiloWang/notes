package reactvice

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.reactive.consumeEach
import kotlinx.coroutines.reactive.publish
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.CoroutineContext

//----------------------------------------------------------------------------------------------------------
//05-08：操作符，像 Rx 之类的 reactive stream 库都会有一大批的操作符用于创建、转换、关联或者处理相应的流。如果要自己实现一个操作符实现背压，众所周知是非常困难的。
//协程和 channel 致力于提供一种截然不同的体验。这儿没有内置的操作符，但是处理元素流非常简单，而且自动支持背压，无需你操心。
//这一部分就讲了基于协程实现 reactive stream 操作符。
//----------------------------------------------------------------------------------------------------------

/*
实现协程版本的 range 操作符，这里的代码不使用 Executor 而是 CoroutineContext ，所有的背压处理都交由协程机来处理。
注意，这儿的实现仅仅依赖了 reactive stream 库中的 Publisher 接口以及其依赖。
 */
@ExperimentalCoroutinesApi
fun CoroutineScope.range(context: CoroutineContext, start: Int, count: Int) = publish<Int>(context) {
    for (x in start until start + count) send(x)
}

@ExperimentalCoroutinesApi
fun main() = runBlocking<Unit> {
    // Range inherits parent job from runBlocking, but overrides dispatcher with Dispatchers.Default
    range(Dispatchers.Default, 1, 5).consumeEach { println(it) }
}