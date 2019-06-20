#########################
#  udp 广播服务端
#########################

import socket


def start_server():
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    s.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST, 1)

    s.bind(('', 8888))
    print('Listening for broadcast at ', s.getsockname())

    while True:
        data, address = s.recvfrom(65535)
        print('Server received from {}:{}'.format(address, data.decode('utf-8')))


start_server()
