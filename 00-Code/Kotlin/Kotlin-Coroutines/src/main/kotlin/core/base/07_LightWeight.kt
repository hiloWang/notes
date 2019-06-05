package core.base

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


//协程是轻量级的，它拥有自己的运行状态，但它对资源的消耗却非常的小
fun main(args: Array<String>) = runBlocking {
    //这里创建了100_000协程
    repeat(100_000) {
        // launch a lot of coroutines
        launch {
            delay(1000L)//每一个协程都停一秒，但是相互之间并不阻塞
            print(".")
        }
    }

    println(" end ....")
}

//结果是：很快的打印了100000个点，所有的协程都在两秒后瞬间执行完毕，如果是换成线程的话，那很可能造成内存溢出。

