# 双重检测锁(DCL)与延迟初始化双重检测锁于延迟初始化的由来

---
## 1 双重检测锁于延迟初始化的由来

在Java中，有可能需要对一些高开销的对象进行延迟初始化，延迟初始化就是在需要用到对象的时候才创建该对象，最常见的场景就是单利模式了，但是使用延迟初始化需要考虑线程的安全性。如下面是一个线程不安全的延迟初始化策略：

```java
    public class UnsafeSingleInstance {
        
        private static UnsafeSingleInstance sSingleInstance;
    
        public static UnsafeSingleInstance getSingleInstance() {
            if (sSingleInstance == null) {//步骤1：线程1
                //问题
                sSingleInstance = new UnsafeSingleInstance();//步骤2：线程2
            }
            return sSingleInstance;
        }
    }
```

上面代码中，线程1执行步骤1时，可能看不到步骤2的sSingleInstance已经被初始化，所以无法保证sSingleInstance只被初始化一次。

对于上面线程不安全的问题，最简单粗暴的方法即使加锁，如下：

```java
    public class SafeSingleInstance {
    
        private static SafeSingleInstance sSingleInstance;
    
        public static synchronized SafeSingleInstance getSingleInstance() {
            if (sSingleInstance == null) {
                
                sSingleInstance = new SafeSingleInstance();
            }
            return sSingleInstance;
        }
    }
```

但是这样又带来了新的问题，原因是synchronized将导致性能开销，如果多个线程访问getSingleInstance方法时，只有获取到锁的线程可以执行，其他线程都将处于阻塞状态，锁的获取与释放是有开销的！，于是就出现了双重检测锁，代码如下：

```java
    public class UnsafeDoubleCheckSingleInstance {
    
        private static UnsafeDoubleCheckSingleInstance sInstance;
    
        public static UnsafeDoubleCheckSingleInstance getInstance() {
            if (sInstance == null) {//第一次检查
                synchronized (UnsafeDoubleCheckSingleInstance.class) {//加锁
                    if (sInstance == null) {//第二次检查
                        sInstance = new UnsafeDoubleCheckSingleInstance();//创建对象，问题出在这里
                    }
                }
            }
            return sInstance;//返回对象
        }
    }
```

- 多个线程视图在同一时刻创建对象时，通过锁来保证只有一个线程能够创建
- 在对象被创建之后，以后的访问将不再需要获取锁

双重检查锁看起来很完美，但是却是一个错误的优化，问题在于：**在执行创建对象时，代码读取到sInstannce不为null时，sInstance的引用的对象可能还没有完成初始化**

---
## 2 问题的根源

问题的根源在于这一句代码`sInstance = new UnsafeDoubleCheckSingleInstance()`，这段代码的执行可以差分为下面三个步骤：

1. menory = allocate(); //1 分配对象的内存空间
2. ctorInstance(memory);//2 初始化对象
3. instance = memory;   //3 设置sInstance指向刚刚分配的内存空间

问题在于synchronized允许在临界区内进行重排序(JVM允许这种重排序)，所以可能存在的2和3之间的重排序情况如下：

    1. menory = allocate(); //1 分配对象的内存空间
    3. instance = memory;   //3 设置sInstance指向刚刚分配的内存空间，这时对象还没有被初始化完毕！
    2. ctorInstance(memory);//2 初始化对象


2和3之间的重排序比不会影响单线程情况下的执行结果(synchrozized只保证临界区内的互斥性和释放锁时的可见性)，因此这种重排序是被允许的。但是在多线程的程序中，这样的重排序可能会造成意想不到的结果.

对于2和3可能的重排序情况下的一种执顺序是：

|  事件 | 线程A  | 线程B  |
| ------------ | ------------ | ------------ |
| t1  |  A1:分配对象的内存空间 |   |
| t2  |  A3:设置sInstance指向内存空间 |   |
| t3  |   |  B1:判断sInstance不为null|
| t4  |   |  B2:由于判断sInstance不为null，线程B将访问sInstance引用的对象  |
| t5  |  A2:初始化对象 |   * |
| t6  |  A4:访问sInstance引用的对象 |   * |

对于上述重排序问题有以下两种解决方案：

- 不允许2和3之间的重排序
- 允许2和3之间的重排序，但是不允许其他线程看到这个重排序


---
## 3 基于volatile的解决方案

```java
     public class UnsafeDoubleCheckSingleInstance {
        
            private volatile static UnsafeDoubleCheckSingleInstance sInstance;
        
            public static UnsafeDoubleCheckSingleInstance getInstance() {
                if (sInstance == null) {//第一次检查
                    synchronized (UnsafeDoubleCheckSingleInstance.class) {//加锁
                        if (sInstance == null) {//第二次检查
                            sInstance = new UnsafeDoubleCheckSingleInstance();//创建对象，问题出在这里
                        }
                    }
                }
                return sInstance;//返回对象
            }
        }
```
只需要在sInstance变量前面加上volatile修饰即可，volatile的作用前面已经讲过，在JDK5或者更高的版本中，已经增强了volatile的内存语义，volatile会禁止多线程情况下2与3之间的重排序。

---
## 4 基于类初始化的解决方案

JVM在类的初始化阶段会(Class被加载后，且被线程使用之前)，会执行类的初始化，在执行类的初始化期间，JVM或去获取一个锁，这个锁可以同步多个线程对同一个类的初始化，基于这个特性可以是新另一个中方式的延迟初始化策略：Initialzation On Demand Holder idiom

```java
    public class InitClassHolder {
        
        public static InitClassHolder getInstance() {
            return Holder.sInitClassHolder;
        }
        
        private static class  Holder{
            private static InitClassHolder sInitClassHolder = new InitClassHolder();//这里将导致sInitClassHolder被延迟初始化
        }
    }
```

初始化一个类，包括初始化一个类的静态初始化和初始化在这个类中声明的静态字段，对于类的初始化，有下面几种方式引起会触发类的初始化：

- T是一个类，一个T类型的变量被创建
- T是一个类，T中声明的要给静态方法被调用
- T中声明的静态字段被赋值
- T中声明的一个静态字段被使用，而且这个字段不是一个常量字段
- T是一个顶级类，而一个断言语句嵌套在T背部被执行

而Java语言规定对于没有给类或者接口C，都有一个唯一的初始化锁LC与之对应。JVM在类的初始化执行期间会获取这个初始化锁，并且每个线程至少要获取一个锁来保证这个类已经被初始化过了。


>具体的类的初始化过程，可以参考《Java并发编程的艺术》

---
## 5 对比

静态内部类实现静态对象的延迟初始化更加简洁，但是基于volatile的双重检测方案除了可以延迟初始化静态字段外，还可以对实例字段实现延迟初始化。字段延迟初始化降低了初始化类或创建实例的开销，但增加了访问被延迟初始化的字段的开销。

所以：

- 在大多数时候，正常的初始化要优于延迟初始化。
- 如果确实需要对实例字段使用线程 安全的延迟初始化，可以使用基于 volatile 的延迟初始化的方案
- 如果确实需要对静态字段使用线程安全的延迟初始化，可以使用基于类初始化的方案。
