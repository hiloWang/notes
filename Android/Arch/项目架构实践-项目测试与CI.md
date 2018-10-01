
---
## 1 测试

### 规范

问题：
- 哪些地方可以依赖具体
- 哪些地方的依赖应该被注入
- 依据为各个层级进行怎样方式的测试，单元测试、Android层测试等

实施：
- Repository、Presenter、VM层应该可以在JVM上进行单元测试
- Presenter和Repository层中依赖Android层的对象都应该是可以被注入的，这样可以方便在测试的视图注入Mock的依赖对象。


### mock框架

*   easyMock
*   jMock
*   mockito
*   jmockit
*   PowerMock

[框架对比](https://blog.csdn.net/yasi_xi/article/details/24642517)

---
### 引用

- [Android单元测试（一）: 首先，从是什么开始](http://chriszou.com/2016/04/13/android-unit-testing-start-from-what.html)
- [Android单元测试（二）：再来谈谈为什么](http://chriszou.com/2016/04/16/android-unit-testing-about-why.html)
- [世界级的Android测试开发流程（一）](http://blog.zhaiyifan.cn/2016/02/23/world-class-testing-development-pipeline-for-android-part-1/)
- [世界级的Android测试开发流程（二）](http://blog.zhaiyifan.cn/2016/02/23/world-class-testing-development-pipeline-for-android-part-2/)
- [单元测试利器-Mockito 中文文档](http://blog.csdn.net/bboyfeiyu/article/details/52127551)
- GoogleSample [android-architecture](https://github.com/googlesamples/android-architecture)，架构蓝图

---
## 2 CI集成

- [ ] todo