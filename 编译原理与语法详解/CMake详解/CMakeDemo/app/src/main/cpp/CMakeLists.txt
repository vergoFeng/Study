# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

# 变量
set(var 123)
message("var = ${var}")

# 列表
set(list_var 1 2 3)
message("list_var = ${list_var}")

# 条件命令
set(if_tap OFF)
set(elseif_tap ON)
if(${if_tab})
    message("if")
elseif(${elseif_tap})
    message("elseif")
else(${if_tab})
    message("else")
endif(${if_tab})

# 循环命令
#set(a "")
#while(NOT a STREQUAL "xxxxx")
#    set(a "${a}x")
#    message("a = ${a}")
#endwhile()

# 第一种
#foreach(item 1 2 3 4 5)
#    message("item = ${item}")
#endforeach(item)

# 第二种
#foreach(item RANGE 5)
#    message("item = ${item}")
#endforeach(item)

# 第三种
#foreach(item RANGE 1 5 2)
#    message("item = ${item}")
#endforeach(item)

# 第四种
foreach(item IN LISTS list_var)
    message("item = ${item}")
endforeach(item)

# 函数
function(func a b c)
    message("a = ${a}")
    message("b = ${b}")
    message("c = ${c}")
    # cmake中内置变量
    # ARGC：参数个数
    message("参数个数 ARGC = ${ARGC}")
    # ARGV：所有参数列表
    message("参数列表 ARGV = ${ARGV}")

    # ARGV0/1/2..n：第(n+1)个参数值
    message("第1个参数值 ARGV = ${ARGV0}")
    message("第2个参数值 ARGV = ${ARGV1}")
    message("第3个参数值 ARGV = ${ARGV2}")
    message("第4个参数值 ARGV = ${ARGV3}")
endfunction(func)
func(123 456 789)

# 宏命令
macro(ma a b )
    message("a = ${a}")
    message("b = ${b}")
endmacro(ma)
ma("hello" "world")

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
             native-lib

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             native-lib.cpp )

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
                       native-lib

                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} )