#########################
# 演示各种String的常用操作方法
#########################


from pkg.Tools import print_divider

str1 = "abcdefghijklmnopqrstuvwxyz"
str2 = 'life is short, i use python, i love python'
str3 = '演示各种 String 的常用操作方法'
str4 = 'python.py'
str5 = 'ad dsf \t sfdf \n sdfdf j sd l a\t dsf  da r \t sdfa fd '

# find & rfind
print_divider("find")
index = str1.find("d", 2, len(str1))  # len 获取items的长度
print("find result = %d" % index)

# index
print_divider("index")
index = str1.index("d", 2, len(str1))  # len 获取items的长度
print("index result = %d" % index)

# count
print_divider("count")
count = str1.count('b', 0, 7)
print("count = " + str(count))

# replace
print_divider("replace")
print(str2.replace('python', 'java'))

# split
print_divider('split')
print(str2.split(' '))
print(str5.split())  # split不加任何字符，自动切割

# endswith startswitch
print_divider('endswith startswitch')
print(str4.endswith('.py'))
