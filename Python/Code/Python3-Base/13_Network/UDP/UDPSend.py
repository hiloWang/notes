#########################
#  udp 发送数据
#########################

import socket

# 创建UDP套接字
udpSocket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
# 绑定本地端口，如果不绑定，则系统分配一个随机的端口
udpSocket.bind(('', 35539))  # ip地址和端口号，ip一般不用写，表示本机的任何一个ip)
# 准备接收方的地址
send_address = ('192.168.0.109', 8888)

while True:
    sendData = input("请输入要发送的数据，输入 quit退出：")
    print(sendData)
    if sendData == 'quit':
        break
    print(1)

    # 发送数据
    udpSocket.sendto(sendData.encode('UTF-8'), send_address)
    print(2)

    # 等待接收对方发送的数据，receiveData是元组类型
    receiveData = udpSocket.recvfrom(1024)  # 1024表示本次接收的最大字节数
    print(str(receiveData[0], encoding="GBK"))
    print(3)

udpSocket.close()
