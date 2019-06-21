package me.ztiany.generic

/** 运行时泛型：擦出和实例化参数*/

private fun printSum(c: Collection<*>) {
    val intList = c as? List<Int> ?: throw IllegalArgumentException("List is expected")
    println(intList.sum())
}

private fun printSum1(c: Collection<Int>) {
    //这是允许的，因为 Int 已经被确定了：Kotlin编译器是足够智能的，在编译期它已经知道相应的类型信息时，is 检测是允许的。
    if (c is List<Int>) {
        println(c.sum())
    }
}

private inline fun <reified T> isA(value: Any) = value is T

fun main(args: Array<String>) {
    printSum(listOf(1, 2, 3))

    println(isA<String>("abc"))
    println(isA<String>(123))
}
