cmake_minimum_required(VERSION 3.7)
project(Sample03)

set(CMAKE_C_STANDARD 99)

#添加子目录
add_subdirectory(Lib)

set(SOURCE_FILES main.c)
add_executable(Sample03 ${SOURCE_FILES})

#添加链接库
target_link_libraries(Sample03 SubLib)