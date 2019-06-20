#########################
#  生成器
#########################

# 创建一个生成器
L = (x * 2 for x in range(5))
print(type(L))
print(L)

for value in L:
    print(value, end=' ')
print()


# 创建一个函数生成器

def fib(times):
    n = 0
    a, b = 0, 1
    while n < times:
        """
        在循环过程中不断调用 yield ，就会不断中断。当然要给循环设置一个条件来退出循环，不然就会产生一个无限数列出来。
        同样的，把函数改成generator后，我们基本上从来不会用 next() 来获取下一个返回值，而是直接使用 for 循环来迭代
        """
        yield b
        a, b = b, a + b
        n += 1
    return 'done'


for value in fib(10):
    print(value, end=' ')
print()

# 如果想要拿到返回值，必须捕获StopIteration错误，返回值包含在StopIteration的value中
try:
    fibSqu = fib(10)
    while True:
        print(next(fibSqu), end=' ')
except StopIteration as e:
    print("生成器返回值：%s" % e.value, end='')
print()


# 使用send

def gen():
    i = 0
    while i < 5:
        # temp为外部调用send函数时发送的值
        temp = yield i
        print('gen ', temp, end=' ')
        i += 1


f = gen()
print(f.__next__()) # 必须先调用 __next__或 send(None)
print('send ', f.send('a'))
print('send ', f.send('b'))
print('send ', f.send('c'))
