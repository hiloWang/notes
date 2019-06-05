package me.ztiany.basic

import java.util.*

/** 定义局部变量 */
fun main(args: Array<String>) {
    //一次赋值（只读）的局部变量
    val a: Int = 3
    val b = 4
    val c: Int = 5
    println("a = $a, b = $b, c = $c")

    //可变变量
    var x = 5 // 自动推断出 `Int` 类型
    x += 1
    println("x = $x")
}

private class ClassA {

    //延迟初始化
    lateinit var value: Date

    companion object {
        const val VALUE = 1
    }

}

/*编译期常量*/
const val GLOBAL_VALUE = 1
