cmake_minimum_required(VERSION 3.6)
project(LinuxProgram)

set(CMAKE_C_STANDARD 99)

set(SOURCE_FILES pthread/03.c)
add_executable(LinuxProgram ${SOURCE_FILES})