# Kotlin 协程学习

Kotlin协程以包括以下模块

- common：common coroutines across all backends:
- core：kotlin协程核心基础模块
- reactive：为各种响应式库(RxJava等)提供构建器和迭代器
- ui 模块
- integration模块

---
## 1 Core 模块主要包括以下内容

### 协程基础(core.base)

- 连接阻塞和非阻塞世界
- 等待一个Job
- Coroutines是轻量级的
- Coroutines类似守护线程
    
###  撤销和超时(cancel and timeouts)

- 取消协程执行
- 创建可以取消的计算任务
- 在finally中释放资源
- 设置协程超时
    
### 组合暂停函数(core.functions)

- 顺序执行的协程
- 使用async并发执行——带返回值的协程Deferred
- 懒执行
- 编写异步风格的协程函数
    
###  调度器(coroutine context and dispatchers)

- 调度器于线程
- 无限制的dispatcher vs confined(限制的) dispatcher
- 如何调试协程
- 在不同的线程中跳转
- Job in the context(Job是context的属性)
- 协程与子协程
- 组合多个协程contexts
- 给协程命名
- 明确的取消Job

###  通道(core.channels)

- 通道
- 生产者消费者
- 管道
- 通道是公平的

###  并发(shared mutable state and core.concurrency)

- 写成也是线程不安全的
- 线程控制——细粒度
- 线程控制——粗粒度
- 互斥
- Actor

### 选择表达式(select expression)

- 选择表达式(还不是很理解)

---
## 2 reactive

- [ ] todo