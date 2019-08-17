cmake_minimum_required(VERSION 3.12)
project(Code)

set(CMAKE_C_STANDARD 99)

include_directories(lib)
add_subdirectory(lib)

add_executable(Code chapter06/example/00_get_first_command.c)
target_link_libraries(Code libtlpi)