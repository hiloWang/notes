#########################
# 演示Python中的函数
#########################


# 定义函数
def get_name():
    return 'Jordan'


# 函数的说明，下面的注释(字符串)是函数说明
def add(b, a):
    """用来对两个数求和"""
    return a + b


# 不按顺序传参调用函数
print(add(a=3, b=4))


# 含有默认值的函数
def get_completed_name(first, end, middle=""):
    return str(first) + str(middle) + str(end)


print(get_completed_name("Zhan", "Tianyou"))
print(get_completed_name("Zhan", "Tianyou", "-Big-"))


# * 与 **
# 对于 *形参 python创建一个空的元组，然后把接收到的参数存储在元组中
# **形参接收的是字典

def arg(name, *numbers, **address):
    print(name)
    print(numbers)
    print(type(numbers))
    print(address)
    print(type(address))


arg("ztiany", "139", "158", "188", beijin="故宫", changsha="橘子洲", shenzhen="白石龙")

# 作用域
g_num = 100


def change_num():
    global g_num  # 使用global才表示使用的时全局的g_num
    g_num = 1
    return


change_num()
print("g_num = ", g_num)
