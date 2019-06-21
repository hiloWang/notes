package cn.kotliner.coroutine.sequence

import kotlin.coroutines.experimental.buildSequence

/**
 * Created by benny on 5/29/17.
 */
fun main(args: Array<String>) {
    for (i in fibonacci){
        println(i)
        if(i > 100) break
    }
}

val fibonacci = buildSequence {
    yield(1)
    var cur = 1
    var next = 1

    while(true){
        yield(next)
        val tmp = cur + next
        cur = next
        next = tmp
    }
}