#########################
#  队列
#########################

import threading
import time
from queue import Queue  # python2：from Queue import Queue


class Producer(threading.Thread):
    """
    生产者
    """

    def run(self):
        global queue
        count = 0
        while True:
            if queue.qsize() < 1000:
                for i in range(100):
                    count = count + 1
                    msg = '生成产品' + str(count)
                    queue.put(msg)
                    print(msg)
            time.sleep(0.5)


class Consumer(threading.Thread):
    """
    消费者
    """

    def run(self):
        global queue
        while True:
            if queue.qsize() > 100:
                for i in range(3):
                    msg = self.name + '消费了 ' + queue.get()
                    print(msg)
            time.sleep(1)


if __name__ == '__main__':
    # 创建队列
    queue = Queue()

    for i in range(500):
        queue.put('初始产品' + str(i))
    for i in range(2):
        p = Producer()
        p.start()
    for i in range(5):
        c = Consumer()
        c.start()
