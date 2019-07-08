## Handler、Message 源码分析

### 什么是Handler

Handler主要用于异步消息的处理：当发出一个消息后，首先进入一个消息队列，发送消息的函数即可返回，而另外一个部分在消息队列中逐一将消息取出，然后对消息进行处理。

### Handler+Message原理分析

![](images/handler_01.png)

