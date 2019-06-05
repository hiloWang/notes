#########################
# 属性property
#########################


class Money1(object):
    def __init__(self):
        self.__money = 0

    def getMoney(self):
        print("Money1 get")
        return self.__money

    def setMoney(self, value):
        print("Money1 set")
        if isinstance(value, int):
            self.__money = value
        else:
            print("error:不是整型数字")

    money = property(getMoney, setMoney)


class Money2(object):
    def __init__(self):
        self.__money = 0

    @property
    def money(self):
        print("Money2 get")
        return self.__money

    @money.setter
    def money(self, value):
        print("Money2 set")
        if isinstance(value, int):
            self.__money = value
        else:
            print("error:不是整型数字")


money1 = Money1()
money1.money = 200
money2 = Money2()
money2.money = 100
