package me.ztiany.functions

import java.io.File

fun File.isInsideHiddenDirectory() = generateSequence(this) {
    println("generate：$it")
    it.parentFile
}.any {
    println("any：$it")
    it.isHidden
}


private fun useAsSquence() {
    listOf(1, 2, 3, 4).asSequence()
            .map { print("map($it) "); it * it }
            .filter { print("filter($it) "); it % 2 == 0 }

    listOf(1, 2, 3, 4).asSequence()
            .map { print("map($it) "); it * it }
            .filter { print("filter($it) "); it % 2 == 0 }
            .toList()
}

private fun stringSquence(args: Array<String>) {
    val naturalNumbers = generateSequence(0) { it + 1 }
    val numbersTo100 = naturalNumbers.takeWhile { it <= 100 }
    println(numbersTo100.sum())
}


fun main(args: Array<String>) {
    val file = File(".").absoluteFile
    println(file.isInsideHiddenDirectory())
}