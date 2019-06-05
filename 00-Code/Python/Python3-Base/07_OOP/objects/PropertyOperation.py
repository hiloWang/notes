#########################
#  类的属性与相关内置函数
#########################


class Test:
    def __init__(self):
        self.a = 12

    # 为对象添加属性时会触发此方法
    def __setattr__(self, key, value):
        print('__setattr__ - >', 'key = ', key, "value = ", value)
        object.__setattr__(self, key, value)

    # 获取对象没有的属性时，才触发此方法
    def __getattr__(self, item):
        print('__getattr__ -> item = ', item)
        return "abc"

    # 获取读写的任何属性(包括)方法时会触发此方法
    def __getattribute__(self, item):
        print('__getattribute__ -> item = ', item)
        return object.__getattribute__(self, item)


test = Test()

# hasattr(obj,name) : 检查是否存在一个属性。
print(hasattr(Test, "a"))
# print(hasattr(test, "a"))

# 访问对象的属性。
print(getattr(test, "a"))  # 触发 __getattribute__

# 设置一个属性。如果属性不存在，会创建一个新属性。
# setattr(test, "a", 222)
# print(getattr(test, "a"))

# delattr(obj, name) : 删除属性。
delattr(test, "a")
print(hasattr(test, "a"))
