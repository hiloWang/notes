package com.ztiany.client

import okhttp3.*
import okio.ByteString
import java.util.concurrent.TimeUnit
import kotlin.concurrent.timer

/**
 *
 * @author ztiany
 *          Email: ztiany3@gmail.com
 */
fun main(args: Array<String>) {
    val server_address = "ws://127.0.0.1:49438/"
    WebSocketClient(server_address)
}

private val okHttpClient = OkHttpClient.Builder()
        .retryOnConnectionFailure(true)
        .connectTimeout(10, TimeUnit.SECONDS)
        .build()

private fun getRequest(url: String): Request {
    return Request.Builder().get().url(url).build()
}

private class WebSocketClient(private val url: String) {

    private val listener = object : WebSocketListener() {

        override fun onOpen(webSocket: WebSocket?, response: Response?) {

            println("Client start success")

            var count = 0

            timer("client", false, 0, 3000, {
                webSocket?.send("client message, no ${count++}")
            })
        }

        override fun onFailure(webSocket: WebSocket?, t: Throwable?, response: Response?) {
            println("WebSocketClient.onFailure: " + t)
        }

        override fun onClosing(webSocket: WebSocket?, code: Int, reason: String?) {
            println("WebSocketClient.onClosing: " + reason)
        }

        override fun onMessage(webSocket: WebSocket?, text: String?) {
            println("收到服务器消息：" + text)
        }

        override fun onMessage(webSocket: WebSocket?, bytes: ByteString?) {
        }

        override fun onClosed(webSocket: WebSocket?, code: Int, reason: String?) {

        }
    }

    private val webSocket: WebSocket = okHttpClient.newWebSocket(getRequest(url), listener)

}
