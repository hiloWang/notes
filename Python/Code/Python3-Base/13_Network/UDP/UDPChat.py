#########################
#  udp 聊天
#########################

from threading import Thread
from socket import *


# 1. 收数据，然后打印
def receive_data():
    while True:
        receive_info = udpSocket.recvfrom(1024)
        print(">>%s:%s" % (str(receive_info[1]), receive_info[0]))


# 2. 检测键盘，发数据
def send_data():
    while True:
        send_info = input("<<")
        udpSocket.sendto(send_info.encode("gb2312"), (destIp, destPort))


udpSocket = None
destIp = ""
destPort = 0


def main():
    global udpSocket
    global destIp
    global destPort

    destIp = input("对方的ip:")
    destPort = int(input("对方的端口:"))

    udpSocket = socket(AF_INET, SOCK_DGRAM)
    udpSocket.bind(("", 4567))

    tr = Thread(target=receive_data)
    ts = Thread(target=send_data)

    tr.start()
    ts.start()

    tr.join()
    ts.join()


if __name__ == "__main__":
    main()
