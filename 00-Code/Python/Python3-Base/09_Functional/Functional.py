#########################
# 函数式编程
#########################

from pkg.Tools import print_divider
from functools import reduce

# lambda表达式
print_divider('lambda表达式')
sum = lambda x, y: x + y
max = lambda x, y: x if x > y  else y  # x if x > y  else y 是Python中的三元表达式
print(sum(1, 2))
print(max(100, 200))

# map函数
print_divider('map函数')


def square(x):
    return x * x


list_x = [1, 2, 3, 4, 5, 6, 7, 8]
list_y = [1, 2, 3, 4, 5, 6]
list_z = [-7, -6, -5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8]

result = map(square, list_x)
print(list(result))
result = map(lambda x: x * x, list_x)
print(list(result))
result = map(lambda x, y: x * x + y, list_x, list_y)  # lambda定义了几个参数就应该传入几个参数集，以最小长度结果集进行运算
print(list(result))

# reduce函数
print_divider('reduce函数')
result = reduce(lambda x, y: x + y, list_x)
print(result)

# filter函数
print_divider('filter函数')
result = filter(lambda x: x > 5, list_x)
print(list(result))
result = filter(lambda x: x, list_z)
print(list(result))
