package me.ztiany.advance

/**
 * 可空类型与非空类型：Kotlin 的类型系统旨在消除来自代码空引用的危险，也称为《十亿美元的错误》。
 *
 * 许多编程语言（包括 Java）中最常见的陷阱之一是访问空引用的成员，导致空引用异常。在 Java 中，
 * 这等同于 NullPointerException 或简称 NPE。
 *
 * Kotlin 的类型系统旨在从我们的代码中消除 NullPointerException。NPE 的唯一可能的原因可能是
 *
 * 1，显式调用 throw NullPointerException()
 * 2，使用了 !! 操作符
 * 3，外部 Java 代码导致的
 * 4，对于初始化，有一些数据不一致（如一个未初始化的 this 用于构造函数的某个地方）
 */
fun main(args: Array<String>) {
    //在 Kotlin 中，类型系统区分一个引用可以容纳 null （可空引用）还是不能容纳（非空引用）。
    // 例如，String 类型的常规变量不能容纳 null：

    val a: String = "abc"
    println(a.length)

    var b: String? = "abc"
    b = null // 可空类型必须加上?

    //调用可空类型的方法
    //1 判断
    if (b != null) {
        println(b.length)
    }
    //2 ?符号
    println(b?.length)
    //3 Elvis 操作符
    println(b?.length ?: -1)


    //安全的类型转换
    val aInt: Int? = a as? Int
    println(aInt)

    //可空类型的集合：如果你有一个可空类型元素的集合，并且想要过滤非空元素，你可以使用 filterNotNull 来实现。
    val nullableList: List<Int?> = listOf(1, 2, null, 4)
    val intList: List<Int> = nullableList.filterNotNull()
    println(intList)

    // !! 操作符
    println(b!!)
}