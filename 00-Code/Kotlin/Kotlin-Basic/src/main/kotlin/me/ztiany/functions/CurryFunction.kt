package me.ztiany.functions

import java.io.OutputStream
import java.nio.charset.Charset

/**
 *柯里化函数：多元函数变成单元函数调用链的过程
 *偏函数：指定多参数函数的某些参数而得到新函数，实现方式是函数扩展
 */


private fun log(tag: String, target: OutputStream, message: Any?) {
    target.write("[$tag] $message\n".toByteArray())
}

//柯里化函数
private fun logA(tag: String) = fun(target: OutputStream) = fun(message: Any?) = target.write("[$tag] $message\n".toByteArray())

private fun logAA(tag: String): (target: OutputStream) -> (message: Any?) -> Unit {
    return fun(target: OutputStream): (message: Any?) -> Unit {
        return fun(message: Any?) {
            target.write("[$tag] $message\n".toByteArray())
        }
    }
}

//Function3表示任意该类型的函数
private fun <P1, P2, P3, R> Function3<P1, P2, P3, R>.curried() = fun(p1: P1) = fun(p2: P2) = fun(p3: P3) = this(p1, p2, p3)

fun main(args: Array<String>) {
    //柯里化函数
    log("benny", System.out, "HelloWorld")
    logA("benny")(System.out)("HelloWorld Again.")

    val curriedLog = ::log.curried()("benny")(System.out)("HelloWorld Again.")
    println(curriedLog)
    println()

    val consoleLogWithTag = (::log.curried())("benny")(System.out)
    consoleLogWithTag("HelloAgain Again.")
    consoleLogWithTag("HelloAgain Again.")
    consoleLogWithTag("HelloAgain Again.")
    consoleLogWithTag("HelloAgain Again.")

    //偏函数
    val bytes = "我是中国人".toByteArray(charset("GBK"))
    val stringFromGBK = makeStringFromGbkBytes(bytes)
}


private val makeString = fun(byteArray: ByteArray, charset: Charset): String {
    return String(byteArray, charset)
}

private val makeStringFromGbkBytes = makeString.partial2(charset("GBK"))


private fun <P1, P2, R> Function2<P1, P2, R>.partial2(p2: P2) = fun(p1: P1) = this(p1, p2)
private fun <P1, P2, R> Function2<P1, P2, R>.partial1(p1: P1) = fun(p2: P2) = this(p1, p2)