package me.ztiany.socket.tcp

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.ServerSocket

/*
TCP客户端：

1. 建立tcp的socket服务，最好明确具体的地址和端口。这个对象在创建时，就已经可以对指定ip和端口进行连接(三次握手)。
2. 如果连接成功，就意味着通道建立了，socket流就已经产生了。只要获取到socket流中的读取流和写入流即可，
只要通过getInputStream和getOutputStream就可以获取两个流对象。
3. 关闭资源。

TCP服务端：

1. 创建服务端socket服务，并监听一个端口。
2. 服务端为了给客户端提供服务，获取客户端的内容，可以通过accept方法获取连接过来的客户端对象。
3. 可以通过获取到的socket对象中的socket流和具体的客户端进行通讯。
4. 如果通讯结束，关闭资源。注意：要先关客户端，再关服务端。
 */

/** 服务器 */
fun main(args: Array<String>) {
    // 建立服务端 并监听一个 端口
    val ss = ServerSocket(10338)
    println("服务器启动")

    // 建立连接 获取客户端对象
    val s = ss.accept()
    // 获取ip
    val ip = s.inetAddress.hostAddress
    println("客户端连接成功: $ip")

    // 获取客户端Socket流中的输入流
    val inputStream = s.getInputStream()
    val bufferedReader = BufferedReader(InputStreamReader(inputStream))
    // 获取客户端Socket流中的输出流
    val outputStream = s.getOutputStream()
    val bufferedWriter = BufferedWriter(OutputStreamWriter(outputStream))

    // 读取客户端数据
    var line: String? = bufferedReader.readLine()

    while (line != null) {

        println("收到客户端消息: $line")

        bufferedWriter.write("收到你的消息了: $line")
        bufferedWriter.newLine()
        bufferedWriter.flush()

        line = bufferedReader.readLine()
    }

    s.shutdownOutput()
    s.close()
    ss.close()
}