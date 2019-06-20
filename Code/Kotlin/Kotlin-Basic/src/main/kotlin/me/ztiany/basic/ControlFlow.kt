package me.ztiany.basic

/** 控制流：if，when，for，while，返回和跳转 */

/** if语句：if是一个表达式，即它会返回一个值。 因此就不需要三元运算符 */
fun ifFlow() {

    // 传统用法
    val a = 32
    val b = 31
    var max = a

    if (a < b) max = b

    // 作为表达式
    val max1 = if (a > b) a else b

    //if的分支可以是代码块，最后的表达式作为该块的值
    val max2 = if (a > b) {
        print("Choose a")
        a
    } else {
        print("Choose b")
        b
    }
}

/**
 * when语句：when 取代了类 C 语言的 switch 操作符.
 *
 *1， when 将它的参数和所有的分支条件顺序比较，直到某个分支满足条件。
 *2， when 既可以被当做表达式使用也可以被当做语句使用。如果它被当做表达式，
 *3， 符合条件的分支的值就是整个表达式的值，如果当做语句使用， 则忽略个别分支的值。
 */
fun whenFlow(x: Int) {

    //作为语句
    when (x) {
        1 -> print("x == 1")
        2 -> print("x == 2")
        else -> { // 注意这个块
            print("x is neither 1 nor 2")
        }
    }

    //作为表达式
    var y = when (x) {
        1 -> 2
        3 -> 5
        else -> 3
    }
}


/**
 * for循环：for 循环可以对任何提供迭代器（iterator）的对象进行遍历，对数组的 for 循环会被编译为并不创建迭代器的基于索引的循环
 */
fun forFlow() {

    //如果你想要通过索引遍历一个数组或者一个 list
    val array = arrayOf(1, 2, 3, 4, 5)
    for (i in array.indices) {
        print(array[i])
    }

    //或者用库函数 withIndex
    for ((index, value) in array.withIndex()) {
        println("the element at $index is $value")
    }
}

/**  while循环：while 和 do..while 照常使用 */
fun whileFlow() {
    var num = 5
    while (num > 0) {
        num++
        println("num= $num")
    }
}


/**
 * 定义标签：在 Kotlin 中任何表达式都可以用标签（label）来标记。 标签的格式为标识符后跟 @ 符号，
 * 例如：abc@、fooBar@都是有效的标签。
 */
fun skip() {

    //定义标签
    loop1@ for (i in 1..100) {
        loop2@ for (j in i..100) {

        }
    }

    // 标签处返回：Kotlin有函数字面量、局部函数和对象表达式。因此Kotlin的函数可以被嵌套。
    // 标签限制的 return 允许我们从外层函数返回。 最重要的一个用途就是从 lambda 表达式中返回
    val ints = listOf(1, 2, 3, 4)
    ints.forEach lit@{
        if (it == 1) {
            return@lit
        }
        println(it)//打印：234
    }

    //我们用一个匿名函数替代 lambda 表达式。 匿名函数内部的 return 语句将从该匿名函数自身返回
    ints.forEach(fun(value: Int) {
        if (value == 0) {
            return
        }
        print(value)
    })
}


