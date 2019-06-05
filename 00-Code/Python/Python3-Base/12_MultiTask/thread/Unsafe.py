#########################
#  线程共享变量问题
#########################


from threading import Thread
import time

g_num = 100


def work1():
    global g_num
    for i in range(1, 100):
        g_num += 1
        time.sleep(0.1)
        print("----in work1, g_num is %d---" % g_num)


print("---线程创建之前g_num is %d---" % g_num)

t1 = Thread(target=work1)
t1.start()
t2 = Thread(target=work1)
t2.start()

t1.join()
t2.join()

print("---最后g_num is %d---" % g_num)
