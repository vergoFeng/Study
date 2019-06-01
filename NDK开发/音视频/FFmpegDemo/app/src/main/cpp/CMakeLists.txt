cmake_minimum_required(VERSION 3.4.1)

# 添加头文件
include_directories(${CMAKE_SOURCE_DIR}/include)

add_library(
        native-lib
        SHARED
        native-lib.cpp)

message("${CMAKE_SOURCE_DIR}/../jniLibs/${CMAKE_ANDROID_ARCH_ABI}")
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -L${CMAKE_SOURCE_DIR}/../jniLibs/${CMAKE_ANDROID_ARCH_ABI}")

find_library(
        log-lib
        log)

target_link_libraries(
        native-lib
        avcodec avfilter avformat avutil swresample swscale
        ${log-lib})