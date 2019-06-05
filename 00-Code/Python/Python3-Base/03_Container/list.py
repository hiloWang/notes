#########################
# 演示列表
#########################

# 定义列表
listA = [1, 2, 3, 4, 5, 6, 7, 8, 9]

# 遍历
for a in listA:
    print("%d " % a, end="")
print()

# 创建一个枚举，以支持带索引的遍历
chars = ['a', 'b', 'c', 'd']
for i, ch in enumerate(chars):
    print(i, ch)

# 插入操作
listA.append(11)  # 添加到末尾
listA.insert(len(listA), 12)  # 插入

# 通过extend可以将另一个集合中的元素逐一添加到列表中
list12 = [1, 2]
list34 = [3, 4]
list12.extend(list34)  # 接收可迭代的序列
print(list12)

# 删除
listA.remove(1)  # 根据元素删除
listA.pop()  # 弹出
del listA[3]  # 根据下标删除元素
print(listA)

# 改
listA[1] = 1000

# 查
print(20 in listA)
print(222 not in listA)
print(listA.count(1000))  # 出现的次数
print(listA.index(4))  # 索引

# 排序
listC = ["b", "c", "a", "f"]
print(sorted(listC))  # 排序，返回新的列表
listC.sort()  # 自身排序
listC.reverse()  # 反转

# 列表的长度
print(len(listA))

# range类：
#
#         1， 使用range能够轻松的生成一系列的数组
#         2，使用range创建列表，同时range还可以指定步长

for value in range(1, 4):
    print("%d " % value, end="")
print()

for value in list(range(3, 10)):
    print("%d " % value, end="")
print()

# min、max、sum方法
print(min(listA))
print(max(listA))
print(sum(listA))

# 列表解析
#
#     使用列表解析语法的步骤：
#         1：制定一个描述性的类表明：listD
#         2：定义表达式，用于生成要存储到列表的的值：value ** 2
#         3：编写一个for循环，用于给表达式提供值

listD = [value ** 2 for value in range(2, 20)]
print(listD)

# 切片
print(listA[:])  # 可用于复制列表
print(listA[3:])
print(listA[4:6])
print(listA[-2:-1])

# 元组to列表
dimensions = (1, 2, 3, 4, 5)
print(list(dimensions))
