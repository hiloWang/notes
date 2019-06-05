package me.ztiany.functions

import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock


/** 高阶函数：高阶函数是将函数用作参数或返回值的函数。 */
private fun <T> lock(lock: Lock, body: () -> T): T {
    lock.lock()
    try {
        return body.invoke()
    } finally {
        lock.unlock()
    }
}

private fun testLock() {
    val lock = ReentrantLock()
    //调用lock
    lock(lock, { println("abc") })
    //在 Kotlin 中有一个约定，如果函数的最后一个参数是一个函数，并且你传递一个 lambda 表达式作为相应的参数，可以在圆括号之外指定它
    lock(lock) { println("abc") }
}


/**
 * Lambda 表达式：
 *      1，lambda 表达式总是被大括号括着，
 *      2，其参数（如果有的话）在 -> 之前声明（参数类型可以省略），
 *      3，函数体（如果存在的话）在 -> 后面。
 *      4，如果 lambda 是该调用的是唯一参数，则调用中的圆括号可以完全省略。
 *      5，如果函数字面值只有一个参数， 那么它的声明可以省略（连同 ->），其名称是 it。
 */

//给List添加map操作
private fun <T, R> List<T>.map(transform: (T) -> R): List<R> {
    val result = arrayListOf<R>()
    for (item in this) {
        result.add(transform(item))
    }
    return result
}

private fun testMap() {
    val list = listOf(1, 2, 3)
    list.map {
        {
            it.toString()
        }
    }.forEach {
        println(it)
    }
}


//可以直接声明Lambda类型
private val toStr: (num: Int) -> String = { it.toString() }


// 函数类型：对于接受另一个函数作为参数的函数，我们必须为该参数指定函数类型。
// max函数中必须指定less的类型，其类型为(T, T) -> Boolean) 接收两个类型，返回一个Boolean
private fun <T> max(collection: Collection<T>, less: (T, T) -> Boolean): T? {
    var max: T? = null
    for (item in collection) {
        if (max == null || less(max, item)) {
            max = item
        }
    }
    return max
}


private fun lambdaReturn() {

    val ints = listOf(1, 2, 3, 4, 5, 6, 7)

    //下面两个表达式等价
    ints.filter {
        val shouldFilter = it > 0
        shouldFilter
    }

    ints.filter {
        val shouldFilter = it > 0
        return@filter shouldFilter
    }

}

/**
 * 匿名函数：上面提供的 lambda 表达式语法缺少的一个东西是指定函数的返回类型的能力。在大多数情况下，这是不必要的。
 *                  因为返回类型可以自动推断出来。然而，如果确实需要显式指定，可以使用另一种语法： 匿名函数
 *
 *      1，匿名函数的返回类型推断机制与正常函数一样：对于具有表达式函数体的匿名函数将自动 推断返回类型，
 *            而具有代码块函数体的返回类型必须显式 指定（或者已假定为 Unit）。
 *      2，匿名函数参数总是在括号内传递。 允许将函数留在圆括号外的简写语法仅适用于 lambda 表达式。
 *      3，Lambda表达式和匿名函数之间的另一个区别是 非局部返回的行为。一个不带标签的 return 语句 总是在用 fun 关键字声明的函数中返回。
 *            这意味着 lambda 表达式中的 return 将从包含它的函数返回，而匿名函数中的 return 将从匿名函数自身返回。
 *
 */
private fun anonymityFunction() {
    val ints = listOf(1, 2, 3, 4, 5, 6, 7)
    ints.filter(fun(item): Boolean = item > 0)//指定返回类型
    ints.filter(fun(item) = item > 0)//不指定返回类型
}


/**
 * Lambda 表达式或者匿名函数（以及局部函数和对象表达式）可以访问其闭包 ，即在外部作用域中声明的变量。
 */
private fun testClosure() {
    val ints = listOf(1, 2, 3, 4, 5, 6, 7)
    var sum = 0
    ints.filter { it > 0 }
            .map { it * it }
            .forEach { sum += it }
    print(sum)
}


//自执行闭包：自执行闭包就是在定义闭包的同时直接执行闭包，一般用于初始化上下文环境。
fun autoExecute() {
    { x: Int, y: Int ->
        println("${x + y}")
    }(1, 3)
}


/**
 * 带接收者的函数字面值：
 *
 *      Kotlin 提供了一种能力, 调用一个函数字面值时, 可以指定一个 接收者对象(receiver object).
 *      在这个函数字面值的函数体内部, 你可以调用接收者对象的方法, 而不必指定任何限定符. 这种能力与扩展函数很类似,
 *      在扩展函数的函数体中, 你也可以访问接收者对象的成员
 */
private fun testExtend() {

    //当接收者类型可以从上下文推断时，lambda 表达式可以用作带接收者的函数字面值。
    class HTML {
        fun body() {

        }
    }

    //接收者是HTML
    fun html(init: HTML.() -> Unit): HTML {
        val html = HTML()  // 创建接收者对象
        html.init()        // 将该接收者对象传给该 lambda
        return html
    }
    //此时可以推断出接收者是HTML
    html {
        // 带接收者的 lambda 由此开始
        body()   // 省略HTML直接调用该接收者对象的一个方法
    }
}
