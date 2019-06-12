cmake_minimum_required(VERSION 3.7)
project(Sample05_Check)

set(CMAKE_C_STANDARD 99)
set (MATH_VERSION_MAJOR 1)
set (MATH_VERSION_MINOR 0)

#包含当前目录，查找头文件
set(CMAKE_INCLUDE_CURRENT_DIR ON)

# 检查系统是否支持 pow 函数
include (${CMAKE_ROOT}/Modules/CheckFunctionExists.cmake)
check_function_exists (pow HAVE_POW)


# 加入一个配置头文件，用于处理 CMake 对源码的设置
configure_file (
        "${PROJECT_SOURCE_DIR}/config.h.in"
        "${PROJECT_BINARY_DIR}/config.h"
)


# 是否加入 MathLib 库
if (NOT HAVE_POW)
    include_directories ("${PROJECT_SOURCE_DIR}/Libs")
    add_subdirectory (Libs)
    set (EXTRA_LIBS ${EXTRA_LIBS} MathLib)
endif (NOT HAVE_POW)



#构建
#set(SOURCE_FILES main.c)
aux_source_directory(. SOURCE_FILES)
add_executable(Sample4 ${SOURCE_FILES} )
target_link_libraries (Sample4  ${EXTRA_LIBS})