#########################
# 装饰器：带参数的装饰器
#########################

# 如果decorator本身需要传入参数，那就需要编写一个返回decorator的高阶函数


def log(text):
    def decorator(func):
        def wrapper(*args, **kw):
            print('%s %s():' % (text, func.__name__))
            return func(*args, **kw)

        return wrapper

    return decorator


@log('execute')
def now():
    print('2015-3-25')


now()  # 相当于log('execute')(now)
# 首先执行log('execute')，返回的是decorator函数，再调用返回的函数，参数是now函数，返回值最终是wrapper函数，所以下面now.__name__结果是 wrapper
print(now.__name__)  # wrapper
