package core.select

import kotlinx.coroutines.*
import kotlinx.coroutines.selects.select
import java.util.*


//延迟值可以使用 onAwait 子句查询。 让我们启动一个异步函数，它在随机的延迟后会延迟返回字符串：
fun CoroutineScope.asyncString(time: Int) = async {
    delay(time.toLong())
    "Waited for $time ms"
}

//让我们随机启动十余个异步函数，每个都延迟随机的时间。
fun CoroutineScope.asyncStringsList(): List<Deferred<String>> {
    val random = Random(3)
    return List(12) { asyncString(random.nextInt(1000)) }
}

/*
现在 main 函数在等待第一个函数完成，并统计仍处于激活状态的延迟值的数量。注意，我们在这里使用 select 表达式事实上是作为一种 Kotlin DSL，
所以我们可以用任意代码为它提供子句。在这种情况下，我们遍历一个延迟值的队列，并为每个延迟值提供 onAwait 子句的调用。
 */
fun main() = runBlocking<Unit> {
    //sampleStart
    val list = asyncStringsList()
    val result = select<String> {
        list.withIndex().forEach { (index, deferred) ->
            deferred.onAwait { answer ->
                "Deferred $index produced answer '$answer'"
            }
        }
    }
    println("result = $result ----")
    val countActive = list.count { it.isActive }
    println("$countActive coroutines are still active")
//sampleEnd
}