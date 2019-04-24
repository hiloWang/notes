# Flutter从入门到进阶

>[Flutter从入门到进阶 实战携程网App](https://coding.imooc.com/class/chapter/321.html) 学习笔记

该课程对应的与手记：

- [博客](http://www.devio.org/tags/#Flutter)
- [Flutter从入门到进阶系列手记](http://www.imooc.com/article/282910)

## 1 Flutter 简介

### Flutter 历史

- 2014.10 Open the Sky
- 2015.10 改名为 Flutter
- 2017.05 Googol I/O
- 2018.06 1.0 预览版
- 2019.02 1.2 发布

### 学习步骤

- 入门基础
- 入门实战
- 进阶提升
- 进阶实战
- 进阶拓展

### 相关技术

- Scaffold
- PageView
- Navigator
- NotificationListener
- 自定义组件
- Native Modules
- AI 智能语音
- Channel 通信
- 混合开发
- 插件

## 2 Flutter入门：开发工具准备与开发环境搭建

### 开发工具

- VS Code
- Android Studio（推荐）
  
### 开发环境配置

- 在 AndroidStuiod 上安装 Dart 和 Flutter 开发插件
- 下载 Flutter SDK，配置环境变量
- 设置 Flutter 镜像(可以翻墙则非必须)

命令行运行 `flutter doctor` 检测和修复 Flutter 开发环境。

## 3 Flutter入门：基础知识十讲

### 3.1 Flutter基础知识

- Dart 基础知识
- 什么是声明式 UI
- Flutter 入门基础知识
- 项目结构，资源，依赖，本地化
- 认识 Flutter 视图
- 布局和列表
- 状态管理
- 路由和导航
- 线程和异步 UI
- 首饰检测及触摸事件处理
- 文字和主体处理
- 表单输入、富文本处理
- 调用硬件，第三方服务以及平台交互，通知
  
### 3.2 Dart 基础知识

在线编辑器：

- DartaPad
- OnlineDartCompiler

Dart基础：

- 程序入口：main 方法
- 创建分配变量：`var` 或 `具体类型`
- 默认值：`null`
- 检车 null 值：从 Dart 1.12 开始，使用`?.` 或 `??` 运算符可用帮助我们做 null 检查
- 在 Dart 中，只有布尔值“true”被视为true。
- Dart 中定义函数不需要关键字，直接定义即可。
- 异步编程：Future 对象、async 和 await 函数。

具体参考[Flutter开发之Dart必备基础知识](https://www.imooc.com/article/282910)

### 3.3 什么是声明式 UI

Flutter 是受 React 编程范式启发而设计的“现代响应式框架”，什么是“响应式”呢？

>“响应式”按照标准定义的话，是一种基于“数据流”模型的声明式编程范式。所谓“声明式”，是相对于“命令式”的，我们最早接触的编程都是命令式的，同一个流程，“命令式”会将命令下达到每一个具体的步骤，直到达到目标，而“声明式”，直接声明预期的结果，程序自己完成中间的步骤，也就是说“声明式”编程范式的关键是让程序能理解你的描述，知道干什么，然后自发的去完成。

在声明式的 UI 中，组件是不可变的，每一次状态的改变都会导致组件的重新构建，但是 SDK 对此做了很多的优化，而不用过度关心性能相关的问题。

### 3.4 Flutter 入门基础知识

- 如何插件 Flutter 项目
- 如何运行 Flutter 项目
- 如何导入 Widget？
- 如何使用 Widget 并将其嵌套已形成 Widget 树？

具体参考[Flutter入门必备基础知识](http://www.imooc.com/article/282911)

#### 如何导入 Widget？

使用 import 导入相关文件即可：

```dart
//导入系统material widget 库
import 'package:flutter/material.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/widgets.dart';
```

#### 如何使用 Widget 并将其嵌套已形成 Widget 树？

在 Flutter 中，一切都是 Widget，而 Widget 的组织形式天然就是树状的。

### 3.5 项目结构、资源、依赖和本地化

- 项目文件结构是怎样子的？
- 在哪里归档图片资源以及如何处理不同分辨率？
- 如何归档strings资源，以及如何处理不同语言？
- 如何添加Flutter项目所需的依赖？

具体参考[两分钟带你快速掌握Flutter的项目结构、资源、依赖和本地化](http://www.devio.org/2019/04/02/flutter-project-structure-resources-dependencies-and-localization/)

#### 项目结构

```
android：Android 部分工程文件
ios：IOS 部分工程文件
lib：项目中的 Dart 源文件
    src：包含其他源文件
    main.dart：自动生成的项目入口文件，类似 RN 的 index.js 文件
test：测试相关代码
.pubspec.yaml：项目依赖配置文件
```

#### 在哪里归档图片资源以及如何处理不同分辨率？

- Flutter 中，统一讲资源作为 assets 处理。
- 默认情况下，Flutter 并没有预先定义的文件结构，如果要使用资源，则需要在 pubspec.yaml 中配置 assets 文件夹位置。
- 配置的 assets 文件夹中可以存储任意类型的资源文件，比如图片、Json 文件。
- 在代码中，通过 [AssetsBundle](https://docs.flutter.io/flutter/services/AssetBundle-class.html) 对资源进行访问。
- 对于图片，Flutter 像 iOS 一样，遵循了一个简单的基于像素密度的格式。Image assets 可能是 1.0x 2.0x 3.0x 或是其他的任何倍数。

具体参考官方文档[Adding assets and images](https://flutter.dev/docs/development/ui/assets-and-images)

#### 如何归档strings资源，以及如何处理不同语言？

Flutter目前没有专门的字符串资源系统。 目前，最佳做法是将strings资源作为静态字段保存在类中。

#### 如何添加Flutter项目所需的依赖？

Flutter 使用 Dart 构建系统和 Pub 包管理器来处理依赖。这些工具将 Android 和 iOS native 包装应用程序的构建委派给相应的构建系统。在 Flutter 中，虽然在 Flutter 项目中的 Android 文件夹下有 Gradle 文件，但只有在添加平台相关所需的依赖关系时才使用这些文件。 否则，应该使用 pubspec.yaml 来声明用于 Flutter 的外部依赖项。

### 3-6 认识视图(Views)

- 什么是 Flutter View？
- 如何更新 Widgets?
- 如何布局？
- 如何在布局中添加或删除组件？
- 如何对 Widget 做动画？
- 如何绘图(Canvas draw/paint)？
- 如何构建自定义 Widgets?
- 如何设置 Widget 的透明度？

具体参考[带你快速掌握Flutter的视图(Widgets)](http://www.devio.org/2019/03/16/flutter-views/)

### 3-7 Fluter 布局与列表

- LinearLayout 在 Flutter 中等价于什么？
  - Row 和 Colum
- RelativeLayout 在 Flutter 中等价于什么？
  - Row 和 Colum、Stack
  - [equivalent-of-relativelayout-in-flutter](https://stackoverflow.com/questions/44396075/equivalent-of-relativelayout-in-flutter)
- 如何使用 Widget 定义布局属性？
  - Flutter 中布局属性也使用对应的 Widget 实现，比如 Padding、Align 都是 Widget。
- 如何分层布局？
  - 在 Flutter 使用 Stack 控制子 Widget 在哪一层。
- 如何设置布局样式？
  - Flutter 中没有内联样式和 stylesheets 的概念
  - 布局属性都是 Widget，比如：Padding，Center，Colunm，Row 等都是 Widget。
  - 组件也通常接受用于布局样式的构造函数，比如 Text 可以使用 TextStyle 属性。
- ScrollView 在 Flutter 中等价于什么？
  - 在 Flutter 中使用 ListView 来实现滑动界面。
- 如何动态更新 ListView？
  - setState() 方法(数据量小时)。
  - 使用 ListView.Builder（适用于动态创建列表或数据量较大的列表）

### 3.8 状态管理

- 什么是 StatelessWidget？
- 什么是StatefulWidget？
- StatefulWidget 和 StatelessWidget 有哪些最佳实践？

具体参考[两分钟带你掌握Flutter的StatelessWidget与StatefulWidget](http://www.devio.org/2019/03/23/flutter-statelesswidget-statefulwidget/)

### 3.9 路由与导航

- 在 Flutter 中如何实现不同页面跳转（导航）？
- 如何获取路由跳转返回的结果？
- 如何在 Flutter 中处理来自外部应用程序传入的 Intents？(Android)
- 怎么跳转到其他 App？

具体参考[两分钟带你掌握Flutter的路由与导航](http://www.devio.org/2019/03/31/flutter-router-navigator/)

### 3.10 线程与异步

- 怎么编写异步的代码？
- 怎么把工作放到后台线程执行？
- 如何进行网络请求？
- 如何为长时间运行的任务添加一个进度指示器？

具体参考[「快速上手Flutter开发系列教程」之线程和异步UI](http://www.devio.org/2019/03/16/thread-and-asynchronous-ui/)

### 3.11 手势检测与触摸事件处理

- 某些组件直接支持点击事件，比如：`onPressed`
- 使用 GestureDetector

### 3.12 主体与文字处理

- 在 pubspec.yaml 中配置字体
- 使用 MaterialDesignAp

### 3.13 表单输入与富文本

- 表单输入：TextField、TextFormField
- 如何设置输入框提是文字？
- 如何现实表单验证信息？
- 实现富文本展示：RechText

### 3.14 调用硬件、第三方服务以及平台交互、通知

- 如何调用硬件、第三方服务：通过集成对应的插件完成，比如：
  - geolocator
  - image_picker
  - SharedPreferences plugin
- 如何构建集成 Native SDK?

## 4 Flutter 入门：Flutter 必备基础

### 4.1 实例参考

- [flutter-official-examples](https://github.com/flutter/flutter/blob/master/examples/README.md)
- [flutter-official-samples](https://github.com/flutter/samples)
- [Simple basic isolated apps, for budding flutter devs.](https://github.com/nisrulz/flutter-examples)
- [Basic Flutter apps, for flutter devs.](https://github.com/iampawan/FlutterExampleApps)

### 4.2 图片控件

- ImageWidget 的使用
- FadeInImage 的使用
- path_provider 插件
- transparent_image 插件
- cached_newwork_image 插件
- Icon 使用, material_icon 插件
- 自定义 Icon, IconData 使用

### 4.3 动画 Animation 开发指南

### 4.4 调试技巧
