package me.ztiany.socket.ip

import java.net.InetAddress

/**
 *
 * @author Ztiany
 *          Email ztiany3@gmail.com
 *          Date 17.12.2 12:56
 */
fun main(args: Array<String>) {
    //本地
    val localIpAddress = InetAddress.getLocalHost()
    println(localIpAddress.hostAddress)

    //百度
    val baiduIpAddressArr = InetAddress.getAllByName("www.baidu.com")
    baiduIpAddressArr.forEach {
        println(it.hostAddress)
    }


}