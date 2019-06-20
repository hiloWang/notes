#########################
# 演示Python中的流程控制语句
#########################

# if 语句
age = 18
password = "201314"
name = "ztiany"

inputName = input("请输入姓名：")
inputPwd = input("请输入密码：")
inputAge = input("请输入年龄：")

if int(inputAge) < age:
    print("你未成年，回去吧")
elif inputName != name or password != inputPwd:
    print("没有这个用户或者密码错误")
else:
    print("欢迎回来")

# while 语句 打印九九乘法表，while后面可以加else语句，表示while条件不满足时执行
startRow = 1
startColumn = 1
while startRow <= 9:
    while startColumn <= startRow:
        print("%d * %d = %d     " % (startColumn, startRow, startRow * startColumn), end="")
        startColumn += 1
    print()
    startRow += 1
    startColumn = 1

# for 循环
for x in "abc":
    print(x)
else:
    print("end")

for x in range(1, 100):
    print(x, end="")
