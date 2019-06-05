#########################
#  fork(only unix like)
#########################

import os
import time

pid = os.fork()

if pid == 0:
    print("父进程")
    time.sleep(10)
else:
    print('子进程')
    time.sleep(10)
