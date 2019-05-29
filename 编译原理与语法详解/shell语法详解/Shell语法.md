## Shell 语法

### 1. 流程控制

### 1.1 if else

#### if

if else 语法格式：

```
if condition
then
    command1 
    command2
    ...
    commandN
else
    command
fi
```

#### if else

if else 语法格式：

```
if condition
then
    command1 
    command2
    ...
    commandN
else
    command
fi
```

#### if else-if else

if else-if else 语法格式：

```
if condition1
then
    command1
elif condition2 
then 
    command2
else
    commandN
fi
```

示例：

```
a=10
b=20
if (($a==$b))
then
   echo "a 等于 b"
elif (($a>$b))
then
   echo "a 大于 b"
elif (($a<$b))
then
   echo "a 小于 b"
else
   echo "没有符合的条件"
fi
```

输入结果

```
a 小于 b
```

