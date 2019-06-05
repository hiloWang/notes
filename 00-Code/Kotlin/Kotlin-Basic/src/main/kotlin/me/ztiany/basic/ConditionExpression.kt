package me.ztiany.basic

/** 条件表达式 */

//传统方式返回最大值
private fun maxOf_1(a: Int, b: Int): Int {
    if (a > b) {
        return a
    } else {
        return b
    }
}

//条件表达式返回最大值
private fun maxOf_2(a: Int, b: Int): Int = if (a > b) a else b

fun main(args: Array<String>) {
    println("max of 433 and 42 is ${me.ztiany.basic.maxOf_1(433, 42)}")
    println("max of 0 and 42 is ${me.ztiany.basic.maxOf_2(0, 42)}")
}