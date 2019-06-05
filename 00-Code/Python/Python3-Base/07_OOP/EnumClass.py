#########################
#  枚举
#########################

from enum import Enum, IntEnum, unique
from pkg.Tools import print_divider


class VIP(Enum):
    YELLOW = 1
    YELLOW_ALIAS = 1  # 相当于YELLOW的别名
    GREEN = 2
    BLACK = 3
    RED = 4


# 所有枚举的值必须为int
class IntVIP(IntEnum):
    GREEN = 2
    BLACK = 3


# 所有枚举的值必须为唯一装饰器
@unique
class UniqueVIP(IntEnum):
    GREEN = 2
    BLACK = 3


print_divider("枚举操作")
print(type(VIP.YELLOW))
print(VIP.YELLOW)
print(type(VIP.YELLOW.name))
print(VIP.YELLOW.name)
print(VIP['GREEN'])
print(VIP.GREEN.value)

print_divider("枚举遍历")
for vip in VIP:
    print(vip)

print_divider("枚举遍历，包括别名")
for vip in VIP.__members__:
    print(vip)

print_divider("枚举类型转换")
a = 3
vip = VIP(a)  # 转换为枚举，实质上时是根据数值访问具体的枚举类型
if vip == VIP.YELLOW:
    print("黄砖")
if vip == VIP.GREEN:
    print("绿砖")
if vip == VIP.RED:
    print("红砖")
if vip == VIP.BLACK:
    print("黑砖")
