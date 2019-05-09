## 1 Kotlin 简介

Kotlin主要是由俄罗斯圣彼得堡的JetBrains开发团队所发展出来的编程语言，其名称来自于圣彼得堡附近的科特林岛。Kotlin是一门 **静态的强类型编程语言**，可以运行在JVM上，但不限于JVM。Kotlin与所有基于 Java的框架完全兼容，应用领域有 **服务端开发、Android开发、Kotlin For JavaScript、Kotlin Native**，可以说是全栈语言。

Kotlin 领域：

- Kotlin Script，直接运行.kts文件
- gradle脚本
- web开发
- javaFX
- 前端开发，编译为JavaScript
- native，与c代码交互，不依赖JVM

kotlin 发展简史：

- 2010 立项
- 2011.6 公开
- 2012.2 开源
- 2013.8 支持 AndroidStudio
- 2014.4.6 全新web站点 `kotlinlang.org`
- 2016.2 发布1.0
- 2016.9 支持 apt
- kotlin 生成的字节码默认支持 java6


## 2 Kotlin 学习资料

### 1 文档与资料

- [官网](https://kotlinlang.org/)
- [官方：Kotlin协程](https://github.com/Kotlin/kotlinx.coroutines)
- [官方：kotlin-examples](https://github.com/JetBrains/kotlin-examples)
- [Kotlin 中文站](https://www.kotlincn.net/)
- [Ktor 中文站](https://ktor.kotlincn.net/)
- [kymjs：Kotlin Primer](https://kymjs.com/code/2017/02/03/01/)
- [EasyKotlin](https://github.com/JackChan1999/EasyKotlin)
- [EasyKotlin-All](https://github.com/EasyKotlin)
- [《Kotlin实战》](https://panxl6.gitbooks.io/kotlin-in-action-in-chinese/content/introduction.html)
- [《Atomic Kotlin》]((https://github.com/BruceEckel/AtomicKotlinExamples))

### 2 Android

- [kotlin-for-android-developers](https://wangjiegulu.gitbooks.io/kotlin-for-android-developers-zh/guan_yu_ben_shu.html)
- [Kotlin on Android FAQ](https://developer.android.com/kotlin/faq.html)
- [官方：anko库](https://github.com/Kotlin/anko)
- [kovenant](http://kovenant.komponents.nl/)：Kotlin 的简单异步库
- [Kotlin Android Extensions: Say goodbye to findViewById](https://antonioleiva.com/kotlin-android-extensions/)
- [kotlin in medium](https://medium.com/androiddevelopers/tagged/kotlin)

### 3 项目

- [awesome-kotlin](https://github.com/KotlinBy/awesome-kotlin)
- [awesome-kotlin-cn](https://github.com/kymjs/awesome-kotlin-cn)
- [Kodein](https://github.com/SalomonBrys/Kodein/) Kotlin 依赖注入框架
- [Fuel](https://github.com/kittinunf/Fuel) Kotlin 网络请求库
- [kotlinpoet](https://github.com/square/kotlinpoet) 类似 square 的 javapoet，用于生成 kotlin 代码
- [Apt-Utils](https://github.com/enbandari/Apt-Utils) 与相关教程 [Apt-Tutorials](https://github.com/enbandari/Apt-Tutorials)

### 4 协程

什么是协程：

- [我理解的协程](https://www.zybuluo.com/kuailezhishang/note/128823)
- [协程(廖雪峰)](http://www.liaoxuefeng.com/wiki/001374738125095c955c1e6d8bb493182103fac9270762a000/0013868328689835ecd883d910145dfa8227b539725e5ed000)
- [协程的好处有哪些？](https://www.zhihu.com/question/20511233/answer/24260355)

文档：

- [kotlinx.coroutines guide en](https://github.com/Kotlin/kotlinx.coroutines)
- [kotlinx coroutines guide cn](https://www.kotlincn.net/docs/reference/coroutines.html)
- [kotlinx coroutines api doc](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/)
- [kotlinx.coroutines cn](https://github.com/hltj/kotlinx.coroutines-cn)
- [kotlinx.coroutines cn](https://saplf.gitbooks.io/kotlinx-coroutines/content/)

博客：

- [深入理解 Kotlin Coroutine](https://blog.kotliner.cn/tags/Coroutine/)
- [Kotlin Coroutines(协程)](https://blog.dreamtobe.cn/kotlin-coroutines/)
- [深入浅出 Kotlin 协程](https://cloud.tencent.com/developer/article/1334825)
- [31 天，从浅到深轻松学习 Kotlin](https://mp.weixin.qq.com/s?__biz=MzAwODY4OTk2Mg==&mid=2652046391&idx=1&sn=46efa48076a4533f355af6351b76c012&chksm=808ca472b7fb2d64afc89edf6beba1540e5a6ff49ad6346bd5d72b3957fa5f9323e07b8aab03&mpshare=1&scene=1&srcid=0615eHvcY8XijqYM5CH09baV#rd)
- [Kotlin 实战指南 | 如何在大型应用中添加 Kotlin](https://mp.weixin.qq.com/s?__biz=MzAwODY4OTk2Mg==&mid=2652047413&idx=1&sn=d8b248868406fc641b8a11ccc16807a5&scene=21#wechat_redirect)
- [google-codelabs：kotlin-coroutines](https://codelabs.developers.google.com/codelabs/kotlin-coroutines/#0)
- [RxJava to Kotlin coroutines](https://medium.com/androiddevelopers/rxjava-to-kotlin-coroutines-1204c896a700)
- [Kotlin coroutines vs RxJava: an initial performance test](https://proandroiddev.com/kotlin-coroutines-vs-rxjava-an-initial-performance-test-68160cfc6723)
- [Forget RxJava: Kotlin Coroutines are all you need. Part 1/2](https://proandroiddev.com/forget-rxjava-kotlin-coroutines-are-all-you-need-part-1-2-4f62ecc4f99b)

Sample：

- [coroutine-recipes 协程风格代码](https://github.com/dmytrodanylyk/coroutine-recipes)

教程：

- [Kotlin 系统入门到进阶 视频教程](http://coding.imooc.com/class/108.html)
- [基于 GitHub App 业务深度讲解 Kotlin1.2高级特性与框架设计](https://coding.imooc.com/class/232.html)

破解协程系列：

- [破解 Kotlin 协程(1) - 入门篇](https://mp.weixin.qq.com/s/XAZCzxTDc8XISfWzsjpsng)
- [破解 Kotlin 协程(2) - 协程启动篇](https://mp.weixin.qq.com/s/nE2fW5ZBkbX2z_JeQmqkrA)
- [破解 Kotlin 协程(3) - 协程调度篇](https://mp.weixin.qq.com/s/Kn1yhxslRIzwykpf_FsWEA)
- [破解 Kotlin 协程(4) - 异常处理篇](https://mp.weixin.qq.com/s/WMFQuffyIq00Ai-XM5ISOA)
- [破解 Kotlin 协程(5) - 协程取消篇](https://mp.weixin.qq.com/s/l17Jc_FvQ44WYopx867xeA)
- [破解 Kotlin 协程(6) - 协程挂起篇](https://mp.weixin.qq.com/s/CKVfG3seCLjQSl_UTaJHCg)