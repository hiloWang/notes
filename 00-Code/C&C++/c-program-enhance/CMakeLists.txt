cmake_minimum_required(VERSION 3.7)
project(c_program_enhance)

set(CMAKE_C_STANDARD 99)

set(SOURCE_FILES part08/03_function_pointer.c)
add_executable(c_program_enhance ${SOURCE_FILES})