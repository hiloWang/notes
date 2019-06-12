# 子目录下需要一个新的CMakeList文件

#设置源码路径
set(SOURCE_FILES Lib.c)

#add_library的意思是将目录中的源文件编译为静态链接库。
add_library(SubLib ${SOURCE_FILES})