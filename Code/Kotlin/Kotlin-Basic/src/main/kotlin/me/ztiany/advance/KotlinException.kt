package me.ztiany.advance

import java.lang.Integer.parseInt
import java.nio.file.Files
import java.nio.file.Paths


/**
 * 异常：kotlin 中所有异常类都是 Throwable 类的子孙类。 每个异常都有消息、堆栈回溯信息和可选的原因。
 *
 *          1，使用 throw-表达式来抛出异常。
 *          2，使用 try-表达式来捕获异常
 *          3，Try 是一个表达式，即它可以有一个返回值。finally 块中的内容不会影响表达式的结果。
 *          4，Kotlin 没有受检的异常。
 *
 * Nothing 类型：在 Kotlin 中 throw 是表达式，所以你可以使用它（比如）作为 Elvis 表达式的一部分：
 *
 *          1，throw 表达式的类型是特殊类型 Nothing。 该类型没有值，而是用于标记永远不能达到的代码位置。
 *          2，在你自己的代码中，你可以使用 Nothing 来标记一个永远不会返回的函数：
 */


fun main(args: Array<String>) {

    val a: Int? = try {
        parseInt("abc")
    } catch (e: NumberFormatException) {
        null
    }

    class Person(val name: String)

    val person: Person = Person("s")
    val s = person.name ?: throw IllegalArgumentException("Name required")

    fun fail(message: String): Nothing {
        throw IllegalArgumentException(message)
    }

    //try with source
    val stream = Files.newInputStream(Paths.get("/some/file.txt"))
    stream.buffered().reader().use { reader ->
        println(reader.readText())
    }
}