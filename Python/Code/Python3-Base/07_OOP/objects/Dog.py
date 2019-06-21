#########################
# 演示定义类
#########################
import pkg.Tools as Tools


class Dog:
    """一只狗"""

    # 类的构造方法
    def __init__(self, name, age):
        self.__name = name  # 对象属性的定义和初始化，name是实例变量，使用双下划线表示私有对象属性，Python会把私有属性__name的名称修改为：_Dog__name
        self.__age = age

    # 实例方法：按照约定，任何实例方法的第一个参数应该是self(可以是其他名称)，self表示调用这个方法的实例对象，示例方法由对象调用
    def sit(self):
        print(self.__name.title() + " is new sitting")

    def roll_over(self):
        print(self.__name.title() + " rolled over!")


Tools.print_divider("调用对象方法")
my_dog = Dog("小黄", 2)
my_dog.sit()
my_dog.roll_over()

Tools.print_divider("访问对象的私有属性问题")
# 严格意义上来讲，Python并没有所谓的私有变量，也不会阻止你去访问对象的任何变量，对私有变量的保护只是一个重命名
# print(my_dog.__name) # 这里报错
print(my_dog._Dog__name)  # 这里没有报错
my_dog.__name = "小黑"  # 并没有访问到my_dog的__name属性，这里只是给my_dog对象添加了一个__name属性
print(my_dog.__name)

# 对象内置方法和内置属性
Tools.print_divider("对象内置方法和内置属性")

attDict = Dog.__dict__  # 类的属性（包含一个字典，由类的数据属性组成）

for key, value in zip(attDict.keys(), attDict.values()):
    print(key, " = ", value)

print("类的文档字符串 = ", Dog.__doc__)  # 类的文档字符串
print("类名 = ", Dog.__name__)  # 类名
print("类定义所在的模块 = ", Dog.__module__)  # 类定义所在的模块
print("父类 = ", Dog.__bases__)  # 父类
