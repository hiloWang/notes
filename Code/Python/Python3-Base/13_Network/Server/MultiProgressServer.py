#########################
#  多进程服务器
#########################

"""
通过为每个客户端创建一个进程的方式，能够同时为多个客户端进行服务
当客户端不是特别多的时候，这种方式还行，如果有几百上千个，就不可取了，因为每次创建进程等过程需要好较大的资源
"""
from socket import *
from multiprocessing import *


# 处理客户端的请求并为其服务
def dealWithClient(newSocket, destAddress):
    while True:
        reveiveData = newSocket.reveive(1024)
        if len(reveiveData) > 0:
            print('reveive[%s]:%s' % (str(destAddress), reveiveData))
        else:
            print('[%s]客户端已经关闭' % str(destAddress))
            break

    newSocket.close()


def main():
    serverSocket = socket(AF_INET, SOCK_STREAM)
    serverSocket.setsockopt(SOL_SOCKET, SO_REUSEADDR, 1)
    localAddress = ('', 7788)
    serverSocket.bind(localAddress)
    serverSocket.listen(5)

    try:
        while True:
            print('-----主进程，，等待新客户端的到来------')
            newSocket, destAddress = serverSocket.accept()

            print('-----主进程，，接下来创建一个新的进程负责数据处理[%s]-----' % str(destAddress))
            client = Process(target=dealWithClient, args=(newSocket, destAddress))
            client.start()

            # 因为已经向子进程中copy了一份（引用），并且父进程中这个套接字也没有用处了
            # 所以关闭
            newSocket.close()
    finally:
        # 当为所有的客户端服务完之后再进行关闭，表示不再接收新的客户端的链接
        serverSocket.close()


if __name__ == '__main__':
    main()
