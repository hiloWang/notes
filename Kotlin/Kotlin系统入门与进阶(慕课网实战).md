

[Kotlin系统入门与进阶](https://coding.imooc.com/class/108.html) 学习笔记

## 第2章 数据类型

区间：

- 区间的接口是 `public interface ClosedRange<T: Comparable<T>>{}`，处理 kotlin 提供的区间，我们还可以定义自己的区间。
- 使用 in 判断元素是否在区间中
- `..` 创建闭区间，`until` 创建开区间

## 第3章 程序结构

### 编译期常量

```java
//编写的代码
public class Main{
    public final String FINAL = "FINAL_STRING";
    public String hello = FINAL;
}
//编译后的伪代码
public class Main{
    public final String FINAL = "FINAL_STRING";
    public String hello = "FINAL_STRING";//不需要去获取 FINAL 的值。
}
```

在编译的时候就能够确定的值，比如 FINAL，FINAL 在编译期就能够确定，所以编译的时候，在引用 FINAL 的地方会直接使用 `"FINAL_STRING"` 替换 `"FINAL_STRING"`。这有点像 c 中的宏定义。

在 kotlin 中定义编译器常量应该使用 `const val`，使用 val 定义的变量虽然是常量，但不是编译期常量。

### 属性初始化

- var 用 lateinit 延迟初始化
- val 用 by lazy 延迟初始化


### 基本运算符  

infix 中缀表达式，一般用于 DSL。

### 导出可执行程序

基本中添加

```groovy
apply plugin: 'application'
//类目的 Kt 需要自己补上哦
mainClassName = "net.println.kotlin.chapter3.CalcKt"
```
然后同步之后会发现 gradle 任务中多了一个 distribution 任务中，运行其中的 installDist 任务即可生成可以执行程序，可以在 build/install 中找到。

## 第4章 面向对象

### 对象基础

- 抽象类：半成品，A 继承 B，则 A 是 B。抽象类是对具体事物的抽象。
- 接口：是约定、协议，A 实现 B，则 A 像 B。接口是对行为的抽象，接口不能有状态。
- 包级函数：对于在 Java 中定义静态函数的场景，建议在 Kotlin 中使用包级函数。
- companion 对象：一个类的 companion 对象其实是该类的一个静态内部类，全局只有一份（相当于一个单例），companion 对象中的方法添加了 `@JvmStatic` 注解后会在外部类上生成对应的同名静态方法。如果可以用包级函数，那么优先使用包级函数，否则才使用 companion 对象，这样能减少堆内存的消耗。

### 方法/属性扩展

- 属性扩展和方法扩展本质上都是编译器生成的静态方法。
- kotlin 的扩展属性是无法拥有状态的。

### data class

- data class 坑比较多，比如没有为所有参数指定默认值时，data class 将没有无参构造函数。
    - 使用 noarg 插件，noarg 会在编译器编辑字节码插入无参构造函数，只能通过凡是访问。
    - 为所有参数指定默认值（推荐）。
- 默认 data class 是 final的，使用 allOpen 插件可以让 data class 变为非 final 的。
- data class 默认实现了一些方法：toString、copy 、equals 等等。
- data class 支持解构声明，解构声明用于支持多返回值。


## 第5章 高阶函数

### 尾递归优化

对于尾递归（递归的最后，只是调用自己，没有别的操作）调用，加上 tailrec 关键字后，编译器可以将其优化为遍历代码。

### 闭包

函数式编程的福音，可以作为一个对象进行传递和赋值，同时闭包可以被调用，闭包还可以引用其作用外的变量，这时被闭包引用的变量的声明周期与闭包一致。

```kotlin
fun makeFun(): () -> Unit {
    var count = 0
    return fun() {
        println(++count)
    }
}

fun add(x: Int): (Int) -> Int {
     return fun(y: Int): Int {
        return x + y
    }
}

//斐波那契数列
fun fibonacci(): Iterable<Long> {
    var first = 0L
    var second = 1L

    return Iterable {
        object : LongIterator() {
            override fun nextLong(): Long {
                val result = second
                second += first
                first = second - first
                return result
            }

            override fun hasNext() = true

        }

    }
}

fun main(args: Array<String>) {
    val add5 = add(5)
    println(add5(2))

    for (i in fibonacci()) {
        if (i > 100) {
            break
        }
        println(i)
    }
}
```

### 函数复合

函数复合就是 `f(g(x))`，可以发挥自己的想象力去定制各种符合函数。

```kotlin
infix fun <P1, P2, R> Function1<P1, P2>.andThen(function: Function1<P2, R>): Function1<P1,R>{
    return fun(p1: P1): R{
        return function.invoke(this.invoke(p1))
    }
}

infix fun <P1,P2, R> Function1<P2, R>.compose(function: Function1<P1, P2>): Function1<P1, R>{
    return fun(p1: P1): R{
        return this.invoke(function.invoke(p1))
    }
}
```

### 柯里化和偏函数

- 柯里化：把一个多元函数变成多个单元函数调用链的过程
- 偏函数：指定多参数函数的某些参数而得到新函数，实现方式是函数扩展

## 第6章 领域特定语言 DSL

领域特定语言，用于针对某一个领域而构建的语言，比如 SQL 和 正则表达式。DSL 可以分为`外部 DSL` 和 `内部 DSL`。