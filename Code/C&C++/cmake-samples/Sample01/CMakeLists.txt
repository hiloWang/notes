#指定要求的版本
cmake_minimum_required(VERSION 3.7)

#指定项目信息
project(Sample01)

#指定C标准版本
set(CMAKE_C_STANDARD 99)

#指定需要编译的源
set(SOURCE_FILES main.c)
add_executable(Sample01 ${SOURCE_FILES})