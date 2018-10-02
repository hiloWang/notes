# Instrumentation 机制

---
## 1 Instrumentation 机制介绍

在项目开发中，往往需要对代码运行进行各种检测，在没有 Instrumentation 机制之前，往往会使检测代码和业务代码混杂在一起，不利于开发。

Instrumentation 机制 是 Java SE 5 的新特性，利用它可以分离逻辑代码与性能分析等工具代码，使用 Instrumentation 机制可以对虚拟机加载的类的字节码进行转换，利用 API 或者字节码操作类库，我们可以插入需要的检测代码，这可以理解为是一个 JVM 层级的 AOP 实现。

在 Java SE 6 里面，instrumentation 包被赋予了更强大的功能：启动后的 instrument、本地代码（native code）instrument，以及动态改变 classpath 等等。这些改变，意味着 Java 具有了更强的动态控制、解释能力，它使得 Java 语言变得更加灵活多变。

**Instrumentation 的最大作用，就是类定义动态改变和操作。**

---
## 2 Instrumentation 的基本功能和用法

### 2.1 Java SE 5

在 Java SE 5 中，开发者可以在一个普通 Java 程序（带有 main 函数的 Java 类）运行时，通过 `– javaagent:xxx.jar="args"`参数指定一个特定的 jar 文件（包含 Instrumentation 代理）来启动 Instrumentation 的代理程序。让 Instrumentation 代理在 main 函数运行前执行。需要如下几个步骤：

#### 定义代理类

每个代理的实现类必须实现 ClassFileTransformer 接口。这个接口提供了一个 transform 方法，通过这个方法，代理可以得到虚拟机载入的类的字节码（通过 classfileBuffer 参数）。代理的各种功能一般是通过操作这一串字节码得以实现的。同时还需要提供一个公共的静态方法：`public static void premain(String agentArgs, Instrumentation inst)`，一般需要在这个方法中创建一个代理对象，通过参数 inst 的 `addTransformer()`方法，将创建的代理对象再传递给虚拟机。这个方法是一个入口方法，有点类似于一般类的 main 方法。

最后通过：`-javaagent:E:\****\ASM-Base\JavaAgent\build\libs\JavaAgent-1.0.jar="test args"` 执行该程序

```java
package me.ztiany.instrumentation;

public class Greeting implements ClassFileTransformer {

    //虚拟机加载的每一个类都会调用transform方法，在transform中可以对字节码进行自定义的转换
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        //如果transform 函数返回 null 值，表示不需要进行类字节码的转化。
        System.out.println("load->" + className);
        return null;
    }

    //定制完代理的行为之后，创建一个代理的实例，将该实例传递给虚拟机。
    //改方法也可以是 public static void premain(String agentArgs);，优先级比下面低
    //Inst 是一个 java.lang.instrument.Instrumentation 的实例，由 JVM 自动传入。java.lang.instrument.Instrumentation 是 instrument 包中定义的一个接口，
    // 也是这个包的核心部分，集中了其中几乎所有的功能方法，例如类定义的转换和操作等等。
    public static void premain(String agentArgs, Instrumentation inst) {
        //options 参数是通过命令行传递进来的，类似于调用 main 函数时传递的参数。被传递进来的命令行参数是一个完整的字符串，
        // 不同于 main 方法，该字符串的解析完全由代理自己负责。
        System.out.printf("I've been called with options: \"%s\"\n", agentArgs);
        inst.addTransformer(new Greeting());
    }

}
```

#### 打 jar 包

在jar的清单文件中需要定义一下属性：

```
Manifest-Version: 1.0
Premain-Class: me.ztiany.instrumentation.Greeting
Can-Redefine-Classes: true
```

#### 通过命令执行

```java
public class JavaAgentTest {

    public static void main(String... args) {
        System.out.println("----------------JavaAgentTest.main");
    }

}
```

这里VM参数为`-javaagent:E:\code\studio\my_github\Repository\Java\ASM-Base\JavaAgent\build\libs\JavaAgent-1.0.jar="test args"`,运行后可以看到控制台打印了如下信息：

```
     I've been called with options: "test args"
load->各种类路径
......
JavaAgentTest.main
load->java/lang/Shutdown
load->java/lang/Shutdown$Lock
```

### 2.2 Java SE 6 的新特性：虚拟机启动后的动态 instrument

在 `Java SE 5` 的基础上，`Java SE 6` 针对这种状况做出了改进，开发者可以在 main 函数开始执行以后，再启动自己的 Instrumentation 程序。

---
## 引用

- [Java 5 特性 Instrumentation 实践](https://www.ibm.com/developerworks/cn/java/j-lo-instrumentation/)
- [JavaSE6 Instrumentation 新功能](https://www.ibm.com/developerworks/cn/java/j-lo-jse61/index.html)




