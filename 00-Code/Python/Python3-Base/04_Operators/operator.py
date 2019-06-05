#########################
# 演示Python中的运算符
#########################

# 运算符
a = 10
b = 4
print(a ** b)  # 幂运算
print(a / b)  # 除发，返回浮点数
print(a // b)  # 整除

# 数据类型转换
s = str(a)
print(type(s))
word = "324"
c = int(word)
print(type(c))

# 符号+： 表示合并，可用于字符串、列表、元组
print("aa" + "bb")
print([1, 2] + [3, 4])
print((1, 2) + (4, 5))

# 符号*：表示复制，可用于字符串、列表、元组
print("aa" * 4)
print([1, 2] * 3)
print((1, 2) * 2)

# in表示元素是否存在;
# not in表示元素是否不存在，可用于字符串、列表、元组、字典
print("a" in "aa")
print("dd" in [])
print("dd" in ())
print("dd" in {})
