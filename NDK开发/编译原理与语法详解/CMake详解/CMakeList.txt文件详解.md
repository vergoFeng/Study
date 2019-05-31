## CMakeList.txt文件详解

### CMakeList.txt简析

使用Android Studio创建一个Native C++项目，默认会在app/src/main目录下生成cpp目录，里面包含 CMakeList.txt 和 native-lib.cpp。

CMakeList.txt文件

```cmake
# 指定cmake最小支持版本号
cmake_minimum_required(VERSION 3.4.1)
# 添加一个库，根据native-lib.cpp源文件编译一个native-lib的动态库
add_library(
		native-lib
		SHARED
		native-lib.cpp )
# 查找系统库，这里查找的是系统日志库，并赋值给变量log-lib
find_library(
		log-lib
		log )
# 设置依赖的库（第一个参数必须为目标模块，顺序不能换）
target_link_libraries(
		native-lib
		${log-lib} )
```

### 常用命令

#### cmake_minimum_required

```cmake
# 指定cmake最低支持的版本
# 可选的，但是如果使用了一些高版本特有的命令的话，就需要加上这行命令来指定最低支持版本
# 如果cmake当前版本低于指定的版本，就会报错
cmake_minimum_required(VERSION 3.4.1)
```

#### aux_source_directory

查找指定目录下所有源文件，并将源文件名称列表保存到变量中。

有两个参数，第一个参数是：要查找源文件的目录；第二个参数是：保存源文件名称列表的变量。

不能查找子目录中的源文件

```cmake
# 表示查找 当前目录所有源文件，将源文件名称列表保存到DIR_SRCS变量中
aux_source_directory(. DIR_SRCS)
```

####  add_library

##### 添加一个库

```
<name>：添加一个库的库名
[STATIC | SHARED | MODULE]：指定库的类型。
	STATIC：静态库
	SHARED：动态库
	MODULE：在使用dyld的系统有效，若不支持dyld，等同于SHARED
EXCLUDE_FROM_ALL：表示该库不会被默认构建
source1 source2 ... sourceN：用来指定库的源文件

add_library(
		<name>
		[STATIC | SHARED | MODULE]
		[EXCLUDE_FROM_ALL]
		source1 source2 ... sourceN )
```

##### 导入预编译库

```
add_library(
		<name>
		[STATIC | SHARED | MODULE | UNKNOWN]
		IMPORTED)
```

一般配合 set_target_properties 使用

```cmake
# 比如导入一个libtest.so的预编译库
add_library(test SHARED IMPORTED)
set_target_properties(
    test    #指明目标库名
    PROPERTIES IMPORTED_LOCATION  #指明要设置的参数
    库路径/${ANDROID_ABI}/libtest.so  #导入库的路径
)
```

#### set

设置cmake变量

```cmake
# 设置可执行文件的输出路径（EXECUTABLE_OUTPUT_PATH是全局变量）
set(EXECUTABLE_OUTPUT_PATH [output_path])

# 设置库文件的输出路径（LIBRARY_OUTPUT_PATH是全局变量）
set(LIBRARY_OUTPUT_PATH [output_path])

# 设置C++编译参数（CMAKE_CXX_FLAGS是全局变量）
set(CMAKE_CXX_FLAGS "-Wall std=c++11")

# 设置源文件集合（SOURCE_FILES是自定义变量）
set(SOURCE_FILES main.cpp test.cpp ...)
```

#### include_directories

设置头文件目录，相当于g++选项中 -I 参数

```cmake
# 可以使用相对或绝对路径，也可以使用自定义的变量值
include_directories(./include ${MY_INCLUDE})
```

#### add_executable

添加可执行文件

```cmake
# 第一个参数：文件名
# 第二个参数：源文件
add_executable(<name> ${SRC_LIST})
```

#### target_link_libraries

```
# 将若干个库链接到目标文件<name>中
# 链接顺序应当符合gcc链接顺序规则，被链接的库放在依赖它的库的后面。比如在下面的命令中，lib1依赖于lib2，lib2依赖于lib3，则必须严格按照lib1 lib2 lib3的顺序排列，否则会报错
target_link_libraries(<name> lib1 lib2 lib3)

# 如果出现互相依赖的静态库，CMake允许依赖途中包含循环依赖，如：
add_library(A STATIC a.c)
add_library(B STATIC b.c)
target_link_libraries(A B)
target_link_libraries(B A)
add_executable(main main.c)
target_link_libraries(main A)
```

#### add_definitions

添加编译参数

```
add_definitions(-DFOO -DDEBUG)
```

#### add_subdirectory

如果当前目录下还有子目录是可以使用add_subdirectory来为子目录添加CMake源文件，子目录中也需要包含有CMakeList.txt。

```cmake
# sub_dir：指定包含CMakeList.txt和源码文件的子目录位置
# binary_dir：输出路径，一般可以不指定
add_subdirectory(sub_dir [binary_dir])
```

#### file

文件操作命令

```cmake
# 将message写入filename文件中，会覆盖文件原有内容
file(WRITE filename "message")

# 将message写入filename文件中，会追加在文件末尾
file(APPEND filename "message")

# 从filename文件中读取内容并存储到var变量中，如果指定了numBytes和offset,
# 则从offset处开始最多读numBytes个字节，另外如果指定了HEX参数，则内容会以十六进制形式存储在var变量中
file(READ filename var [LIMIT numBytes] [OFFSET offset] [HEX])

# 重命名文件
file(RENAME <oldname> <newname>)

# 删除文件，等于 rm 命令
file(REMOVE [file1 ...])

# 递归的执行删除文件命令， 等于 rm -r
file(REMOVE_RECURSE [file1 ...])

# 根据指定的url下载文件
# TIMEOUT：超时时间；STATUS：下载的状态；LOG：下载日志；
# EXPECTED_MD5：指定所下载文件预期的MD5值，如果指定会自动进行比对，如果不一致，则返回一个错误；
# SHOW_PROGRESS：进度信息会以状态信息的形式被打印出来
file(DOWNLOAD url file [TIMEOUT timeout] [STATUS status] [LOG log] [EXPECTED_MD5 sum] [SHOW_PROGRESS])

# 创建目录
file(MAKE_DIRECTORY [dir1 dir2 ...])

# 会把path转换为以unix的/开头的cmake风格路径，保存在result中
file(TO_CMAKE_PATH result)

# 它会把cmake风格的路径转换为本地路径风格：windows下用"\"，而unix下用"/"
file(TO_NATIVE_PATH result)

# 将会为所有匹配查询表达式的文件生成一个list，并将该list存储进变量variable里，如果一个表达式指定了RELATIVE，返回的结果将会是相对于给定路径的相对路径，查询表达式例子：*.cxx，*.vt
# 注意：按照官方文档的说法，不建议使用file的GLOB指令来收集工程的源文件
file(GLOB variable [RELATIVE path] [globbing expressions]...)
```

#### set_directory_properties

```cmake
# 设置某个路径的一种属性
# prop1,prop2代表属性，取值为：
#   INCLUDE_DIRECTORIES
#   LINK_DIRECTORIES
#   INCLUDE_REGULAR_EXPRESSION
#   ADDITIONAL_MAKE_CLEAN_FILES
set_directory_properties(PROPERTIES prop1 value1 prop2 value2)
```

#### set_property

```
set_property(<GLOBAL |
              DIRECTORY [dir] |
              TARGET [target ...] |
              SOURCE [SRC1 ...] |
              TEST [test1 ...] |
              CACHE [entry1 ...] |>
             [APPEND]
             PROPERTY <name> [value ...])
```

在给定的作用域内设置一个命名的属性

PROPERTY 参数是必须的，跟在要设置的属性后面

第一个参数决定了属性可以影响的作用域：
	GLOBAL：全局作用域
	DIRECTORY：路径作用域，默认当前路径，也可以用[dir]指定路径
	TARGET：目标作用域，可以是0个或多个已有目标
	SOURCE：源文件作用域，可以是0个或多个源文件（源文件属性只对同目录下的CMakeList中目标可见）
	TEST：测试作用域，可以是0个或多个已有的测试
	CACHE：必须指定0个或多个cache中已有的条目

### 多个源文件处理

如果源文件很多，把所有文件一个一个加入很麻烦，可以使用 aux_source_directory 命令或 file 命令，会查找指定目录下的所有源文件

```cmake
# 查找当前目录所有源文件，并将源文件名称列表保存到 DIR_SRCS 变量中
aux_source_directory(. DIR_SRCS)
# 也可以使用
# file(GLOB DIR_SRCS *.c *.cpp)

add_library(
        native-lib
        SHARED
        ${DIR_SRCS})
```

### 多目录多个源文件处理

主目录中的 CMakeList.txt 中添加 add_subdirectory(child) 命令，指明本项目包含一个子项目 child。并在 target_link_libraries 指明本项目需要链接一个名为 child 的库

子目录 child 中创建 CMakeList.txt，这里 child 编译为共享库。

```cmake
aux_source_directory(. DIR_SRCS)
# 添加 child 子目录下的 cmakelist
add_subdirectory(child)

add_library(
        native-lib
        SHARED
        ${DIR_SRCS})
target_link_libraries(native-lib child)
```

child目录下的CMakeList.txt

```
aux_source_directory(. DIR_LIB_SRCS)
add_library(
        native-lib
        SHARED
        ${DIR_LIB_SRCS})
```

### 添加预编译库

#### Android6.0版本以前

- 假设我们本地项目引用了 libimported-lib.so。
- 添加 add_library 命令，第一个参数是模块名，第二个参数 SHARED 表示动态库，STATIC 表示静态库，第三个参数 IMPORTAL 表示以导入的形式添加。
- 添加 set_target_properties 命令设置导入路径属性。
- 将 imported-lib 添加到 target_link_libraries 命令参数中，表示 native-lib 需要链接 imported-lib 模块。

```cmake
# 使用 IMPORTED 标志告知 CMake 只希望将库导入到项目中
# 如果是静态库则将 SHARED 改为 STATIC
add_library(
        imported-lib
        SHARED
        IMPORTED)
set_target_properties(
        imported-lib    #库名
        PROPERTIES      #属性
        IMPORTED_LOCATION  #导入地址
        库路径/libimported-lib.so  #库所在地址
        
aux_source_directory(. DIR_SRCS)
add_library(
        native-lib
        SHARED
        ${DIR_SRCS})
target_link_libraries(native-lib imported-lib)
)
```

#### Android6.0版本以后

```cmake
# set命令定义一个变量
# CMAKE_C_FLAGS：c的全局变量，会传递给编译器
# 如果是c++文件，需要用 CMAKE_CXX_FLAGS
# -L：库的查找路径
set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -L[SO所在目录]")
```

### 添加头文件目录

为了确保CMake可以在编译时定位头文件，使用 include_directories，相当于g++选项中 -I 参数。
这样就可以使用  #include<xx.h>，否则需要使用 #include “path/xx.h”

```cmake
# 设置头文件目录
include_directories(<文件目录>)
```

### Build.gradle配置

```
android {
    defaultConfig {
        externalNativeBuild {
            cmake {
                // 使用的编译器clang/gcc
                // cmake默认就是 gnustl_static
                arguments "-DANDROID_TOOLCHAIN=clang", "-DANDROID_STL=gnustl_static"
                cFlags ""
                cppFlags ""
                // 指定需要编译的cpu架构
                abiFilters "armeabi-v7a"
            }
        }
    }

    externalNativeBuild {
        cmake {
            // 指定CMakeList.txt文件相对于当前build.gradle的路径
            path "src/main/cpp/CMakeLists.txt"
        }
    }
}
```