# 《Java虚拟机规范》导读

---
## 1 内容概览

### 第 1 部分：走进Java

 - 了解Java技术的发展
 - 了解JVM的发展

### 第 2 部分：自动内存管理机制

 - Java如何划分内存
 - 垃圾回收算法
 - JVM参数
 - 虚拟机调优与调试工具
 - 理解GC日志

### 第 3 部分：虚拟机执行子系统

 - Class是**平台无关性和语言无关性**基石
 - Class文件的构成：魔数、版本、常量池
 - 对象的init方法、类的clinit方法(static)，以及类的ContantValue(static final基本数据类型和String)属性
 - 字节码指令、工程
 - 类的加载过程
 - 虚拟机执行代码引擎
 - 运行时帧栈结构、方法的动态分派和静态分派

### 第 4 部分：程序编译与代码优化

- 早期的源码编译优化：javac命令
- 编译的过程:
    - 词法、语法分析
    - 填充符号表
    - 注解处理器
    - 语义分析与字节码生成
- 语法糖的解析
    - 泛型擦除
    - 自动装箱与解箱
- JIT(Just in time)编译优化(主要的优化部分，不是针对Java代码，而是针对字节码)
- AOP编译

### 第 5 部分：高效并发

- Java内存模型
- 线程安全与锁的优化

---
## 2 其他参考的资料

- [《Java虚拟机规范》](http://icyfenix.iteye.com/)
- [《Java虚拟机规范》笔记](https://gavinzhang1.gitbooks.io/java-jvm-us/content/)
- [《Java虚拟机规范》系列博客](https://blog.csdn.net/zq602316498/article/list/4)