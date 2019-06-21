package me.ztiany.generic

/** 泛型类型参数 */

private val <T> List<T>.penultimate: T
    get() = this[size - 2]

private fun <T : Number> oneHalf(value: T): Double {
    return value.toDouble() / 2.0
}

private fun <T : Comparable<T>> max(first: T, second: T): T {
    return if (first > second) first else second
}

private fun <T> ensureTrailingPeriod(seq: T) where T : CharSequence, T : Appendable {
    if (!seq.endsWith('.')) {
        seq.append('.')
    }
}

private fun <T : Number> testList(list: MutableList<T>) {
    //不能添加元素
    //list.add(1)

    //可以调用对应的方法
    list.forEach {
        it.toByte()
    }
}

fun main(args: Array<String>) {
    /*列表*/
    val letters = ('a'..'z').toList()
    println(letters.slice<Char>(0..2))
    println(letters.slice(10..13))
    println(listOf(1, 2, 3, 4).penultimate)

    /*泛型上限*/
    println(oneHalf(3))
    println(max("kotlin", "java"))

    /*多泛型上线*/
    val helloWorld = StringBuilder("Hello World")
    ensureTrailingPeriod(helloWorld)
    println(helloWorld)
}