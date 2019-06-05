package me.ztiany.socket.udp

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress


/*


### UDP示例

DatagramSocket具有发送和接收数据的功能，因为数据包中包含的信息较多，为了操作这些信息方便，也一样会将其封装成对象。这个数据包对象就是DatagramPacket

udp的发送端：

1. 建立udp的socket服务，创建对象时如果没有明确端口，系统会自动分配一个未被使用的端口。
2. 明确要发送的具体数据。
3. 将数据封装成了数据包。
4. 用socket服务的send方法将数据包发送出去。
5. 关闭资源。

udp的接收端：

1. 创建udp的socket服务，必须要明确一个端口，作用在于，只有发送到这个端口的数据才是这个接收端可以处理的数据。
2. 定义数据包，用于存储接收到数据。
3. 通过socket服务的接收方法将收到的数据存储到数据包中。
4. 通过数据包的方法获取数据包中的具体数据内容，比如ip、端口、数据等等。
5. 关闭资源。

 */

/**
 *
 * @author Ztiany
 *          Email ztiany3@gmail.com
 *          Date 17.12.2 13:10
 */

fun main(args: Array<String>) {
    // 创建Udp服务 设置端口
    val datagramSocket = DatagramSocket(12990)
    // 定义数据包 用于存储数据
    val bufferedReader = BufferedReader(InputStreamReader(System.`in`))

    var line: String?
    line = bufferedReader.readLine()
    while (line != null) {

        if ("over" == line) {
            break
        }

        val buf = line.toByteArray()

        // 发送数据
        val datagramPacket = DatagramPacket(buf, buf.size, InetAddress.getLocalHost(), 12980)
        //阻塞方法用于发送数据
        datagramSocket.send(datagramPacket)

        line = bufferedReader.readLine()
    }
    bufferedReader.close()
    datagramSocket.close()
}

