#########################
# 演示列表操作
#########################

list1 = [10, 0, 142, 5, 111, 23, 34, 88, 6, 7, 8, 9]
list2 = [{"key": "B"}, {"key": "A"}, {"key": "Z"}, {"key": "G"}, {"key": "E"}, {"key": "Q"}, {"key": "N"}, {"key": "S"}]

# sort排序
list1.sort(reverse=True)
print(list1)
list2.sort(key=lambda x: x['key'], reverse=True)
print(list2)
