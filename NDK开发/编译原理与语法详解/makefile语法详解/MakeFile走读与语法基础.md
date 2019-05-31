## MakeFile走读与语法基础

### 什么是MakeFile

- makefile 定义了一系列的规则来指定，哪些文件需要先编译，哪些文件需要重新编译，如何进行链接等操作。
- makefile 就是“自动化编译”，告诉 make 命令如何进行编译和链接。

**makefile包含以下五个：**

- 显示规则

  说明如何生存一个或多个目标文件。


- 隐晦规则

  因为make有自动推导的功能，所以隐晦规则可以比较简略的来书写makefile，这是由make所支持的。

- 变量定义

  在makefile中可以定义一系列的变量，变量一般都是字符串，这个有点类似于C语言中的宏。当makefile被执行时，其中的变量都会被扩展到相应的引用位置上。

- 文件指示

  包含了三个部分：

  - 在一个makefile中引用另一个makefile，类似于C语言中的 #include。
  - 根据某些情况指定makefile中有效部分。
  - 定义一个多行的命令。


- 注释

  makefile中只有行注释，用 # 字符

### MakeFile的规则

- target：目标文件

  可以使Object File，也可以是执行文件，还可以是标签（Label）

- prerequisites：依赖文件

  即要生成那个target所需要的文件或其他target

- command：make需要执行的命令

这是一个文件的依赖关系，也就是说，target这一个或多个的目标文件依赖于prerequisites中的文件，其生成规则定义在command中。说白一点就是说，prerequisites中如果有一个以上的文件比target文件要新的话，command所定义的命令就会被执行(command一定要以Tab键开始，否则编译器无法识别command），减少重复编译，提高了其软件工程管理效率。

### MakeFile是如何工作的

**默认方式下，输入make命令后：**

- make会在当前目录下找到名字叫 “Makefile” 或 “makefile” 的文件。
- 如果找到，它会找文件中第一个目标文件（target），并把这个target作为最终的目标文件，如上面示例中的 “main”。
- 如果 main 文件不存在，或 main 所依赖的 .o 文件的修改时间要比 main 文件要新，那么它会执行后面所定义的命令来生成 main 文件。
- 如果main所依赖的 .o 文件也存在，那么make会在当前文件中找目标为 .o 文件的依赖性，若找到则根据规则生成 .o 文件。
- make 再用 .o 文件声明 make 的终极任务，也就是执行文件 ”main“。

### MakeFile中使用变量

