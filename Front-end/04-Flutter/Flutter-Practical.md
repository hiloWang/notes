## 构建篇

### Flutter resolve dependencies 很慢

切换到 Android 工程，去掉无用的测试框架，添加国内代理：

```groovy
    repositories {
        maven{ url 'https://maven.aliyun.com/repository/google'}
        maven{ url 'https://maven.aliyun.com/repository/gradle-plugin'}
        maven{ url 'https://maven.aliyun.com/repository/public'}
        maven{ url 'https://maven.aliyun.com/repository/jcenter'}
        google()
        jcenter()
    }
```

### Waiting for another flutter command to release the startup lock

解决方法：

1. 打开flutter的安装目录/bin/cache/ 
2. 删除lockfile文件 
3. 重启AndroidStudio

### Flutter 卡在 package get 的解决办法

替换国内镜像：

linux：

```
export PUB_HOSTED_URL=https://pub.flutter-io.cn
export FLUTTER_STORAGE_BASE_URL=https://storage.flutter-io.cn
```

Windows 添加下面环境变量：

```
PUB_HOSTED_URL=https://pub.flutter-io.cn
FLUTTER_STORAGE_BASE_URL=https://storage.flutter-io.cn
```

具体参考 [Using Flutter in China](https://flutter.dev/community/china)

## Widget 如何管理自己的状态

- 自己管理。
- parent 管理。
- 混合方式管理。

具体参考[interactive](https://flutter.dev/docs/development/ui/interactive)

### Flutter for Android 开发者

Views

- Flutter 和 Android中的 View： 
  - Flutter 中一切都是 Widget。
- 如何更新 widget：
  - 唯一的方式——调用 setState 触发 UI 重新构建。
- 如何布局？ XML layout 文件跑哪去了？
  - Flutter 中是使用代码进行布局。
- 如何在布局中添加或删除组件
  - 无法添加或删除组件，调用 setState 触发 UI 重新构建，然后根据状态决定插件哪些 Widget。
- 在 Android 中，可以通过 View.animate() 对视图进行动画处理，那在 Flutter 中怎样才能对Widget进行处理
  - 将 widget 包装到 Animation 中，Flutter 提供一系列可以实现动画的 Widget，比如 FadeTransition。使用 AnimationController 控制动画的致性。
- 如何使用 Canvas draw/paint
  - 使用 CustomPaint 和 CustomPainter。
  - 参考：https://stackoverflow.com/questions/44396075/equivalent-of-relativelayout-in-flutter
- 如何构建自定义 Widgets
  - 在 Flutter 中采用继承 StatelessWidget/StatefulWidget 以组合其他 Widget 的方式，来实现自定义 Widgets。


Intents：

- Intent在Flutter中等价于什么？
  -  Flutter 不具有 Intents 的概念，但如果需要的话，Flutter 可以通过 Native 整合来触发 Intents。
  -  在 Flutter 中切换屏幕使用 Route 和 Navigator。
- 如何在Flutter中处理来自外部应用程序传入的Intents
  - 继承 FlutterActivity，处理外部 Intent，然后使用 MethodChannel 与 Flutter 进行通讯。
- startActivityForResult 在 Flutter 中等价于什么
  - Flutter 中有自己的返回数据给上一个 Widget 的方式，使用 Navigator 即可。

异步UI

- runOnUiThread 在 Flutter 中等价于什么
  - Dart 是单线程执行模型，支持 Isolates（在另一个线程上运行 Dart 代码的方式）、事件循环和异步编程。 除非启动一个 Isolate，否则你的 Dart 代码将在主 UI 线程中运行，并由事件循环驱动。
- AsyncTask和IntentService在Flutter中等价于什么
    - 由于 Flutter 是单线程的，所以不必担心线程管理或者使用AsyncTasks、IntentServices。要异步运行代码，可以将函数声明为异步函数，然后调用即可。
- OkHttp 在 Flutter 中等价于什么
  - Flutter 中也有 Http 请求相关库，比如 http/dio。
- 如何在Flutter中显示进度指示器
    - 调用 setState 触发 UI 重新构建，然后根据状态决定插件哪些 Widget，比如没有数据时展示 Loading Widget。

项目结构和资源：

- 在哪里存储分辨率相关的图片文件? HDPI/XXHDPI
  - Flutter 遵循像 iOS 这样简单的 3 种分辨率格式: 1x, 2x, and 3x，创建一个名为 images 的文件夹，并为每个图像文件生成一个 `@2x` 和 `@3x` 文件，并将它们放置在如下这样的文件夹中。
- 在哪里存储字符串? 如何存储不同的语言
  - 没有特定的资源文件，目前，最好的做法是创建一个名为 Strings 的类专门用于存储字符串。
  - Flutter对Android的可访问性提供了基本的支持(尚在进行中)，可以参考[intl](https://pub.dartlang.org/packages/intl)
- Android Gradle vs Flutter pubspec.yaml
  - 在 Flutter 中，虽然在 Flutter 项目中的 Android 文件夹下有 Gradle 文件，但只有在添加平台相关所需的依赖关系时才使用这些文件。 否则，应该使用 pubspec.yaml 声明用于 Flutter 的外部依赖项。

Activities 和 Fragments：

- Activity 和 Fragment 在 Flutter 中等价于什么
  - 可以认为式 Widget。
- 如何监听 Android Activity 生命周期事件
  - 通过挂接到 WidgetsBinding 观察并监听 didChangeAppLifecycleState 更改事件来监听生命周期事件。

Layouts：

- LinearLayout在Flutter中相当于什么
  - 使用 Row 或 Co​​lumn 可以实现相同的结果。
- RelativeLayout 在 Flutter 中等价于什么。
  - 通过使用 Column、Row 和 Stack 的组合来实现 RelativeLayout 的效果。
  - 参考：https://stackoverflow.com/questions/44396075/equivalent-of-relativelayout-in-flutter
- ScrollView 在 Flutter 中等价于什么
  - Android 中的 ScrollView/ListView/RecyclerView 等，在 Flutter 都等价于 LiveView。


手势检测和触摸事件处理：

- 如何将一个 onClick 监听器添加到 Flutter 中的 widget
  - 某些 Widget 支持 onPressed。
  - 如果 Widget 不支持事件监听，则可以将该 Widget 包装到 GestureDetector 中，并将处理函数传递给 onTap 参数。
- 如何处理widget上的其他手势
  - 将 Widget 包装到 GestureDetector 中，GestureDetector 支持各种手势动作。

Listview & Adapter：

- ListView 在 Flutter 中相当于什么
  - Flutter 中也提供了 ListView。
- 怎么知道哪个列表项被点击
  - 在构建 Item 的时候设置监听函数。
- 如何动态更新ListView
  - 依然是使用 setState 方法。

使用 Text：

- 如何在 Text widget 上设置自定义字体
  - 把字体文件放在项目文件夹中。
  - 在 pubspec.yaml文件中，声明字体。
  - 使用 TextStyle 将字体应用到Text widget。
- 如何在 Text 上定义样式
    - TextStyle

表单输入：

- Input的”hint”在flutter中相当于什么
  - 通过向 Text Widget 的装饰构造函数参数添加 InputDecoration 对象，轻松地为输入框显示占位符文本。
- 如何显示验证错误
  - 通过向Text Widget 的装饰构造函数参数添加 InputDecoration 对象，轻松地为输入框显示错误信息。


Flutter 插件：

- 如何使用 GPS sensor
  - 使用相关插进： https://pub.dartlang.org/packages/location
- 如何访问相机
  - 使用相关插进：https://pub.dartlang.org/packages/image_picker
- 如何使用Faceboo k登陆
  - https://pub.dartlang.org/packages/flutter_facebook_connect
- 如何构建自定义集成 Native 功能
  - 参考 [开发packages](https://flutterchina.club/developing-packages)
- 如何在我的 Flutter 应用程序中使用 NDK
  - 自定义插件


主题：

- 如何构建 Material 主题风格的 app
  - MaterialApp 是一个方便的 widget，它包装了许多 Material Design 应用通常需要的 widget，它通过添加 Material 特定功能构建在 WidgetsApp上。
  - 只有使用的 MaterialApp 才能使用 Material 中的 Widget。

数据库和本地存储：

- 如何在Flutter中访问 Shared Preferences ?
  - 使用相关插件：https://pub.dartlang.org/packages/shared_preferences
  - 自定义插件
- 如何在 Flutter 中访问 SQLite
  - 使用相关插件：https://pub.dartlang.org/packages/sqflite
  - 自定义插件