#########################
#  协程非阻塞服务器
#########################


import gevent

from gevent import socket, monkey

# 如果有IO操作，需要调用此函数
monkey.patch_all()


def handle_request(conn):
    while True:
        data = conn.recv(1024)
        if not data:
            conn.close()
            break
        print("receive:", data)
        conn.send(data)


def server(port):
    s = socket.socket()
    s.bind(('', port))
    s.listen(5)
    while True:
        cli, addr = s.accept()
        gevent.spawn(handle_request, cli)


if __name__ == '__main__':
    server(7788)
