#########################
#  select版非阻塞
#########################

"""
select：
在多路复用的模型中，比较常用的有select模型和epoll模型。这两个都是系统接口，由操作系统提供。当然，Python的select模块进行了更高级的封装。
网络通信被Unix系统抽象为文件的读写，通常是一个设备，由设备驱动程序提供，驱动可以知道自身的数据是否可用。支持阻塞操作的设备驱动通常会实现一组自身的等待队列，
如读/写等待队列用于支持上层(用户层)所需的block或non-block操作。设备的文件的资源如果可用（可读或者可写）则会通知进程，反之则会让进程睡眠，等到数据到来可用的时候，
再唤醒进程。这些设备的文件描述符被放在一个数组中，然后select调用的时候遍历这个数组，如果对于的文件描述符可读则会返回改文件描述符。当遍历结束之后，
如果仍然没有一个可用设备文件描述符，select让用户进程则会睡眠，直到等待资源可用的时候在唤醒，遍历之前那个监视的数组。每次遍历都是依次进行判断的。

优点：select目前几乎在所有的平台上支持，其良好跨平台支持也是它的一个优点。

缺点：select的一个缺点在于单个进程能够监视的文件描述符的数量存在最大限制，在Linux上一般为1024，可以通过修改宏定义甚至重新编译内核的方式提升这一限制，
但是这样也会造成效率的降低。一般来说这个数目和系统内存关系很大，具体数目可以cat /proc/sys/fs/file-max察看。32位机默认是1024个。64位机默认是2048.
对socket进行扫描时是依次扫描的，即采用轮询的方法，效率较低。当套接字比较多的时候，每次select()都要通过遍历FD_SETSIZE个Socket来完成调度，
不管哪个Socket是活跃的，都遍历一遍。这会浪费很多CPU时间。
"""

import socket
from queue import Queue
from select import select

SERVER_IP = ('', 9999)

# 保存客户端发送过来的消息,将消息放入队列中
message_queue = {}
input_list = []
output_list = []

if __name__ == "__main__":
    server = socket.socket()
    server.bind(SERVER_IP)
    server.listen(10)
    # 设置为非阻塞
    server.setblocking(False)

    # 初始化将服务端加入监听列表
    input_list.append(server)

    while True:
        # 开始 select 监听,对input_list中的服务端server进行监听
        stdinput, stdoutput, stderr = select(input_list, output_list, input_list)

        # 循环判断是否有客户端连接进来,当有客户端连接进来时select将触发
        for obj in stdinput:
            # 判断当前触发的是不是服务端对象, 当触发的对象是服务端对象时,说明有新客户端连接进来了
            if obj == server:
                # 接收客户端的连接, 获取客户端对象和客户端地址信息
                conn, addr = server.accept()
                print("Client %s connected! " % str(addr))
                # 将客户端对象也加入到监听的列表中, 当客户端发送消息时 select 将触发
                input_list.append(conn)
                # 为连接的客户端单独创建一个消息队列，用来保存客户端发送的消息
                message_queue[conn] = Queue()

            else:
                # 由于客户端连接进来时服务端接收客户端连接请求，将客户端加入到了监听列表中(input_list)，客户端发送消息将触发
                # 所以判断是否是客户端对象触发
                try:
                    recv_data = obj.recv(1024)
                    # 客户端未断开
                    if recv_data:
                        print("received %s from client %s" % (recv_data, str(addr)))
                        # 将收到的消息放入到各客户端的消息队列中
                        message_queue[obj].put(recv_data)

                        # 将回复操作放到output列表中，让select监听
                        if obj not in output_list:
                            output_list.append(obj)

                except ConnectionResetError:
                    # 客户端断开连接了，将客户端的监听从input列表中移除
                    input_list.remove(obj)
                    # 移除客户端对象的消息队列
                    del message_queue[obj]
                    print("\n[input] Client %s disconnected" % str(addr))

        # 如果现在没有客户端请求,也没有客户端发送消息时，开始对发送消息列表进行处理，是否需要发送消息
        for sendobj in output_list:
            try:
                # 如果消息队列中有消息,从消息队列中获取要发送的消息
                if not message_queue[sendobj].empty():
                    # 从该客户端对象的消息队列中获取要发送的消息
                    send_data = message_queue[sendobj].get()
                    sendobj.send(send_data)
                else:
                    # 将监听移除等待下一次客户端发送消息
                    output_list.remove(sendobj)

            except ConnectionResetError:
                # 客户端连接断开了
                del message_queue[sendobj]
                output_list.remove(sendobj)
                print("\n[output] Client  %s disconnected" % str(addr))
