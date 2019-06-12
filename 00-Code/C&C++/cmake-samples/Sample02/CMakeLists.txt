cmake_minimum_required(VERSION 3.7)
project(Sample02)

set(CMAKE_C_STANDARD 99)

#方式1
set(SOURCE_FILES Source/main.c Source/Lib.c)
add_executable(Sample02 ${SOURCE_FILES})

#方式2
# 查找Source目录下的所有源文件，并将名称保存到 DIR_SRCS 变量
#aux_source_directory(Source DIR_SRCS)
#add_executable(Sample02 ${DIR_SRCS})