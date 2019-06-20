#########################
# 演示python中的模块与包导入
#########################

import math  # 导入模块
import os
from calendar import calendar  # 导入模块的一部分
from time import *  # 导入模块内部所有元素
import sys
import pkg.module_a as ma  # 导入自己的模块
import pkg.module_b as mb  # 导入自己的模块
import pkg.module_a

print("time() = %s" % time())
print("calendar = ", calendar)
print("module_a = ", ma.ma_a)
print("module_b = ", mb.mb_a)
print("math.fabs(-1) = %s" % math.fabs(-1))
print(os.__file__)
print(os.path)
print(dir(sys))  # 打印一个模块里的元素
print("dir(pkg.module_a) = ", dir(pkg.module_a))

# 添加导入模块的搜索路径
# sys.path.append("../01_Basic")
# import data_type
