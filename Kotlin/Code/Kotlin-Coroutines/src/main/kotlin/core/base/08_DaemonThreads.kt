package core.base

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


//Coroutines are like daemon threads，当主线程退出时，协程也会退出。
fun main() = runBlocking {
    //sampleStart
    GlobalScope.launch {
        //repeat作用：对给定的函数执行给定的次数
        repeat(1000) { i ->
            println("I'm sleeping $i ...")
            delay(500L)
        }
    }
    delay(1300L) // just quit after delay
//sampleEnd
}