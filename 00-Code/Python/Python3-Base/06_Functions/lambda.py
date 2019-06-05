#########################
# 匿名函数
#########################

# 匿名函数示例
sum = lambda a, b, c: (a + b + c)
print(sum(1, 3, 4))

# 匿名函数应用
functionNew = input("请输入一个lambda表达式：")
print(type(functionNew))
functionNew = eval(functionNew)
print(type(functionNew))


def test(x, y, func):
    return func(x, y)


print(test(3, 4, functionNew))
