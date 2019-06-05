#########################
# 演示异常处理
#########################

# 一般的捕获异常
try:
    a = 3 / 0
except ZeroDivisionError as error:  # 指定要捕获的异常
    a = 0
    print("error = %s" % error)
else:  # 没有发生异常时运行
    print("a=%d" % a)
finally:  # 始终运行
    print("run...")

# 捕获所有的异常
try:
    a = 4 / 0
except:  # BaseException as e
    a = 0
finally:
    print(a)

# try-finally 语句
try:
    a = 4 / 1
finally:
    print(a)

# 捕获多个的异常
try:
    a = 4 / 0
except (IOError, NameError, ZeroDivisionError):
    a = 0
finally:
    print(a)

# try-finally 语句
try:
    a = 4 / 1
finally:
    print(a)


# 抛出异常
def function_name(level):
    if level < 1:
        raise Exception("Invalid level!", level)
        # raise "哈哈"
        # 触发异常后，后面的代码就不会再执行


function_name(0)
