熟悉v4、v7包类库，其中包含了许多有用且方便的API，其实很多时候没有必要使用开源库。关于 Design 包具体参考

- [Android Design Support Library 的 代码实验——几行代码，让你的 APP 变得花俏](http://mrfu.me/english/2015/07/01/Translate_Codelab_Android_Design_Support_Library/)


# Material Design

## 一、关于Material Design

从Android5.0开始引入的，是一种全新的设计语言（翻译为“原材料设计”），其实是谷歌提倡的一种设计风格、理念、原则。拟物设计和扁平化设计一种结合体验。还吸取了最新一些科技理念。让设计层次感：**View具有Z轴**

1. 对于美工：遵循MD的界面设计、图标合集。
2. 对于产品经理：遵循MD界面设计、页面的跳转及动画效果、交互设计。
3. 对于开发人员：参与原型设计、辅助美工原型设计的素材准备。开发实现MD的设计界面、动画、转场动画等等。


## 二、MD的使用及开发

使用 support 兼容库：

1. android-support-v4：最低兼容到 Android 1.6 系统，里面有类似 ViewPager 等控件。
2. android-support-v7：appcompat、CardView、gridlayout、mediarouter、palette、preference、recyclerView(最低兼容到3.0)  最低兼容到Android 2.1的系统，这个工程可以让开发人员统一开发标准，在任何的系统版本下保证兼容性。（比如：Theme,value,布局，新的控件，新的动画特效实现）
3. android-support-design：包含用于实现 MD 设计的各种控件

所以现在AndrodStudio一般都会直接创建项目的时候就直接帮你新建或者引入了一个叫做 appcompat 的项目。


### MaterialDesign控制项目全局样式

为什么要用appcompat项目，因为里面是谷歌精心准备的---解决android碎片化开发蛋疼的问题，让我们app编译出来在
各种高低版本之间、不同的厂商生产的ROM之间显示出来的效果UI控件等有一致的体验。


1.引入appcompat-v7项目（包括了android-support-v7-appcompat.jar和资源文件）
2.编写全局样式：

```xml
    <style name="AppBaseTheme" parent="Theme.AppCompat.Light">
        <!--
            Theme customizations available in newer API levels can go in
            res/values-vXX/styles.xml, while customizations related to
            backward-compatibility can go here.
        -->
    </style>

    <!-- Application theme. -->
    <style name="AppTheme" parent="AppBaseTheme">
        <!-- All customizations that are NOT specific to a particular API-level can go here. -->
        <item name="android:textColor">@color/mytextcolor</item>
        <item name="colorPrimary">@color/colorPrimary_pink</item>
        <item name="colorPrimaryDark">@color/colorPrimary_pinkDark</item>
        <item name="android:windowBackground">@color/background</item>
        <item name="colorAccent">@color/accent_material_dark</item>
         <!-- 设置虚拟导航栏背景颜色 -->
           <item name="android:navigationBarColor">@color/colorPrimary_pink</item>
    </style>

    colorPrimary：主色，
    colorPrimaryDark：主色--深色，一般可以用于状态栏颜色、底部导航栏
    colorAccent：（代表各个控件的基调颜色--CheckBox、RadioButton、ProgressBar等等）
    android:textColor：当前所有的文本颜色
```

## 三、MaterialDesign兼容性控件的使用

在 appcompat-V7 以及 appcompat-design 里面有很多为兼容而生的控件这样就可以做到高低版本和不同的 ROM 之间体验一致，还可以配合appcompat的主题使用达到体验一致性


### RecyclerView

RecyclerView特点：

1. 谷歌在高级版本提出一个新的替代ListView、GridView的控件。
2. 高度解耦。
3. 自带了性能优化。ViewHolder。
4. 低耦合高内聚。
5. RecyclerView没有条目点击事件，需要自己写。
6. RecyclerView没有默认的分割线，使用RecyclerView.ItemDecoration
1. RecyclerView 如何添加头部和底部
1. RecyclerView交互动画
1. ItemTouchHelper

>可以通过修改Theme.Appcompa主题样式里面的android:listSelector或者 android:listDivider属性达到改变间隔线的大小和颜色哦！
```
<style name="AppTheme" parent="AppBaseTheme">
   <item name="android:listDivider">@drawable/item_divider</item>
</style>
```

### MateriaDesign侧滑

1.DrawerLayout 抽屉容器，来自support-v4包里面的。
2.NavigationView是谷歌在侧滑的MaterialDesign的一种规范，所以提出了一个新的控件，用来规范侧滑的基本样式。DrawerLayout+ NavigationView结合使用，注意：使用NavigationView，需要依赖项目：Design项目。同时还需要依赖recyclerView项目和CardView项目。

### SnackBar

Snackbar:的提出实际上是界于Toast和Dialog的中间产物。

- Toast：用户无法交互；
- Dialog：用户可以交互，但是体验会打折扣，会阻断用户的连贯性操作；
- Snackbar：既可以做到轻量级的用户提醒效果，又可以有交互的功能（必须是一种非必须的操作）。


### TextInputLayout

是强大的带提示的MD风格的EditText,看源码：`TextInputLayout extends android.widget.LinearLayout`
```xml
<android.support.design.widget.TextInputLayout
        android:layout_width="fill_parent"
        android:layout_height="wrap_content" 
        app:hintAnimationEnabled="true">

        <EditText
            android:layout_width="fill_parent"
            android:layout_height="wrap_content"
            android:hint="请输入用户名" />

</android.support.design.widget.TextInputLayout>
```

- `app:hintAnimationEnabled="true"` 是否获得焦点的时候hint提示问题会动画地移动上去。
- `app:errorEnabled="true"` 是都打开错误提示。要使用错误提示还得自己定义规则。
- `app:counterTextAppearance=""` 可以自己修改计数的文字样式。
- `app:counterOverflowTextAppearance=""` 超出字数范围后显示的警告的文字样式


### Toolbar

顶部导航

- 以前：谷歌干脆规范了顶部导航---ActionBar（3.0API，也有兼容包）
- 后来：ActionBar开发起来很蛋疼（1.用来比较费劲；2.扩展性太差 太死板）,大多数人都会使用一个开源的ActonBar，叫SherlockActionBar。谷歌就重新定义了一个Toolbar。现在又有个MaterialDesign的APPBar

使用：`android.support.v7.widget.Toolbar`

1. 引入support-v7包
2. 修改主题：`<style name="AppBaseTheme" parent="Theme.AppCompat.Light.NoActionBar">`，注意主题一定要使用：NoActionBar
3. 写布局，把Toolbar当做一个普通的容器使用。
4. Activity--->AppcompatActivity
5. 使用Toolbar替换ActionBar，setSupportActionBar(toolbar);
6. Toolbar属性：`android:background="?attr/colorPrimary"` 设置背景颜色，使用系统属性colorPrimary主色调。


让标题居中显示在toobar
```xml
    <android.support.v7.widget.Toolbar
        android:id="@+id/toolbar"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="?attr/colorPrimary"
        app:title="网易新闻">

        <TextView 
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="center"//重点
            android:text="textview"/>

    </android.support.v7.widget.Toolbar>
```

设置NavigationIcon的点击事件监听，比如返回按钮。
```java
app:navigationIcon="@drawable/abc_ic_ab_back_mtrl_am_alpha"
toolbar.setNavigationOnClickListener(new OnClickListener() {
    @Override
    public void onClick(View v) {
        finish();
    }
});
```

将弹出的菜单泡泡窗体修改成黑底白字Dark；Light白底黑字
```
app:popupTheme="@style/ThemeOverlay.AppCompat.Dark"
```

### SearchView

1. 像AutoCompleteTextView一样使用提示：`searchView.setSuggestionsAdapter(adapter)`

### 实现Toolbar滑动半透明效果

- 监听ScrollView的滑动事件不断地修改Toolbar透明度
- 布局：
```xml
<RelativeLayout>
    <ScrollView>
    </ScrollView>
    <Toobar>
</RelativeLayout>
```
- 给MyScrollView设置paddingTop,然后还要注意设置：
```
android:clipToPadding="false" 该控件的绘制范围是否不在Padding里面。false：绘制的时候范围会考虑padding即会往里面缩进。
android:clipChildren="false"  子控件是否能不超出padding的区域（比如ScrollView上滑动的时候，child可以滑出该区域）
```

### Palette调色板

Palette：可以在一张图片里面分析出一些色彩特性：主色调、鲜艳的颜色、柔和颜色等等

### TabLayout

引入Design兼容包

```
        app:tabIndicatorColor="@color/colorPrimary_pink"//指示器的颜色
        app:tabTextColor="@color/colorPrimary_pink"//tab的文字颜色
        app:tabSelectedTextColor="@color/colorPrimary_pinkDark"//选中的tab的文字颜色
        app:tabMode="fixed"//scrollable:可滑动；fixed：不能滑动，平分tabLayout宽度
        app:tabGravity="center"// fill:tabs平均填充整个宽度;center:tab居中显示
```

如何将TabLayout实现成底部导航的样子？

1. 就把TabLayout放在布局底部
2. 如何去掉底部的indicator，可以app:tabIndicatorHeight="0dp"
3. 实现自己的效果，比如微信：设置自定义的标签布局
```
    Tab tab = tabLayout.getTabAt(i);
    tab.setCustomView(view);
```

### CardView

CardView特性
```
    1) 边框圆角效果
        5.x 图片和布局都可以很好的呈现圆角效果，图片也变圆角了
        4.x 图不能变成圆角，如果要做成5.x一样的效果：通过加载图片的时候自己去处理成圆角

    2）阴影效果

    3）5.x上有Ripple水波纹效果（低版本需要自己做自定义的）
        android:foreground="?attr/selectableItemBackground"
        android:clickable="true"

    4）5.x实现按下的互动效果---下沉，松开弹起---Z轴位移效果（低版本也需要自己自定义做）

    5）可以设置内容的内边距 app:contentPadding="5dp"
```

### FloatingActionButton

FloatingActionButton :悬浮动作按钮

### CoordinatorLayout

监听滑动控件的滑动通过Behavior反馈到其他子控件并执行一些动画。注意：滑动控件指的是：`RecyclerView/NestedScrollView/ViewPager`，
开发者可以自定义Behavior。

### AppBarLayout

AppBarLayout继承自LinearLayout，勇于配合CoordinatorLayout使用，在互动的时候对AppBar做一些联动效果
```
    app:layout_scrollFlags="scroll"
    flag包括：
        scroll: 里面所有的子控件想要当滑出屏幕的时候view都必须设置这个flag，没有设置flag的view将被固定在屏幕顶部。
        enterAlways:('quick return' pattern)。
        enterAlwaysCollapsed：当你的视图设置了minHeight属性的时候，那么视图只能以最小高度进入， 只有当滚动视图到达顶部时才扩大到完整高度。
        exitUntilCollapsed：滚动退出屏幕，最后折叠在顶端。
        snap：
```

### CollapsingToolbarLayout

CollapsingToolbarLayout可以实现Toolbar折叠效果.

使用注意:
1.AppBarLayout设置固定高度，并且要实现折叠效果必须比toolbar的高度要高。
2.CollapsingToolbarLayout最好设置成match_parent

```
    app:layout_collapseMode="parallax" 
        parallax:视差模式，在折叠的时候会有折叠视差效果。
        none:没有任何效果，往上滑动的时候toolbar会首先被固定并推出去。
        pin:固定模式，在折叠的时候最后固定在顶端。
    app:layout_collapseParallaxMultiplier=“0.5”视差的明显程度(0 - 1.0)
```

示例：
```xml
  <android.support.design.widget.CollapsingToolbarLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            <!-- AppBarLayout属性 -->
            app:layout_scrollFlags="scroll|enterAlways"
            <!-- CollapsingToolbarLayout属性 -->
            app:collapsedTitleTextAppearance=""
            app:expandedTitleMargin="5dp"
            app:contentScrim="@color/colorPrimary_pink"//内容部分的沉浸式效果：toolbar和imageview有一个渐变过渡的效果
            app:statusBarScrim="@color/colorPrimary_pink"//和状态栏的沉浸式效果：指定颜色。
            app:title="动脑学院">

   </CollapsingToolbarLayout>
```

### 其他

1. android.support.v7.app.AlertDialog
2. 进度条样式设置style="@style/Widget.AppCompat.ProgressBar.Horizontal"
3. SwipeRefreshLayout下拉刷新
4. PopupWindow：ListPopupWindow、PopupMenu
5. android.support.v7.widget.LinearLayoutCompat给包裹在里面的所有子控件添加间隔线
```xml
        <android.support.v7.widget.LinearLayoutCompat
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            app:divider="@drawable/abc_list_divider_mtrl_alpha"
            app:showDividers="beginning|middle"
            android:orientation="vertical" >
```

## 四、MaterialDesign-沉浸式设计

- 官方的沉浸式Translucent：就是让整个APP沉浸(充斥了整个屏幕)在屏幕里面，没有显示状态栏，甚至没有显示底部导航栏。
- 平时大家所讨论的沉浸式：比如QQ的顶部Toolbar和状态栏程一体的颜色。

### 兼容开发

1 **5.0+ API**：5.0+自动实现了沉浸式效果，状态栏的颜色跟随你的主题里面的colorPrimaryDark属性。

```xml
    1）通过设置主题达到
    <!-- Application theme. -->
        <style name="AppTheme" parent="AppBaseTheme">
            <!-- All customizations that are NOT specific to a particular API-level can go here. -->
            <item name="android:textColor">@color/mytextcolor</item>
            <item name="colorPrimary">@color/colorPrimary_pink</item>
            <item name="colorPrimaryDark">@color/colorPrimary_pinkDark</item>
            <!--         <item name="android:windowBackground">@color/background</item> -->
            <!--         <item name="colorAccent">#906292</item> -->
        </style>

    2）通过设置样式属性解决
        <item name="android:statusBarColor">@color/system_bottom_nav_color</item>

    3）通过代码设置
        //5.0+可以直接用API来修改状态栏的颜色。
        getWindow().setStatusBarColor(getResources().getColor(R.color.material_blue_grey_800));
        (注意：要在setContentView方法之前设置)
```

2. **4.4 API**（低于4.4API，不可以做到）

用一些特殊手段，4.4API可以设置状态栏为透明的。

```
    1.在属性样式里面解决（不推荐使用，因为兼容不好）
        <item name="android:windowTranslucentStatus">true</item>

    2.再代码里面解决
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_main);

    出现副作用：APP的内容顶到最上面去了，即状态栏会遮挡一部分界面。

        解决办法（有几种）：

            1 给Toolbar设置android:fitsSystemWindows="true"
                该属性的作用：设置布局时，是否考虑当前系统窗口的布局，
                如果为true就会调整整个系统窗口布局(包括状态栏的view)以适应你的布局。

                但是：又出现了一个bug,当里面有ScrollView并且ScrollView里面有Edittext的时候，
                就会出现软键盘一弹起就会把toolbar拉下来，很难看
                这种办法有什么价值呢？如果里面没有ScrollView就可以用。
        
            2 发现给布局最外层容器设置android:fitsSystemWindows="true" 可以达到状态栏透明，
                并且露出底色---android:windowBackground颜色。不会出现toolbar被状态栏遮挡的情况。
            
                巧妙地解决：步骤：
                    1.在最外层容器设置android:fitsSystemWindows="true"，不要给Toolbar设置android:fitsSystemWindows="true"
                    2.直接将最外层容器(也可以修改-android:windowBackground颜色)设置成状态栏想要的颜色
                    3.下面剩下的布局再包裹一层正常的背景颜色。

            3 修改Toolbar的高度
                3.1.不要给Toolbar设置android:fitsSystemWindows="true"
                3.2.需要知道状态栏的高度是多少？去源码里面找找
                    <!-- Height of the status bar -->
                    <dimen name="status_bar_height">24dp</dimen>
                    <!-- Height of the bottom navigation / system bar. -->
                    <dimen name="navigation_bar_height">48dp</dimen>
                    反射手机运行的类：android.R.dimen.status_bar_height.
                3.3.修改Toolbar的PaddingTop（因为纯粹增加toolbar的高度会遮挡toobar里面的一些内容）
                    toolbar.setPadding(
                        toolbar.getPaddingLeft(),
                        toolbar.getPaddingTop()+getStatusBarHeight(this), 
                        toolbar.getPaddingRight(),
                        toolbar.getPaddingBottom());
```


### 如何让虚拟导航NavigationBar也做出沉浸式效果

1 **5.x 底部虚拟导航沉浸效果**

1. 属性解决navigationBarColor
2. 代码getWindow().setNavigationBarColor()
    

2 **4.4系统**

用一些特殊手段！4.4可以设置虚拟导航栏为透明的。

```
1在布局底部添加一个高度为0.1dp的view

2动态设置底部View的高度为虚拟导航栏的高度

        View nav = findViewById(R.id.nav);
        LayoutParams p = nav.getLayoutParams();
        p.height += getNavigationBarHeight(this);
        nav.setLayoutParams(p);
```

3.**做兼容性判断**

1 SDK版本不一样，两个区间：1. 大于5.0；2.=<4.4sdk<5.0
2 有的没有虚拟导航栏：判断是否有虚拟导航栏（源码里面有方法可以得到是否有虚拟导航，反射得到）
3 有的有虚拟导航，但是还可以开关，判断是否虚拟导航栏打开了

解决方案：`NavigationBarHeight= 整个屏幕的高度 - 内容部分view的高度，然后判断是否>0`
