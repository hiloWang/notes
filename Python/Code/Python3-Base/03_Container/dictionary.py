#########################
# 演示字典(key value)
#########################

# 字典：字典存储的是键值对，不会记住存入元素的顺序

alien = {'a': "EA", "b": "阿凡达", 'c': "异形"}
print(alien)
print(type(alien))

# 获取元素
print(alien['a'])

# 在我们不确定字典中是否存在某个键而又想获取其值时，可以使用get方法
unknown = alien.get('f')
print("unknown = %s" % type(unknown))
# 还可以设置默认值
unknown = alien.get('f', "擎天柱")
print("unknown = %s" % unknown)

# 增加键值对
alien['d'] = '铁血战士'
print(alien)

# 修改字典中的值
alien['a'] = "超人"

# 遍历字典
for key, value in alien.items():  # 返回一个包含所有（键，值）元祖的列表
    print("key = " + key + " value = " + value)
for key in alien.keys():
    print(key)
for value in alien.values():
    print(value)

# 删除
del alien["c"]
print(alien)

# 清空整个字典
alien.clear()
print(alien)
