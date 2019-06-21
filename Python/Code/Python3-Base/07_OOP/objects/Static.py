#########################
# 静态属性和方法
#########################


class TestStatic:
    country = 'China'  # 类属性
    _city = "BeiJin"

    # 类方法：是类对象所拥有的方法，第一个参数必须是类对象，一般约定为cls
    @classmethod
    def test_class(cls):
        return cls.country

    @classmethod
    def test_class_set(cls, country):
        cls.country = country

    # 静态方法：不需要定义任何参数，用得比较少，和一个普通函数没有多少差别
    @staticmethod
    def test_static():
        return TestStatic.country


print(TestStatic.country)
print(TestStatic.test_class())
print(TestStatic.test_static())
