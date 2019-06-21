#########################
#  元类
#########################


class UpperAttrMetaClass(type):
    def __new__(cls, future_class_name, future_class_parents, future_class_attr):
        print("cls = ", cls)  # 具体的class
        print("future_class_name = ", future_class_name)  # 类名
        print("future_class_parents = ", future_class_parents)  # 父类
        print("future_class_attr = ", future_class_attr)  # 属性和方法

        # 遍历属性字典，把不是__开头的属性名字变为大写
        new_attr = {}
        for name, value in future_class_attr.items():
            if not name.startswith("__"):
                new_attr[name.upper()] = value

        # return type(future_class_name, future_class_parents, new_attr)
        # return type.__new__(cls, future_class_name, future_class_parents, new_attr)
        return super(UpperAttrMetaClass, cls).__new__(cls, future_class_name, future_class_parents, new_attr)


class Foo(object, metaclass=UpperAttrMetaClass):
    bar = 'bip'


f1 = Foo()
print(Foo)
print(Foo.__class__)  # <class '__main__.UpperAttrMetaClass'>
print(f1)
print(f1.__class__)
print(dir(Foo))  # ['BAR', ......]
