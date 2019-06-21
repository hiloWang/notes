package me.ztiany.collections


/**
 * 区间：区间表达式由具有操作符形式 .. 的 rangeTo 函数辅以 in 和 !in 形成。
 *      1，区间是为任何可比较类型定义的，但对于整型原生类型，它有一个优化的实现。
 *      2，整型区间（IntRange、 LongRange、 CharRange）有一个额外的特性：它们可以迭代。 编译器负责将其转换为类似 Java 的基于索引的 for-循环而无额外开销。
 *      3，如果需要倒序迭代数字也很简单。可以使用标准库中定义的 downTo() 函数
 *      4，以不等于 1 的任意步长迭代数字？ 当然没问题， step() 函数有助于此
 *      5，要创建一个不包括其结束元素的区间，可以使用 until 函数
 */

private fun useRange() {
    //使用区间
    for (i in 1..10) {
        print("$i ,")
    }
    println()
    //倒序
    for (i in 10 downTo 1) {
        print("$i ,")
    }
    println()
    //使用step
    for (i in 1..100 step 10) {
        print("$i ,")
    }
    println()
}

/** 区间如何工作：区间实现了该库中的一个公共接口：ClosedRange<T>。*/
private fun rangeFunction() {

    //rangeTo()：整型类型的 rangeTo() 操作符只是调用 *Range 类的构造函数
    //downTo()：为任何整型类型对定义的
    //reversed()：为每个 *Progression 类定义的，并且所有这些函数返回反转后的数列
    //step()：扩展函数 step() 是为每个 *Progression 类定义的， 所有这些函数都返回带有修改了 step 值（函数参数）的数列。

    // rangeTo
    val rangeTo = 10.rangeTo(100)
    rangeTo.forEach {
        print("$it ")
    }

    rangeTo.reversed().forEach {
        print("$it ,")
    }
}


/** 使用 in 运算符来检测某个数字是否在指定区间内 */
private fun range() {
    val x = 10
    val y = 9
    if (x in 1..y + 1) {
        println("fits in range")
    }
}

/** 区间迭代 */
private fun rangeIteration() {
    for (x in 1..5) {
        print(x)
        print(',')
    }
    println()
    for (x in 1..10 step 2) {
        print(x)
        print(',')
    }
    println()
    for (x in 9 downTo 0 step 3) {
        print(x)
        print(',')
    }
    for (x in 1 until 10) {//不包含10
        print(x)
        print(',')
    }
}

fun main(args: Array<String>) {
    useRange()
    rangeFunction()
}