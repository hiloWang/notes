# Jar

## 1 Jar文件

Jar是java的归档文件，一个jar即可包含类文件,**也可以包含图形和声音这些其他类型的文件**，此外jar文件时压缩的，使用的是zip压缩格式。一个可运行的jar需要在jar的清单文件中包含主类路径,如：`Main-Class:com.ztiany.Main`,清单文件中的最后一行必须以换行符结束。使用第三方打包器可以将jar文件转换为Windows的可执行文件，比如JSmooth和、Launch4J、LzPack等工具。


### 打 jar 包

工具的帮助稳定如下：

```
用法: jar {ctxui}[vfmn0PMe] [jar-file] [manifest-file] [entry-point] [-C dir] files ...
选项:
    -c  创建新档案
    -t  列出档案目录
    -x  从档案中提取指定的 (或所有) 文件
    -u  更新现有档案
    -v  在标准输出中生成详细输出
    -f  指定档案文件名
    -m  包含指定清单文件中的清单信息
    -n  创建新档案后执行 Pack200 规范化
    -e  为捆绑到可执行 jar 文件的独立应用程序
        指定应用程序入口点
    -0  仅存储; 不使用任何 ZIP 压缩
    -P  保留文件名中的前导 '/' (绝对路径) 和 ".." (父目录) 组件
    -M  不创建条目的清单文件
    -i  为指定的 jar 文件生成索引信息
    -C  更改为指定的目录并包含以下文件

如果任何文件为目录, 则对其进行递归处理。
清单文件名, 档案文件名和入口点名称的指定顺序
与 'm', 'f' 和 'e' 标记的指定顺序相同。

示例 1: 将两个类文件归档到一个名为 classes.jar 的档案中:
       jar cvf classes.jar Foo.class Bar.class
示例 2: 使用现有的清单文件 'mymanifest' 并
           将 foo/ 目录中的所有文件归档到 'classes.jar' 中:
       jar cvfm classes.jar mymanifest -C foo/ .
```

### 示例讲解 1

` jar cvf classes.jar Foo.class Bar.class`

- c表示创建新档案,不需要参数
- v表示在标准输出中生成详细输出，不需要参数
- f表示指定档案文件名，这里的参数是`classes.jar`

最后的参数`Foo.class Bar.class`表示需要被加入jar包的文件。**不需要命令选项指定，一律在最后进行输入**

### 示例讲解 2

`jar cvf classes.jar com`

此时com是文件夹，适用于有包名的class，比如`A.class`的所在包是`com.ztiany`,则打包时应该指定文件夹com，而`A.class`放在`com/ztiany`目录下

### 其他命令

```
jar -tf HelloWorld.jar   //查看归档文件的内容
jar -cvfe HelloWorld.jar HelloWorld HelloWorld.class   //创建可以运行的jar包，其中HelloWorld为类的全限定路径
jar cfm jar-file manifest-addition input-file(s)     //修改清单文件的内容
```

---
## 2 jar的遍历

`java.util.jar.JarFile` 提供了遍历 jar 的方法：

```
                    JarFile jarFile = new JarFile(jarInput.file)
                    Enumeration enumeration = jarFile.entries()
                    while (enumeration.hasMoreElements()) {
                        JarEntry jarEntry = (JarEntry) enumeration.nextElement()
                        String entryName = jarEntry.getName()
                        println "==== jarInput class entryName :" + entryName
                        if (entryName.endsWith(".class")) {
                             //...
                        }
                    }
```

## 参考

[Java之jar打包](http://www.jianshu.com/p/61cfa1347894)

