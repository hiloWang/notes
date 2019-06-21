#########################
#  函数式编程：闭包
#########################
from pkg.Tools import print_divider

# 1 认识闭包
print_divider("闭包")


def curve_pre():
    a = 10

    def curve(x):
        return a * x * x

    return curve


curve = curve_pre()
print(curve(10))
print(curve)  # <function curve_pre.<locals>.curve at 0x0000023BC8D651E0>
print(curve.__closure__)  # (<cell at 0x0000023BC8D26CA8: int object at 0x0000000074E6F050>,)

# 2 闭包应用
print_divider("闭包应用")


def traveller(paces):
    def go(pace):
        nonlocal paces
        paces += pace
        return paces

    return go


counter = traveller(0)
print(counter(1))
print(counter(2))
print(counter(3))
print("counter.__closure__[0].cell_contents) = ", counter.__closure__[0].cell_contents)
