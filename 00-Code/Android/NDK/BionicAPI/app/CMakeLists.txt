cmake_minimum_required(VERSION 3.4.1)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11")

# DIR
set(cpp_dir ${CMAKE_SOURCE_DIR}/src/main/cpp)

message("------------------------cmake: " ${cpp_dir})
message("------------------------cmake: " ${CMAKE_CXX_FLAGS})

# Android log
find_library(log-lib  log)

add_library(native-lib SHARED
${cpp_dir}/native-lib.cpp
${cpp_dir}/bionic-api.cpp
${cpp_dir}/threads-api.cpp
${cpp_dir}/socket-api.cpp
${cpp_dir}/tcp.cpp
${cpp_dir}/udp.cpp
${cpp_dir}/common.cpp
${cpp_dir}/local-server.cpp)


target_link_libraries(native-lib  ${log-lib})