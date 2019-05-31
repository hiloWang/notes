# Ztiany's programming notes

- 这是笔记，不是博客，很多内容并非原创，仅作为日常学习记录。
- 笔记内容来源于读书、开发实践总结、阅读他人博客、翻译技术文章等。
- 笔记中的引用一般会标明出处并加上链接、如有纰漏，请联系我及时补充。
- 笔记难免存在错误，如有发现，请帮我指出，不甚感激。
- 相关代码可以在 [Programming-Notes-Code](https://github.com/Ztiany/Programming-Notes-Code) 找到。

## 总结

如何解决问题?

- 切忌焦躁，静下心来思考
- 从日志找答案
- Debug
- 从源码分析
- Google
- StackOverflow

如何学习 GitHub 项目?

- 仔细阅读 README
- 查看 Wiki
- 查看 branch
- 使用 Chrome 相关插件，高效使用 Github。

如何学习一门技术：

- 它是什么，自己能简单是叙述出来
- 它解决了什么问题，有什么意义
- 是如何解决问题的，内部如何实现
- 有何缺点，多角度分析
- 提问是有效学习一种方式，在一篇技术笔记后面加上自己的疑问，让学习变得主动

一个复杂的需求从何下手？

1. 不要急于编码、不要急于编码、不要急于编码。
2. 实现一个复杂的需求真正需要的是你确实理解了该需求，具体的业务逻辑。
    - 业务流程，业务有起始点和结束点。业务是如何从起始点逐步进展到结束点的。
    - 业务流程中，数据的状态与范围，是独立的，还是共享的，是主从关系，还是兄弟关系。
3. 真正理清需求之后，可以编写一份简单的设计文档，你的这个模块：
    - 打算采用什么设计方案。
    - 有哪些类，各种实现什么职责。
    - 业务流程如何跳转。
    - 数据如何管理，是否需要抽象数据模型。如何操作业务模型。
4. 根据自己的设计文档，开展编码实现工作。
    - 以数据为驱动，不是以UI为驱动，很多问题都可以采用数据结构来抽象，数据变量，UI自动就会响应。
    - 把数据逻辑抽象为对具体数据结构的操作。

关于英语：

- 英语是学习的一大利器，作为程序员，你真的需要掌握它。

## 内容

基础

- [操作系统](Computer-System-Basic/README.md)
- [网络协议](Network/README.md)
- [数据结构与算法](DataStructure-Algorithms/README.md)

编程语言

- [C-CPP](C-CPP/README.md)
- [Java](Java/README.md)
- [RxJava](RxJava/README.md)
- [Groovy](Groovy/README.md)
- [Kotlin](Kotlin/README.md)
- [Python](Python/README.md)

平台

- [Android](Android/README.md)
- [Linux](Linux/README.md)

工程构建

- [Gradle](Build-System/Gradle/README.md)

后端

- [JavaEE](JavaEE/README.md)

前端

- [前端](Front-end/README.md)