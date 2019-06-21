package core.select

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.selects.select

/*
select 中的 onReceive 子句在已经关闭的通道执行会发生失败，并导致相应的 select 抛出异常。我们可以使用 onReceiveOrNull 子句在关闭通道时执行特定操作。
以下示例还显示了 select 是一个返回其查询方法结果的表达式：

有几个结果可以通过观察得出。
    首先，select 偏向于 第一个子句，当可以同时选到多个子句时， 第一个子句将被选中。在这里，两个通道都在不断地生成字符串，因此 a 通道作为 select 中的第一个子句获胜。
    然而因为我们使用的是无缓冲通道，所以 a 在其调用 send 时会不时地被挂起，进而 b 也有机会发送。

    第二个观察结果是，当通道已经关闭时， 会立即选择 onReceiveOrNull。
 */
private suspend fun selectAorB(a: ReceiveChannel<String>, b: ReceiveChannel<String>): String =
        select<String> {
            a.onReceiveOrNull { value ->
                if (value == null)
                    "Channel 'a' is closed"
                else
                    "a -> '$value'"
            }
            b.onReceiveOrNull { value ->
                if (value == null)
                    "Channel 'b' is closed"
                else
                    "b -> '$value'"
            }
        }

@ExperimentalCoroutinesApi
fun main() = runBlocking<Unit> {
    //sampleStart
    val a = produce<String> {
        repeat(4) { send("Hello $it") }
    }
    val b = produce<String> {
        repeat(4) { send("World $it") }
    }
    repeat(8) {
        // print first eight results
        println(selectAorB(a, b))
    }
    coroutineContext.cancelChildren()
//sampleEnd
}