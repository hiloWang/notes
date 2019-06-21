# coding=utf-8

# 使用except而带多种异常类型
try:
    a = 9 / 0
except(ZeroDivisionError, SystemError):
    a = 0
else:
    print a


# 异常的参数
def convert(var):
    try:
        return int(var)
    except ValueError, argument:  # argument用于引用参数
        print "参数无法转换为数字:\n", argument


convert("xyz")
