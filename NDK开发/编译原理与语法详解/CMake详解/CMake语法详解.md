## CMake语法详解

### 什么是CMake

- 在Android Studio 2.2 及以上，构建原生库的默认工具是CMake。
- CMake是一个跨平台的构建工具，可以用简单的语句来描述所有平台的安装（编译过程）。能够输出各种各样的makefile或者project文件。CMake并不直接构建出最终的软件，而是产生其他工具的脚本（如makefile），然后再依据这个工具的构建方式使用。
- CMake是一个比make更高级的编译配置工具，它可以更具不同的平台、不同的编译器，生成相应的makefile或者vcproj项目，从而达到跨平台的目的。Android Studio利用CMake生成的是ninja。ninja是一个小型的关注速度的构建系统。我们不需要关心ninja的脚本，知道怎么配置CMake就可以了。
- CMake其实是一个跨平台的支持产生各种不同的构建脚本的一个工具。

### CMake源文件

- CMake的源码文件可以包含命令、注释、空格和换行。
- 一CMake编写的源文件以CMakeList.txt命名，或以.cmake为扩展名。
- 可以通过add_subdirectory()命令把子目录的CMake源文件添加进来。
- CMake源文件中所有有效的语句都是命令，可以是内置命令或自定义的函数/宏命令。

### CMake注释

- 单行注释

  注释从#开始到行尾结束

  ```
  # 注释内容
  ```

- 多行注释

  可以使用括号来实现多行注释

  ```
  #[[多行注释1
  多行注释2
  多行注释3]]
  ```

### CMake变量

- CMake中所有变量都是 String 类型。可以使用 set() 和 unset() 命令来声明或移除一个变量。

  ```cmake
  # set(变量名 变量值) 
  set(var 123)
  ```

- 变量的引用：`${变量名}`

  ```cmake
  # message命令用来打印
  message("var = ${var}")
  ```

> PS：变量名区分大小写

### CMake列表

- 列表也是字符串，可以把列表看作一个特殊的变量，这个变量有多个值。

- 语法格式

  ```
  set(列表名 值1 值2 ... 值n)
  set(列表名 "值1;值2;...;值n")
  ```

  ```cmake
  set(list_var 1 2 3)
  set(list_var "1;2;3")
  ```

- 列表的引用

  `${列表名}`

  ```cmake
  message("list_var = ${list_var}")
  ```

### CMake流程控制-操作符

| 类型 |                             名称                             |
| :--: | :----------------------------------------------------------: |
| 一元 |                    EXIST,COMMAND,DEFINED                     |
| 二元 | EQUAL(等于),LESS(小于),LESS_EQUAL,GREATER(大于),GREATER_EQUAL,STREQUAL,<br/>STRLESS,STRLESS_EQUAL,STRGREATER,STRGREATER_EQUAL,<br/>VERSION_EQUAL,VERSION_LESS,VERSION_LESS_EQUAL,<br/>VERSION_GREATER,VERSION_GREATER_EQUAL,MATCHES |
| 逻辑 |                          NOT,AND,OR                          |

逻辑符的优先级：() > 一元操作符 > 二元操作符 > 逻辑操作符

### CMake流程控制-布尔常量值

| 类型  |                              值                              |
| :---: | :----------------------------------------------------------: |
| true  |                 1，ON，YES，TRUE，Y，非0的值                 |
| false | 0，OFF，NO，FALSE，N，IGNORE，NOTFOUND，<br/>空字符串，以-NOTFOUND结尾的字符串 |

### CMake流程控制-条件命令

语法格式：

```
if(表达式)
	COMMAND(ARGS...)
elseif(表达式)
	COMMAND(ARGS...)
elseif(表达式)
	COMMAND(ARGS...)
endif(表达式)
```

elseif和else部分是可选的，也可以有多个elseif部分，缩进和空格对语句的解析没有影响。

### CMake流程控制-循环命令

语法格式：

```
while(表达式)
	COMMAND(ARGS...)
endwhile(表达式)
```

break()命令可以跳出整个循环。
continue()命令可以跳出当前循环。

示例：

```cmake
set(a "")
while(NOT a STREQUAL "xxxxx")
    set(a "${a}x")
    message("a = ${a}")
endwhile()
```
![](/images/cmake_01.png)

### CMake流程控制-循环遍历

语法格式一：

```
foreach(循环变量 参数1 参数2 ... 参数n)
	COMMAND(ARGS...)
endforeach(循环变量)
```

每次迭代时会将参数赋值给变量，同时也支持break()和continue()命令。

示例：

```cmake
foreach(item 1 2 3 4 5)
    message("item = ${item}")
endforeach(item)
```
![](/images/cmake_02.png)

语法格式二：

```
foreach(循环变量 RANGE total)
	COMMAND(ARGS...)
endforeach(循环变量)
```

循环范围从0到total

示例：

```cmake
foreach(item RANGE 5)
    message("item = ${item}")
endforeach(item)
```
![](/images/cmake_03.png)

语法格式三：

```
foreach(循环变量 RANGE start stop step)
	COMMAND(ARGS...)
endforeach(循环变量)
```

循环范围从 start 到 stop，循环增量为 step

示例：

```cmake
foreach(item RANGE 1 5 2)
    message("item = ${item}")
endforeach(item)
```
![](/images/cmake_04.png)

语法格式四：

foreach还支持对列表的循环

```
foreach(循环变量 IN LISTS 列表)
	COMMAND(ARGS...)
endforeach(循环变量)
```

示例：

```cmake
set(list_var 1 2 3)
foreach(item IN LISTS list_var)
    message("item = ${item}")
endforeach(item)
```
![](/images/cmake_05.png)

### CMake自定义函数命令

自定义函数命令格式：

```
function(函数名 参数1 参数2 ... 参数n)
    COMMAND()
endfunction(函数名)
```
函数命令调用格式：

`函数名(实参列表)`

示例

```cmake
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
```

![](/images/cmake_06.png)

### CMake自定义宏命令

自定义宏命令格式：

```
macro(宏名 参数1 参数2 ... 参数n)
    COMMAND()
endmacro(宏名)
```
宏命令调用格式：

`宏名(实参列表)`

示例：

```
macro(ma a b )
    message("a = ${a}")
    message("b = ${b}")
endmacro(ma)
ma("hello" "world")
```

![](/images/cmake_07.png)

### CMake变量的作用域

- **全局层：**cache变量，在整个项目范围内可见，一般在set定义变量时，指定CACHE参数就能定义为cache变量。
- **目录层：**在当前目录CMakeLists.txt中定义，以及在该文件包含的其他cmake源文件中定义的变量。
- **函数层：**在命令函数中定义的变量，属于函数作用域内的变量。

优先级：函数层 > 目录层 > 全局层