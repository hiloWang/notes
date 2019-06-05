#########################
# 类的继承
#########################


class Person:
    """一个人"""

    def __init__(self, name, age):
        self.__name = name
        self.__age = age

    def get_name(self):
        return self.__name

    def get_age(self):
        return self.__age

    def eat(self, things):
        print(self.__name, " eat ", things)


class Man(Person):
    """男人"""

    def __init__(self, name, age):
        super().__init__(name, age)  # 调用父类的方法

    def work(self):
        print("man %s is working" % (self.get_name()))


class Driver:
    __is_driving = False

    def __init__(self, car_type):
        self.car_type = car_type

    def drive(self, car):
        self.__is_driving = True
        car.start()

    def eat(self, things):
        print("Driver ", " eat ", things)


#########################
# 多继承
#########################

class SpeedRacer(Person, Driver):
    """极速赛车手"""

    def __init__(self, name, age):
        Person.__init__(self, name, age)
        Driver.__init__(self, 100)
        # super().__init__(self,  age)
        # super(SpeedRacer, self).__init__(name=name, age=age) # 推荐

    def eat(self, things):
        super(SpeedRacer, self).eat("A")  # 这种方式调用集成的第一个父类的方法
        Driver.eat(self, things)  # 指定调用某个父类的方法


SpeedRacer("Ztiany", 18).eat(" 香蕉 ")  # Ztiany  eat  A Driver   eat   香蕉
sr = SpeedRacer("BaiLong", 18)
super(SpeedRacer, sr).eat(" 苹果 ")  # BaiLong  eat   苹果
print(SpeedRacer.__mro__)  # 决定方法查找顺序
