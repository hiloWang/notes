#########################
# 列表解析式
#########################

# 列表
list1 = [i for i in range(10)]  # 默认0开始
list2 = [i for i in range(0, 10) if i % 2 == 0]
list3 = [(i, j) for i in range(3) for j in range(2)]
list4 = [m + n for m in 'ABC' for n in 'XYZ']
org_dict = {'x': 1, 'y': 2, 'z': 3}

print("list1: ", list1)
print("list2: ", list2)
print("list3: ", list3)
print("list4: ", list4)

# 字典
dict1 = {key: value for (value, key) in org_dict.items()}  # 反转字典

print("dict1: ", dict1)
