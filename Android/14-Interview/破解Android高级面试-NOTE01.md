# 大厂资深面试官 带你破解Android高级面试——NOTE01

[大厂资深面试官 带你破解Android高级面试](https://coding.imooc.com/class/317.html)

---
## 1 面试准备

梳理自身技能：

- Java功底：语言特性，框架原理
- Android 系统原理：
  - 程序时是怎样运行的
  - 窗口是怎么显示的
  - 声音是如何播发的
- 项目经验
  - 技术难点如何解决
  - 有哪些失败之处
- 架构设计
  - 为什么选择这个方案
  - 如果进行模块边界划分
  - 如何实现模块解耦

工程师级别：

- 初级工程师
- 中级工程师：独立解决复杂问题
- 高级工程师：
  - 对复杂问题解决有自己的见解，对问题的识别，优先级分配见解尤其有影响力，善于寻找资源解决问题
  - 独立领导跨部分项目，能够培训或指导新员工
  - 部份内具有专业影响力

明确自己的目标：

- 了解市场需求
- 明确自身水平，根据水平筛选可能的公司

能力跨分：

- 技术实力
- 业务能力
- 团队合作

面试过程的 star 法则：

- 情境(situation)：所处情况
- 任务(task)：什么任务
- 行动(action)：如何做
- 结果(result)：结果是？用数字说明

如何准备简历：

- 基本信息简明扼要
- 求职意向清晰明了
- 根据 JD 定制建立（让面试官觉得你的背景与其要求菲常匹配）
- 拿出项目成果（量化结果）
- 避免空洞自我评价（落到实处，比如经常编写脚手架，经常在公司内部做技术分享）

项目成果模板：`我主要负责部分的开发，尝试对页面过度绘制进行优化，优化后页面平均绘制耗时从 20ms 降低到 3ms，活动、频道等功能的架构优化，对这些模块切蒂彻底解耦，降低维护成本约 30%。评论模块组件化通用化，在其他三个项目中得到服用，每个项目节省 10 人天的工作量。`

简历的“四要四不要”

- 要聚焦核心技能，不要到处熟练精通
- 要突出技术亮点，不要罗列开源框架
- 要体现业务背景，不要堆积项目细节
- 要明确项目成果，不要陈述项目过程

---
## 2 Java 功底

---
### 2.1 Java 的 char 是两个字节，如何存储 UTF-8 字符？

#### 考察什么

- 是否熟悉 Java Char 和字符串（初级）
- 是否了解字符串的隐射和存储细节（中级）
- 是否能触类旁通，横向对比其他语言（高级）

#### 题目剖析

- 面试题经常故意设计陷阱
- 但你不能说题目错了
- 题目只是给定范围，是话题作为而不是命题作文

#### 题目结论

- UTF-8 编码需要占用 1-3 个字节。（题目是个陷阱）
- Java中字符仅以一种形式存在，那就是 Unicode。由于 java 采用 unicode 编码，char 在 java 中占 2 个字节。2 个字节（16位）来表示一个字符。
- UTF-8 不是字符集，而是字符编码：`人类自然语言-->字符集(映射)-->计算机存储(编码)`
- Unicode 通用字符集占两个字节，例如“中”。
- **字节序标志**：`byte [] bytes = "中".getBytes("utf-16")` 的长度为 4, 内容为 `fe ff 4e 2d`，为什么会是四个呢？前面两个存储的是字节序标志。
- Unicode 扩展字符串需要用一对 char 表示，比如表情。
- Unicode 是字符集，不是编码，作用类似于 ASCII 码。
- Java String 的 length 不是字符的个数。
- Jave9 对 Latin 字符存储空间做了优化，但`字符串长度`依然不等于`字符个数`。

#### 与 python 对比

```python
# 没问题
byteString = "abc"

# python 使用中文字符串，需要指定字符编码，否则 python 不支持采用什么编码节码。
# 使用 codeing = utf-8，*.py文件中存放的是 UTF-8 编码后的字节，字面量会用 utf-8 编码成字节存入字符串。
# codeing = utf-8
byteString = "中国"
```

Java 中的字节与字符串

```java
byte [] byteString = "中国".getBytes("uft-16")
String unicodeString = "中国"
```

python 中的字节与字符串

```python
byteString = "中国"
unicodeString = u"中国"
```

- `*.py` 文件中存放的是 `UCS2(≈UTF-16)` 编码后的字节，python 是解释执行的，源文件与执行时内存中的字符串一直
- Javac 指定编码将字符串统一转换为 `MUTF-8`
- PYthon 的 `len(字符串)` 等于字符的个数。

#### 深入

- 计算机中储存的信息都是用二进制数表示的；而我们在屏幕上看到的英文、汉字等字符是二进制数转换之后的结果。通俗的说，按照何种规则将字符存储在计算机中，如'a'用什么表示，称为"编码"；反之，将存储在计算机中的二进制数解析显示出来，称为"解码"，如同密码学中的加密和解密。在解码过程中，如果使用了错误的解码规则，则导致'a'解析成'b'或者乱码。
- **字符集（Charset）**：是一个系统支持的所有抽象字符的集合。字符是各种文字和符号的总称，包括各国家文字、标点符号、图形符号、数字等。
- **字符编码（Character Encoding）**：是一套法则，使用该法则能够对自然语言的字符的一个集合（如字母表或音节表），与其他东西的一个集合（如号码或电脉冲）进行配对。即在符号集合与数字系统之间建立对应关系，它是信息处理的一项基本技术。通常人们用符号集合（一般情况下就是文字）来表达信息。而以计算机为基础的信息处理系统则是利用元件（硬件）不同状态的组合来存储和处理信息的。元件不同状态的组合能代表数字系统的数字，因此字符编码就是将符号转换为计算机可以接受的数字系统的数，称为数字代码。

具体参考[字符集和字符编码（Charset & Encoding）](https://www.cnblogs.com/skynet/archive/2011/05/03/2035105.html)

#### 技巧点拨

- 抓住细节，有技巧地回避知识盲区
- 把我节奏，不要等面试官追问
- 主动深入，让面试管了解你的知识体系
- 触类旁通，让面试官眼前一亮

---
### 2.2 Java String 可以有多长

#### 考察什么？

- 是否对字符串编码有深入了解（中级）
- 是否对字符串在内存中的存储形式有深入了解（高级）
- 是否对 Java 虚拟机字节码有足够的了解（高级）
- 是否对 Java 虚拟机指令有一定的了解（高级）

#### 问题剖析

- 字符串有多长是指字符串数还是字节数？
- 字符串有几种存在的形式？
- 字符串的不同形式受到何种限制？

```java
//字面量，编译期决定，存储在栈上
String longString = "aaaa...aaaa"

//存储在堆上
byte[] bytes = loadFromFile(new File("x.txt"));
String supperLongString = new String(bytes);
```

**Java 栈**：

```java
//源文件
String longString = "aaaa...aaaa"

//字节码 *.class，字符串在字节码中是使用 u8 的结构存储的
CONSTANT_Utf8_info{
    u1 tag;
    u2 length;//2个字节，65535个
    u1 bytes[length];
}

//Java虚拟机内存：字面量加载到方法区中的常量池中
```

结论：栈上的字面量的`字节数`应该 `<= 65535`？

- 受字节码限制，字符串最终的 MUTF-8 字节数不能超过 65535 个。
- Latin 字符，受到 javac 代码限制，最多 65534 个(javac 的 bug)。
- 非 Latin 字符最终对于字节数差异较大，最多字节数个数是 65535 个。
- 如果运行时方法区设置较小，也会受到方法区大小的限制。

**Java 堆**：

- 受虚拟机指令限制，字符数理论上限为 `Integer.MAX_VALUE`
- 手虚拟机实现限制，实际上线可能小于 `Integer.MAX_VALUE`
- 如果堆内存较小，也会受到堆内存的限制

#### 技巧点拨

- 思路很重要
  - 这种类型的问题最终往往结果不重要
  - 拿到问题，知道如何分析，知道从哪分析是关键
- 切不可眼高手低
  - 简单的问题背后藏着玄机
  - 尽一切可能将题目引向自己熟悉的领域

#### 书籍推荐

- 《Java 虚拟机规范》
- 《Java 预言规范》

---
### 2.3 Java 匿名内部类有哪些限制

#### 考察什么？

- 考察匿名内部类的概念和用法（中级）
- 考察语言规范以及横向对比等（中级）
- 作为考察内存泄漏的切入点（高级）

#### 问题剖析

- 匿名内部类有名字：`com.xx.OuterClass$1`，$1 表示定义的第一个匿名内部类。
- 匿名内部类的继承结构，new 时不能再实现其他接口，(java内部类可以，kotlin 是可以的)。
- 非静态匿名内部类的构造方法由编译器定义，构造方法上有一个外部类类型的作为方法参数。
```java
class OuterClass{
    public abstract class InnerClass{
        abstract void test();
    }
}

public class Client{
    public void run(){
        InnrClass innerClas = new OuterClass.new InnerClass(){};
    }
}

//编译结果，注意 InnerClass 定义为接口，则不会有 OuterClass 作为构造参数
public class Client$1{
    //由编译器定制
    public Client$1(Client client, OuterClass outerClass){

    }
}
```
- 匿名内部类所引用的句部变量应该是 final 的
```java
class OuterClass{
    public interface InnerClass{
        void test();
    }
}

public class Client{
    public static void run(){
        final Object object = new Object();
        InnrClass innerClas = new OuterClass.new InnerClass(){
            @Override
            void test(){
                System.out.print(object.toString());
            }
        };
    }
}

//如果 object 不是 final 的，那么 object 就是可以被修改的，那就会造成局部的 object 与匿名内部类所引用的不是同一个对象，
//从而给开发者带来困惑，仅此而已。
public class Client$1{
    //由编译器定制
    public Client$1(Object obj){

    }
}
```

#### 技巧点拨

- 关注语言版本变化
  - 体现堆技术的热情
  - 体现好学本质
  - 现得专业

---
### 2.4 怎么理解 Java 的方法分派

#### 考察什么？

- 多态、虚方法表的认识（初级）
- 对编译和运行时的理解和认识（中级）
- 对 Java 语言规范和运行机制的深入认识（高级）
- 横向对比各类语言的能力（高级）
  - Groovy
  - C++，Native 程序开发

#### 题目剖析

怎么理解 Java 的方法分派

- 就是确定调用了谁的方法，哪个方法
- 针对方法重载的情况进行分析
- 针对方法覆写的情况进行分析

```java
public SuperClass{
    public String getName(){
        return "Super";
    }
}

public SubClass{
    public String getName(){
        return "Sub";
    }
}

public class Client{
    public static void main(String... args){
        SuperClass superClass = new SubClass();
        printHello(superClass);
    }
}

public static void printHello(SuperClass superClass){
    System.out.println("Hello "+superClass.getName())
}
public static void printHello(SubClass subClass){
    System.out.println("Hello "+subClass.getName())
}
```

结论：

- 执行结果：`"Hello Sub"`，`getName` 方法的调用取决于变量运行时的实际类型。
- 重载方法的方法分派：程序如何执行，调用的方法取决于编译时期的类型，调用的是第一个。

**方法分派种类**：

静态分派-方法重载分派

- 编译期确定
- 依据调用者的声明类型和方法参数类型

动态分派-方法覆写分派

- 运行时确定
- 依据调用者的实际类型

**触类庞统**：

- groovy 运行上面代码时，所运行的函数是第二个，也就是 groovy 中重载方法的方法分派取决于参数的实际类型。
- c++ 中的方法分配

#### 技巧点拨

- 横向对比
  - 体现扎实的语言功底
  - 体现对编程语言特性的专研精神
  - 现得专业

---
### 2.5 Java 泛型机制

#### 考察什么？

- 对 Java 泛型使用是否停留在集合框架的使用(初级)
- 对 Javc 泛型的实现机制的认知和理解（中级）
- 是否有足够的项目开发实战经验和“踩坑”经验（中级）
- 对泛型（或模板）编程是否有深入的对比研究（高级）
- 对常见的框架原理是否有过深入剖析（高级）

#### 题目剖析

- 题目区分度比较大
- 回答需要提及以下几个点才能突出亮点：
  - 类型擦除从编译角度的细节
  - 类型擦除对运行时的影响
  - 类型擦除对发射的影响
  - 对比类型不擦处的语言(C++/C#)
  - 为什么 Java 选择类型擦除
- 可从类型擦除的优劣来着手分析

**类型擦除的优势**：

- 运行时内存负担小
- 兼容性好，Java1.5 才推出泛型，此时 Java 的使用者已经很多。

**Java 泛型劣势**：

- 基本类型无法作为泛型实参，有装箱插箱的开销。
- 泛型类型无法用作方法重载（类型擦除后一致）
- 泛型类型无法当作真实的类型
  - 无法 new T()
  - 无法使用 instanceof 判断泛型类型
- 静态方法无法引用类泛型参数
- 类型强转的运行时开销（编译时插入强转代码）
  
**Java 泛型的附加的签名信息**：

- Gson 中的 TypeTOken
- Retrofit

```java
class SuperClass<T>{}

//反射 SubClass 时，是可以拿到 SuperClass 上的泛型实参 String 的信息的
class SubClass extends SuperClass<String>{

}
```

**关于 Koltin**：

- 扩展：Kotlin 中的反射更强大，有 mate 注解保留了很多编译期信息

#### 技巧点拨

- 结合项目实战
  - 阐述观点，给出实际案例，例如 Gson、Retrofit
  - 实战中需要混淆，选哟注意哪些点以及原理

---
### 2.6 Activity 的 onActivityResult 为什么不设计成回调

#### 考察什么？

- 是否熟悉 onActivityResult 的用法（初级）
- 是否思考过用回调代替 onActivityResult（中级）
- 是否实践过用回调代替 onActivityResult（中级）
- 是否意识到回调存在的问题（高级）
- 是否能给出匿名内部类对外部类引用的解决方案（高级）

#### 题目剖析

Activity 的 onActivityResult 使用起来菲常麻烦，为什么不设计成回调？

- onActivityResult 的作用
- 回调在这样的场景下适用么？
- 如果适用，那为什么不适用回调？
- 如果不适用，给出理由

**onActivityResult 作用**：

- startActivityForResult --> onActivityResult

**onActivityResult 为什么麻烦**：

- 代码逻辑分离，容易出现遗漏和不一致问题（startActivityForResult 后需要在 onActivityResult 继续处理逻辑，使用回调可以解决该问题）
- 写法不够直观，且结果数据没有类型安全的保障（setResult，使用回调无法解决该问题）
- 结果种类较多时，onActivityResult 就会逐渐臃肿且难以维护（使用回调可以解决该问题）

```java
startActivityForResult(intent, onResultCallback(){

})
```

**回调不适用，为什么**：

- Activity 在后台是可能被回收的，如果被回收那么回收前后的 Activity 是不同的实例。

**解决方案**：

- 基于注解处理器和 DummyFragment 的解决方案。
- 使用一个 DummyFragment，转发 Activity 的 onActivityResult。
- 注意在 Activity 回收后，通过 DummyFragment 替换之前 Activity 的引用。
- Fragment 的 mWho 的字段用于标识自身。

---
## 3 熟悉并发编程吗

---
### 3.1 如何停止一个线程

#### 考察什么？

- 是否对线程的用法有所了解？（初级）
- 是否对线程的 stop 方法有所了解（初级）
- 是否对线程 stop 过程中存在的问题有认识（中级）
- 是否熟悉 interupt 中断的用法（中级）
- 是否能解释清楚使用 boolean 标志位的好处（高级）
- 是否知道 interrupt 底层的细节（高级）
- 通过该题目能否将话题转移到线程安全，并阐述无误（高级）

#### 题目剖析

如何停止一个线程

- 官方停止线程的方法被废弃，所以不能直接简单的停止线程
- 如何设计可以随时被终端而取消的任务线程

**为什么不能直接简单的停止线程**：

- 线程可能在占用某些资源，如果直接停止，那么其占用的资源将无法被使用。
- 线程的直接终止，将导致逻辑上的不确定性（某些逻辑执行到一半），很容易造成问题。

**协作的任务执行模式**：

- 通过目标线程自行结束，而不是强制停止。
- 目标线程应该处理中断的能力。
- 中断方式：
  - interrupt 方式
  - boolean 标志位

**interrupt 方式**：

- 原生支持
- 使用 `interrupted()` 方法和 `interrupt()` 方法
- interrupted 和 isInterrupted 方法的区别
  - interrupted 静态方法，获取当前线程的中断状态，并清空。
  - isInterrupted 实例方法，获取该线程的中断状态，不清空。
- interrupted 状态，在 JNI 底层是被加锁保护的。

```java
for(int i = 0; i < 1000000; i++){
    if(interrupted()){
        break;
    }
}
```

**boolean 标志位**：

- 注意使用 volatile 修饰标志位。
- 性能上个更好，不需要 JNI 调用。

---
### 3.2 如何写出线程安全的程序

#### 考察什么？

- 是否对线程安全有初步了解（初级）
- 是否线程安全产生的原因有所思考（中级）
- 是否知道 final、volatile 关键字的作用（中级）
- 是否清楚 1.5 之前 Java DCL 为什么有缺陷（中级）
- 是否清楚地知道如何编写线程安全的程序（高级）
- 是否对 ThreadLocal 的使用注意事项有认识（高级）

#### 题目剖析

如何写出线程安全的程序

- 什么是线程安全？
- 如何实现线程安全？

**什么是线程安全**？

- 线程安全只关注可变可共享的内存
- 线程有自己的工作内存
- 对变量的操作不是原子性的

**如何实现线程安全**？

- 不共享资源
  - ThreadLocal
- 共享不可变资源
  - final
- 工作可变资源
  - 可见性
  - 原子性
  - 禁止重排序

**ThreadLocal**：

- ThreadLcoal 存储的变量是绑定到线程上的
- 内部使用的是 ThreadLocalMap
- ThreadLocalMap 与 WeakHashMap 对比

---| ThreadLocalMap | WeakHashMap
--- | --- | ---
对象持有 | 弱引用 | 弱引用
对象 GC | 不影响 | 不影响
引用清除 | 1 主动移除<br/>2 线程退出时退出 | 1 主动移除<br/>2 GC后移除
Hash 冲突 | 开放地址法 | 单链表法
Hash 计算 | 神奇数字的倍数 | 对象 Hash 值再散列
使用场景 | 对象较少 | 通用

**ThreadLocal使用建议**：

- 声明为静态的 final 成员
- 避免存储大量对象
- 用完后及时移除对象

**final** 字段

- final 字段有禁止重排序的功能
- 不要在构造方法中暴露 final 字段

**volatile** 字段

- 保证可见性
- 禁止重排序

**JUC 工作包**

- lock 等
- atomic
- 并发容器

---
### 3.3 ConcurrentHashMap 如何支持并发访问

#### 考察什么？

- 是否数量掌握线程安全的概念（高级）
- 是否深入理解 CHM 的各项并发优化原理（高级）
- 是否掌握锁的优化方法（高级）

#### 题目剖析

- 并发访问即考察线程安全问题
- 回调 ConcurrentHashMap 的原理即可

**如果对 ConcurrentHashMap 不了解**：

- 分析 HashMap 为什么线程不安全
- 阐述说明你在编写并发程序时，你会怎么做

**CHM 的优化历程**：

- 1.5 分段锁，必要时加锁
  - hash(key)，高位找 segment 锁，低位找 `table[]`。
  - 如果使用整数作为 key，会导致 key 的分布极为不均匀，甚至退化为 HashTable。
- 1.6 优化二次 Hash 算法
  - single-word 二次 hash 优化
- 1.7 段懒加载，volatile & cas
  - getObjectVolatile() 保证锁的可见性
- 1.8 摒弃段，基于 HashMap 原理的并发实现

**CHM 如何计数**：

- JDK5-7 基于段元素个数求和，二次不同就加锁
- JDK8 引入 CounterCell，本质上也是分段计算

**CHM 的弱一致性**：

- 添加元素后不一定能马上读到
- 清空元素后可能仍然有元素
- 遍历之前的段元素的变化会读到
  - 遍历到 14，此时修改了 15，是可以读到 15 的变化的
- 遍历之后的段元素变化读不到
  - 遍历到 15，此时修改了 13，是不可以读到 13 的变化的
- 遍历时元素发生变化不抛异常

**HashTable 的问题**:

- 大锁：对 HashTable 整体加锁
- 长锁：直接对方法加锁
- 读写锁共用：只有一把锁，从头锁到尾

**CHM 的解法**：

- 小锁：分段锁(5-7)、桶节点锁(8)
- 短锁：先尝试获取，失败再加锁
- 分离读写锁：读失败再加锁(5-7)，volatile 读，CAS 写(7-8)

**锁的优化建议**：

- 长锁不如短锁
- 大锁不如小锁
- 共锁不如私锁
- 嵌套锁不如扁平锁
- 分离读写锁
- 粗化高频锁
- 消除无用锁，或用 volatile 代替

---
### 3.4 AtomicReference 和 AtomicReferenceFieldUpdater 有何区别

#### 考察什么？

- 是否熟练掌握原子操作的概念（中级）
- 是否熟悉 AR 和 ARFU 这两个类的用法和原理（中级）
- 是否对 Java 对象的内存占用有认识（高级）
- 是否有较强的敏感度和深入探索的精神（高级）

#### 题目剖析

- AtomicReference 的使用
  - AtomicReference 本身是对对象的引用，对应每个属性，都要创建一个 AtomicReference 对象。
  - 32 位和 64 位(启用指针压缩)，每次创建，都需要消耗 16 个字节的空间，不启用指针压缩的 64 位对象，占用 24 个字节。
- AtomicReferenceFieldUpdater 的使用
  - 需要将那些希望被原子更新的对象属性声明为 volatile。
  - AtomicReferenceFieldUpdater 使用时，不需要针对每个属性创建对象，由此节约了内存。
- BufferedInputStream 中有使用到 AtomicReferenceFieldUpdater。
- kotlin 中的 `val by lazy` 也有使用到 AtomicReferenceFieldUpdater。

---
### 3.5 如何在 Android 中写出优雅的异步代码

#### 考察什么？

- 是否熟练编写异步同步代码（中级）
- 是否熟悉回调地狱（中级）
- 是否能够熟练使用 RxJava（中级）
- 是否对 Kotlin 协程有所了解（高级）
- 是否具备编写良好代码的意识和能力（高级）

#### 题目剖析

如何在 Android 中写出优雅的异步代码

**什么是异步**：
  
- 取决于代码是否顺序执行，而不是线程数。
  
**为什么需要异步**：

- 提高 CPU 利用率（CPU密集型，IO密集型）
- 提升 GUI 程序的响应速度
- 异步不一定快、

**RxJava 异常处理**：

- 没有传入 onError 会导致程序崩溃
- 注意页面销毁时取消 RxJava
- AutoDispose 的使用

**kotlin 协程**：

- 将异步代码转变位同步代码

---
## 4 JNI 编程的细节

---
### 4.1 CPU 架构适配需要注意哪些问题？

#### 考察什么？

- 是否有 Native 开发经验（中级）
- 是否关注过 CPU 架构适配（中级）
- 是否有过含 Native 代码的 SDK 开发经历（中级）
- 是否针对 CPU 架构适配做出过包体积优化（高级）

#### 题目剖析

- native 开发才需要关注 CPU 架构适
- 不同 cpu 架构之间的兼容性
- so 太多，如何优化 apk 体积
- sdk 开发者应该提供哪些 so 库

**cpu 架构之间的兼容性**：

- mip（已经废弃）
- mips 64（已经废弃）
- x86
- x86_64
- armeabi
- armeabi_v7a
- arm64_v8a
- armeabi 兼容 x86 和 其他 arm 架构

**系统加载 so 库的顺序**:

- 系统优先加载对应架构目录下的 so 库
- 要提供就提供一整套

**兼容模式的一些问题**：

- 兼容模式运行的 Native 无法获得最佳性能
  - 所以 x86 的电脑上运行 arm 的虚拟机会很慢
- 兼容模式容易出现一些难以排除的内存问题
- 系统优先加载对应架构目录下的 so 库

**如果优化 APP 体积**

- 结果目标用户群体的设备 cpu 架构来选择合适的 so 库。
- 目前兼容性最好的是 armeabi_v7a，大部分机器都是这个。
- 根据设备 cpu 架构动态加载 so 库。
- 线上监控问题，针对性提供 Native 库
- 非启动加载库可云端下发

**优化 so 体积**：

- 默认隐藏所有符号，只暴露必须公开的
- 禁用 C++ Exception 和 RTTI，用处不大
- 不要使用 iostream，优先使用 Android Log
- 使用 gcc-sections 去掉无用代码
- 构建时分包，借助应用市场分发对应的 APK

**SDK 开发注意**

- 尽量不要使用  Natrive 开发
- 尽量优化 Native 库的体积
- 必须提供完整的 CPU 架构依赖

---
### 4.2 Java Native 方法与 Native 函数是怎么绑定的？

#### 考察什么？

- 是否有 Native 开发经验（中级）
- 是否面对知识善于发现背后的原因（高级）

#### 题目剖析

- 静态绑定：命名规则
- 动态绑定：JVM 注册

**静态绑定**：

- java_类路径名_方法名
- `extern C` 的作用，告诉编译器，编译 Native 函数时，保留名字按照 C 的规则，不能去混淆名字(c++为了避免命名冲突，默认会混淆命名)。
- JNIEXPORT 用于告知编译器，公开该方法符号(即 visibility 位 default)，这样才能被 JVM 发现。
- JNICALL 处理兼容性问题，最好加上。

**动态绑定**：

- 动态绑定任何时候都可以触发。
- 可以借此来实现 so 替换，动态绑定可以覆盖静态绑定。
- 性能由于静态绑定，无需查找
- 重构方便

---
### 4.3 JNI 如何实现数据传递？

#### 考察什么？

- 是否有 Native 开发经验（中级）
- 是否对 JNI 数据传递中的细节有认识（高级）
- 是否能够合理地设计 JNI 的界限（高级）

#### 题目剖析

**通过 long 类型传递对象指针**：

- Bitmap 在 Native 层也有一个类对应，Bitmap 类中有一个 `long mNativePtr` 用于引用底层的 bitmap 指针。

**字符串的操作**：

- GetStringUTFChars/ReleaseStringUTFChars
  - 返回 `const char*` 型，类似 java 中的 byte
  - 拷贝出 `Modified-UTF-8` 的字节流
  - `\0`编码成 `0xC080`，不会影响 C 字符串结尾
- GetStringChars/ReleaseStringChars
  - 返回 `const jchar*` 类型
  - JNI 函数自动处理字节序转换
- GetStringUTFRegion/GetStringRegion
  - 先在 C 层创建足够容量的空间
  - 将字符串的某一个部分拷贝到开辟好的空间
  - 针对性复制，少量读取时效率更优
- GetStringCritical/ReleaseStringCritical
  - 调用对中间会停止 JVM GC
  - 调用对之间不可有其他的 JNI 操作
  - 调用对可嵌套

**字符串操作的 isCopy**：

- `const char * GetStringUTFChars(JNIEnv *env, jstring string, jboolean *isCopy);`
- `const jchar * GetStringChars(JNIEnv *env, jstring string, jboolean *isCopy);`
- `const jchar * GetStringCritical(JNIEnv *env, jstring string, jboolean *isCopy);`

- `isCopy = false` 表示 native 中的指针指向的字符串就是在 Java 内存分配的。JVM GC 应该保证该内存块不被回收，大部分 JVM 都不会这样实现，因为很繁琐。
- `isCopy = true` 表示 native 中的指针指向的字符串不是在 Java 内存分配的。而是复制一份到 native 内存中。
- 拷贝与否，取决于 JVM 实现。

**对象数组传递**：

- LocalReference 在方法结束后会被自动使用。
- 但 LocalReference 有数量限制，如果一个方法中需要大量创建 LocalReference(比如 for 循环中)，则应该一边释放旧的 LocalReference，一边创建新的 LocalReference。

**DirectBuffer**：

- `ByteBuffer.allocateDirect()`
- 不需要拷贝
- 需要自己处理字节序

---
### 4.4 如何全局捕获 Native 异常？

#### 考察什么？

- 是否熟悉 Linux 的信号（中级）
- 是否熟悉 Native 层任意位置获取 jclass 的方法（高级）
- 是否熟悉底层线程与 Java 虚拟机的关系（高级）
- 通过实现细节的考察，确认候选人的项目经验（高级）

#### 题目剖析

- 如果捕获异常
- 如何清理 Native 层和 Java 层的资源
- 如何排除定位问题

**捕获 Native 异常**：

- `sigaction()` 函数

**传递异常到 Java 层**：

- javaVM 全局不会变
- 通过  javaVM 可以获取到 JNIEnv 指针
- 返回通过反射调用 Java 层
- 注意：Native 线程需要 attach 到 JVM，才能通过 javaVM 获取 JNIEnv，只有 detach 才能清理期间创建的 Jvm 对象
- ClassLoader 一定要保证一致，可以在初始化时设置全局的 ClassLoader。

```c
static jobject classLoader;

jint setUpClassLoader(JNIEnv *env){
    jclass applicationClass = ent->FindClass("xxx/xxx/xxx/AppContext");//获取 AppContext.class
    jclass classClass = getObjectClass(applicationClass);//获取 java.lang.Class 对象
    jmethodID getClassLoaderMethod = env->GetMethodID(classClass, "getClassLoader", "()Ljava/lang/ClassLoader;");
    classLoader = env->NewGlobalRef(env->CallObjectMethod(applicationClass, getClassLoaderMethod));
    return classLoade == NULL? JNI_ERR:JNI_OK;
}
```

**捕获 Native 异常堆栈**：

- 设备备用栈，防止 SIGSEGV 因栈溢出而出现堆栈被破坏。
- 创建独立线程专门用于堆栈收集并回收至 Java 层。
- 收集堆栈信息：
    - `[4.4.1, 5.0]` 使用内置 `libcorkscrew.so`
    - `5.0+`使用自己编译的 `libnuwind`
- 通过线程关联 Native 异常对应的 Java 堆栈

---
### 4.5 只有 C、C++ 可以编写 JNI 的 Native 库吗？

#### 考察什么？

- 是否对 JNI 函数绑定的原理有深入认识（高级）
- 是否有底层开发有丰富经验

#### 题目剖析

- Native 程序与 Java 关联的本质是什么？

**JVM 对 Native 函数的要求**：

- 静态绑定
  - 符号绑定
  - 符号符合 Java Native 方法的 `包名_类名_方法名`
  - 符号名按照 C 语言的规则修饰
- 动态绑定
  - 函数本身无要求
  - JNI 可识别入口函数如 JNI_OnLoad 进行注册即可

**可选的 Native 语言**：

- Golang
- Rust
- Kotlin Native
- Scala Native
- 其他语言，理论上都可以

**认识 Kotlin Native**

- kotlin jvm
- kotlin js
- kotlin native