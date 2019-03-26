#  编译器 API

随着 `Java 6` 的发布，java编译器已经有了开源的版本了。开源的编译器是 OpenJDK 项目的一部分，可以从Java编译器小组的网站下载 `http://www.openjdk.org/groups/compiler/` 。


Javac 的 API 随着 JDK 的发展有许多的变更，比如：

1. 在没有引入 `JSR-199` 前，只能使用 javac 源码提供的内部 API `sun.tools.jar.Main`
2. JSR-199（Java Compiler API） 引入了 Java 编译器 API，`javax.tools.JavaCompiler` 就是其中之一
3. JSR-269（Pluggable Annotation Processing API）引入了编译器注解处理
4. 另外 `com.sun.tools.javac.*`内部 API 和编译器的实现类库中，提供了编译器操作 AST 的功能，从而可以在源码编译器修改被编译的源码。


具体参考：

- [Java 编译器 javac 笔记](http://nullwy.me/2017/04/javac-api/)
- [The Hacker’s Guide to Javac](chrome-extension://oemmndcbldboiebfnladdacbdfmadadm/http://scg.unibe.ch/archive/projects/Erni08b.pdf)
- [The Hacker’s Guide to Javac 中文翻译](https://my.oschina.net/superpdm/blog/129715)
- [Java编译过程：Compilation Overview](http://openjdk.java.net/groups/compiler/doc/compilation-overview/index.html)
