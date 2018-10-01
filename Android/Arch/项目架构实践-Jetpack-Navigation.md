# Navigation

Navigation 组件简化了 Android 应用程序中导航的实现。官方介绍：

>今天，我们宣布推出Navigation组件，作为构建您的应用内界面的框架，重点是让单 Activity 应用成为首选架构。利用Navigation组件对 Fragment 的原生支持，您可以获得架构组件的所有好处（例如生命周期和 ViewModel），同时让此组件为您处理 FragmentTransaction 的复杂性。此外，Navigation组件还可以让您声明我们为您处理的转场。它可以自动构建正确的“向上”和“返回”行为，包含对深层链接的完整支持，并提供了帮助程序，用于将导航关联到合适的 UI 小部件，例如抽屉式导航栏和底部导航。

---
## 1 Navigation 原则

- 应用应该有一个固定的起始目的地
- 有一个栈用于表示应用程序的“导航状态”，基于栈的导航具有 pop 和 push 操作
- **向上按钮**的功能不应该是退出你的应用
- 当系统“后退”按钮不会退出你的应用程序时，向上按钮的功能应与系统“后退”按钮相同
- 深度链接到目标或导航到相同的目标应产生相同的堆栈

---
## 2 使用 `navigation component` 实现导航

- 一个 `destination` 表示一个特定的界面
- 默认情况下，navigation 组件支持包含 Activity 和 Fragment 作为应用程序的目标，但是 navigation 组件支持开发者添加新的类型作为 destination
- 一系列的**destination**构成了一个 app 的**导航图**
- 除目的地之外，导航图在 destination 之间具有称为“行动”的连接。

### 构建 navigation 导航图

1. 生命 navigation 组件依赖
2. 在 res 下新建一个 natigation 文佳佳，然后新建导航文件，用一个 xml 来表示不同目标之前的层次关系和跳转行为
3. 开始编辑 navigation，AndroidStudio3.2 开始支持 navigation 的图形编辑

参考下面示例：
```xml
<navigation
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    app:startDestination="@id/searchFragment">

    <fragment
        android:id="@+id/searchFragment"
        android:name="com.android.example.github.ui.search.SearchFragment"
        android:label="SearchFragment">

        <action
            android:id="@+id/showRepo"
            destination="@+id/repoFragment"
            app:destination="@id/repoFragment"/>

    </fragment>

    <fragment
        android:id="@+id/repoFragment"
        android:name="com.android.example.github.ui.repo.RepoFragment"
        android:label="RepoFragment">
        <argument
            android:name="owner"
            app:type="string"/>
        <argument
            android:name="name"
            app:type="string"/>
        <action
            android:id="@+id/showUser"
            app:destination="@id/userFragment"/>
    </fragment>

    <fragment
        android:id="@+id/userFragment"
        android:name="com.android.example.github.ui.user.UserFragment"
        android:label="UserFragment">
        <argument
            android:name="login"
            app:type="string"/>
        <action
            android:id="@+id/showRepo"
            app:destination="@id/repoFragment"/>
    </fragment>

</navigation>
```

光有了 navigation 图还是不够的，需要添加一个 NavHostFragment 作为导航控制的实现。

### NavHostFragment

在一个 Activity 添加一个 NavHostFragment 作为导航控制器，添加方式如下：

```xml
?xml version="1.0" encoding="utf-8"?>
<android.support.constraint.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <fragment
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:id="@+id/my_nav_host_fragment"
        android:name="androidx.navigation.fragment.NavHostFragment"
        app:navGraph="@navigation/nav_graph"
        app:defaultNavHost="true"
        />

</android.support.constraint.ConstraintLayout>
```

- navGraph属性用于关联一个 navigation xml
- `defaultNavHost=true`：该属性可确保 NavHostFragment 拦截系统后退按钮。同时需要覆盖 `AppCompatActivity.onSupportNavigateUp（）`并调用 `NavController.navigateUp()`方法，该方法用于管理回退栈

```
override fun onSupportNavigateUp()
        = findNavController(R.id.nav_host_fragment).navigateUp()
```

#### 将 `destination` 绑定到UI小部件

NavController 类提供导航控制的功能，可以从下面静态方法获取到一个 NavController 实例：

- `NavHostFragment.findNavController(Fragment)`
- `Navigation.findNavController(Activity, @IdRes int viewId)`
- `Navigation.findNavController(View)`

获取到 NavController 后可以调用其  `navigate()` 方法导航到一个 destination，navigate 方法接收一个 Resource Id，Resource Id 可以是在 xml 中定义的目标ID 或者是一个 Action。除此之外，还可以通过 transitions 进行导航。

```
viewTransactionsButton.setOnClickListener { view ->
   view.findNavController().navigate(R.id.viewTransactionsAction)
}
```

Android 维持着一个回退栈，包含最后一个可见的目标，navigate 用于添加一个目标到栈中，而  `NavController.navigateUp()` 和 `NavController.popBackStack()` 方法用于出栈。

对于 Button，还可以使用 `Navigation` 类的 `createNavigateOnClickListener()`便捷方法导航到目标：

```
button.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.next_fragment, null))`
```

#### 将 `destination` 绑定到菜单驱动的 UI 组件

可以使用目标的 id 作为 XML 中导航抽屉或溢出菜单项的相同 id，将目标绑定到导航抽屉和溢出菜单。

```xml
//navigatoin
<fragment android:id="@+id/details_page_fragment"
     android:label="@string/details"
     android:name="com.example.android.myapp.DetailsFragment" />
//menu
<item
    android:id="@id/details_page_fragment"
    android:icon="@drawable/ic_details"
    android:title="@string/details" />
```

navigaton 组件包含一个 `NavigationUI` 类。这个类有几个静态方法，你可以使用这些方法连接菜单项和导航目的地。示例：

```
//NavigationView代表应用程序的标准导航菜单。菜单内容可以由菜单资源文件填充，NavigationView通常放置在DrawerLayout中
NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
NavigationUI.setupWithNavController(navigationView, navController);
```

### 在 `destination` 之间传递数据

在 xml 中定义 argument 元素表示需要传递的数据：

```xml
//xml
<argument android:name="amount" android:defaultValue=”0” />

//传递方
var bundle = bundleOf("amount" to amount)
view.findNavController().navigate(R.id.confirmationAction, bundle)

//接收方
val tv = view.findViewById(R.id.textViewAmount)
tv.text = arguments.getString("amount")
```

### 以类型安全的方式在 `destination` 之间传递数据

`navigation component` 有一个 Gradle 插件 `safeargs`，这个插件可以生成一些模板代码(简单对象和构建器)用于在不同的 destination 之间传递数据，我们只需要在 gradle 脚本中应用这个插件即可：

```
//插件地址
classpath 'android.arch.navigation:navigation-safe-args-gradle-plugin:1.0.0-alpha01'
//应用插件
apply plugin: 'androidx.navigation.safeargs'
```

示例代码：

```xml
//xml
<fragment
    android:id="@+id/confirmationFragment"
    android:name="com.example.buybuddy.buybuddy.ConfirmationFragment"
    android:label="fragment_confirmation"
    tools:layout="@layout/fragment_confirmation">
    <argument android:name="amount" android:defaultValue="1" app:type="integer"/>
</fragment>

//传递方
@Override
public void onClick(View view) {
   EditText amountTv = (EditText) getView().findViewById(R.id.editTextAmount);
   int amount = Integer.parseInt(amountTv.getText().toString());
   ConfirmationAction action =
           SpecifyAmountFragmentDirections.confirmationAction()
   action.setAmount(amount)
   Navigation.findNavController(view).navigate(action);
}

//接收方
@Override
public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    TextView tv = view.findViewById(R.id.textViewAmount);
    int amount = ConfirmationFragmentArgs.fromBundle(getArguments()).getAmount();
    tv.setText(amount + "")
}
```

### 将 destinations 组合到一个嵌套的导航图中

一系列 destinations 可以在导航图中组成一个子图。子图称为“嵌套图”，而包含的图称为“根图”。嵌套图形可用于组织和重用应用程序UI的各个部分，例如单独的登录流。“嵌套图”同样需要定义`起始 destinations`

```xml
<?xml version="1.0" encoding="utf-8"?>
<navigation xmlns:app="http://schemas.android.com/apk/res-auto"
   xmlns:tools="http://schemas.android.com/tools"
   xmlns:android="http://schemas.android.com/apk/res/android"
   app:startDestination="@id/mainFragment">

   <fragment
       android:id="@+id/mainFragment"
       android:name="com.example.cashdog.cashdog.MainFragment"
       android:label="fragment_main"
       tools:layout="@layout/fragment_main" >
       <action
           android:id="@+id/action_mainFragment_to_chooseRecipient"
           app:destination="@id/sendMoneyGraph" />
       <action
           android:id="@+id/action_mainFragment_to_viewBalanceFragment"
           app:destination="@id/viewBalanceFragment" />
   </fragment>

   <fragment
       android:id="@+id/viewBalanceFragment"
       android:name="com.example.cashdog.cashdog.ViewBalanceFragment"
       android:label="fragment_view_balance"
       tools:layout="@layout/fragment_view_balance" />

   <navigation android:id="@+id/sendMoneyGraph" app:startDestination="@id/chooseRecipient">
       <fragment
           android:id="@+id/chooseRecipient"
           android:name="com.example.cashdog.cashdog.ChooseRecipient"
           android:label="fragment_choose_recipient"
           tools:layout="@layout/fragment_choose_recipient">
           <action
               android:id="@+id/action_chooseRecipient_to_chooseAmountFragment"
               app:destination="@id/chooseAmountFragment" />
       </fragment>
       <fragment
           android:id="@+id/chooseAmountFragment"
           android:name="com.example.cashdog.cashdog.ChooseAmountFragment"
           android:label="fragment_choose_amount"
           tools:layout="@layout/fragment_choose_amount" />
   </navigation>
</navigation>
```

导航到子图

```
Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_sendMoneyGraph);
```

### 为 destination 分配 deep link

`navigation component` 也对 deep link 提供了支持，可以直接导航到某个 Activity 中的 Fragment。


### 为 destinations 之间的转换创建动画

`navigation component` 提供了在 destination 之间轻松添加转换的功能，例如淡入和淡出。例如：

```xml
<fragment
    android:id="@+id/specifyAmountFragment"
    android:name="com.example.buybuddy.buybuddy.SpecifyAmountFragment"
    android:label="fragment_specify_amount"
    tools:layout="@layout/fragment_specify_amount">

    <action
        android:id="@+id/confirmationAction"
        app:destination="@id/confirmationFragment"
        app:enterAnim="@anim/slide_in_right"
        app:exitAnim="@anim/slide_out_left"
        app:popEnterAnim="@anim/slide_in_left"
        app:popExitAnim="@anim/slide_out_right" />

 </fragment>
```


---
## 3 迁移到 `navigation component`

NavController及其导航图包含在单个 Activity 中。因此，在迁移现有项目以使用 `navigation component` 时，通过为每个 Activity 中的目标创建导航图，一次集中一次迁移一个 Activity。

---
## 4 添加对新 destination 类型的支持

NavControllers 依赖一个或多个 Navigator 对象来执行导航操作。默认情况下，所有 NavController 支持通过使用 `ActivityNavigator` 及其嵌套的 `ActivityNavigator.Destination` 导航到另一个活动来离开导航图。为了能够导航到任何其他类型的 destination，必须将一个或多个附加的 Navigator 对象添加到 NavController中，例如，当使用 Fragment 作为 destination 时，NavHostFragment 自动将 FragmentNavigator 添加到其NavController。要将新的 Navigator 对象添加到 NavController，必须使用相应的 Navigator 类的`getNavigatorProvider()` 方法，然后使用该类的 `addNavigator()` 方法。

```
CustomNavigator customNavigator = new CustomNavigator();
navController.getNavigatorProvider().addNavigator(customNavigator);
```

>Navigator 对象是导航行为的执行者

---
## 5 实现有条件的导航

比如某些 destination 要求用户已经登录才能进入。

---
## 6 为多个 UI 元素定义公共 destination

可以使用全局 action 来标识可以通过多个UI元素访问的公共 destination。比如，可能在几个不同 destination 上的取消按钮都需要导航到相同的主应用程序屏幕。

navigation:

```xml
    <fragment
        android:id="@+id/mainFragment"
        android:name="com.ztiany.androidstudio32.ui.main.MainFragment"
        android:label="main_fragment"
        tools:layout="@layout/main_fragment">
    </fragment>
    <!-- fragment外面定义action -->
    <action
         android:id="@+id/action_global_mainFragment"
         app:destination="@id/mainFragment"/>
```

代码：

```java
//使用全局action
viewTransactionsButton.setOnClickListener(new View.OnClickListener() {
   @Override
   public void onClick(View view) {
       Navigation.findNavController(view).navigate(R.id.action_global_mainFragment);
   }
});
```

---
## 7 总结

- 使用 `navigation component` 操作 Fragment 进出堆栈时，其生命周期与 `attach/detach` 操作相同。
- 如何向导航图中默认的 Fragment 传递数据？Navigator 组件没有提供此类 API，通过 Activity 或者注入的方式。
- Navigator 是  `navigation component` 中的抽象类，抽取了导航的抽象行为，现在组件提供了 ActivityNavigator 和 FragmentNavigator。
- 从 Navigator 的设计来看，Navigator 不仅仅是为 Fragment 提供管理的，Navigator 把导航目的抽象为了 NavDestination 类，NavDestination 有不同更多实现，分别针对不同的导航目的地。

其他文章：

- [The Navigation Architecture Component](https://developer.android.com/topic/libraries/architecture/navigation/)
- [Android官方架构组件Navigation：大巧不工的Fragment管理框架](https://mp.weixin.qq.com/s?__biz=MzIwMTAzMTMxMg==&mid=2649492677&idx=1&sn=4d04f1045a01131d8bef185f625e2745&chksm=8eec873ab99b0e2cf8835f2a5b43f0c8aee4c7debb277895d850e8d12a3168a1cf2413d87161&scene=38#wechat_redirect)
- [google codelabs](https://codelabs.developers.google.com/)，搜索 navigation 可查询 navigation 相关代码