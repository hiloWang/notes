package com.ztiany.server

import okhttp3.WebSocket
import sun.rmi.runtime.Log
import okhttp3.WebSocketListener
import okhttp3.mockwebserver.MockResponse
import com.sun.deploy.util.URLUtil.getPort
import okhttp3.Response
import okhttp3.mockwebserver.MockWebServer


/**
 *
 * @author ztiany
 *          Email: ztiany3@gmail.com
 */

fun main(args: Array<String>) {
    startServer()
}

fun startServer() {

    val mockWebServer = MockWebServer()

    var server: WebSocket

    val server_address = "ws://" + mockWebServer.hostName + ":" + mockWebServer.port + "/"

    println("server start success, address= {$server_address}")

    mockWebServer.enqueue(MockResponse().withWebSocketUpgrade(

            object : WebSocketListener() {

                override fun onOpen(webSocket: WebSocket, response: Response) {
                    server = webSocket
                    webSocket.send("hello, I am  WebServer !")
                    println("connect client, handle it by " + webSocket)
                }

                override fun onMessage(webSocket: WebSocket?, text: String?) {
                    println("收到客户端消息={$text}")
                }

                override fun onClosed(webSocket: WebSocket?, code: Int, reason: String?) {

                }

                override fun onFailure(webSocket: WebSocket?, t: Throwable?, response: Response?) {

                }

            }))
}