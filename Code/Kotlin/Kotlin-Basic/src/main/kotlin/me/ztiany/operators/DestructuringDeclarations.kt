package me.ztiany.operators


/** 解构声明   Destructuring Declaration：
 *
 *      有些时候, 能够将一个对象 解构(destructure) 为多个变量, 将会很方便，
 *      这种语法称为 解构声明(destructuring declaration). 一个解构声明会一次性创建多个变量
 *
 *      解构声明在编译时将被分解为以下代码：
 *
 *              val name = person.component1()
 *              val valage = person.component2()
 */
private class Person constructor(name: String, age: Int) {

    var mName = name
    var mAge = age

    operator fun component1(): String {
        return mName
    }

    operator fun component2(): Int {
        return mAge
    }
}

private data class Result(val result: Int, val status: Int)

private fun getResult(): Result {
    return Result(11, 12)
}

private fun structDeclarations() {

    val person = Person("ztiany", 27)
    val (name, age) = person
    //数据类自动声明 componentN() 函数，所以这里可以用解构声明。
    var (result, statue) = getResult()

    //下划线用于未使用的变量
    val (_, status) = getResult()

    //遍历map，之所致可以这样遍历，是应为Map提供了component1函数
    val map = mapOf(Pair("A", 1), Pair("B", 2), Pair("C", 3))
    for ((key, value) in map) {
        println("key is $key, value is $value")
    }

    //在 lambda 表达式中解构
    println(map.mapValues {
        entry ->
        "${entry.value}!"
    })
    println(map.mapValues {
        (key, value) ->
        "$key->$value"
    })
}

data class NameComponents(val name: String, val extension: String)

fun splitFilename(fullName: String): NameComponents {
    val result = fullName.split('.', limit = 2)
    return NameComponents(result[0], result[1])
}

fun main(args: Array<String>) {
    val (name, ext) = splitFilename("example.kt")
    println(name)
    println(ext)
}
