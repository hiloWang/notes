package me.ztiany.operators

import java.math.BigDecimal

/**
 *操作符重载
 *
 * @author Ztiany
 *          Email ztiany3@gmail.com
 *          Date 17.6.7 23:08
 */

/**
 * 操作符符重载：Kotlin 允许我们为自己的类型提供预定义的一组操作符的实现。
 * 这些操作符具有固定的符号表示 （如 + 或 *）和固定的优先级。为实现这样的操作符，
 * 我们为相应的类型（即二元操作符左侧的类型和一元操作符的参数类型）提供了一个固定名字的成员函数或扩展函数。
 * 重载操作符的函数需要用 operator 修饰符标记。
 *
 *   一元操作符
 *   递增和递减
 *   二元操作
 *   “In”操作符
 *   索引访问操作符
 *   调用操作符
 *   广义赋值
 *   相等与不等操作符
 *   比较操作符
 *   命名函数的中缀调用
 *
 *   函数名与对应的操作符参考：https://www.kotlincn.net/docs/reference/operator-overloading.html
 */


private data class Point(val x: Int, val y: Int)

private operator fun Point.unaryMinus() = Point(-x, -y)//给Point定义取反操作符，这里可以访问Point内部值

private operator fun Point.plus(other: Point): Point {
    return Point(x + other.x, y + other.y)
}

private operator fun Point.times(scale: Double): Point {
    return Point((x * scale).toInt(), (y * scale).toInt())
}

private operator fun Char.times(count: Int): String {
    return toString().repeat(count)
}

private operator fun BigDecimal.inc() = this + BigDecimal.ONE


fun main(args: Array<String>) {
    println("------------------------------------------")
    val point = Point(10, 20)
    println(-point)
    println("------------------------------------------")
    println('a' * 3)
    println("------------------------------------------")
    var bd = BigDecimal.ZERO
    println(++bd)

}



