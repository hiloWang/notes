#########################
# 装饰器：标准的装饰器
#########################

import functools


def log_fixed(text):
    def decorator(func):
        # @functools.wraps(func)修复函数的__name__错误问题
        @functools.wraps(func)
        def wrapper(*args, **kw):
            print('%s %s():' % (text, func.__name__))
            return func(*args, **kw)

        return wrapper

    return decorator


@log_fixed('execute')
def now_fixed():
    print('2015-3-25')


now_fixed()
print(now_fixed.__name__)
