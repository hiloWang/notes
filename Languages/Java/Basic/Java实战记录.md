
---
# 一：如何学习 Java

- Java 常用API
- JDK 中的各种工具
- JDK 版本各种特新：5、6、7、8、9...
- JCP 接收的 JSR，很多 JSR 都值得研究和学习

---
# 二：经验总结


## 0 基本数据类型计算可能的溢出

基本数据类型计算时，如果不能确定数据的范围，则计算应该考虑可能发生的数据溢出，比如：

```
Integer.MAX_VALUE + 1 = -2147483648
```

## 1 System控制台

```java
   char[] chars = System.console().readPassword();
   System.out.println(new String(chars));
```

可以使用上面代码从控制台读取密码，然而这个方法总是不能正常运行，因为System.console()总是返回null。所有还是使用Sannner比较安全。

```java
import java.util.Scanner;
Scanner in;
in = new Scanner(System.in);
String s = in.nextLine();
```

## 2 创建File的相对位置

```
        File file = new File("a.txt");

```

当构建一个File使用的是相对路径时，文件位于Java虚拟机启动的相对路径，而如果使用命令行的方式启动程序，启动路径就是命名解释器的当前路径，如果使用集成开发环境，那么启动路径由IDE控制，可以使用下面方式获取虚拟机的相对路径：

```
System.getProperty("user.dir")
```

## 3 注意浮点数据类型的比较

```java
 public static void main(String... args) {
        for (double i = 0; i != 10; i += 0.1) {
            System.out.println(i);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

```

这段代码很有可能永远都不会执行完。[因为0.1无法精确的用二进制表示](http://www.cnblogs.com/Nobel/archive/2013/04/08/3009162.html)。

通用在equal方法、compareTo等方法中，浮点数也不适合用来做运算，比如：

```java
class Manager implements Comparable<Manager> {
    private float salary;
    @Override
    public int compareTo(Manager other) {
        //.....省略
        //return (int) (this.salary - other.salary);error
        return Double.compare(salary, other.salary);//
    }
}
```

## 4 实现equals方法的规范

- 自反性
- 对称性
- 传递性
- 一致性

一般的做法是：
- 显示的参数命名：otherObject用于表示需要比较的对象
- 检查this==otherObject
- 检查otherObject是否为null
- 比较this与otherObject是否属于同一个类
    - 如果equasl语义在每个子类中有所改变，就是用getClass监测
    - 如果所有子类都用统一的语义：就使用instanceof比较
- 将otherObject转换为this对应的类型
- 进行内容比较

## 5 HashCode方法

 HashCode即散列码是由对象到处的一个整数值，散列码时没有规律的，两个不同的对象的散列码一般不会相等。

hashCdoe方法应该返回一个整数，并合理的组合实例域的散列码以便能够让各个不同的对产生的散列码更加均匀。

```
    Objects.hashCode("abc");
    Arrays.hashCode(new int[]{12, 3});
```

## 6 trimToSize

```
            ArrayList<String> list = new ArrayList<>();
            list.add("A");
            list.add("B");
            list.add("C");
            list.add("D");
            list.trimToSize();
```

当确认不再有元素需要被添加后，可以调用trimToSize方法优化内存



## 7 编译器编译后的代码

许多优化来自于编译器，编译器在生成类的字节码时，会插入必须要的方法调用。而虚拟机只是执行这些字节码。

比如：

```
     private static void test(Integer integer) {
            int val = integer;
            System.out.println(val);
     }
    会被翻译为：
    private static void test(Integer integer) {
            int val = integer.intValue();
            System.out.println(val);
     }
```

## 8 对象克隆

克隆分为：深拷贝和浅拷贝，数组的克隆方法永远是浅拷贝。

## 9 回调的意义

回调(callback)是一种常见的程序设计模式，可以指出某个特定的事件发生时应该采取的动作。


## 10 内部类

内部类是一种编译器现象，与虚拟机无关，编译器将会把内部类翻译成$符号分隔外部类和内部类名的常规类文件。而虚拟机对此一无所知。

```
    public class Test {
        class A implements Cloneable {
            int a;
            public A(int a) {
                this.a = a;
            }
        }
    }
```

对于上面代码可以使用反射或者使用`javap -private Test$A`命名查看A的编译结果:

```
        class basic.Test$A{
                  public basic.IntegerTest$A(basic.IntegerTest, int);
                  public java.lang.Cloneable clone();
                  public volatile java.lang.Object clone();
                  int a;
                  final basic.Test this$0;
           }
```

可以看到编译器为内部类生成了一个附加的实例域this$0指向它的外部类。

### 局部类只可以引用final的局部遍历。

```
    final int val = 3;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    System.out.println(val);
                }
            };
```

当创建Runnable时，val就会被传递给Runnable构造器，并存储在实例域中。将val声明为final的，就使得局部变量域与在局部类内建立的拷贝保持一致。

```java
    public class OuterTest {

        public static void main(String... args) {
            class InnerRunnable implements Runnable {
                @Override
                public void run() {
                    System.out.println(Arrays.toString(args));
                }
            }
            new InnerRunnable().run();
            ReflectTools.analyze(InnerRunnable.class.getName());
        }
```

通过反射分析InnerRunnable生成的字节码为：

```java
    class basic.IntegerTest$1InnerRunnable
    {
       basic.IntegerTest$1InnerRunnable([Ljava.lang.String;);

       public void run();

       final [Ljava.lang.String; val$args;
    }
```

## 11 Java国际化

Java国际化涉及内容：

- 时间国际化
- 货币国际化
- 百分比
- 数字格式化
- 消息国际化

相关类：

- Locales
- NumberFormat
- DataFormat
- MessageFormat

## 12 Java 平台脚本简介

脚本语言是一种通过在运行时解释程序文本，从而避免使用通常的编辑->编译->连接->运行循环的语言，脚本语言有以下许多优势：

- 编译快速变更，鼓励不断实验
- 可以修改运行时程序的行为
- 支持程序用户的定制化

java可以与一些脚本语言很好的进行交互，比如javascript、groovy、ruby等。通过Java提供的`javax.script.ScriptEngineManager`可以执行脚本语言，不过并没有这么简单

```java
          ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
          List<ScriptEngineFactory> engineFactories = scriptEngineManager.getEngineFactories();//枚举所有发现可用的脚本引擎
```

默认情况下只有一个`jdk.nashorn.api.scripting.NashornScriptEngineFactory`，是用于执行javaScript的引擎(JDK1.7)，可通过在类路径中提供必要的jar来添加对额外的语言支持。

