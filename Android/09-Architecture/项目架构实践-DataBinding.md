# Databinding学习

开启databinding非常简单，只需要在对应的模块的gradle脚本中添加如下代码即可：

```groovy
    android {
        dataBinding {
            enabled = true
        }
    }
```

---
## 1  数据绑定与事件处理

定义一个layout布局，在外围添加layout节点，把需要用到的数据当以在data节点中。

```xml
     <layout
        xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        xmlns:bind="http://schemas.android.com/apk/res-auto">

       <data>

            <import type="android.view.View"/>
            <import type="java.lang.String"/>
            <variable
                name="userVO"
                type="com.test.UserVO"/>
            <variable
                name="viewHandler"
                type="com.test.ViewHandler"/>

        </data>

           <ScrollView
             android:layout_width="match_parent"
             android:layout_height="match_parent">
        </ScrollView>
     </layout>
```

### 数据绑定

数据依赖可以依赖方法和字段，一般我们选择依赖方法，依赖方法都是按照javaBean中方法命名规则来依赖的，比如getName，在xml中就是name。

```xml
     android:text="@{userVo.name}"
     android:text="@{String.valueOf(userVo.age)}"
     android:visibility="@{userVo.vip?View.VISIBLE:View.GONE}"
```

### 方法引用

要求方法签名一致

```xml
     <EditText
       android:id="@+id/frag_name_et"
       android:layout_width="match_parent"
       android:layout_height="wrap_content"
       android:hint="name"
       android:onTextChanged="@{viewHandler::onTextChanged}"/>
```

### 事件监听

使用的lambda表达式，不要求方法签名一致

```xml
     android:onClick="@{()->viewHandler.onNameClick(userVo.name)}"
     android:onClick="@{v->viewHandler.inflate()}"
     android:onTextChanged="@{(s,start,end,count)->viewHandler.onPhoneChanged(s)}"
```

lambda表达式定义技巧：

- `()->`,不管方法有没有参数，都可以使用这个
- `(s,start,end,count)->`,如果方法有参数，又需要把参数传递给调用的方法，就需要安装View中方法签名来定义方法参数。
- `(v) -> presenter.onEmployeeLongClick(userVo, context)`,被调用的方法可以定义Context参数和布局中定义的variable参数。


**方法引用与事件绑定必须是对应View中存在的方法**，如：

- View 的 `onAttachedToWindow`
- EditText 的 `onTextChanged`
- CheckBook 的 `onCheckedChanged`

还有一个需要注意的是** Avoid Complex Listeners**，具体参考官方文档


---
## 2 布局详情

### import

使用import可以导入使用到的类，就像Java中的import一样：

```xml
    <data>
        <import type="android.view.View"/>
        <import type="java.lang.String"/>
    </data>

    <TextView
       android:text="@{user.lastName}"
       android:layout_width="wrap_content"
       android:layout_height="wrap_content"
       android:visibility="@{user.isVIP ? View.VISIBLE : View.GONE}"/>
```

当然也可以给导入的类取别名。如果导入的类含有静态的方法，可以直接使用

### Variable定义

在data节点内声明的variable都会在对应生成的DataBinding类中被定义成成员变量，比如：

```xml
    <data>
            <import type="android.view.View"/>
            <import type="java.lang.String"/>
            <import type="com.ztiany.basic.bean.UserVo"/>
            <import type="java.util.List"/>

            <variable
                name="userList"
                type="List&lt;UserVO&gt;"/>
            <variable
                name="userVO"
                type="com.test.UserVO"/>
            <variable
                name="viewHandler"
                type="com.ztiany.basic.BasicFragment.ViewHandler"/>

        </data>
```

在data标签中我们定义了：

  - userList
  - userVO
  - viewHandler

三个变量，在生成的java类中就会定义三个成员变量：

```java
            mFragmentDataBindingBasicBinding.getUserList();
            mFragmentDataBindingBasicBinding.getUserVo();
            mFragmentDataBindingBasicBinding.getViewHandler();
```

这里需要注意`List&lt;UserVo&gt;`就是`List<String>`

### 自定义DataBinding类名

```xml
    <data class="ContactItem">
        ...
    </data>
```

在data节点上定义xml生成的类名

### include/merge/viewStub

Databing通用支持`include/merge/viewStub`，只是需要为布局单独设置一个Data。

---
## 3 在xml中支持的表达式

```
    算术 + - / * %
    字符串合并 +
    逻辑 && ||
    二元 & | ^
    一元 + - ! ~
    移位 >> >>> <<
    比较 == > < >= <=
    Instanceof
    Grouping ()
    文字 - character, String, numeric, null
    Cast
    方法调用
    Field 访问
    Array 访问 []
    三元 ?:
    空合并运算符
```
示例

```xml
    android:text="@{String.valueOf(index + 1)}"
    android:visibility="@{age < 13 ? View.GONE : View.VISIBLE}
    android:transitionName='@{"image_" + id}'
    android:text='@{String.valueOf(1 + (Integer)user["age"])}'
    android:text="@{(String)list[0] + str}"
    android:text="@{String.valueOf(str instanceof String)}"
```

空合并运算符：

```xml
    android:text="@{user.displayName ?? user.lastName}"
    等价于
    android:text="@{user.displayName != null ? user.displayName : user.lastName}"
```

---
## 4 使用集合

```xml
    <data>
        <import type="android.util.SparseArray"/>
        <import type="java.util.Map"/>
        <import type="java.util.List"/>
        <variable name="list" type="List&lt;String&gt;"/>
        <variable name="sparse" type="SparseArray&lt;String&gt;"/>
        <variable name="map" type="Map&lt;String, String&gt;"/>
        <variable name="index" type="int"/>
        <variable name="key" type="String"/>
    </data>
    …
    android:text="@{list[index]}"
    …
    android:text="@{sparse[index]}"
    …
    android:text="@{map[key]}"
```

### 字符串字面量

    使用单引号把属性包起来，就可以很简单地在表达式中使用双引号：
    android:text='@{map["firstName"]}'
    或者也可以用双引号将属性包起来。这样的话，字符串字面量就可以用`&quot;`或者反引号(`) 来调用
    android:text="@{map[`firstName`}"
    android:text="@{map['firstName']}"

---
## 5 引用资源

```xml
    android:padding="@{large? @dimen/largePadding : @dimen/smallPadding}"//选择
    android:text="@{@string/resource_name(firstName, lastName)}"//格式化
    android:text="@{@plurals/banana(bananaCount)}"//格式化
    android:marginLeft="@{@dimen/margin + @dimen/avatar_size}"//运算
    android:text="@{@string/colon_end_mask(@string/phone)}"//格式化
```

| Type | Normal Reference | Expression Reference |
|---|----|---|
| String[] | @array | @stringArray |
| int[] | @array | @intArray |
| TypedArray | @array | @typedArray |
| Animator | @animator | @animator |
| StateListAnimator | @animator | @stateListAnimator |
| color `int` | @color | @color |
| ColorStateList | @color | @colorStateList |


---
## 6 Observable实现数据更改后立即刷新

>一个纯净的Java ViewModel类被更新后，并不会让UI去更新。而数据绑定后，我们当然会希望数据变更后UI会即时刷新，Observable就是为此而生的概念。

一共有三种方式

- 继承BaseObservable
- 使用ObservableField
- 使用Observable Collections 可以单独使用，也可以在javaBean中使用
    - ObservableArrayMap
    - ObservableArrayList

### 继承BaseObservable或者使用 ObservableField

```java
    public class UserVO extends BaseObservable {

        @Bindable
        private String mName;//1 继承BaseObservable，字段需要添加@Bindable才可以绑定

        @Bindable
        private int mAge;

        public static final String PROFESSIONAL = "professional";
        
       //2 使用ObservableField
        public ObservableBoolean mIsVip = new ObservableBoolean();
    
        //3 使用Observable Collections
        public ObservableArrayMap<String, String> userInfo = new ObservableArrayMap<>();

        @Bindable
        private String phoneNumber;

        public String getName() {
            return mName;
        }

        public void setName(String name) {
            mName = name;
            notifyPropertyChanged(BR.name);
        }

        public int getAge() {
            return mAge;
        }

        public void setAge(int age) {
            mAge = age;//   @Bindable的字段需要主动刷新
            notifyPropertyChanged(BR.age);
        }

        public boolean isVip() {
            return mIsVip.get();
        }
    
        public void setVip(boolean vip) {
            mIsVip.set(vip);
        }
    
        public String getPhoneNumber() {
            return phoneNumber;
        }
    
        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            notifyPropertyChanged(BR.phoneNumber);
        }
    
    
        public void setProfessional(String professional) {
            userInfo.put(PROFESSIONAL, professional);
        }
    
        public String getProfessional() {
            return userInfo.get(PROFESSIONAL);
        }
    }
```

---
## 7  生成的类与字段

### 类

生成的类是默认按照xml命名来生成：比如`R.layout.fragment_data_binding_basic`生成的就是`FragmentDataBindingBasicBinding`

生成类的方法有很多种

### 方式1：使用DataBindingUtil

```java
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            mFragmentDataBindingBasicBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_data_binding_basic, container, false);
            return mFragmentDataBindingBasicBinding.getRoot();
        }
```

### 方式2：使用生成类的静态方法

```java
    MyLayoutBinding binding = MyLayoutBinding.inflate(layoutInflater);
    MyLayoutBinding binding = MyLayoutBinding.inflate(layoutInflater, viewGroup, false);

    //如果inflate布局中使用不同的机制，它可以单独的约束：

    MyLayoutBinding binding = MyLayoutBinding.bind(viewRoot);
```

### 字段

凡是在xml定义了id的View，都会在生成的Binding类中被定义`android:id="@+id/frag_phone_et"`对应生成的字段名就是`        mFragmentDataBindingBasicBinding.fragPhoneEt`.

### 设置数据

生成了Binding类后，需要设置数据与事件处理。

```java
        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            initData();
            mFragmentDataBindingBasicBinding.setUserVo(mUserVo);
            mFragmentDataBindingBasicBinding.setViewHandler(new ViewHandler());
        }
```

---
## 8 动态变量

很多有时候，我们并不知道具体生成的binding类是什么。比如在列表视图中，可能有多种ViewHolder，而我们拿到的holder可能只是一个基类，只能直接使用setVariable设置变量了，此时需要传入BR常量。

```java
     holder.getBinding().setVariable(BR.item, item);//BR.item就是在layout中定义的别名
     holder.getBinding().executePendingBindings();//立即刷新
```

---
## 9 自定义属性

###  Automatic Setters

什么叫 Automatic Setters，比如DrawerLayout有一个属性为scrimColor，我们要在xml中使用dataBinding设置这个属性就会这样写：

```xml
    <android.support.v4.widget.DrawerLayout
        android:layout_width=“wrap_content”
        android:layout_height=“wrap_content”
        app:scrimColor=“@{@color/scrimColor}”/>
```

于是DataBinding就会去查找这个属性对应的set方法**setScrimColor**，并调用设置对应的值。

```java
        public void setScrimColor(@ColorInt int color) {
            mScrimColor = color;
            invalidate();
        }
```

这样的一个过程就称之为 `Automatic Setters`.

但是如果DrawerLayout并没有这个属性，就会在编译期报错。对于一些第三方自定义View，没有与属性对应的方法或者命名没有遵守setter，getter怎么办呢？这是就需要使用 `BindingMethods & BindingAdapter`了


### BindingMethods

**View的方法名与xml中属性名称不一样时使用**

如果View本身就支持这种属性的set，只是xml中的属性名字和java代码中的方法名不相同，那么可以使用BindingMethods注释。

比如：

android:tint是给ImageView加上着色的属性。如果直接对android:tint使用data binding，由于会去查找setTint方法，而该方法不存在，则会编译出错。而实际对应的方法，应该是`setImageTintList`。

这时候我们就可以使用BindingMethod指定属性的绑定方法：

```java
    BindingMethods({
           @BindingMethod(type = “android.widget.ImageView”,
                          attribute = “android:tint”,
                          method = “setImageTintList”),
    })
```

### BindingAdapter

View的方法签名与对应的属性不一致时使用

```java
    @BindingAdapter("android:paddingLeft")
    public static void setPaddingLeft(View view, int padding) {
        view.setPadding(padding,
                        view.getPaddingTop(),
                        view.getPaddingRight(),
                        view.getPaddingBottom());
    }
```

**android.databinding.adapters.ViewBindingAdapter看到有很多定义好的适配器，还有BindingMethod。如果需要自己再写点什么，仿照这些来写就好了。**


我们还可以进行多属性绑定，比如：

```java
    @BindingAdapter({"bind:imageUrl", "bind:error"})
    public static void loadImage(ImageView view, String url, Drawable error) {
       Glide.with(view.getContext()).load(url).error(error).into(view);
    }

    <ImageView 
      app:imageUrl="@{venue.imageUrl}"
      app:error="@{@drawable/venueError}"/>
```


---
## 10 转换Converters

当一个对象从绑定表达式返回一个值，一设set方法会自动根据属性名称或者BindingMethods来选择，而返回值的类型会自动转换成方法需要的类型，如下所示，userMap["lastName"]返回的是一个Object，而TextView的setText方法需要的是String类型，这时Object会自动转换成String类型

```xml
    <TextView
       android:text='@{userMap["lastName"]}'
       android:layout_width="wrap_content"
       android:layout_height="wrap_content"/>
```

有时转换应该是特定类型之间的自动。例如，设置背景时：

```xml
    <View
       android:background="@{isError ? @color/red : @color/white}"
       android:layout_width="wrap_content"
       android:layout_height="wrap_content"/>
```

这里，背景需要drawbale，但color是一个整数类型。Databinding不能自定把颜色转换成drawable，所以需要定义个自定义的转换器：

```java
    @BindingConversion
    public static ColorDrawable convertColorToDrawable(int color) {
       return new ColorDrawable(color);
    }
```

定义了这个BindingConversion我们才能愉快的在DataBindding中使用background属性。其他属性亦是如此。

---
## 11 AndroidStudio对DataBinding的支持

Android Studio supports many of the code editing features for data binding code. For example, it supports the following features for data binding expressions:

- Syntax highlighting 语法高亮
- Flagging of expression language syntax errors 语法错误标志
- XML code completion 代码提示
- References, including navigation (such as navigate to a declaration) and quick documentation 代码跳转与引用

```xml
<TextView
   android:layout_width="wrap_content"
   android:layout_height="wrap_content"
   android:layout_gravity="center_horizontal"
   android:text="@{userVO.phoneNumber,default=没}"/>
```
default用于设置预览显示的文字

---
## 12 双向绑定

```xml
    <EditText
                android:id="@+id/act_name_et"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:allowUndo="true"
                android:text="@={twoWayBean.mName}"/>
```
使用的是`@={}`，具体原理参考TextViewBindingAdapter


---
## 13 注入容器

- 定义抽象
- 实现DataBindingComponent
- 注入Component

---
## 14 更新

DataBinding v2 编译器用于与 AAC 在的 LiveData 配合工作。

---
## 引用

- [官方文档](https://developer.android.com/topic/libraries/data-binding/index.html#studio_support)
- [从零开始的Android新项目7 - Data Binding入门篇](http://blog.zhaiyifan.cn/2016/06/16/android-new-project-from-0-p7/)
- [从零开始的Android新项目8 - Data Binding高级篇](http://blog.zhaiyifan.cn/2016/07/06/android-new-project-from-0-p8/)
- [Data Binding Component详解 - 换肤什么的只是它的一个小应用](http://blog.zhaiyifan.cn/2016/07/21/data-binding-component/)
- [DataBindingAdapter](https://github.com/markzhai/DataBindingAdapter/blob/master/README_CN.md)
- [DataBindingSample](https://github.com/markzhai/DataBindingSample)