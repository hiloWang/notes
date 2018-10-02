# Java8 概要

---
## 1 Lambda表达式

- 一个Lambda表达式是一个带有参数的代码块
- Lambda表达式可以被转换为函数式接口
- Lambda表达式可以在闭包的范围中有效的访问final变量
- 方法和构造器引用可以引用方法和构造器，但无须调用它们

“Lambda 表达式”(lambda expression)是一个匿名函数，Lambda表达式基于数学中的λ演算得名，直接对应于其中的lambda抽象(lambda abstraction)，是一个匿名函数，即没有函数名的函数。Lambda表达式可以表示闭包，带有参数变量的表达式都被称为Lamdba表达式。

### 函数式接口

对于只有一个抽象方法的接口，可以使用Lambda表达式来创建该接口的对象，这种接口称为**函数式接口**

可以在任意函数式接口上添加`@FunctionInterface`注解，这样有两个好处：

- 编译器会检查该标注的实体，检查其是否只包含一个方法
- javadoc页面会包含一个声明，说明此接口为函数式接口

### 方法引用

有些时候想要传递给其他代码的操作已经有了实现的方法，可以直接传递方法，使用`:`操作符。语法为:

- 对象：：实例方法
- 类：：静态方法
- 类：实例方法(第一个参数会称为执行方法的对象，例如`String:compareToIgnoreCase`等同于`(x,y)->x.compareToIgnoreCase(y))`.
- 类名：new(构造器引用)

和lambda表达式一样，方法引用也不会独立存在，它们经常被用于转换为函数式接口。


### 默认方法

接口中可以添加静态方法和默认方法。

---
## 2 StreamAPI

Stream是Java8中处理集合的关键抽象概念，它可以指定你希望对集合进行的操作，但是将执行操作的时间交给具体的实现来决定。


- 创建Stream
- 使用各种操作符
- Optional类型
- 并行Stream
- 函数式接口

Stream中的函数式接口包括：

- `Supplier<T>` 提供一个T类型值
- `Consumer<T>` 处理一个T类型值
- `BiConsumer<T,U>` 处理T类型和U类型的值
- `Predicate<T>` 根据T参数返回Boolean类型的函数
- `ToIntFunction<T>` 计算int值的函数，另还有long、double
- `IntFunction<R>` 参数为int，返回一个R类型的值，另还有long、double
- `Function<T,R>` 参数类型为T，返回类型为R的函数
- `BiFunction<T,U,R>` 参数类型为T和U，返回类型为R的函数
- `UnaryOperator<T>` 一元操作符
- `BinaryOperator<T>` 二元操作符

---
## 3 新的日期和时间API

内容在java.time包中

---
## 4 并发增强

- 原子值
- ConcurrentHashMap改进
- 并行数组操作
- CompletableFuture

参考[Java8 和 Java 9中并发工具的改变](http://colobu.com/2018/03/12/Concurrency-Utilities-Enhancements-in-Java-8-Java-9/)

---
## 5  其他改进

- 字符串增加了join方法
- 可重复注解

---
## 6 JavaScript引擎：Nashorn

略

---
# 参考

- [Java8Stream详解](http://colobu.com/2016/03/02/Java-Stream/)
- [Java8新特性终极版](http://www.jianshu.com/p/5b800057f2d8)
- [现代java开发指南](http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0428/2811.html)

