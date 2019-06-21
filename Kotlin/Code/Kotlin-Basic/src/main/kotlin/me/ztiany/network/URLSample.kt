package me.ztiany.network

import java.net.URL

/**
 *
 * @author ztiany
 *          Email: ztiany3@gmail.com
 */
fun main(args: Array<String>) {
    doGet()
}

private fun doGet() {
    //执行网络请求非常方便
    val text = URL("https://www.baidu.com/").readText()
    println(text)
}