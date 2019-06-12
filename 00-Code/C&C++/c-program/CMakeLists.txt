cmake_minimum_required(VERSION 3.6)
project(c-program)

set(CMAKE_C_STANDARD 99)

set(SOURCE_FILES 08-ErrorHandler/ErrorHandle.c)
add_executable(C_Program ${SOURCE_FILES})