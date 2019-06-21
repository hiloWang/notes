#  演示重命名和删除文件
import os

# chdir()方法来改变当前的目录

# 获取当前路径
print(os.getcwd())

# os.remove("c.txt")  # 删除文件
os.mkdir("dir_c")  # 创建目录
os.rmdir("dir_c")  # 删除目录
# os.rename("test2.txt", 'test1.txt')  # 重命名
print(os.curdir)
print(os.pardir)
file_list = os.listdir(".")
for file in file_list:
    print(file, os.path.isfile(file), os.path.isdir(file))

# os的path相关操作
try:
    print(os.path.isdir("dir_c"))
    print(os.path.isfile("dir_c"))
    print(os.path.exists("dir_c"))
except FileNotFoundError as e:
    pass
