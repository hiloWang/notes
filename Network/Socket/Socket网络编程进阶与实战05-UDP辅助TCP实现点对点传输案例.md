# UDP 辅助 TCP 实现点对点传输案例

编码实现：

1. TCP客户端事先不知道TCP服务端的IP和端口。
2. TCP服务端提供了通过UDP获取TCP服务端IP和端口的服务，TCP客户端事先知道服务端UDP的端口。
3. 客户端通过发送广播获取TCP服务端的IP和TCP服务端口。然后建立TCP连接。

[示例代码](https://github.com/Ztiany/Programming-Notes-Code/tree/master/Java/Java-Socket/src/main/java/immoc/socket/l5)


