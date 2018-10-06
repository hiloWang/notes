#  操作文件

JDK1.7引入了两个强大的类：**Path和Files**

---
## 1 Path

Path表示一个目录名序列，其后还可以跟着一个文件名，其相关用法如下：

```java
            String property = System.getProperty("user.dir");
            Path userDir = Paths.get(property);//获取一个Path，G:\MyWrokSpace\IDEA\Java\JavaAPI这里是绝对路径
            Path work = Paths.get("work");//获取一个Path，这里是相对路径
    
            System.out.println(userDir.resolve(work));//解析，如果是绝对路径返回本身，否则work加到userDir后面
            System.out.println(userDir.resolve("work"));//等效于上面方法
            System.out.println(userDir.resolveSibling("work"));//产生兄弟路径
    
            Path home1 = Paths.get("home", "cay");
            Path home2 = Paths.get("home", "fred", "pro");
            System.out.println(home1.relativize(home2));//相对化, 结果为： ..\fred\pro
            System.out.println(Paths.get("a\\b\\..\\fred\\pro").normalize());//使正常化；使规格化，使标准化-->a\fred\pro
            System.out.println(home1.toAbsolutePath());
    
            System.out.println(userDir.getFileName());
            System.out.println(userDir.getRoot());
            System.out.println(userDir.getParent());
```

---
## 2 Files

Files使得普通文件的读写操作变得简洁。

### 读写文件

```java
            //读取文件所有内容
            byte[] bytes = Files.readAllBytes(Paths.get("TestFiles/a.txt"));
            System.out.println(bytes.length);
            //当作字符串读入
            System.out.println(new String(bytes, Charset.forName("utf8")));
            //将文件读入行序列
            List<String> list = Files.readAllLines(Paths.get("TestFiles/a.txt"));
            System.out.println(list);
            //写入一个文件
            Files.write(Paths.get("TestFiles/b.txt"), "abc".getBytes());
            //向指定的文件追加内容
            Files.write(Paths.get("TestFiles/b.txt"), "ddd".getBytes(), StandardOpenOption.APPEND);
            //写入行序列
            Files.write(Paths.get("TestFiles/b.txt"), list, StandardOpenOption.APPEND);
    
            //上面方法适合于中等长度的文本文件，如果是文件较大，或是二进制文件，则还是应该使用传统的IO或者读入器/写出器：
            InputStream inputStream = Files.newInputStream(Paths.get("TestFiles/b.txt")); //ect
```

### 移动删除

```java
            Files.copy(Paths.get("TestFiles/a.txt")/*from*/, Paths.get("a.txt")/*to*/);//copy文件
            Files.move(Paths.get("TestFiles/a.txt")/*from*/, Paths.get("a.txt")/*to*/);//移动文件
            Files.move(Paths.get("TestFiles/a.txt")/*from*/, Paths.get("a.txt")/*to*/, StandardCopyOption.REPLACE_EXISTING);
            //如果目标路径已经存在，那么复制和移动将会失败，使用REPLACE_EXISTING表示来覆盖文件 ,ATOMIC_MOVE表示原子移动 etc...
    
            Files.delete(Paths.get("a.txt"));//删除文件，不存在则会抛出异常
            Files.deleteIfExists(Paths.get("a.txt"));//删除文件

### 创建文件

            Files.createDirectories(Paths.get("TestFiles1"));//创建目录
            Files.createFile(Paths.get("TestFiles1/a.txt"));//创建文件，如果文件已经存在，则会抛出异常
    //        Files.createTempFile()
```

### 获取文件信息

```java
     long size = Files.size(Paths.get("TestFiles1/a.txt"));
            System.out.println(size);
            Files.isHidden(Paths.get("TestFiles1/a.txt"));
            BasicFileAttributes basicFileAttributes = Files.readAttributes(Paths.get("TestFiles1/a.txt"), BasicFileAttributes.class);
            System.out.println(basicFileAttributes.creationTime());
            System.out.println(basicFileAttributes.isDirectory());
            System.out.println(basicFileAttributes.isSymbolicLink());
```

### 遍历文件目录

与传统的`listFiles`方法相比，Files提供了一种高效的迭代文件夹的方式。


迭代当前目录，重载的newDirectoryStream(Path dir, String glob)可以根据glob来过来文件

```java
            for (Path path : Files.newDirectoryStream(Paths.get("."))) {
                System.out.println(path);
            }
```

glob模式可以参考相关文档

如果想要访问某个目录的所有子孙文件，可以使用walkFileTree，返回值FileVisitResult用于表示进一步的动作：

- CONTINUE            继续访问
- TERMINATE,          终止访问
- SKIP_SUBTREE,       继续访问，但是不访问这个目录下的任何项了
- SKIP_SIBLINGS;      继续访问，但是不访问这个目录下的同级项了

代码：

```java
            Files.walkFileTree(Paths.get("."), new FileVisitor<Path>() {
                //在一个目录被处理之前
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    return FileVisitResult.CONTINUE;
                }
    
                //访问文件和目录
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    System.out.println(file);
                    return FileVisitResult.CONTINUE;
                }
    
                //访问文件目录发生异常
                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }
    
                //在一个目录被处理之后
                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }
            });
```

### 文件系统

Paths默认从文件系统中查找文件，也即是用户本地磁盘的文件，但是我们可以创建自己的文件系统，比如ZIP文件系统，如果知道zip文件的path，就可以使用下面方式：

```java
            FileSystem fs = FileSystems.newFileSystem(Paths.get("TestFiles/zip_read.zip"), null);
    
            Files.copy(fs.getPath("v2ch01\\findDirectories\\FindDirectories.java"), Paths.get("TestFiles/a.java"));
    
            //列出所有的zip文档文件
            Files.walkFileTree(fs.getPath("/"),new SimpleFileVisitor<Path>(){
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    System.out.println(file);
                    return super.visitFile(file, attrs);
                }
            });
```