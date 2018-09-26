# Android中Attr、Style、Theme学习

Android的ResurceType中的Style用于定义UI元素的外观和格式,包括如下概念：
- attr 风格样式的最小单元，表示声明的一个属性，可以单独在view上使用
- style 样式 是指定视图或窗口的外观和格式属性的集合。
- theme 主题 theme与style类似，但是theme应用于Activity和Application

---
## 1 Attr

下面布局中，`layout_width`就是一个属性。

```xml
      <View
        android:id="@+id/view"
        android:layout_width="300dp"
        android:layout_height="300dp"
        android:background="#ffffff"/>
```

这些属性都是Android系统自带的，我们可以在系统源码中找到这些定义属性的文件，目录是：`frameworks\base\core\res\res\values`。比如下面是系统attrs.xml中定义的布局宽高属性：

```xml
    <declare-styleable name="ViewGroup_Layout">
            <attr name="layout_width" format="dimension">
                <enum name="fill_parent" value="-1" />
                <enum name="match_parent" value="-1" />
                <enum name="wrap_content" value="-2" />
            </attr>
            <attr name="layout_height" format="dimension">
                <enum name="fill_parent" value="-1" />
                <enum name="match_parent" value="-1" />
                <enum name="wrap_content" value="-2" />
            </attr>
        </declare-styleable>
```

当然我们也可以自定义attr，单独定义属性：

```xml
    <resources>
        <attr name="testAttr" format="integer" />
    </resources>
```

声明属性组：

```xml
<resources>
    <declare-styleable name="ViewGroup_Layout">//使用declare-styleable声明一个属性组，当然attr也可以单独声明。
                <attr name="layout_width" format="dimension">//name表示属性名称，format表示属性的取值类型，如这里是一个枚举
                    <enum name="fill_parent" value="-1" />
                    <enum name="match_parent" value="-1" />
                    <enum name="wrap_content" value="-2" />
                </attr>
     </declare-styleable>
</resources>
```

说明：format的类型有如下几种：

```
    reference - 表示引用了另一个资源ID，比如@layout/my_layout"
    color     - 颜色
    boolean   - 布尔值
    dimension - 尺寸
    float     - 浮点值
    integer   - int类型
    string    - 字符串
    fraction  - 分数
    enum - normally implicitly defined -枚举
    flag - normally implicitly defined -类似于枚举，但是多个值可以同时附加，使用"|"连接多个值，比如Gravity的 bottom|right.

    "format可以定义单个类型，也可以使用与reference一起使用，"reference|string"，表示支持字符串和引用类型
```

>Android中的任何ResourceType都要以`<resources>`为顶级节点

### 如何获取属性

声明如下属性：

```xml
       <declare-styleable name="CustomView">
            <attr name="custom_attr_a" format="integer"/>
            <attr name="custom_attr_c" format="integer"/>
            <attr name="custom_attr_d" format="integer"/>
        </declare-styleable>
    
         <com.loopeer.springheader.sample.view.CustomView
                        xmlns:app="http://schemas.android.com/apk/res-auto"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="你好啊"
                        app:custom_attr_a="1"
                        app:custom_attr_c="2"
                        app:custom_attr_d="3"/>
```
    
通过AttributeSet可以获取所有属性

```java
            int attributeCount = attrs.getAttributeCount();
            for (int i = 0; i < attributeCount; i++) {
                Log.d(TAG,"Name"+ attrs.getAttributeName(i)+"Value"+attrs.getAttributeValue(i));
            }
```

使用TypedArray获取属性

```java
//但是使用TypedArray可以更加方便的帮助我们获取属性
TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomView);
Log.d(TAG, "typedArray.getInteger(R.styleable.CustomView_custom_attr_a):" + typedArray.getInteger(R.styleable.CustomView_custom_attr_a, 0));
Log.d(TAG, "typedArray.getInteger(R.styleable.CustomView_custom_attr_c):" + typedArray.getInteger(R.styleable.CustomView_custom_attr_c, 0));
Log.d(TAG, "typedArray.getInteger(R.styleable.CustomView_custom_attr_d):" + typedArray.getInteger(R.styleable.CustomView_custom_attr_d, 0));
typedArray.recycle();
```

打印结果分别是：

```
    Name layout_width Value -2
    Name layout_height Value -2
    Name text Value 你好啊
    Name custom_attr_a Value 1
    Name custom_attr_c Value 2
    Name custom_attr_d Value 3
    
    
    typedArray.getInteger(R.styleable.CustomView_custom_attr_a): 1
    typedArray.getInteger(R.styleable.CustomView_custom_attr_c): 2
    typedArray.getInteger(R.styleable.CustomView_custom_attr_d): 3
```

### 获取声明的单个属性

刚刚说到也可以定义单个属性，如何获取单个属性：


同样在布局文集中定义，可以通过遍历 AttributeSet 获取使用的单个属性：

```xml
    <com.loopeer.springheader.sample.view.CustomView
                        xmlns:app="http://schemas.android.com/apk/res-auto"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        app:single_attr="false"/>
```

也可以下面方式获取属性：

```java
         //定义用于获取属性的数组
         public static final int[] mAttrs = {R.attr.single_attr};//定义数组存储属性集合
         ...
         //方法中获取属性
         {
            TypedArray typedArray1 = context.obtainStyledAttributes(attrs, mAttrs);
            boolean aBoolean = typedArray1.getBoolean(0, false);//通过脚标获取属性
            Log.d(TAG, "aBoolean:" + aBoolean);
        }

        //打印结果
        Name single_attr Value false
```



可以看出只要布局文件中使用了属性，在被解析成对象时，都会这些属性封装到构造函数AttributeSet中，而通过
typedArray可以更加方便的获取指定的属性。因为有时候属性可能是引用类型，直接从AttributeSet中获取的话会比较麻烦。

### 复用系统的属性

刚刚在 CustomView 中使用了系统自带的`text`属性，表明系统属性也是可以复用的，但是如果我们的自定义控件使用了系统的属性，但是又没有在我们定义的attrs中找到，这样可读性就不搞了，于是可以在我们声明的`declare-styleable`中声明系统text属性，只是不要加format就行：

声明：
```xml
     <declare-styleable name="CustomView">
            <attr name="custom_attr_a" format="integer"/>
            <attr name="custom_attr_c" format="integer"/>
            <attr name="custom_attr_d" format="integer"/>
            <attr name="android:text"/>
        </declare-styleable>
```
使用：
```xml
     <com.loopeer.springheader.sample.view.CustomView
                        xmlns:app="http://schemas.android.com/apk/res-auto"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        app:single_attr="true"
                        app:custom_attr_a="1"
                        app:custom_attr_c="2"
                        android:text="@string/app_name"
                        app:custom_attr_d="3"/>
```

获取：
```java
     typedArray.getString(R.styleable.CustomView_android_text)
```

### obtainStyledAttributes方法

上面实例使用了context的obtainStyledAttributes方法来获取使用的属性：

```java
        //context直接调用Theme的方法：
        public final TypedArray obtainStyledAttributes(AttributeSet set, @StyleableRes int[] attrs) {
            return getTheme().obtainStyledAttributes(set, attrs, 0, 0);
        }
```

context直接调用了getTheme的obtainStyledAttributes方法，可以看到这个方法其实有四个参数，一般我们使用的是两个参数的方法，那个这个四个参数中后面参数是用来做什么的呢？

看下方法签名：

```java
    public TypedArray obtainStyledAttributes(AttributeSet set,
                    @StyleableRes int[] attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes)
```

-  第三个参数：`@AttrRes int defStyleAttr `：An attribute in the current theme that contains a reference to a style resource that supplies defaults values for the TypedArray.  Can be 0 to not look for defaults. 
-  第四个参数：`@StyleRes int defStyleRes` A resource identifier of a style resource that supplies default values for the TypedArray,used only if defStyleAttr is 0 or can not be found in the theme.  Can be 0 to not look for defaults.

这两个参数都可以用于设置默认的属性值

#### defStyleRes的使用

先定义一个style:
```xml
     <style name="DefCustomAttr">
            <item name="custom_attr_a">22</item>
            <item name="custom_attr_c">33</item>
            <item name="custom_attr_d">44</item>
        </style>
```
在布局中不使用添加属性
```xml
    <com.loopeer.springheader.sample.view.CustomView
                        xmlns:app="http://schemas.android.com/apk/res-auto"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        app:single_attr="true"
                        android:text="@string/app_name"/>
```
在构造函数中获取属性，在第四个参数传入声明的DefCustomAttr：
```java
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomView,0,R.style.DefCustomAttr);
```
最后获取结果：
```java
    typedArray.getInteger(R.styleable.CustomView_custom_attr_a): 22
    typedArray.getInteger(R.styleable.CustomView_custom_attr_c): 33
    typedArray.getInteger(R.styleable.CustomView_custom_attr_d): 44
```

#### defStyleAttr使用

声明一个引用属性：

```xml
     <declare-styleable name="CustomView">
            <attr name="custom_attr_a" format="integer"/>
            <attr name="custom_attr_c" format="integer"/>
            <attr name="custom_attr_d" format="integer"/>
            <attr name="android:text"/>
            <attr name="CustomStyleRef" format="reference"/><!--引用属性-->
        </declare-styleable>
```
定义一个style：
```xml
     <style name="DefCustomAttr">
            <item name="custom_attr_a">22</item>
            <item name="custom_attr_c">33</item>
            <item name="custom_attr_d">44</item>
     </style>
```
在theme中指定CustomStyleRef引用DefCustomAttr
```xml
        <style name="AppTheme" parent="Theme.AppCompat.Light.DarkActionBar">
            <!-- Customize your theme here. -->
            <item name="colorPrimary">@color/colorPrimary</item>
            <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
            <item name="colorAccent">@color/colorAccent</item>
            <item name="CustomStyleRef">@style/DefCustomAttr</item>
        </style>
```
 在构造函数获取属性时传入引用：
```java
    TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomView,R.attr.CustomStyleRef,0);
    //一样可以获取属性：
    typedArray.getInteger(R.styleable.CustomView_custom_attr_a): 22
    typedArray.getInteger(R.styleable.CustomView_custom_attr_c): 33
    typedArray.getInteger(R.styleable.CustomView_custom_attr_d): 44
```

这些方式在系统控件中使用的很频繁，因为系统控件往往有很多属性，而开发者往往只会定义一些基本属性，所以需要使用这些提供一些默认的属性，比如background，textColor等，下面是TextView的一个构造方法：

```java
    public TextView(Context context, @Nullable AttributeSet attrs) {
            this(context, attrs, com.android.internal.R.attr.textViewStyle);
    }
```

我们可以在系统的xml中找到这些属性定义:

```xml
    <attr name="textViewStyle" format="reference" />
    
    <item name="textViewStyle">@style/Widget.TextView</item>
    
    
    <style name="Widget.TextView">
            <item name="textAppearance">?attr/textAppearanceSmall</item>
            <item name="textSelectHandleLeft">?attr/textSelectHandleLeft</item>
            <item name="textSelectHandleRight">?attr/textSelectHandleRight</item>
            <item name="textSelectHandle">?attr/textSelectHandle</item>
            <item name="textEditPasteWindowLayout">?attr/textEditPasteWindowLayout</item>
            <item name="textEditNoPasteWindowLayout">?attr/textEditNoPasteWindowLayout</item>
            <item name="textEditSidePasteWindowLayout">?attr/textEditSidePasteWindowLayout</item>
            <item name="textEditSideNoPasteWindowLayout">?attr/textEditSideNoPasteWindowLayout</item>
            <item name="textEditSuggestionItemLayout">?attr/textEditSuggestionItemLayout</item>
            <item name="textCursorDrawable">?attr/textCursorDrawable</item>
            <item name="breakStrategy">high_quality</item>
            <item name="hyphenationFrequency">normal</item>
        </style>
```
如是继承系统已有的一些控件时，需要注意构造函数的写法，一般我们会这样写:

```java
        public CustomView(Context context) {
            this(context, null);
        }
    
    
        public CustomView(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }
    
        public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }
```
但是如是Button就要注意了，这样写存在一定问题，因为`com.android.internal.R.attr.textViewStyle`将不会被使用到，就会照成一些默认属性的丢失。

>注意：只有defStyleAttr设置为0或者在当前的theme中没有找到相关属性时，才会去defStyleRes中读取，defStyleAttr的优先级比defStyleRes高。

---
## 2 Style

样式是指定视图或窗口的外观和格式属性的集合。样式可以指定诸如height, padding, font color, font size, background color，以及更多的特性。

style的定义：

```xml
     <style name="CodeFont" parent="@android:style/TextAppearance.Medium">
            <item name="android:layout_width">fill_parent</item>
            <item name="android:layout_height">wrap_content</item>
            <item name="android:textColor">#00FF00</item>
            <item name="android:typeface">monospace</item>
        </style>
```

使用：

```xml
    <TextView
        style="@style/CodeFont"
        android:text="@string/hello" />
```

### Style的继承

可以通过`.`符号或者指定parent来定义Style的继承关系：

指定`parent`：

```xml
    <style name="GreenText" parent="@android:style/TextAppearance">
            <item name="android:textColor">#00FF00</item>
        </style>
```

使用`.` 符号：

```xml
    <style name="CodeFont.Red">
            <item name="android:textColor">#FF0000</item>
        </style>
```

但是需要注意的是如果继承的是系统属性，那么只能通过制定parent来指定style的基础关系，

---
## 3 Theme

Theme与Style使用同一个元素标签`<style>`，区别在于所包含的属性不同，并且使用的地方也不一样。Theme应用于`AndroidManifest.xml`的`<application>`或者`<activity>`标签下，设置后，被设置的Activity或整个应用下所有的View都可以使用该`<style>`里面的属性。

Android系统提供了多套主题，查看Android的[frameworks/base/core/res/res/values](https://android.googlesource.com/platform/frameworks/base/+/refs/heads/master/core/res/res/values/themes.xml)目录，就会看到有以下几个文件(目前为止)：

*   **themes.xml**：低版本的主题，目标API level一般为10或以下
*   **themes_holo.xml**：从API level 11添加的主题
*   **themes_device_defaults.xml**：从API level 14添加的主题
*   **themes_material.xml**：从API level 21添加材料的主题
*   **themes_micro.xml**：
*   **themes_leanback.xml**：

不过在实际应用中，因为大部分都采用兼容包的，一般都会采用兼容包提供的一套主题：**Theme.AppCompat**。**AppCompat**主题默认会根据不同版本的系统自动匹配相应的主题，比如在Android 5.0系统，它会继承Material主题。不过这也会导致一个问题，不同版本的系统使用不同主题，就会出现不同的体验。因此，为了统一用户体验，最好还是自定义主题。

**一般我们在定义一个theme时会让其继承某一个系统已有的属性，**，如果直接在application或者activity中使用完全自定义的属性是会报错的，因为Activity或Application的需要很多的属性才能工作，
比如现在新建项目默认都是继承的`Theme.AppCompat.Light.DarkActionBar`。使用Compat包中的属性，可以帮助我们做好各个系统的版本适配。

```xml
        <!-- Base application theme. -->
        <style name="AppTheme" parent="Theme.AppCompat.Light.DarkActionBar">
            <!-- Customize your theme here. -->
            <item name="colorPrimary">@color/colorPrimary</item>
            <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
            <item name="colorAccent">@color/colorAccent</item>
            <item name="CustomStyleRef">@style/DefCustomAttr</item>
        </style>
```


### 优先级

View某一个属性优先于View指定的Style，而View中的Style会优先于Activity中的Theme，Activity中的Theme会优先于Application中的Theme，所以说你可以定义整个应用的总体风格，但局部风格你也可以做出自己的调整。

### 使用 theme 中的属性值



有些情况下，可能需要使用theme中的属性值，比如让一个TextView直接显示我们在theme中定义的一个属性的内容，并且使用系统字体的颜色，则可以如下做：

```xml
    <attr name="DefaultText" format="string"/>
    
    <style name="AppTheme" parent="Theme.AppCompat.Light.DarkActionBar">
            <!-- Customize your theme here. -->
            <item name="colorPrimary">@color/colorPrimary</item>
            <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
            <item name="colorAccent">@color/colorAccent</item>
            <item name="CustomStyleRef">@style/DefCustomAttr</item>
            <item name="DefaultText">@string/app_name</item>
        </style>
    
    <com.loopeer.springheader.sample.view.CustomView
                        xmlns:app="http://schemas.android.com/apk/res-auto"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        app:single_attr="true"
                        android:textColor="?android:textColorSecondary"
                        android:text="?com.loopeer.springheader.sample:attr/DefaultText"/>
```

获得一个Attr的方法，不同于普通资源使用`@`符号获得的方式，而是需要使用`?`符号来获得属性，整体的表达方式如下：

```
?[*<package_name>*:][*<resource_type>*/]*<resource_name>*
```

上面`android:text="?com.loopeer.springheader.sample:attr/DefaultText"`可以简写成`android:text="?attr/DefaultText"`,因为是使用本应用中的attr，可以省去`<package_name>`部分。


---
## 引用

### Document

- [Defining custom attrs](http://stackoverflow.com/questions/3441396/defining-custom-attrs)
- [访问资源](https://developer.android.com/guide/topics/resources/accessing-resources.html)
- [Resource Types](https://developer.android.com/guide/topics/resources/available-resources.html?hl=zh-cn)
- [提供资源](https://developer.android.com/guide/topics/resources/providing-resources.html?hl=zh-cn)
- [Styles and Themes](https://developer.android.com/guide/topics/ui/themes.html?hl=zh-cn#PlatformStyles)
- [面向开发者的材料设计](https://developer.android.com/training/material/get-started.html)
- [Android 深入理解Android中的自定义属性](http://blog.csdn.net/lmj623565791/article/details/45022631)

### Blog

- [Android样式的开发:shape篇](http://keeganlee.me/post/android/20150830)
- [Android样式的开发:selector篇](http://keeganlee.me/post/android/20150905)
- [Android样式的开发:layer-list篇](http://keeganlee.me/post/android/20150909)
- [Android样式的开发:drawable汇总篇](http://keeganlee.me/post/android/20150916)
- [Android样式的开发:View Animation篇](http://keeganlee.me/post/android/20151003)
- [Android样式的开发:Property Animation篇](http://keeganlee.me/post/android/20151026)
- [Android样式的开发:Style篇](http://keeganlee.me/post/android/20151031)
