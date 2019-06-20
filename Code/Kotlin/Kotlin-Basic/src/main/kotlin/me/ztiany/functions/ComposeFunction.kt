package me.ztiany.functions

/** 函数复合：组合多个函数成一个新的函数 */

//f(g(x))   m(x) = f(g(x))
val add5 = { i: Int -> i + 5 } // g(x)
val multiplyBy2 = { i: Int -> i * 2 } // f(x)

fun main(args: Array<String>) {
    println(multiplyBy2(add5(8))) // (5 + 8) * 2

    val add5AndMultiplyBy2 = add5 andThen multiplyBy2
    val add5ComposeMultiplyBy2 = add5 compose multiplyBy2
    println(add5AndMultiplyBy2(8)) // m(x) = f(g(x))
    println(add5ComposeMultiplyBy2(8)) // m(x) = g(f(x))
}

private infix fun <P1, P2, R> Function1<P1, P2>.andThen(function: Function1<P2, R>): Function1<P1, R> =
        fun(p1: P1): R = function.invoke(this.invoke(p1))

private infix fun <P1, P2, R> Function1<P2, R>.compose(function: Function1<P1, P2>): Function1<P1, R> =
        fun(p1: P1): R = this.invoke(function.invoke(p1))