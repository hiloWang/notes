package core.select

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.selects.select

/*
select 表达式可以同时等待多个挂起函数，并选择 第一个可用的。（这个有点类似于 NIO 中的 Selector）

在通道中 select，使用 receive 挂起函数，我们可以从两个通道接收 其中一个 的数据。 但是 select 表达式允许我们使用其 onReceive 子句 同时 从两者接收
 */
private fun CoroutineScope.fizz() = produce<String> {
    while (true) { // sends "Fizz" every 300 ms
        delay(300)
        send("Fizz")
    }
}

private fun CoroutineScope.buzz() = produce<String> {
    while (true) { // sends "Buzz!" every 500 ms
        delay(500)
        send("Buzz!")
    }
}

private suspend fun selectFizzBuzz(fizz: ReceiveChannel<String>, buzz: ReceiveChannel<String>) {
    select<Unit> {
        // <Unit> means that this select expression does not produce any result
        fizz.onReceive { value ->
            // this is the first select clause
            println("fizz -> '$value'")
        }
        buzz.onReceive { value ->
            // this is the second select clause
            println("buzz -> '$value'")
        }
    }
}

fun main() = runBlocking<Unit> {
    //sampleStart
    val fizz = fizz()
    val buzz = buzz()
    repeat(7) {
        selectFizzBuzz(fizz, buzz)
    }
    coroutineContext.cancelChildren() // cancel fizz & buzz coroutines
//sampleEnd
}