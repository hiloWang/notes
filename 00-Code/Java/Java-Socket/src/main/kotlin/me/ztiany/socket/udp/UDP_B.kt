package me.ztiany.socket.udp

import java.net.DatagramPacket
import java.net.DatagramSocket



/**
 *
 * @author Ztiany
 *          Email ztiany3@gmail.com
 *          Date 17.12.2 13:10
 */
fun main(args: Array<String>) {
    // 创建Udp服务 设置端口
    val ds = DatagramSocket(12980)

    while (true) {

        // 定义数据包 存储接收的数据
        val buf = ByteArray(1024)
        val dp = DatagramPacket(buf, buf.size)

        //阻塞方法，用于接受数据
        ds.receive(dp)

        //打印数据
        val ip = dp.address.hostAddress
        val data = String(dp.data, 0, dp.length)
        val port = dp.port
        println("$ip::$data::port::$port")
    }
}