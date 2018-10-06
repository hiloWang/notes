# Java File

---
## 1 File类

在java中使用`File`表示**文件和目录路径名的抽象表示形式**。`File` 类的实例是不可变的；也就是说，一旦创建，`File` 对象表示的抽象路径名将永不改变。

Java编程思想中说到：File这个名字有一定误导性，我们可能认为它指代文件，实际上它既能表示一个特定的文件名称，又能表示一个目录下的一组文件的名称。

用户界面和操作系统使用**与系统相关的路径名字符串**来命名文件和目录，File呈现分层路径名的一个抽象的、与系统无关的视图。

**抽象路径名**有两个组件：

1.  一个可选的与系统有关的**前缀**字符串，比如盘符，`"/"` 表示 UNIX 中的根目录，`"\\\\"` 表示 Microsoft Windows UNC 路径名。
2.  零个或更多字符串_名称_ 的序列。

抽象路径名中的第一个名称是目录名，第一个名称之后的每个名称表示一个目录；最后一个名称既可以表示目录，也可以表示文件。_空_ 抽象路径名没有前缀和名称序列。

- 路径名字符串与抽象路径名之间的转换与系统有关。将抽象路径名转换为路径名字符串时，**每个名称与下一个名称之间用一个默认_分隔符_ 隔开**。默认名称分隔符由系统属性 `file.separator` 定义

无论是抽象路径名还是路径名字符串，都可以是_绝对_ 路径名或_相对_ 路径名。

 - 绝对路径名是完整的路径名，不需要任何其他信息就可以定位它所表示的文件。
 - 相对路径名必须使用取自其他路径名的信息进行解释。

>默认情况下，`java.io` 包中的类总是**根据当前用户目录来解析相对路径名**。此目录由系统属性 **`user.dir`** 指定，通常是 Java 虚拟机的调用目录。而当使用IDE时这个可能会被IDE修改，一般会被修改为项目的根目录

### 处理盘符

在处理 UNIX 平台的根目录，以及 Microsoft Windows 平台的盘符、根目录和 UNC 路径名时，将用到前缀这一概念。如下所示：

*   对于 UNIX 平台，绝对路径名的前缀始终是 `"/"`。相对路径名没有前缀。表示根目录的绝对路径名的前缀为 `"/"` 且名称序列为空。
![](index_files/b27a60c6-e4fa-4650-bc09-e81fa8ae480c.png)
*   对于 Microsoft Windows 平台，包含盘符的路径名前缀由驱动器号和一个 `":"` 组成。如果路径名是绝对路径名，还可能后跟 `"\\"`。UNC 路径名的前缀是 `"\\\\"`；主机名和共享名是名称序列中的前两个名称。没有指定驱动器的相对路径名没有前缀。
![](index_files/ef57949c-7e85-4912-81fe-21e4d45427fc.png)


### 权限

文件系统可以实现对实际文件系统对象上的某些操作（比如，读、写、执行）进行限制。这些限制统称为_访问权限_。文件系统可以对一个对象设置多个访问权限。例如，一个设置可能适用于对象的_所有者_，另一个设置则可能适用于所有其他用户。对象上的访问权限可能导致此类的某些方法执行失败。


### File相关方法

```java
    // 静态成员
    public static final String     pathSeparator        // 路径分割符":"
    public static final char     pathSeparatorChar    // 路径分割符':'
    public static final String     separator            // 分隔符"/"
    public static final char     separatorChar        // 分隔符'/'

    // 构造函数
    File(File dir, String name)
    File(String path)
    File(String dirPath, String name)
    File(URI uri)

    File sub2 = new File(dir, "sub2");
    sub2.mkdir();//在使用此种方式创建文件时确保父级目录已经存在
    File sub2 = new File("dir/sub2");则不需要
```

## 2 正斜杠 `/` 与反斜杠 `\`


Windows:

```java
             File file  = new File("\\");//当前存储分区的根目录
             File file2 = new File("");//不能代表任何目录或者文件，也不能做任何操作
             File file1 = new File("/a");//根目录上的a文件，已经脱离了IDE环境
             File file3 = new File("/");//当前存储分区的根目录
             File file4 = new File("a");//如果java运行在ide中，则一般的ide设置的默认的相对路径是项目的根目录，如果是单独的执行java文件，则在java文件所在的目录，也就是说相对路径不可靠，开发中不应该适用相对路径

             File file5 = new File("\\a");//根目录上的a文件
             File file6 = new File("a\\4.txt");//同file4
             System.out.println("flile  =  "+file.getAbsolutePath());
             System.out.println("flile1 =  "+file1.getAbsolutePath());
             System.out.println("flile2 =  "+file2.getAbsolutePath());
             System.out.println("flile3 =  "+file3.getAbsolutePath());
             System.out.println("flile4 =  "+file4.getAbsolutePath());
             System.out.println("flile5 =  "+file5.getAbsolutePath());
             System.out.println("flil6 =  "+file6.getAbsolutePath());
    
             System.out.println("File.pathSeparatorChar =  "+File.pathSeparatorChar);
             System.out.println("File.separator =  "+File.separator);

    打印结果分别为：
    
             flile1 =  G:\a
             flile2 =  G:\MyWrokSpace\IDEA\Java\rx
             flile3 =  G:\
             flile4 =  G:\MyWrokSpace\IDEA\Java\rx\a
             flile5 =  G:\a
             flil6 =  G:\MyWrokSpace\IDEA\Java\rx\a\4.txt
    
             File.pathSeparatorChar =  :
             File.separator =  /
```

Android(Linux):

```java
             File file2 = new File("/init.rc");//根目录的init.rc
             File file3 = new File("/");//当前设备根目录，在Android中，不能写文件
             File file7 = new File("/sdcard/123.txt");//Android 的sd存储卡，可以写文件
             System.out.println("flile2 exists =  " + file2.exists());//
             System.out.println("flile3 =  " + file3.getAbsolutePath());
             System.out.println("flile3 list=  " + Arrays.toString(file3.list()));
             System.out.println("flie7 =  " + file7.getAbsolutePath());
    
            System.out.println("File.pathSeparatorChar =  " + File.pathSeparatorChar);
             System.out.println("File.separator =  " + File.separator);

    打印结果：

             flile2 exists =  true
             flile3 =  /
             flile3 list=  [sdcard, custom, protect_s, protect_f, storage, config, cache, acct, vendor, d, etc, mnt, ueventd.rc, system, sys, service_contexts, sepolicy, selinux_version, seapp_contexts, sbin, property_contexts, proc, meta_init.rc, meta_init.project.rc, meta_init.modem.rc, init.zygote64_32.rc, init.zygote32.rc, init.xlog.rc, init.usb.rc, init.trace.rc, init.ssd.rc, init.rc, init.project.rc, init.mt6795.usb.rc, init.mt6795.rc, init.mt6595.rc, init.modem.rc, init.environ.rc, init.aee.rc, init, fstab.mt6795, file_contexts, factory_init.rc, factory_init.project.rc, enableswap.sh, default.prop, data, charger, root, dev]
             flie7 =  /sdcard/123.txt
             File.pathSeparatorChar =  :
             File.separator =  /
```

### 不要使用相对路径

关于安全的使用路径可以参考 `Class.getResource()` 相关方法。

---
## 3 文件描述符(FileDescriptor)

什么是文件描述符：

>文件描述符（File descriptor）是计算机科学中的一个术语，是一个用于表述指向文件的引用的抽象化概念，文件描述符在形式上是一个非负整数。实际上，它是一个索引值，指向内核为每一个进程所维护的该进程打开文件的记录表。当程序打开一个现有文件或者创建一个新文件时，内核向进程返回一个文件描述符。在程序设计中，一些涉及底层的程序编写往往会围绕着文件描述符展开。但是文件描述符这一概念往往只适用于UNIX、Linux这样的操作系统。每个Unix进程（除了可能的守护进程）应均有三个标准的POSIX文件描述符，对应于三个标准流：stdin、stdout、stderr。——维基百科

文件描述符类的实例用作**与基础机器有关的某种结构的不透明句柄**，该结构表示:

- 开放文件
- 开放套接字
- 字节的另一个源或接收者。

文件描述符的主要实际用途是创建一个包含该结构的 `FileInputStream` 或`FileOutputStream`。

**应用程序不应创建自己的文件描述符。**

以FileDescriptor表示文件来说：当FileDescriptor表示某文件时，我们可以通俗的将FileDescriptor看成是该文件。但是，我们不能直接通过FileDescriptor对该文件进行操作；若需要通过FileDescriptor对该文件进行操作，则需要新创建FileDescriptor对应的FileOutputStream，再对文件进行操作

---
## 4  标准IO：err/in/err

    static FileDescriptor err  标准错误流的句柄。
    static FileDescriptor in  标准输入流的句柄。
    static FileDescriptor out  标准输出流的句柄。

它们3个的原理和用法都类似，下面通过out来进行深入研究。

out是标准输出(屏幕)的描述符。但是它有什么作用呢？

我们可以通俗理解，out就代表了标准输出(屏幕)。若我们要输出信息到屏幕上，即可通过out来进行操作；但是，out又没有提供输出信息到屏幕的接口(因为out本质是FileDescriptor对象，而FileDescriptor没有输出接口)。怎么办呢？

很简单，我们创建out对应的“输出流对象”，然后通过“输出流”的write()等输出接口就可以将信息输出到屏幕上。如下代码：

```java
           try {
                FileOutputStream out = new FileOutputStream(FileDescriptor.out);
                out.write('A');
                out.close();
            } catch (IOException e) {
            }
            //等同于System.out.print('A');
```

通过文件名创建FileOutputStream"与“通过文件描述符创建FileOutputStream”对象是等效的

```java
         try {
                FileOutputStream out = new FileOutputStream("av.txt");
                FileOutputStream out2 = new FileOutputStream(out.getFD());
                out.write('A');
                out2.write('B');
                out.close();
            } catch (IOException e) {
            }
            //结果实在同一个文件上编辑
```

---
## 相关链接

- [java io系列08之 File总结](http://www.cnblogs.com/skywang12345/p/io_08.html)
- [java io系列09之 FileDescriptor总结](http://www.cnblogs.com/skywang12345/p/io_09.html)
