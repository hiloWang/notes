package me.ztiany.socket.tcp

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.Socket
import java.util.*
import kotlin.concurrent.thread


/** 客户端 */
fun main(args: Array<String>) {

    // 建立客户端 并制定连接的服务端
    val socket = Socket("192.168.43.213", 10338)

    // 键盘录入
    val scanner = Scanner(System.`in`)

    // 获取Socket 流的输出流
    val outputStream = socket.getOutputStream()
    val bufferedWriter = BufferedWriter(OutputStreamWriter(outputStream))
    // 获取Socket流 的 读取流
    val inputStream = socket.getInputStream()
    val serverReader = BufferedReader(InputStreamReader(inputStream))


    thread { readServerData(serverReader) }

    var line: String? = scanner.nextLine()

    while (line != null) {

        if ("over" == line) {
            //告诉服务器写完毕
            // socket.shutdownOutput()
            break
        }

        bufferedWriter.write(line)
        bufferedWriter.newLine()
        bufferedWriter.flush()

        line = scanner.nextLine()
    }

    bufferedWriter.close()
    socket.close()
}

private fun readServerData(serverReader: BufferedReader) {
    try {
        var string: String? = serverReader.readLine()
        while (string != null) {
            println("收到服务端数据： $string")
            string = serverReader.readLine()
        }
    } catch (e: Exception) {
        //e.printStackTrace()
    } finally {
        serverReader.close()
    }
}
