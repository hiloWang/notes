# Lint

---
## 1 AndroidLint

Lint是Android提供的静态代码检测工具，不仅仅包括Java代码，还包括xml等。在Android的Gradle插件中已经集成了lint代码检测。

---
## 2 使用AndroidLint

![](index_files/fe3db0ea-feb7-4038-aab6-0647fe226031.png)

![](index_files/f3a97dab-706d-4e9f-b19b-9f47f2d06dc8.png)

如上图点击OK即可开始Lint检测，上面还可以对检测范围进行设置，这里选择的是Module app，表示只对主项目进行检测。检测的结果如下图所示：

![](index_files/79345b90-c4db-4b62-8efa-d154f1d54e1f.png)

于是我们可以有针对性的对我们的代码进行优化。

---
## 3 使用lint检测无用的资源

![](index_files/fc0c1c18-a70a-451d-802d-a1d12eb6e3df.png)

![](index_files/c83d5be0-8b40-4d29-a5f9-f984247c6b86.png)

如上图所以，可以选择Lint的任务类型，这里选择的是`Unused resource`，等待分析完成之后，就可以清理哪些无用的资源了。