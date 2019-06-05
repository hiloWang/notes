#########################
# 定义类，属性、方法、私有属性和方法
#########################


class Car:
    """一辆汽车"""

    __code = "2123120FJJ"  # private的类变量
    _motor = "牛逼的要死"  # protected的类变量
    country = "德国"  # public的类属型

    # 构造方法
    def __init__(self, make, year):
        self.make = make  # 对象属性
        self.year = year
        self.color = "red"
        self.__address = "china"  # 私有对象属性

    # 类似Java的toString方法
    def __str__(self):
        return "Car made in %s brand = %s year = %d color = %s" % (self.__address, self.make, self.year, self.color)

    # 析构函数，类似C++中的析构函数
    def __del__(self):
        print(self.__str__() + " was deleted")

    # 示例方法
    def get_address(self):
        return self.__address

    # 私有示例方法
    def __start_motor(self):
        print(self.make + "motor is start")

    # 示例方法
    def start(self):
        self.__start_motor()


BMW = Car("BMW", 2014)
BMW.color = "white"
print(BMW)
print(Car.country)  # 打印类变量
print(BMW.country)  # 如果实例对象没有该属性，则在类上查找
