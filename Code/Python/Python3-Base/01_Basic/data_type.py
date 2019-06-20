#########################
# 演示Python的数据类型
#########################

# 定义变量
age = 20  # 整型
name = 'Ztiany'  # 字符串
score = 32.44  # 浮点类型
isStudent = False  # 布尔类型
friends = ["张三", "李四", "王五", "赵六"]  # 列表
worker1, worker2 = "Wa", "Wb"  # 定义多个值，也可以使用["Wa", "Wb"]解包赋值

# 进制
binaryN = 0b10  # 二进制
octN = 0o77  # 八进制
hexN = 0xFF  # 16进制
print(bin(10))  # to二进制
print(oct(10))  # to八进制
print(hex(10))  # to十六进制
print(int(0xF0))  # to十进制

# 使用type函数获取变量的数据类型
print(type(age))
print(type(name))
print(type(score))
print(type(isStudent))
print(type(friends))

# 多赋值
a, b, c = 1, 3, "d"
print(a)
print(b)
print(c)
