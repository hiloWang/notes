# Javassist

Javassist 是一个字节码操作库，Javascript（Java编程助手）使Java字节码操作变得简单。它使Java程序能够在运行时定义一个新类，并在JVM加载时修改类文件，Javassist提供了两个级别的API：source level 和 bytecode level。

- source level：如果使用源代码级 API，则可以在不知道 Java 字节码规范的情况下编辑类文件。可以用源文本的形式指定插入的字节码，Javassist 即时编译它。
- bytecode level：使用这个级别的API，可以直接编辑 class 字节码。


文档版本：`3.22.0-GA`

---
## 1 读写字节码

- Java 字节码以二进制的形式存储在 class 文件中
- 在 Javassist 中，类 `javaassit.CtClass` 表示 class 文件
- ClassPool 是 CtClass 对象的容器。它按需读取类文件来构造 CtClass 对象，并且保存 CtClass 对象以便以后使用。从 ClassPool 中获取的 CtClass 是可以被修改的

```
//使用 getDefault() 方法获取的 ClassPool 对象使用的是默认系统的类搜索路径。
ClassPool pool = ClassPool.getDefault();

//ClassPool 是一个存储 CtClass 的 Hash 表，类的名称作为 Hash 表的 key。ClassPool 的 get() 函数用于从 Hash 表中查找 key 对应的 CtClass 对象。
//如果没有找到，get() 函数会创建并返回一个新的 CtClass 对象，这个新对象会保存在 Hash 表中。
CtClass cc = pool.get("test.Rectangle");

//编辑类
cc.setSuperclass(pool.get("test.Point"));

//调用 writeFile() 后，这项修改会被写入原始类文件。writeFile() 会将 CtClass 对象转换成类文件并写到本地磁盘。
cc.writeFile();
```

- `byte[] b = cc.toBytecode();`：获取修改过的字节码
- `Class clazz = cc.toClass();`：toClass 请求当前线程的 ClassLoader 加载 CtClass 所代表的类文件。它返回此类文件的 `java.lang.Class` 对象

### 定义新的类

使用 ClassPool 的 `makeClass()` 方法可以定义一个新类。

```java
//定义了一个空的 Point 类。Point 类的成员方法可以通过 CtNewMethod 类的工厂方法来创建，
//然后使用 CtClass 的 addMethod() 方法将其添加到 Point 中。
ClassPool pool = ClassPool.getDefault();
CtClass cc = pool.makeClass("Point");
```

- `makeInterface()` 方法可以创建新接口。接口中的方法可以使用 CtNewMethod 的 `abstractMethod()` 方法创建。

### 将类冻结

- 如果一个 CtClass 对象通过 `writeFile(), toClass(), toBytecode()` 被转换成一个类文件，此 CtClass 对象会被冻结起来，不允许再修改。因为一个类只能被 JVM 加载一次。一个冷冻的 CtClass 可以被解冻，调用 `defrost()`解冻之后，此 CtClass 对象又可以被修改
- `ClassPool.doPruning`：这是一个静态字段，表示 Javassist 在冻结 CtClass 时，会修剪 CtClass 的数据结构。为了减少内存的消耗，修剪操作会丢弃 CtClass 对象中不必要的属性。例如，Code_attribute 结构会被丢弃。一个 CtClass 对象被修改之后，方法的字节码是不可访问的，但是方法名称、方法签名、注解信息可以被访问。修剪过的 CtClass 对象不能再次被解冻。
- `stopPruning()` ：用来驳回修剪操作
- `debugWriteFile()`：调试的时候，你可能临时需要停止修剪和冻结，然后保存一个修改过的类文件到磁盘，该方法停止修剪，然后写类文件，然后解冻并再次打开修剪

### 类搜索路径

通过 `ClassPool.getDefault()` 获取的 ClassPool 使用 JVM 的类搜索路径。不过可以给 ClassPool 添加额外的类路径

```
//将 this 指向的类添加到 pool 的类加载路径中
pool.insertClassPath(new ClassClassPath(this.getClass()));

//注册一个目录作为类搜索路径
pool.insertClassPath("/usr/local/javalib");

//注册一个URL作为类搜索路径
pool.insertClassPath(new URLClassPath("www.javassist.org", 80, "/java/", "org.javassist."))

//直接给一个ClassPool对象提供一个字节数组，并从该数组构造一个CtClass对象
ClassPool cp = ClassPool.getDefault();
byte[] b = a byte array;
String name = class name;
cp.insertClassPath(new ByteArrayClassPath(name, b));
CtClass cc = cp.get(name);
```

如果不知道类的完全限定名称，则可以在 ClassPool 中使用 `makeClass()`方法,`makeClass()` 返回从给定输入流构造的 CtClass 对象。 使用 `makeClass()` 将类文件提供给 ClassPool 对象。如果搜索路径包含大的 jar 文件，这可能会提高性能。由于 ClassPool 对象按需读取类文件，它可能会重复搜索整个 jar 文件中的每个类文件。 `makeClass()` 可以用于优化此搜索。由 `makeClass()` 构造的 CtClass 保存在 ClassPool 对象中，从而使得类文件不会再被读取。

```
ClassPool cp = ClassPool.getDefault();
InputStream ins = an input stream for reading a class file;
CtClass cc = cp.makeClass(ins);
```

---
## 2 ClassPool

ClassPool 是 CtClass 对象的容器。因为编译器在编译引用 CtClass 代表的 Java 类的源代码时，可能会引用 CtClass 对象，所以一旦一个 CtClass 被创建，它就被保存在 ClassPool 中。

### 避免内存溢出

如果 CtClass 对象的数量变得非常大（这种情况很少发生，因为 Javassist 试图以各种方式减少内存消耗），ClassPool 可能会导致巨大的内存消耗。 为了避免此问题，可以从 ClassPool 中显式删除不必要的 CtClass 对象。 如果对 CtClass 对象调用 `detach()`，那么该 CtClass 对象将被从 ClassPool 中删除。

```
CtClass cc = ... ;
cc.writeFile();
//在调用 detach() 之后，就不能调用这个 CtClass 对象的任何方法了。但是如果调用 ClassPool 的 get() 方法，
//ClassPool 会再次读取这个类文件，创建一个新的 CtClass 对象。
cc.detach();
```

另一种方式是：用新的 ClassPool 替换旧的 ClassPool，并将旧的 ClassPool 丢弃。 如果旧的 ClassPool 被垃圾回收掉，那么包含在 ClassPool 中的 CtClass 对象也会被回收。

```
ClassPool cp = new ClassPool(true);//true表示附加了系统搜索路径
//或
ClassPool cp = new ClassPool();
cp.appendSystemPath();
```


### 级联的 ClassPools

就像 ClassLoader 有继承关系和委托机制一样，ClassPools也具有类似的特性，如果程序正在 Web 应用程序服务器上运行，应为每个类加载器（即容器）创建一个ClassPool实例。程序应该通过不调用 `getDefault()` 而是通过ClassPool 的构造函数来创建一个 ClassPool 对象。

```
ClassPool parent = ClassPool.getDefault();
ClassPool child = new ClassPool(parent);
child.insertClassPath("./classes");
```

如果调用 child.get()，子 ClassPool 首先委托给父 ClassPool。如果父 ClassPool 找不到类文件，那么子 ClassPool 会尝试在 `./classes` 目录下查找类文件。如果 `child.childFirstLookup` 返回 true，那么子类 ClassPool 会在委托给父 ClassPool 之前尝试查找类文件。

### 更改类名以定义新类

下面程序调用 CtClass 的 `setName()`方法将  CtClass 对象的名称设置为 Pair。在这个调用之后，这个 CtClass 对象所代表的类的名称 Point 被修改为 Pair。类定义的其他部分不会改变。

```
ClassPool pool = ClassPool.getDefault();
CtClass cc = pool.get("Point");
cc.setName("Pair");
```

CtClass 中的 setName() 改变了 ClassPool 中的记录。从实现的角度来看，一个 ClassPool 对象是一个 CtClass 对象的哈希表。setName() 更改了与哈希表中的 CtClass 对象相关联的 Key。Key 从原始类名更改为新类名。因此，如果后续在 ClassPool 对象上再次调用 get("Point")，则它不会返回变量 cc 所指的 CtClass 对象。 而是再次读取类文件 Point.class，并为类 Point 构造一个新的 CtClass 对象。 因为与 Point 相关联的 CtClass 对象不再存在。

```
ClassPool pool = ClassPool.getDefault();
CtClass cc = pool.get("Point");
CtClass cc1 = pool.get("Point");   // cc1与cc相同。
cc.setName("Pair");
CtClass cc2 = pool.get("Pair");    // cc2与cc相同。
CtClass cc3 = pool.get("Point");   // cc3与cc不相同。

cc.writeFile(path); //将新的类写入磁盘
```

cc1 和 cc2 指向 CtClass 的同一个实例，而 cc3 不是。 注意，在执行 `cc.setName("Pair")` 之后，cc 和 cc1 引用的 CtClass 对象都表示 Pair 类。ClassPool 对象用于维护类和 CtClass 对象之间的一对一映射关系。 为了保证程序的一致性，**Javassist 不允许用两个不同的 CtClass 对象来表示同一个类，除非创建了两个独立的 ClassPool。**如果有两个 ClassPool 对象，那么可以从每个 ClassPool 中获取一个表示相同类文件的不同的 CtClass 对象。 然后修改这些 CtClass 对象来生成不同版本的类。

### 通过重命名冻结的类来生成新的类

一旦一个 CtClass 对象被 `writeFile()` 或 `toBytecode()` 转换为一个类文件，Javassist 会拒绝对该 CtClass 对象的进一步修改。因此，在表示 Point 类的 CtClass 对象被转换为类文件之后，不能将 Pair 类定义为 Point 的副本，因为在 Point 上执行 `setName()` 会被拒绝。 以下代码段是错误的：

```
ClassPool pool = ClassPool.getDefault();
CtClass cc = pool.get("Point");
cc.writeFile();
cc.setName("Pair");    // 错误，因为writeFile（）已被调用。
```

为了避免这种限制，应该在 ClassPool 中调用 `getAndRename()` 方法。

```
ClassPool pool = ClassPool.getDefault();
CtClass cc = pool.get("Point");
cc.writeFile();
CtClass cc2 = pool.getAndRename("Point", "Pair");
```

如果调用 `getAndRename()`，ClassPool 首先**读取** Point.class 来**创建**一个新的表示 Point 类的 CtClass 对象。 而且它会在这个 CtClass 被记录到哈希表之前，将 CtClass 对象重命名为 Pair。因此，`getAndRename()` 可以在表示 Point 类的 CtClass 对象上调用 `writeFile()` 或 `toBytecode()` 后执行。


---
## 3 类加载器

如果事先知道哪些类必须被修改，修改这些类最简单的方法如下：

1. Get a CtClass object by calling ClassPool.get(),
2. Modify it, and
3. Call `writeFile()` or `toBytecode()` on that CtClass object to obtain a modified class file.

如果一个类在加载是才确定是否要修改，必须使 Javassist 与类加载器协作，Javassist 可以与类加载器一起使用，以便在加载时可以修改字节码。我们可以定义自己的类加载器版本，也可以使用Javassist提供的类加载器。

### The toClass method in CtClass


CtClass 提供了一个方便的方法 `toClass()`，它请求当前线程的上下文类加载器加载由 CtClass 对象表示的类。要调用此方法，调用者必须具有适当的权限，否则可能会抛出 SecurityException。

如何使用`toClass()`：

```
public class Hello {
    public void say() {
        System.out.println("Hello");
    }
}

public class Test {
    public static void main(String[] args) throws Exception {
        ClassPool cp = ClassPool.getDefault();
        CtClass cc = cp.get("Hello");
        CtMethod m = cc.getDeclaredMethod("say");
        m.insertBefore("{ System.out.println(\"Hello.say():\"); }");
        Class c = cc.toClass();
        Hello h = (Hello)c.newInstance();
        h.say();
    }
}
```

**如果程序在 JBoss 和 Tomcat 等应用服务器上运行**，`toClass()` 使用的默认上下文类加载器可能不合适，在这种情况下会抛出 ClassCastException。为了避免这个异常，你必须明确地给 `toClass()`一个合适的类加载器。例如，如果 bean 是你的会话 bean 对象，那么下面的代码才能正常工作：

```
CtClass cc = ...;
Class c = cc.toClass(bean.getClass().getClassLoader());
```
`toClass()` 是为了方便而提供的。如果需要更复杂的功能，应该编写自己的类加载器。


### Class loading in Java

在 Java 中，多个类加载器可以共存，并且每个类加载器都创建它自己的名称空间。不同的类加载器可以使用相同的类名加载不同的类文件。加载的两个类被认为是不同的类。此功能使我们能够在单个JVM上运行多个应用程序，即使这些程序包含具有相同名称的不同类。**注意**：JVM不允许动态重载一个类。一旦类加载器加载一个类，它就不能在运行时重新加载该类的修改版本。因此，在JVM加载它之后无法更改类的定义。但是，JPDA（Java Platform Debugger Architecture）为重新加载类提供了有限的能力。

如果同一个类文件由两个不同的类加载器加载，那么JVM将创建两个具有相同名称和定义的不同类，这两个 Class 会被认为是不同的 Class。一个类的实例不能分配给另一个类的变量。两个类之间的转换操作失败并抛出 ClassCastException。

例如，下面的代码片段会引发异常：

```
MyClassLoader myLoader = new MyClassLoader();
Class clazz = myLoader.loadClass("Box");
Object obj = clazz.newInstance();
Box b = (Box)obj;    // this always throws ClassCastException.
```

多个类加载器形成一个树结构。除引导程序加载程序之外的每个类加载程序都有一个父类加载程序，它通常加载该子类加载程序的类。由于加载类的请求可以沿着这个类加载器的层次结构进行委派，一个类可以被一个类加载器加载，而可能你没有请求加载这个类。因此，被请求加载类 C 的类加载器可能与实际加载类 C 的加载器不同。为区分，我们称前者为`the initiator of C `，我们称后者 `the real loader of C`。

除此之外，如果请求加载类 C（`the initiator of C`）的类加载器 CL 委派给父类加载器 PL，则类加载器 CL 永远不会被请求加载在类 C 的定义中引用的任何类，因此，CL 不是这些类的`the initiator of C`。相反，父类加载器PL成为它们的`the initiator of C`，并且请求加载它们，**C 的定义中引用的类由 C 的实际装入器加载。**

为了理解这种行为，我们来考虑下面的例子。：

```
public class Point {    // loaded by PL
    private int x, y;
    public int getX() { return x; }
        :
}

public class Box {      // the initiator is L but the real loader is PL
    private Point upperLeft, size;
    public int getBaseX() { return upperLeft.x; }
        :
}

public class Window {    // loaded by a class loader L
    private Box box;
    public int getBaseX() { return box.getBaseX(); }
}
```

假设类窗口是由类加载器L加载的。Window的 initiator 和 real loader 都是L。由于 Window 的定义指向 Box，因此 JVM 将请求L加载Box。这里，假设L将这个任务委托给父类加载器 PL。Box 的 initiator 是L，但 real loader 是 PL。在这种情况下，Point 的发起者不是 L，而是 PL，因为它与 Box 的真实加载器相同。因此 L 永远不会被请求加载 Point。

接下来，让我们考虑一个稍微修改的例子：

```
public class Point {
    private int x, y;
    public int getX() { return x; }
        :
}

public class Box {      // the initiator is L but the real loader is PL
    private Point upperLeft, size;
    public Point getSize() { return size; }
        :
}

public class Window {    // loaded by a class loader L
    private Box box;
    public boolean widthIs(int w) {
        Point p = box.getSize();
        return w == p.getX();
    }
}
```

现在，Window 的定义也指向 Point。在这种情况下，如果请求加载 Point，则类加载器 L 也必须委派给 PL。**必须避免让两个 ClassLoader 双重加载同一个 Class。**两个 ClassLoader 中的一个必须委托给另一个。如果 L 在加载 Point 时未委托给 PL，则` widthIs()` 将引发 ClassCastException。因为 Box 的 `real loader` 是 PL，因此，`getSize()` 的结果值是由 PL 加载的 Point 的实例，而 `widthIs()` 中的变量 p 的类型是由 L 加载的 Point。

这种行为有些不方便，但是必要的。如果以下声明：`Point p = box.getSize();`。如果没有抛出异常，那么 Window 的程序员可能会破坏 Point 对象的封装。例如，字段 x 在由 PL 加载的 Point 中是私有的。但是，如果 L 使用以下定义加载 Point，那么 Window 类可以直接访问 x 的值：

```
public class Point {
    public int x, y;    // not private
    public int getX() { return x; }
        :
}
```

###  Using javassist.Loader

Javassist 提供了一个类加载器 `javassist.Loader`。这个类加载器使用 `javassist.ClassPool` 对象来读取类文件。例如，javassist.Loader 可用于加载用 Javassist 修改的特定类。

```
import javassist.*;
import test.Rectangle;

public class Main {
  public static void main(String[] args) throws Throwable {
     ClassPool pool = ClassPool.getDefault();
     Loader cl = new Loader(pool);

     CtClass ct = pool.get("test.Rectangle");
     ct.setSuperclass(pool.get("test.Point"));

     Class c = cl.loadClass("test.Rectangle");
     Object rect = c.newInstance();
         :
  }
}
```

如果用户想要在加载时按需修改类，则用户可以将事件监听器添加到 `javassist.Loader`。当类加载器加载一个类时，会通知添加的事件监听器。事件侦听器类必须实现以下接口：

```
public interface Translator {
    //当通过javassist.Loader中的addTranslator() 将此事件侦听器添加到javassist.Loader对象时，会调用start()方法。
    public void start(ClassPool pool)
        throws NotFoundException, CannotCompileException;

    //在javassist.Loader加载一个类之前调用onLoad()方法。 onLoad()可以修改加载类的定义。
    public void onLoad(ClassPool pool, String classname)
        throws NotFoundException, CannotCompileException;
}
```
比如：以下事件监听器在加载之前将所有类更改为公共类。

```
public class MyTranslator implements Translator {
    void start(ClassPool pool)
        throws NotFoundException, CannotCompileException {}
    void onLoad(ClassPool pool, String classname)
        throws NotFoundException, CannotCompileException
    {
        CtClass cc = pool.get(classname);
        cc.setModifiers(Modifier.PUBLIC);
    }
}
```
注意，`onLoad()` 方法不必调用 `toBytecode()` 或 `writeFile()`，因为 javassist.Loader 会调用这些方法来获取类文件。要使用 MyTranslator 对象运行应用程序类 MyApp，请编写一个主类，如下所示：

```
import javassist.*;

public class Main2 {
  public static void main(String[] args) throws Throwable {
     Translator t = new MyTranslator();
     ClassPool pool = ClassPool.getDefault();
     Loader cl = new Loader();
     cl.addTranslator(pool, t);
     cl.run("MyApp", args);
  }
}
```

执行这个程序`java Main2 arg1 arg2...`，MyApp 类和其他应用程序类由 MyTranslator 转换，象 MyApp 这样的应用程序类不能访问诸如Main2、MyTranslator 和 ClassPool 的加载器类，因为它们是由不同的加载器加载的。`javassist.Loader` 以与 `java.lang.ClassLoader` 不同的顺序搜索类。 ClassLoader 首先将加载操作委托给父类加载器，然后仅当父类加载器找不到它们时才尝试加载类，另一方面，**`javassist.Loader` 尝试在委托给父类加载器之前加载这些类**。只有在以下情况下才会授权：

- 通过在 ClassPool 对象上调用 `get()`不能找到这些类
- 通过使用由父类加载器加载的 `delegateLoadingOf()` 来指定类

此搜索顺序允许Javassist加载修改后的类，但是，如果由于某种原因无法找到修改的类，它将委托给父类加载器。一旦一个类被父类加载器加载，该类中引用的其他类也将由父类加载器加载，因此它们不会被修改，回想一下，C 类中引用的所有类都由C 的真实 `real loader` 加载，**如果程序无法加载修改的类，则应确保使用该类的所有类已由 `javassist.Loader` 加载。**



### Writing a class loader

使用Javassist的简单类加载器如下所示：

```
import javassist.*;

public class SampleLoader extends ClassLoader {
    /* Call MyApp.main().
     */
    public static void main(String[] args) throws Throwable {
        SampleLoader s = new SampleLoader();
        Class c = s.loadClass("MyApp");
        c.getDeclaredMethod("main", new Class[] { String[].class })
         .invoke(null, new Object[] { args });
    }

    private ClassPool pool;

    public SampleLoader() throws NotFoundException {
        pool = new ClassPool();
        pool.insertClassPath("./class"); // MyApp.class must be there.
    }

    /* Finds a specified class.
     * The bytecode for that class can be modified.
     */
    protected Class findClass(String name) throws ClassNotFoundException {
        try {
            CtClass cc = pool.get(name);
            // modify the CtClass object here
            byte[] b = cc.toBytecode();
            return defineClass(name, b, 0, b.length);
        } catch (NotFoundException e) {
            throw new ClassNotFoundException();
        } catch (IOException e) {
            throw new ClassNotFoundException();
        } catch (CannotCompileException e) {
            throw new ClassNotFoundException();
        }
    }
}
```

MyApp 类是一个应用程序。要执行这个程序，首先将类文件放在 `./class` 目录下，这个目录不能包含在类搜索路径中。否则，MyApp.class将被默认系统类加载器加载，该加载器是SampleLoader的父装载器。目录名./class由构造函数中的insertClassPath（）指定。如果需要，你可以选择不同的名称而不是./class。然后执行如下操作：`% java SampleLoader`，类加载器加载 MyApp类（`./class/MyApp.class`），并调用 `MyApp.main()` 的命令行参数。

这是使用 Javassist 最简单的方法。但是，如果你编写更复杂的类加载器，可能需要详细了解 Java 的类加载机制。例如，上面的程序将MyApp 类放入与 SampleLoader 类所属名称空间分开的名称空间中，因为这两个类是由不同的类加载器加载的。因此，MyApp 类不能直接访问类 SampleLoader。

### Modifying a system class

诸如 `java.lang.String` 之类的系统类不能由系统类加载器以外的类加载器加载，因此，上面显示的`SampleLoader`或`javassist.Loader`无法在加载时修改系统类。如果应用程序需要这样做，则系统类必须进行静态修改：

```
ClassPool pool = ClassPool.getDefault();
CtClass cc = pool.get("java.lang.String");
CtField f = new CtField(CtClass.intType, "hiddenValue", cc);
f.setModifiers(Modifier.PUBLIC);
cc.addField(f);
cc.writeFile(".");
```
这个程序生成一个文件`“./java/lang/String.class”。`要使用此修改的 String 类运行程序 MyApp，应执行如下操作：`java -Xbootclasspath/p:. MyApp arg1 arg2...`，假设MyApp的定义如下：

```
public class MyApp {
    public static void main(String[] args) throws Exception {
        System.out.println(String.class.getField("hiddenValue").getName());
    }
}
```

如果修改后的 String 类被正确加载，MyApp 将打印 `hiddenValue`。


### Reloading a class at runtime

如果在启用JPDA（Java Platform Debugger Architecture）的情况下启动JVM，则可以动态地重新加载类。在JVM加载一个类之后，可以卸载旧版本的类定义，并且可以重新加载一个新类。也就是说，该类的定义可以在运行时动态修改。但是，新的类定义必须与旧的定义兼容。**JVM 不允许两个版本之间的模式更改。它们必须具有相同的方法和字段。**，Javassist为在运行时重新加载类提供了一个方便的类：`javassist.tools.HotSwapper`。


----
## 4 自省和自定制 (Introspection and customization)

CtClass 提供了内省方法。 Javassist 的自省能力与 Java 反射 API 兼容。CtClass提供了`getName()，getSuperclass()，getMethods()`等。CtClass还提供了修改类定义的方法。它允许添加一个新的字段，构造函数和方法。插装(添加新的逻辑)方法体也是可能的。

Javassist中，Method 由 CtMethod 对象表示。 CtMethod 提供了几种修改 Method 定义的方法，请注意，如果方法是从超类继承的，那么表示继承方法的相同 CtMethod 对象表示在该超类中声明的方法。CtMethod对象对应于每个方法声明。

例如，如果类Point声明方法 `move()` 并且 Point 的子类 ColorPoint 不覆盖 `move()`，在 Point 中声明并在 ColorPoint 中继承的两个 `move()` 方法由相同的 CtMethod 对象表示。如果修改了此CtMethod 对象表示的方法定义，则修改将反映在两个方法上。如果只想修改 ColorPoint 中的 `move()` 方法，则首先必须向 ColorPoint 添加表示 Point 中的 `move()` 的 CtMethod 对象的副本。 CtMethod 对象的副本可以通过`CtNewMethod.copy()` 获取。


Javassist**不允许删除方法或字段**，但它允许更改名称。因此，如果方法不再必要，则应通过调用在 CtMethod 中声明的 `setName()` 和 `setModifiers()`来重命名并更改为私有方法。Javassist不允许向现有方法添加额外的参数，如果需要这样做，那么接收额外参数以及其他参数的新方法应该添加到同一个类中。例如，如果你想添加一个额外的 int 参数newZ 给一个方法：

```java
void move(int newX, int newY) { x = newX; y = newY; }
//在Point类中，则应该将以下方法添加到Point类中：
void move(int newX, int newY, int newZ) {
    // do what you want with newZ.
    move(newX, newY);
}
```

Javassist 还提供了用于直接编辑原始类文件的低级 API。例如，CtClass 中的 `getClassFile()` 返回表示原始类文件的 ClassFile 对象。 CtMethod 中的 `getMethodInfo()` 返回一个 MethodInfo 对象，该对象表示包含在类文件中的 method_info 结构。低级 API 遵循 Java 虚拟机规范，开发者必须具备关于类文件和字节码的知识。

只有使用以 `$` 开头的特殊标识符时，Javassist 修改的类文件才需要 `javassist.runtime` 包用于运行时支持。下面介绍这些特殊标识符。在没有这些特殊标识符的情况下修改的类文件在运行时不需要 `javassist.runtime` 包或任何其他Javassist 包。

### 在方法主体的 开始/结尾 处插入源代码

CtMethod 和 CtConstructor 提供了 `insertBefore()`，`insertAfter()` 和 `addCatch()`方法。它们用于将代码片段插入现有方法的主体中。这些代码片段可以是开发者可以用 Java 编写的源代码。Javassist包含一个用于处理源文本的简单Java编译器。它接收用 Java 编写的源文本并将其编译为 Java 字节码，该字节码将**内联**到方法体中。

方法`insertBefore()`，`insertAfter()`，`addCatch()`和`insertAt()` 接收表示语句或块的 String 对象。声明是一个单一的控制结构，如 if 和 while 或以分号`;` 结尾的表达式。块是用大括号`{}`包围的一组语句。因此，以下每行都是有效语句或块的示例：

```
System.out.println("Hello");
{ System.out.println("Hello"); }
if (i < 0) { i = -i; }
```

这些语句和块可以引用字段和方法。如果使用 `-g` 选项编译该方法（在类文件中包含本地变量属性），它们也可以引用它们插入的方法的参数。否则，他们必须通过下面描述的特殊变量`$ 0，$ 1，$ 2 ...`来访问方法参数。**访问方法中声明的局部变量是不允许的**，但是允许在块中声明新的局部变量。但是，`insertAt()` 允许语句和块访问局部变量（如果这些变量在指定的行号可用并且目标方法是使用 `-g` 选项编译的）。

> 出于优化考虑，java编译会把方法参数名重命名为arg1,arg2之类的参数。javac 的 -g 指令用于生成所有调试信息

传递给方法`insertBefore()`，`insertAfter()`，`addCatch()` 和 `insertAt()` 的 String 对象由Javassist中包含的编译器编译。由于编译器支持语言扩展，因此以 `$` 开头的多个标识符具有特殊含义：


符号 | 含义
--- | ---
`$0, $1, $2, ...` | this and 方法的参数
`$args` | 方法参数数组.它的类型为 `Object[]`
`$$` | 所有实参。例如, `m($$)` 等价于 `m($1,$2,...)`
`$cflow(...)` | cflow 变量
`$r` | 返回结果的类型，用于强制类型转换
`$w` | 包装器类型，用于强制类型转换
`$_` | 返回值
`$sig` | 类型为 `java.lang.Class` 的参数类型数组
`$type` | 一个 `java.lang.Class` 对象，表示返回值类型
`$class` | 一个 `java.lang.Class` 对象，表示当前正在修改的类]


####  `$n` 引用参数

传递给目标方法的参数使用 `$1，$2，...` 访问，而不是原始的参数名称。 `$1` 表示第一个参数，`$2` 表示第二个参数，以此类推。 这些变量的类型与参数类型相同。 `$0` 等价于 `this` 指针。 如果方法是静态的，则 `$0` 不可用。

下面有一些使用这些特殊变量的例子。假设一个类 Point：

```
class Point {
    int x, y;
    void move(int dx, int dy) { x += dx; y += dy; }
}
```

要在调用方法 `move()` 时打印 dx 和 dy 的值，请执行以下程序：

```
ClassPool pool = ClassPool.getDefault();
CtClass cc = pool.get("Point");
CtMethod m = cc.getDeclaredMethod("move");
m.insertBefore("{ System.out.println($1); System.out.println($2); }");
cc.writeFile();
```

请注意，传递给 `insertBefore()` 的源文本是用大括号 `{}` 括起来的。`insertBefore()` 只接受单个语句或用大括号括起来的语句块。修改后的类 Point 的定义是这样的：

```
class Point {
    int x, y;
    void move(int dx, int dy) {
        { System.out.println(dx); System.out.println(dy); }
        x += dx; y += dy;
    }
}
```

`$1` 和 `$2` 分别替换为dx和dy。`$1，$2，$3 ...`是可更新的。如果一个新值被分配给其中一个变量，那么该变量所代表的参数值也会被更新。

####  `$args`

变量 `$args` 表示所有参数的数组。该变量的类型是类 `Object` 的数组。如果参数类型是像 `int` 这样的基本类型，那么该参数值将被转换为包装对象，如 `java.lang.Integer` 以存储在 `$args`中。因此，`$args[0]`相当于 `$1`，除非第一个参数的类型是基本类型。请注意 `$args[0]`不等于 `$0`; `$0`表示 this 引用。

如果将一个 Object 数组分配给 `$args`，则该数组的每个元素都会分配给每个参数。如果参数类型是基本类型，则相应元素的类型必须是包装类型，在将值分配给参数之前，该值将从包装器类型转换为基元类型。

#### `$$`

变量 `$$` 是以逗号分隔的所有参数列表的缩写。例如，如果方法 `move()`的参数数量是三个：

```
move($$)
//等价于
move($1, $2, $3)
```

如果 `move()` 不带任何参数，则 `move($$)` 等同于 `move()`。`$$`可以与另一种方法一起使用。如果你写一个表达式：`exMove($$, context)`，则它等价于`exMove($1, $2, $3, context)`

请注意，`$$` 允许方法调用的通用符号与参数的数量有关。它通常与 `$proceed`一起使用。

####  `$cflow`

`$cflow` 表示**控制流**。此只读变量将**递归调用的深度**返回给特定的方法。假设下面显示的方法由 CtMethod 对象 cm 表示：

```
int fact(int n) {
    if (n <= 1)
        return n;
    else
        return n * fact(n - 1);
}
```

要使用 `$cflow`，首先声明 `$cflow` 用于监视对方法 `fact()` 的调用：

```
CtMethod cm = ...;
cm.useCflow("fact");
```

`useCflow()`的参数是声明的 `$cflow`变量的标识符。任何有效的Java名称都可以用作标识符。标识符也可以包含`.` 所以 `my.Test.fact`是一个有效的标识符。然后，`$cflow（fact)`表示对由 `cm.` 指定的方法的递归调用的深度。当方法被第一次调用时，`$cflow(fact)`的值为 0，而在方法中递归调用方法时，它的值为 1。例如：

```
//$cflow(fact)用于返回 fact 方法递归调用的深度
cm.insertBefore("if ($cflow(fact) == 0)"
              + "    System.out.println(\"fact \" + $1);");
```

转换方法 `fact()` 以显示参数。由于检查了 `$cflow(fact)` 的值，因此  `fact()` 方法在  `fact()` 中递归调用时不会显示该参数。`$cflow` 的值是与**当前线程的当前最高堆栈帧**下的指定方法cm相关联的堆栈帧的数量。`$cflow`  也可以在不同于指定方法cm的方法中访问。


#### `$r`

`$r` 表示方法的结果类型（返回类型）。它用在 cast 表达式中作 cast 转换类型。 下面是一个典型的用法：

```
Object result = ... ;
$_ = ($r)result;
```

如果结果类型是原始类型，则 `$r` 遵循特殊语义。 首先，如果 cast 表达式的操作数是原始类型，`$r` 作为普通转换运算符。 另一方面，如果操作数是包装类型，`$r` 将从包装类型转换为结果类型。 例如，如果结果类型是 int，那么 `$r` 将从 `java.lang.Integer` 转换为 int。

如果结果类型为 void，那么 `$r` 不转换类型; 它什么也不做。 但是，如果操作数是对 void 方法的调用，则 `$r` 将导致 null。 例如，如果结果类型是 void，而 `foo()` 是一个 void 方法，那么 `$_ = ($r)foo();` 也是一个正确的表达式。

cast 运算符 `$r` 在 return 语句中也很有用。 即使结果类型是 void，下面的 return 语句也是有效的：

```
return ($r)result;
```

这里，result是局部变量。 因为指定了 ($r)，所以结果值被丢弃。此返回语句被等价于:`return;`


#### `$w`

`$w` 表示包装类型。它用在 cast 表达式中作 cast 转换类型。`$w` 把基本类型转换为包装类型。 以下代码是一个示例：

```
Integer i = ($w)5;
```

包装后的类型取决于 `$w` 后面表达式的类型。如果表达式的类型为 double，则包装器类型为 java.lang.Double。如果表达式 `($w)value` 中 value 的类型不是原始类型，那么 `($w)`  什么也不做。

#### `$_`

CtMethod 中的 `insertAfter()` 和 CtConstructor 在方法的末尾插入编译的代码。传递给`insertAfter()` 的语句中，不但可以使用特殊符号如 `$0，$1`。也可以使用 `$_` 来表示方法的结果值。该变量的类型是方法的结果类型（返回类型）。如果结果类型为 void，那么 `$_` 的类型为Object，`$_` 的值为 null。

虽然由 `insertAfter()` 插入的编译代码通常在方法返回之前执行，但是当方法抛出异常时，它也可以执行。要在抛出异常时执行它，insertAfter() 的第二个参数 asFinally 必须为true。如果抛出异常，由 `insertAfter()` 插入的编译代码将作为 finally 子句执行。此时`$_` 的值是 `0` 或 `null`。在编译代码的执行终止后，最初抛出的异常将被重新抛出给调用者。注意，`$_` 的值不会被抛给调用者，它将被丢弃。

#### `$sig`

`$sig` 的值是一个 java.lang.Class 对象的数组，表示声明的形式参数类型。


#### `$type`

`$type` 的值是一个 java.lang.Class 对象，表示结果值的类型。 如果这是一个构造函数，此变量返回 Void.class。

#### `$class`

`$class` 的值是一个 java.lang.Class 对象，表示编辑的方法所在的类。 即表示 `$0` 的类型。

#### `addCatch()`

`addCatch()` 插入方法体抛出异常时执行的代码，控制权会返回给调用者。 在插入的源代码中，异常用 `$e` 表示。

```
CtMethod m = ...; CtClass etype = ClassPool.getDefault().get("java.io.IOException"); m.addCatch("{ System.out.println($e); throw $e; }", etype);
```
转换成对应的 java 代码如下：

```
try {
    // 原来的方法体
} catch (java.io.IOException e) {
    System.out.println(e);
    throw e;
}
```

请注意，**插入的代码段必须以 throw 或 return 语句结束**。

### 改变一个方法体

CtMethod 和 CtConstructor 提供 `setBody()` 来替换整个方法体。它将新的源代码编译成 Java 字节码，并用它替换原方法体。 如果给定的源文本为 null，则替换后的方法体仅包含返回语句，返回零或空值，除非结果类型为 void。

在传递给 `setBody()` 的源代码中，以 `$` 开头的标识符具有特殊含义：

符号 | 含义
--- | ---
`$0, $1, $2, ...` | this and 方法的参数
`$args` | 方法参数数组.它的类型为 Object[]
`$$` | 所有实参。例如, `m($$)` 等价于 `m($1,$2,...)`
`$cflow(...)` | cflow 变量
`$r` | 返回结果的类型，用于强制类型转换
`$w` | 包装器类型，用于强制类型转换
`$sig` | 类型为 java.lang.Class 的参数类型数组
`$type` | 一个 java.lang.Class 对象，表示返回值类型
`$class` | 一个 java.lang.Class 对象，表示当前正在修改的类

注意 `$_` 不可用。

#### 用源代码替换现有的表达式

Javassist 只允许修改方法体中包含的表达式。`javassist.expr.ExprEditor` 是一个用于替换方法体中的表达式的类。用户可以定义 ExprEditor 的子类来指定修改表达式的方式。

要运行 ExprEditor 对象，用户必须在 CtMethod 或 CtClass 中调用 instrument()。例如：

```
CtMethod cm = ... ;
cm.instrument(
    new ExprEditor() {
        public void edit(MethodCall m) throws CannotCompileException {
            if (m.getClassName().equals("Point")
                          && m.getMethodName().equals("move"))
                m.replace("{ $1 = 0; $_ = $proceed($$); }");
        }
    });
```

上面代码，搜索由 cm 表示的方法体，并使用 `{ $1 = 0; $_ = $proceed($$); }` 替换 Point 中的 `move()` 调用，因此 `move()` 的第一个参数总是 0。注意，**替换的代码不是一个表达式**，而是一个语句或块。 它不能是 `try-catch` 语句或包含 `try-catch` 的语句。

>语句使用关键字来组成命令，类似告诉解释器一个命令。而表达式没有关键字。他们可以是使用数字操作符构成的算数表达式，也可以是使用括号调用的函数。它们可以接受用户输入，也可以不接受用户输入。有些会有输出，有些则没有。

方法 `instrument()` 搜索方法体。 如果它找到一个表达式，如方法调用、字段访问和对象创建，那么它调用给定的 ExprEditor 对象上的 `edit()` 方法。 `edit()` 的参数表示找到的表达式。 `edit()` 可以检查和替换该表达式。

调用`edit()` 参数的 `replace()` 方法可以将表达式替换为给定的语句。如果给定的语句是空块，即执行 `replace("{}")`，则将表达式删除。如果要在表达式之前或之后插入语句（或块），则应该将类似以下的代码传递给 `replace()`：

```
{ *before-statements;*
  $_ = $proceed($$);
  *after-statements;* }
```

无论表达式是方法调用，字段访问，对象创建还是其他。第二个陈述可能是：

```
$_ = $proceed();
```

如果表达式是读访问，或者：

```
$proceed($$);
```

如果表达式是写入访问。

如果 `instrument()` 所搜索的方法是使用 `-g` 选项（类文件包含局部变量属性）编译的，则在传递给 `replace()` 的源文本中也可以使用目标表达式中可用的局部变量。

#### javassist.expr.MethodCall

MethodCall 表示方法调用。MethodCall 的 `replace()` 方法用于替换方法调用，它接收表示替换语句或块的源代码。和 `insertBefore()` 方法一样，传递给 replace 的源代码中，以 `$` 开头的标识符具有特殊的含义。

符号 | 含义
--- | ---
`$0` | 方法调用的目标对象。它不等于 this，它代表了调用者。 如果方法是静态的，则 `$0` 为 null
`$1, $2 ..` | 方法的参数
`$_` | 方法调用的结果
`$r` | 返回结果的类型，用于强制类型转换
`$class` | 一个 java.lang.Class 对象，表示当前正在修改的类
`$sig` | 类型为 java.lang.Class 的参数类型数组
`$type` | 一个 java.lang.Class 对象，表示返回值类型
`$class` | 一个 java.lang.Class 对象，表示当前正在修改的类
`$proceed` | 表达式中原方法的名称。

这里的方法调用意味着由 MethodCall 对象表示的方法。其他标识符如 `$w`，`$args` 和 `$$` 也可用。除非方法调用的返回类型为 void，否则返回值必须在源代码中赋给 `$_`，`$_` 的类型是表达式的结果类型。如果结果类型为 void，那么 `$_` 的类型为 Object，并且分配给 `$_` 的值将被忽略。**`$proceed` 不是字符串值，而是特殊的语法。 它后面必须跟一个由括号括起来的参数列表。**

#### javassist.expr.ConstructorCall

ConstructorCall 表示构造函数调用，例如包含在构造函数中的 `this()` 和 `super()`。ConstructorCall 中的方法 `replace()` 可以使用语句或代码块来代替构造函数。它接收表示替换语句或块的源代码。和 `insertBefore()` 方法一样，传递给 replace 的源代码中，以 `$` 开头的标识符具有特殊的含义。

符号 | 含义
--- | ---
`$0` | 构造调用的目标对象。它等于 this
`$1, $2, ...` | 构造函数的参数
`$class` | 一个 java.lang.Class 对象，表示当前正在修改的类
`$sig` | 类型为 java.lang.Class 的参数类型数组
`$proceed` | 表达式中原构造函数的名称。

这里的构造函数调用是由 ConstructorCall 对象表示的。其他标识符如 `$w`，`$args` 和 `$$` 也可用。由于任何构造函数必须调用超类的构造函数或同一类的另一个构造函数，所以替换语句必须包含构造函数调用，通常是对 `$proceed()` 的调用。`$proceed` 不是字符串值，而是特殊的语法。 它后面必须跟一个由括号括起来的参数列表。

#### javassist.expr.FieldAccess

FieldAccess 对象表示字段访问。 如果找到对应的字段访问操作，ExprEditor 中的 `edit()` 方法将接收到一个 FieldAccess 对象。FieldAccess 中的 `replace()` 方法接收替源代码来替换字段访问。

在源代码中，以 `$` 开头的标识符具有特殊含义：

符号 | 含义
--- | ---
`$0` | 表达式访问的字段。它不等于 this。this 表示调用表达式所在方法的对象。如果字段是静态的，则 `$0` 为 null
`$1` | 如果表达式是写操作，则写的值将保存在 `$1` 中。否则 `$1` 不可用
`$_` | 如果表达式是读操作，则结果值保存在 `$1` 中，否则将舍弃存储在 `$_` 中的值
`$r` | 如果表达式是读操作，则 `$r` 读取结果的类型。 否则 `$r` 为 void
`$class` | 一个 java.lang.Class 对象，表示字段所在的类
`$type` | 一个 java.lang.Class 对象，表示字段的类型
`$proceed` | 执行原始字段访问的虚拟方法的名称

其他标识符如 `$w`，`$args` 和 `$$` 也可用。如果表达式是读操作，则必须在源文本中将值分配给 `$`。`$` 的类型是字段的类型。

#### javassist.expr.NewExpr

NewExpr 表示使用 new 运算符（不包括数组创建）创建对象的表达式。 如果发现创建对象的操作，NewEditor 中的 `edit()` 方法将接收到一个 NewExpr 对象。NewExpr 中的 `replace()` 方法接收替源代码来替换字段访问。

在源文本中，以 `$` 开头的标识符具有特殊含义：

符号 | 含义
--- | ---
`$0` | null
`$1` | 构造函数的参数
`$_` | 创建对象的返回值。一个新的对象存储在 `$_` 中
`$r` | 所创建的对象的类型
`$sig` | 类型为 java.lang.Class 的参数类型数组
`$type` | 一个 java.lang.Class 对象，表示创建的对象的类型
`$proceed` | 执行对象创建虚拟方法的名称

其他标识符如 `$w`，`$args` 和 `$$` 也可用。


#### javassist.expr.NewArray

NewArray 表示使用 new 运算符创建数组。如果发现数组创建的操作，ExprEditor 中的 `edit()` 方法一个 NewArray 对象。NewArray 中的 `replace()` 方法可以使用源代码来替换数组创建操作。

在源文本中，以 `$` 开头的标识符具有特殊含义：

符号 | 含义
--- | ---
`$0` | null
`$1, $1` | 每一维的大小
`$_` | 创建数组的返回值。一个新的数组对象存储在 $_ 中
`$r` | 所创建的数组的类型
`$type` | 一个 java.lang.Class 对象，表示创建的数组的类型
`$proceed` | 执行数组创建虚拟方法的名称

其他标识符如 `$w`，`$args` 和 `$$` 也可用。

例如，如果按下面的方式创建数组：

```
String[][] s = new String[3][4];
```

那么 `$1` 和 `$2` 的值分别是 3 和 4。 `$3` 不可用。

例如，如果按下面的方式创建数组：

```
String[][] s = new String[3][];
```

那么 `$1` 的值为 3，但 `$2` 不可用。


#### javassist.expr.Instanceof

一个 InstanceOf 对象表示一个 instanceof 表达式。 如果找到 instanceof 表达式，则ExprEditor 中的 `edit()` 方法接收此对象。Instanceof 中的 `replace()` 方法可以使用源代码来替换 instanceof 表达式。

在源文本中，以 `$` 开头的标识符具有特殊含义：

符号 | 含义
--- | ---
`$0` | null
`$1` | instanceof 运算符左侧的值
`$_` | 表达式的返回值。类型为 boolean
`$r` | instanceof 运算符右侧的值
`$type` | 一个 java.lang.Class 对象，表示 instanceof 运算符右侧的类型
`$proceed` | 执行 instanceof 表达式的虚拟方法的名称。它需要一个参数（类型是 java.lang.Object）。如果参数类型和 instanceof 表达式右侧的类型一致，则返回 true。否则返回 false。


其他标识符如 `$w`，`$args` 和 `$$` 也可用。

#### javassist.expr.Cast

Cast 表示 cast 表达式。如果找到 cast 表达式，ExprEditor 中的 `edit()` 方法会接收到一个 Cast 对象。 Cast 的 `replace()` 方法可以接收源代码来替换替换 cast 表达式。

在源文本中，以 `$` 开头的标识符具有特殊含义：

符号 | 含义
--- | ---
`$0` | null
`$1` | 显示类型转换的目标类型
`$_` | 表达式的结果值。`$_` 的类型和被括号括起来的类型相同
`$r` | 转换之后的类型，即被括号括起来的类型
`$type` | 一个 java.lang.Class 对象，和 `$r` 的类型相同
`$proceed` | 执行类型转换的虚拟方法的名称。它需要一个参数（类型是 java.lang.Object）。并在类型转换完成后返回它

其他标识符如 `$w`，`$args` 和 `$$` 也可用。

#### javassist.expr.Handler

Handler 对象表示 try-catch 语句的 catch 子句。 如果找到 catch，ExprEditor 中的 `edit()` 方法会接收此对象。 Handler 中的 `insertBefore()` 方法会将收到的源代码插入到 catch 子句的开头。

在源文本中，以$开头的标识符具有意义：

符号 | 含义
--- | ---
`$1` | catch 分支获得的异常对象
`$r` | catch 分支获得的异常对象的类型，用于强制类型转换
`$w` | 包装类型，用于强制类型转换
`$type` | 一个 java.lang.Class 对象，表示 catch 捕获的异常的类型

如果一个新的异常分配给 `$1`，它将作为捕获的异常传递给原始的 catch 子句。


### 添加一个新的方法或字段

#### 添加新方法

Javassist 可以创建新的方法和构造函数。CtNewMethod 和 CtNewConstructor 提供了几个工厂方法来创建 CtMethod 或 CtConstructor 对象。`make()` 方法可以通过源代码来创建 CtMethod 或 CtConstructor 对象。

例如，这个程序：

```
CtClass point = ClassPool.getDefault().get("Point");
CtMethod m = CtNewMethod.make(
                 "public int xmove(int dx) { x += dx; }",
                 point);
point.addMethod(m);
```

将一个公共方法 `xmove()`添加到类 Point 中。在这个例子中，x 是类 Point类 中的一个 int 字段。

传递给 `make()` 源代码可以包含以 `$` 开始的标识符，除了 `$_`，如 `setBody()` 中所示。It can also include` $proceed` if the target object and the target method name are also given to make(). For example：

```
CtClass point = ClassPool.getDefault().get("Point");
CtMethod m = CtNewMethod.make(
                 "public int ymove(int dy) { $proceed(0, dy); }",
                 point, "this", "move");
```
这个程序创建一个方法ymove（）定义如下：

```
//请注意，$proceed 已被替换this.move。
public int ymove(int dy) { this.move(0, dy); }
```

Javassist 提供了另一种添加新方法的方法。可以先创建一个抽象方法，然后给它一个方法体：

```
CtClass cc = ... ;
CtMethod m = new CtMethod(CtClass.intType, "move",
                          new CtClass[] { CtClass.intType }, cc);
cc.addMethod(m);
m.setBody("{ x += $1; }");
cc.setModifiers(cc.getModifiers() & ~Modifier.ABSTRACT);
```

由于 Javassist 在向类中添加抽象方法时会抽象出一个类，因此必须在调用 `setBody()`之后将该类显式更改为非抽象类。

#### 相互递归的方法 (Mutual recursive methods)

如果Javassist调用另一个尚未添加到类中的方法，它将无法编译该方法。（Javassist可以编译一个递归调用自己的方法。），要将相互递归方法添加到类中，您需要一个如下所示的技巧。假设你想将方法 `m()`和 `n()`添加到由 cc 表示的类中：

```
CtClass cc = ... ;
CtMethod m = CtNewMethod.make("public abstract int m(int i);", cc);
CtMethod n = CtNewMethod.make("public abstract int n(int i);", cc);
cc.addMethod(m);
cc.addMethod(n);
m.setBody("{ return ($1 <= 0) ? 1 : (n($1 - 1) * $1); }");
n.setBody("{ return m($1); }");
cc.setModifiers(cc.getModifiers() & ~Modifier.ABSTRACT);
```

必须先制作两个抽象方法并将它们添加到类中。然后，即使方法体包含对方的方法调用，也可以将方法体赋予给这些方法。最后必须将该类更改为非抽象类，因为如果添加了抽象方法，`addMethod()` 会自动将类更改为抽象类。

#### Adding a field

Javassist也允许用户创建一个新的字段。

```
CtClass point = ClassPool.getDefault().get("Point");
CtField f = new CtField(CtClass.intType, "z", point);
point.addField(f);
````
如果必须指定添加字段的初始值，则上面显示的程序必须修改为：

```
CtClass point = ClassPool.getDefault().get("Point");
CtField f = new CtField(CtClass.intType, "z", point);
point.addField(f, "0");    // 初始值是 0.
```

`addField()` 方法接收第二个参数，它是表示计算初始值的表达式的源代码。如果表达式的结果类型匹配字段的类型，则此源文本可以是任何 Java 表达式。请注意，表达式不以分号（`;`）结尾。而且，上面的代码可以被重写成下面的简单代码：

```
CtClass point = ClassPool.getDefault().get("Point");
CtField f = CtField.make("public int z = 0;", point);
point.addField(f);
```

#### 删除成员

要删除字段或方法，则在 CtClass 中调用 `removeField()` 或 `removeMethod()`。 CtConstructor 可以通过CtClass 中的 `removeConstructor()` 来移除。

### Annotations

CtClass，CtMethod，CtField 和 CtConstructor 提供了一个方便的方法 `getAnnotations()` 来读取注解。它返回一个注解类型的对象。

比如有下面类型的注解：

```
public @interface Author {
    String name();
    int year();
}
```

这个注解用于下面的类上

```
@Author(name="Chiba", year=2005)
public class Point {
    int x, y;
}
```

然后，注解的值可以通过 `getAnnotations()`来获得。它返回一个包含注释类型对象的数组。

```
CtClass cc = ClassPool.getDefault().get("Point");
Object[] all = cc.getAnnotations();
Author a = (Author)all[0];
String name = a.name();
int year = a.year();
System.out.println("name: " + name + ", year: " + year);
```

输出结果为：`name: Chiba, year: 2005`

要使用 `getAnnotations()`，注解类型（如Author）必须包含在当前类路径中。它们也必须可以从 ClassPool 对象访问。如果找不到注解类型的类文件，Javassist 将无法获取该注释类型成员的默认值。

### Runtime support classes

运行时类的支持参考：`javassist.runtime`

### Import

源代码中的所有类名必须是完全限定的（它们必须包括包名）。但是，`java.lang`
包是一个例外；例如，Javassist 编译器可以解析 Object 时会把它当作 `java.lang.Object`。

要告诉编译器在解析类名时搜索其他包，则在 ClassPool 中调用 `importPackage()`。例如：

```
pool.importPackage("java.awt");
CtClass cc = pool.makeClass("Test");
CtField f = CtField.make("public Point p;", cc);
cc.addField(f);
```
第二行指示编译器导入 `java.awt` 包。因此，第三行不会抛出异常。编译器可以将 Point 识别为 `java.awt.Point`。注意 `importPackage()` 不会影响 ClassPool 中的 `get()` 方法。只有编译器才考虑导入包。 `get()` 的参数必须是完整类名。

### Limitations(限制)

在目前实现中，Javassist 中包含的 Java 编译器有一些限制：

- J2SE 5.0 引入的新语法（包括枚举和泛型）不受支持。注解由 Javassist 的低级 API 支持。 参见 javassist.bytecode.annotation 包（以及 CtClass 和 CtBehavior 中的 `getAnnotations()`）。对泛型只提供部分支持。
- 初始化数组时，只有一维数组可以用大括号加逗号分隔元素的形式初始化，多维数组还不支持。
- 编译器不能编译包含内部类和匿名类的源代码。 但是，Javassist 可以读取和修改内部/匿名类的类文件。
- 不支持带标记的 continue 和 break 语句。
- 编译器没有正确实现 Java 方法调度算法。编译器可能会混淆在类中定义的重载方法（方法名称相同，查参数列表不同）。例如：
```
//如果编译的表达式是 x.foo(new C())，其中 x 是 X 的实例，编译器将产生对 foo(A) 的调用，尽管编译器可以正确地编译 foo((B) new C()) 。
class A {}
class B extends A {}
class C extends B {}
class X {
    void foo(A a) { .. }
    void foo(B b) { .. }
}
```
- 建议使用 `#` 作为类名和静态方法或字段名之间的分隔符。 例如，在常规 Java 中，`javassist.CtClass.intType.getName()`，在 javassist.CtClass 中的静态字段 intType 指示的对象上调用一个方法 `getName()`。 在Javassist 中，用户也可以写上面的表达式，但是建议写成这样：`javassist.CtClass#intType.getName()`，这样可以使编译器可以快速解析表达式。




---
## 5 Bytecode level API

Javassist 还提供了低级的 API 用于直接编辑类文件，为了使用这些 API，你需要详细了解Java字节码和类文件格式，而此级别的 API 允许你对类文件进行任何类型的修改。如果你想产生一个简单的类文件，`javassist.bytecode.ClassFileWriter` 可能为你提供了最好的API。它比`javassist.bytecode.ClassFile`快得多。

### 获取一个ClassFile对象

`javassist.bytecode.ClassFile` 对象表示一个类文件。为了获取这个对象，应该调用 CtClass 中的`getClassFile()`方法。另外，还可以直接从类文件构造 `javassist.bytecode.ClassFile`实例。例如：

```
BufferedInputStream fin
    = new BufferedInputStream(new FileInputStream("Point.class"));
ClassFile cf = new ClassFile(new DataInputStream(fin));
```

一个 ClassFile 对象可以写回到一个类文件。 ClassFile 中的 `write()`方法将类文件的内容写入给定的 DataOutputStream。

你也可以从头开始创建一个新的类文件。例如：

```java
ClassFile cf = new ClassFile(false, "test.Foo", null);
cf.setInterfaces(new String[] { "java.lang.Cloneable" });
 
FieldInfo f = new FieldInfo(cf.getConstPool(), "width", "I");
f.setAccessFlags(AccessFlag.PUBLIC);
cf.addField(f);

cf.write(new DataOutputStream(new FileOutputStream("Foo.class")));
```

上面代码生成一个包含以下类的实现的类文件 Foo.class：

```
package test;
class Foo implements Cloneable {
    public int width;
}
```

###  添加和删除除成员

ClassFile 提供 `addField()` 和 `addMethod()` 用于添加字段或方法（注意构造函数被认为是字节码级别的一种方法），它还提供了用于向类文件添加属性的`addAttribute()`。注意，FieldInfo，MethodInfo 和 AttributeInfo 对象包含指向 ConstPool（常量池表）对象的链接。ConstPool 对象必须与 ClassFile 对象以及添加到该 ClassFile 对象的 FieldInfo（或MethodInfo等）对象通用，换句话说，FieldInfo（或MethodInfo等）对象不能在不同的ClassFile 对象之间共享。

要从 ClassFile 对象中删除字段或方法，必须首先获取包含该类的所有字段的 `java.util.List` 对象。 `getFields()` 和 `getMethods()` 返回列表。通过在 List 对象上调用 `remove()` 可以删除一个字段或方法。一个属性可以用类似的方式删除。在 FieldInfo 或 MethodInfo 中调用 `getAttributes()` 以获取属性列表，并从列表中删除一个。


### 遍历方法体

为了检查方法体中的每个字节码指令，CodeIterator 非常有用。为了避免这个问题，请按如下操作：

```java
ClassFile cf = ... ;
MethodInfo minfo = cf.getMethod("move");    // we assume move is not overloaded.
CodeAttribute ca = minfo.getCodeAttribute();
CodeIterator i = ca.iterator();
```

CodeIterator 对象允许你从开始到结束逐一访问每个字节码指令。以下方法是 CodeIterator 中声明的方法的一部分：

- `void begin()`：移至第一条指令
- `void move(int index)`：移动到给定索引指定的指令。
- `boolean hasNext()`：如果有更多指令，则返回true。
- `int next()`：返回下一条指令的索引。 请注意，它不会返回下一条指令的操作码。
- `int byteAt(int index)`：返回索引处的无符号8位值。
- `int u16bitAt(int index)`：返回索引处的无符号18位值。
- `int write(byte[] code, int index)`：在索引处写入一个字节数组。
- `void insert(int index, byte[] code)`：在索引处插入一个字节数组。分支偏移等会自动调整。

以下代码片段显示了方法体中包含的所有指令：

```java
CodeIterator ci = ... ;
while (ci.hasNext()) {
    int index = ci.next();
    int op = ci.byteAt(index);
    System.out.println(Mnemonic.OPCODE[op]);
}
```

### 生成一个字节码序列

一个 Bytecode 对象表示一系列的字节码指令。它是一个可增长的字节码数组。这是一个示例代码片段：

```
ConstPool cp = ...;    // constant pool table
Bytecode b = new Bytecode(cp, 1, 0);
b.addIconst(3);
b.addReturn(CtClass.intType);
CodeAttribute ca = b.toCodeAttribute();
```

这会产生代表以下序列的代码属性：

```
iconst_3
ireturn
```

您还可以通过调用 Bytecode 中的 `get()` 来获取包含此序列的字节数组。获取的数组可以插入到另一个代码属性中。

Bytecode 提供了许多方法来添加特定的指令，例如使用 addOpcode() 添加一个 8 位操作码，使用 `addIndex()` 用于添加一个索引。每个操作码的值定义在 Opcode 接口中。
`addOpcode()` 和添加特定指令的方法，将自动维持最大堆栈深度，除非控制流没有分支。可以通过调用 Bytecode 的 `getMaxStack()` 方法来获得这个深度。它也反映在从 Bytecode对象构造的 CodeAttribute 对象上。要重新计算方法体的最大堆栈深度，可以调用 CodeAttribute 的 `computeMaxStack()` 方法。

字节码可以用来构造一个方法。例如：

```
ClassFile cf = ...
Bytecode code = new Bytecode(cf.getConstPool());
code.addAload(0);
code.addInvokespecial("java/lang/Object", MethodInfo.nameInit, "()V");
code.addReturn(null);
code.setMaxLocals(1);

MethodInfo minfo = new MethodInfo(cf.getConstPool(), MethodInfo.nameInit, "()V");
minfo.setCodeAttribute(code.toCodeAttribute());
cf.addMethod(minfo);
```

### Annotations 注解

Annotation 作为运行时不可见（或可见）的注记属性，存储在类文件中。调用  `getAttribute(AnnotationsAttribute.invisibleTag)`方法，可以从 ClassFile，MethodInfo 或 FieldInfo 中获取注记属性。更多信息参阅  `javassist.bytecode.AnnotationsAttribute` 和 `javassist.bytecode.annotation` 包的 javadoc 手册。

Javassist 还允许你通过更高级别的 API 访问 Annotation。 如果要通过 CtClass 访问注释，则在 CtClass 或 CtBehavior 中调用 `getAnnotations()`。

---
## 6 泛型

Javassist 的低级 API 完全支持 Java 5 引入的泛型。另一方面，CtClass 等高级 API 不直接支持泛型。但是，这不是字节码转换的严重问题。Java 的泛型是通过擦除技术实现的。编译完成后，所有类型参数都将被删除。例如，假设你的源代码声明了一个参数化类型 `Vector <String>`：

```
Vector<String> v = new Vector<String>();
  :
String s = v.get(0);
```

编译的字节码等同于以下代码：

```
Vector v = new Vector();
  :
String s = (String)v.get(0);
```

**所以当编写一个字节码转换器时，可以放弃所有的类型参数**。由于 Javassist 中嵌入的编译器不支持泛型，如果源代码由 Javassist 编译，则必须在调用者处插入明确的类型转换，例如，通过 `CtMethod.make()`。如果源代码是由普通Java编译器（如javac）编译的，则不需要进行类型转换。

例如，如果你有一个类：

```
public class Wrapper<T> {
  T value;
  public Wrapper(T t) { value = t; }
}
```

并想将接口 `Getter<T>` 添加到类  `Wrapper<T>` 中：

```
public interface Getter<T> {
  T get();
}
```

那么你添加的接口是Getter（类型参数`<T>`需要移除），而且 Wrapper 类的方法应该是下面这种形式：

```
public Object get() { return value; }
```

不需要类型参数。由于 get 返回 Object，因此如果源代码由 Javassist 编译，则需要在调用出添加显式类型转换。比如：

```
Wrapper w = ...
String s = (String)w.get();
```

如果源代码由普通 Java 编译器编译，则不需要类型转换，因为它会自动插入类型转换。如果需要在运行时通过反射访问类型参数，则必须将泛型签名添加到类文件，有关更多详细信息，参考 CtClass 的 `setGenericSignature` 方法的API。



---
## 7 可变参数

Javassist不直接支持可变参数。所以要用可变参数创建一个方法，你必须明确地设置一个方法修饰符。这很容易。假设你现在想要制作以下方法：

```
public int length(int... args) { return args.length; }
```

下面的代码使用 Javassist 将使上面显示的方法：

```
CtClass cc = /* target class */;
CtMethod m = CtMethod.make("public int length(int[] args) { return args.length; }", cc);
m.setModifiers(m.getModifiers() | Modifier.VARARGS);
cc.addMethod(m);
```
参数类型`int ...`更改为`int []`，并将`Modifier.VARARGS`添加到方法修饰符中。要在由嵌入在 Javassist 中的编译器编译的源代码中调用此方法，需要按照下面方式：

```
length(new int[] { 1, 2, 3 });
```

而不是使用可变参数机制调用此方法：

```
length(1, 2, 3);
```

---
## 8 J2ME

如果修改 J2ME 执行环境的类文件，则必须执行预验证。预先验证基本上是生成堆栈图，这与在 JDK 1.6 中引入 J2SE 的堆栈映射表类似。仅当 `javassist.bytecode.MethodInfo.doPreverify` 为 true 时，Javassist 才会维护 J2ME 的堆栈映射。你也可以手动为修改的方法生成堆栈映射。对于由 CtMethod 对象 m 表示的给定方法，可以通过调用以下方法来生成堆栈映射：

```java
m.getMethodInfo().rebuildStackMapForME(cpool);
```

在这里，cpool 是一个 ClassPool 对象，可以通过调用 CtClass 对象上的 `getClassPool()` 来获得。ClassPool 对象负责从给定的类路径中查找类文件。要获取所有 CtMethod 对象，请调用 CtClass 对象上的 `getDeclaredMethods()` 方法。

---
## 9 Boxing/Unboxing

Boxing/Unboxing 是 Java 提供的语法糖，在编译器就会被替换为具体的方法调用，所有 Javassist 编译器 不支持 `Boxing/Unboxing`。


---
## 10 Debug


将 `CtClass.debugDump` 设置为目录名称。然后，所有由Javassist修改和生成的类文件都保存在该目录中。例如下面地代码：

```
CtClass.debugDump = "./dump";
```

所有修改的类将保存到 `./dump`中。

--
## 11 总结

Javassist的使用方式：

- 在编译器使用，可以插入代码或者修改现有的代码，比如在 Android 开发中，利用 gradle 构建的便利性，在编译器插入一些模块代码。
- 在运行时创建新的对象或修改现有对象，比如在 JavaEE 中，Spring 容器会动态的很多代理对象
- 处理好 ClassLoader 很关键
- 调用 `CtClass.toClass()` 时，保证不会导致重复的类声明

---
## 引用

- [tutorial](http://jboss-javassist.github.io/javassist/tutorial/tutorial.html)
- [javassist-gradle-plugin](https://github.com/darylteo/javassist-gradle-plugin)
- [Javassist 使用指南 中文](https://www.jianshu.com/p/43424242846b)

使用官方 Sample：

```
1 javac -classpath C:\Users\Administrator\Desktop\javassist\javassist.jar  sample/evolve/*.java
2 javac -classpath C:\Users\Administrator\Desktop\javassist\javassist.jar  sample/evolve/sample/evolve/WebPage.java
3 java -classpath C:\Users\Administrator\Desktop\javassist\javassist.jar;. sample.evolve.DemoLoader 5003
```