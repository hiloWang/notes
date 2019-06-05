package me.ztiany.functions


/**
 *尾递归函数
 *
 * @author Ztiany
 *          Email ztiany3@gmail.com
 *          Date 17.6.3 0:16
 */

/**
 * 尾递归函数：
 *
 *      Kotlin 支持一种称为尾递归的函数式编程风格。 这允许一些通常用循环写的算法改用递归函数来写，而无堆栈溢出的风险。
 *      当一个函数用 tailrec 修饰符标记并满足所需的形式时，编译器会优化该递归，留下一个快速而高效的基于循环的版本。
 *
 * 要符合 tailrec 修饰符的条件的话，函数必须将其自身调用作为它执行的最后一个操作。在递归调用后有更多代码时，不能使用尾递归，
 * 并且不能用在 try/catch/finally 块中。目前尾部递归只在 JVM 后端中支持。
 */

private tailrec fun findFixPoint(x: Double = 1.0): Double = if (x == Math.cos(x)) x else findFixPoint(Math.cos(x))

//最终代码相当于这种更传统风格的代码
private fun findFixPoint(): Double {
    var x = 1.0
    while (true) {
        val y = Math.cos(x)
        if (x == y) return y
        x = y
    }
}

fun main(args: Array<String>) {
    val point = findFixPoint(1.0)
    println(point)
}