#########################
# 演示Python中的字符串
#########################

# 定义字符串
str1 = "abcdefghijklmnopqrstuvwxyz"
str2 = 'opqrst'
str3 = 'abcabcabc'

# 多行字符串
str4 = """234141
212
"""

# 双引号于单引号的不同.双引号内部可以使用单引号。
str5 = '''423414141'''
str6 = "3424234 '234234'----"
str7 = 'hello\
world'

print("str5 = %s" % str5)
print("str7 = %s" % str7)

# 字符串运算
str8 = str2 * 3
str9 = str1 + str2
print("str8 = %s" % str8)
print("str9 = %s-%s" % (str9, str8))

# 遍历字符串
for s in str1:
    print(s)

# 通过下标访问
print(str1[0])
print("type str[x] = %s" % type(str1[0]))

# 通过下标获取的还是字符串
print(type(str1[1]))

# 字符串切片
print(str1[1:4])
print(str1[0:8:2])  # 2表示步长
print(str1[-1:])

# 原始(非转义)字符串,语法R
print("使用R表示原始字符串 = %s" % R"\n")
