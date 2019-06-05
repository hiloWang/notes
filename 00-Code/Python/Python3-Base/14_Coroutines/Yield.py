"""
CPU密集型任务：多进程
IO密集型任务：多线程、协程

实现协程：
- yield
- greenlet：pip install greenlet
- gevent：pip install gevent
"""

import time


def fa():
    while True:
        print("----A---")
        yield
        time.sleep(0.5)


def fb(c):
    while True:
        print("----B---")
        next(c)
        time.sleep(0.5)


if __name__ == '__main__':
    f = fa()
    fb(f)
