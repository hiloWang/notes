package me.ztiany.collections

/**
 *使用集合
 *
 * @author Ztiany
 *          Email ztiany3@gmail.com
 *          Date 17.5.23 23:11
 */

fun main(args: Array<String>) {

    //使用 in 运算符来判断集合内是否包含某实例：
    val items = setOf("apple", "banana", "kiwi")
    when {
        "orange" in items -> println("juicy")
        "apple" in items -> println("apple is fine too")
    }

    //使用 lambda 表达式来过滤（filter）和映射（map）集合：
    val fruits = listOf("banana", "avocado", "apple", "kiwi")

    /*Stream 操作*/
    fruits.filter { it.startsWith("a") }
            .sortedBy { it }
            .map { it.toUpperCase() }
            .forEach { println(it) }
}


