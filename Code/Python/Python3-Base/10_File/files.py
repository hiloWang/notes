# 演示文件操作

# 用with方式打开文件不需要关闭，运行时会自动关闭
with open('a.txt', 'r', encoding='utf8') as file_a:
    for line in file_a:
        print(line.rstrip())

# 一般的方式
file_a = open('a.txt', 'r', encoding='utf8')
for line in file_a:
    print(line.rstrip())
file_a.close()

# 写文件
file_a = open('a.txt', 'a+', encoding='utf8')
file_a.write("Python 写入")
file_a.close()

# 读取文件
file_a = open('a.txt', 'r', encoding='utf8')
content = file_a.read()
print(content)
print(type(content))
print(type(file_a))
file_a.close()

# 读取二进制文件
file_b = open('b', 'rb')
content = file_b.read(1)
print(content)
print(type(content))  # bytes
print(type(file_b))  # BufferedReader
file_b.close()

# 文件定位
file_a = open('a.txt', 'r', encoding='utf8')
# seek（offset [,from]）方法改变当前文件的位置。
# 如果from被设为0，这意味着将文件的开头作为移动字节的参考位置。
# 如果设为1，则使用当前的位置作为参考位置。
# 如果它被设为2，那么该文件的末尾将作为参考位置。
file_a.seek(20, 0)
print(file_a.tell())  # tell()方法告诉你文件内的当前位置
print(file_a.read(10))
file_a.close()
