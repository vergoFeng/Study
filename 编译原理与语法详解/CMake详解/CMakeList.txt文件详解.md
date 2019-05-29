## CMakeList.txt文件详解

### CMakeList.txt简析

使用Android Studio创建一个Native C++项目，默认会在app/src/main目录下生成cpp目录，里面包含 CMakeList.txt 和 native-lib.cpp。

CMakeList.txt文件

```cmake
cmake_minimum_required(VERSION 3.4.1)

add_library(
		native-lib
		SHARED
		native-lib.cpp )
             
find_library(
		log-lib
		log )
              
target_link_libraries(
		native-lib
		${log-lib} )
```

#### 常用命令 - cmake_minimum_required

```cmake
# 指定cmake最低支持的版本
# 可选的，但是如果使用了一些高版本特有的命令的话，就需要加上这行命令来指定最低支持版本
# 如果cmake当前版本低于指定的版本，就会报错
cmake_minimum_required(VERSION 3.4.1)
```

#### 常用命令 - aux_source_directory

查找指定目录下所有源文件，并将源文件名称列表保存到变量中。

有两个参数，第一个参数是：要查找源文件的目录；第二个参数是：保存源文件名称列表的变量。

不能查找子目录中的源文件

```cmake
# 表示查找 当前目录所有源文件，将源文件名称列表保存到DIR_SRCS变量中
aux_source_directory(. DIR_SRCS)
```

#### 常用命令 - add_library

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

#### 常用命令 - set

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

#### 常用命令 - include_directories

设置头文件目录，相当于g++选项中 -I 参数

```cmake
# 可以使用相对或绝对路径，也可以使用自定义的变量值
include_directories(./include ${MY_INCLUDE})
```

#### 常用命令 - add_executable

添加可执行文件

```cmake
# 第一个参数：文件名
# 第二个参数：源文件
add_executable(<name> ${SRC_LIST})
```

#### 常用命令 - target_link_libraries

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

#### 常用命令 - add_definitions

添加编译参数

```
add_definitions(-DFOO -DDEBUG)
```

#### 常用命令 - add_subdirectory

如果当前目录下还有子目录是可以使用add_subdirectory来为子目录添加CMake源文件，子目录中也需要包含有CMakeList.txt。

```cmake
# sub_dir：指定包含CMakeList.txt和源码文件的子目录位置
# binary_dir：输出路径，一般可以不指定
add_subdirectory(sub_dir [binary_dir])
```

#### 常用命令 - file

