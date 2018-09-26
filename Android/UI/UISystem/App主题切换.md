# App 主题切换

App 主题切换的实现方式有很多中，最传统的方法就是定义 theme，在 Activity 启动时应用对应的 theme，在 AppCompat 23.2.0 中，官方也提供了 DayNight 模式切换。

---
## 1 使用 AppCompat 实现主题切换

AppCompat 23.2.0 开始提供了主题切换的功能，应用这个模式，我们只要准备两套颜色、图片资源等资源放在对应的文件夹，调用切换代码即可轻松实现模式切换。总的来说，步骤如下：

- 主题继承 Theme.AppCompat.DayNight
- 对于 night 主题资源，放在 xxx-night 资源目录下，与默认资源目录一一对于即可
- xml 中直接引用资源即可
- 调用 AppCompatDelegate 切换模式，然后 recreate Activity即可


具体可以参考：

- [Android Developers Blog](https://android-developers.googleblog.com/2016/02/android-support-library-232.html)
- [android-support-23.2-sample](https://github.com/liaohuqiu/android-support-23.2-sample)
- [AppCompat v23.2 - 夜间模式，你所不知道的坑](https://www.jianshu.com/p/7cf714311304)
- [「译」AppCompat v23.2 - 夜间模式最佳实践](http://kingideayou.github.io/2016/03/07/appcompat_23.2_day_night/)
- [AppCompat v23.2 - 夜间模式最佳实践](http://kingideayou.github.io/2016/03/07/appcompat_23.2_day_night/)

---
## 2 定义多套 theme 来实现主题切换

- 定义多套主题
- 在 xml 中不直接引用 `color.xml` 中的颜色，而是使用`android:textColor="?android:textColorPrimaryInverse"` 和 `"?attr/sideBarBackground"`引用演示和 drawable 等资源
- 在 Activity 的 onCreate 方法中调用 setContentView 之前调用 setTheme 方法，设置对于的主题

除此之外，还存在一些遇到的问题：

- `vector.xml 在 api 21 之前不支持引用 style 中的属性`：对于 imageview 可以使用 `tint`
- `api 21 之前只支持 tint 属性，大部分控件不支持 backgroundTint 等属性`，参考 ViewCompat.setBackgroundTintList 方法让自定义 View 支持在代码中设置 backgroundTint 属性，两外 AppCompatButton 支持在 xml 中设置 backgroundTint，参考其源码可以让自定义 View 也支持在 xml 中设置 backgroundTint。
- `xml 中定义的 drawable 不支持引用 style 中的颜色`：这种情况，必须定义多套 drawable，然后在不同的 theme 中引用对于的 drwable，然后 xml 中引用 theme 中定义的 drawable。

---
## 3 使用第三方开源库

略