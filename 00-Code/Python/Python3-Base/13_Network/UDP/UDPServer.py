from socket import *

# 1. 创建套接字
udpSocket = socket(AF_INET, SOCK_DGRAM)

# 2. 绑定本地的相关信息
bindAddress = ('', 7788)  # ip地址和端口号，ip一般不用写，表示本机的任何一个ip
udpSocket.bind(bindAddress)

num = 1
while True:
    # 3. 等待接收对方发送的数据
    receiveData = udpSocket.recvfrom(1024)  # 1024表示本次接收的最大字节数

    # 4. 将接收到的数据再发送给对方
    udpSocket.sendto(receiveData[0], receiveData[1])

    # 5. 统计信息
    print('已经将接收到的第%d个数据返回给对方,内容为:%s' % (num, receiveData[0]))
    num += 1
    if num > 10:
        break

# 5. 关闭套接字
udpSocket.close()
