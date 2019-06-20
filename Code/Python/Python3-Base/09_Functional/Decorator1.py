#########################
# 装饰器：一般的装饰器
#########################

import time


def time_track(func):
    # args为了保证可以接收不定参数的调用
    # kwargs为了保证可以装饰 **kwargs的函数调用
    def wrapper(*args, **kwargs):
        start = time.time()
        print("call function %s at %f" % (func.__name__, start))
        result = func(*args, **kwargs)
        print("use time = ", time.time() - start)
        return result

    return wrapper


@time_track
def add(x, y):
    return x + y


@time_track
def print_function():
    print("this is a function")


print(add(100, 200))
print(add.__name__)
print_function()  # 原理就是 time_track(print_function)
