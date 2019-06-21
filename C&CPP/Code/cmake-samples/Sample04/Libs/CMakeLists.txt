# 设置源码路径并将名称保存到 DIR_SOURCE_FILES 变量
set(DIR_SOURCE_FILES Lib.c)

# 指定生成 Lib 静态链接库
add_library (MathLib ${DIR_SOURCE_FILES})