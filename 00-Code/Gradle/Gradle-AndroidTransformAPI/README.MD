#  TransformAPI

使用 Android 的 Transform 和 javassist 在项目构建阶段，修改 class 文件，注入自定义代码。

## Transform API
 
 Starting with 1.5.0-beta1, the Gradle plugin includes a Transform API allowing 3rd party plugins to manipulate compiled class files before they are converted to dex files.

---
## 注意

如果直接引用代码中的插件，则项目名称必须为 *buildsrc*

---
## 参考

- http://www.jianshu.com/p/417589a561da
- http://www.jianshu.com/p/c9ce643e2f22
- https://github.com/north2016/T-MVP