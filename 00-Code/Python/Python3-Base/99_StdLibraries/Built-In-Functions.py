#########################
# 演示python内置函数 参考 http://www.runoob.com/python/python-built-in-functions.html
#########################

# len(item) 计算容器中元素个数
print(len("A"))

# max/min 返回容器中元素最大值/最小值
item = [1, 2, 3]
max(item),
min(item),

# 删除变量，两种方式，del(a) 或者 del a
a = 3
del a

# 求绝对值
abs(-1)

#  divmod 函数把除数和余数运算结果结合起来，返回一个包含商和余数的元组(a // b, a % b)。
divmod(10, 3)

# all 用于判断给定的可迭代参数 iterable 中的所有元素是否不为 0、''、False 或者 iterable 为空，如果是返回 True，否则返回 False。
all([1, 2, 3, 4])

# ord() 函数是 chr() 函数（对于8位的ASCII字符串）或 unichr() 函数（对于Unicode对象）的配对函数，
# 它以一个字符（长度为1的字符串）作为参数，返回对应的 ASCII 数值，或者 Unicode 数值，
# 如果所给的 Unicode 字符超出了你的 Python 定义范围，则会引发一个 TypeError 的异常。
ord('a')
