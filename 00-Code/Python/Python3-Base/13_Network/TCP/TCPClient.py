#########################
#  tcp客户端
#########################


# coding=utf-8
from socket import *

# 创建socket
tcpClientSocket = socket(AF_INET, SOCK_STREAM)

# 链接服务器
serAddress = ('192.168.0.109', 8088)
tcpClientSocket.connect(serAddress)

# 提示用户输入数据
sendData = input("请输入要发送的数据：")

tcpClientSocket.send(sendData.encode())

# 接收对方发送过来的数据，最大接收1024个字节
receive = tcpClientSocket.recv(1024)
print('接收到的数据为:', receive.decode())

# 关闭套接字
tcpClientSocket.close()
