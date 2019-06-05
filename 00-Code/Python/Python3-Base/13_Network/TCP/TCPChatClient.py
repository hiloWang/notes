from socket import *

# 创建socket
tcpClientSocket = socket(AF_INET, SOCK_STREAM)

# 链接服务器
serAddress = ('192.168.0.109', 7788)
tcpClientSocket.connect(serAddress)

while True:

    # 提示用户输入数据
    sendData = input("send: ")

    if len(sendData) > 0:
        tcpClientSocket.send(sendData)
    else:
        break

    # 接收对方发送过来的数据，最大接收1024个字节
    receiveData = tcpClientSocket.recv(1024)
    print('receive: ', receiveData)

# 关闭套接字
tcpClientSocket.close()


