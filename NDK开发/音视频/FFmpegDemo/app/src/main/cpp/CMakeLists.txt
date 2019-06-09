cmake_minimum_required(VERSION 3.4.1)

file(GLOB SOURCE *.cpp)
add_library( # Sets the name of the library.
        player-lib
        SHARED
        ${SOURCE})
find_library( # Sets the name of the path variable.
        log-lib
        log)
include_directories(${CMAKE_SOURCE_DIR}/include)

#set(my_lib_path  ${CMAKE_SOURCE_DIR}/src/main/jniLibs/${ANDROID_ABI})
#set(CMAKE_CXX_FLAGS  "${CMAKE_CXX_FLAGS} -L${my_lib_path}")
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -L${CMAKE_SOURCE_DIR}/../jniLibs/${CMAKE_ANDROID_ARCH_ABI}")

target_link_libraries( # Specifies the target library.
        player-lib
        avfilter avformat avcodec avutil swresample swscale
        ${log-lib}
        z
        OpenSLES
        android
        )