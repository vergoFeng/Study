# 指定cmake最小支持版本号
cmake_minimum_required(VERSION 3.4.1)

## 变量
#set(var 123)
#message("var = ${var}")
#
## 列表
#set(list_var 1 2 3)
#message("list_var = ${list_var}")
#
## 条件命令
#set(if_tap OFF)
#set(elseif_tap ON)
#if (${if_tab})
#    message("if")
#elseif (${elseif_tap})
#    message("elseif")
#else (${if_tab})
#    message("else")
#endif (${if_tab})
#
## 循环命令
##set(a "")
##while(NOT a STREQUAL "xxxxx")
##    set(a "${a}x")
##    message("a = ${a}")
##endwhile()
#
## 第一种
##foreach(item 1 2 3 4 5)
##    message("item = ${item}")
##endforeach(item)
#
## 第二种
##foreach(item RANGE 5)
##    message("item = ${item}")
##endforeach(item)
#
## 第三种
##foreach(item RANGE 1 5 2)
##    message("item = ${item}")
##endforeach(item)
#
## 第四种
#foreach (item IN LISTS list_var)
#    message("item = ${item}")
#endforeach (item)
#
## 函数
#function(func a b c)
#    message("a = ${a}")
#    message("b = ${b}")
#    message("c = ${c}")
#    # cmake中内置变量
#    # ARGC：参数个数
#    message("参数个数 ARGC = ${ARGC}")
#    # ARGV：所有参数列表
#    message("参数列表 ARGV = ${ARGV}")
#
#    # ARGV0/1/2..n：第(n+1)个参数值
#    message("第1个参数值 ARGV = ${ARGV0}")
#    message("第2个参数值 ARGV = ${ARGV1}")
#    message("第3个参数值 ARGV = ${ARGV2}")
#    message("第4个参数值 ARGV = ${ARGV3}")
#endfunction(func)
#func(123 456 789)
#
## 宏命令
#macro(ma a b)
#    message("a = ${a}")
#    message("b = ${b}")
#endmacro(ma)
#ma("hello" "world")

# 添加头文件
include_directories(${CMAKE_SOURCE_DIR}/inc)

# 添加一个库，根据native-lib.cpp源文件编译一个native-lib的动态库
add_library(
        native-lib
        SHARED
        native-lib.cpp)

# 设置第三方so库的路径
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -L${CMAKE_SOURCE_DIR}/../jniLibs/${CMAKE_ANDROID_ARCH_ABI}")

# 查找系统库，这里查找的是系统日志库，并赋值给变量log-lib
#find_library(
#        log-lib
#        log)

# 设置依赖的库（第一个参数必须为目标模块，顺序不能换）
target_link_libraries(native-lib fmod fmodL log)