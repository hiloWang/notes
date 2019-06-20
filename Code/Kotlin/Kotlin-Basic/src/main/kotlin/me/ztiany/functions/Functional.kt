package me.ztiany.functions

import java.io.File

/**函数式编程*/
fun main(args: Array<String>) {
    //统计一个文件中所有字符串的个数
    File("build.gradle")
            .readText()
            .toCharArray()
            .filterNot(Char::isWhitespace)
            .groupBy { it }
            .map {
                it.key to it.value.size
            }
            .forEach(::println)
}
