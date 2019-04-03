# ACC组件介绍

---
## 1 Lifecycle

**Lifecycle** 是生命周期感知组件，提供了对Android组件(Activity、Service)和Fragment的生命周期的感知(监听)能力。其他类型通过注解(或者定义接口)的方式来感知Android组件的生命周期变化。

LifecycleOwner表示一个具有Android生命周期的类。通过其返回的 Lifecycle 对象可以注册一个LifecycleObserver，还可以通过Lifecycle获取组件当前的状态，LifecycleOwner的实现有：

- LifecycleService
- Fragment(V4)
- SupportActivity

```java
getLifecycle().addObserver(new LifecycleObserver() {
             ......
});
```

LifecycleObserver表示一个生命周期的监听者，可以在LifecycleObserver的子类实现中定义需要的方法，然后加上 OnLifecycleEvent 注解来监听不同的生命周期事件。**Lifecycle-Aware Components** 会在编译的期利用 APT 生成对应的代码实现，如果使用 AndroidStudio3 中的Java8 功能，可以使用针对java8提供的 `DefaultLifecycleObserver`，这样可以避免使用APT。


### lifecycle-aware components最佳实践

- 保持UI的组件的精简，不要在UI组件中去获取它们的数据，而是使用 ViewModel，并观察一个LiveData对象以响应数据变化来更新视图。
- 尝试编写数据驱动的用户界面，其中用户界面控制器的责任是更新数据更改视图，或通知用户操作返回到ViewModel。
- 把数据逻辑放到ViewModel类中，ViewModel应该作为UI控制器和其他应用程序之间的连接器，但是ViewModel不负责提取数据，而是调用相应的组件(比如Repository)来获取数据，然后将结果提供给UI控制器
- 使用Databinding库来避免各种UI模板代码，保持整体代码的整洁。(实践中发现 Databinding 在复杂的项目编译构建过程中有很多问题)
- 如果 UI 很复杂，可以考虑创建一个 Presenter 类来处理UI的修改。
- 避免在 ViewModel 中引用 View 或 Activity 上下文。

>查看SupportActivity和SupportActivity的源码，发现并没有显式的回调它们的生命周期事件，可以通过反编译来查看具体的实现细节。

---
## 2 LiveData

LiveData 是一个可观察的数据持有者类，LiveData 是生命周期感知的,LiveData 只更新处于活跃生命周期状态的应用程序组件观察者。所谓的活跃的生命周期状态是指：Observer 的 Lifecycle 处于 `STARTED` 或 `RESUMED` 状态。

### LiveData的优点

- 确保用户界面符合你的数据状态
- 确保没有内存泄漏
- 不会因停止活动而导致崩溃，如果观察者的生命周期处于非活动状态，例如在后退堆栈中的活动，则不会收到任何 `LiveData` 事件。
- 不需要开发者手动的去管理生命周期。
- 始终保持最新的数据：如果一个组件生命周期变为非活动状态，则在其再次变为活动状态时将收到最新数据。
- 如果由于配置更改（如设备旋转）而重新创建 Activit 或 Fragment，则会立即收到最新的可用数据。
- 共享资源：可以使用单例模式扩展 LiveData 对象以包装系统服务，以便可以在应用程序中共享这些服务。

### 使用LiveData对象

1. 一般是在 ViewModel 中，创建一个 LiveData 对象
2. 通常在UI控制器（Activity 或 fragment）中创建一个Observer对象。来观察 ViewModel 提供的 LiveData
3. 一般情况下，应该调用 LiveData 的 observe 方法来把观察者附加到LiveData对象中，observe方法需要一个LifecycleOwner 对象管管理生命周期
4. 更新 LiveData 对象中存储的值时，只要附加的 LifecycleOwner 处于活动状态，就会触发所有注册的观察者。

>可以使用`observeForever(Observer)`方法注册一个没有关联的LifecycleOwner对象的观察者，这就是一个没有生命周期感知的观察，在需要的时候应该调用`removeObserver(Observer)`方法来移除观察者。

**注意**：确保在ViewModel对象中存储更新 UI 的 LiveData 对象，而不是Activity或Fragment中，原因如下：

- 避免臃肿的 Activity 或 Fragment。
- 确保 Activit y或 Fragment 具有一旦变为活动状态即可显示的数据。将 LiveData 实例与特定 Activity 或 Fragment 实例分离，并允许 LiveData 对象在配置更改后保留下来。

### 观察LiveData对象

在大多数情况下，应用程序组件的 `onCreate()` 方法是开始观察 LiveData 对象的正确位置：原因如下

1. 确保系统不会从 Activity 或 Fragment 的onResume() 方法进行多余的调用。
2. 确保 Activity 或 Fragment 一旦变为活动状态即可显示的数据。也就是应当尽早的观察 LiveData。
3. 但是同时应该保证当LiveData发送数据时，组件的View已经完成了初始化。

```java
public class NameActivity extends AppCompatActivity {

    private NameViewModel mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Other code to setup the activity...
        // Get the ViewModel.
        mModel = ViewModelProviders.of(this).get(NameViewModel.class);

        // Create the observer which updates the UI.
        final Observer<String> nameObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String newName) {
                // Update the UI, in this case, a TextView.
                mNameTextView.setText(newName);
            }
        };

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        mModel.getCurrentName().observe(this, nameObserver);
    }
}
```

### 更新LiveData对象

LiveData 并没有更新数据的方法，其子类 MutableLiveData 类公开公开 `setValue(T)` 和 `postValue(T)` 方法，如果需要编辑存储在 LiveData 对象中的值，则必须使用这些方法。一般情况下 MutableLiveData 类型应该只在 ViewModel 中，而 ViewMode l只暴露LiveDta类型给UI组件。UI组件应该通过 ViewModel 暴露的方法来更新UI数据到 ViewModel 中。

>postValue(T)用于从工作线程更新数据

### 扩展LiveData

如果观察者的生命周期处于 `STARTED` 或 `RESUMED` 状态，则 LiveData 将认为观察者处于活动状态。扩展一个LiveData需要考虑它的两个方法：

```java
    protected void onActive() {
    }

    protected void onInactive() {
    }
```

- 当LiveData对象有一个活动的观察者时，`onActive()`方法被调用。
- 当LiveData对象没有任何活动的观察者时，`onInactive()`方法被调用。由于没有观察员在监听

如果一个 LiveData 需要与多个 Fragment 或 Activity 交互，则可以构建一个全局的 LiveData 对象。

### 转换LiveData

Lifecycle 包提供了 `Transformations` 类来满足某些场景下的针对 LiveData 所持有数据类型的转换需求。比如：

- 可能希望对存储在 LiveData 对象中的值进行更改，然后将其分配给观察者。
- 可能需要根据另一个 LiveData 实例返回不同的 LiveData 实例。

Transformations有两个方法：

- 1 `Transformations.map()`方法：在存储在LiveData对象中的值上应用一个函数，并向下传播结果。这个很好理解，函数式编程中map非常常见。

```java
 LiveData<User> userLiveData = ...;
 LiveData<String> userName = Transformations.map(userLiveData, user -> {
      user.name + " " + user.lastName
  });
```

- 2 `Transformations.switchMap()`方法：响应一个LiveData对象中的值的变化，返回根据这个值产生的新的LiveData

```java
private LiveData<User> getUser(String id) {
      ...;
  }

LiveData<String> userId = ...;
LiveData<User> user = Transformations.switchMap(userId, id -> getUser(id) );
```

使用这些转换允许在整个调用链中携带观察者的 Lifecycle 信息，以便只有在观察者观察到 LiveData 的返回时才运算这些转换。转换的这种惰性运算性质允许隐式的传递生命周期相关行为，而不必添加显式的调用或依赖。

如果需要实现自己的 Transformations，可以使用 MediatorLiveData 类，该类监听其他LiveData对象并处理它们发出的事件。

**switchMap示例**：postalCode用于提供给UI来观察数据的变化，UI可以用户输入通过ViewModel的setInput方法设置给addressInput，当addressInput变化后，如果有处于活动状态的观察者，会触发repository的getPostCode()方法。这样UI就只需要绑定一次LiveData。这样做的好处让UI重置时可以复用之前的状态，而不总是触发repository查询。**所以：每当你认为在 ViewModel 中需要一个 Lifecycle 类时，switchMap示例可能是解决方案。**

```java
class MyViewModel extends ViewModel {

    private final PostalCodeRepository repository;

    private final MutableLiveData<String> addressInput = new MutableLiveData();

    public final LiveData<String> postalCode =
            Transformations.switchMap(addressInput, (address) -> {
                return repository.getPostCode(address);
             });

  public MyViewModel(PostalCodeRepository repository) {
      this.repository = repository
  }

  private void setInput(String address) {
      addressInput.setValue(address);
  }
}
```


### 合并多个LiveData源

MediatorLiveData 是LiveData 的一个子类，允许开发者合并多个 LiveData 源，MediatorLiveData 对象的观察者随后在任何原始的 LiveData 源对象改变时被触发。例如，如果在UI中有一个可从本地数据库或网络更新的LiveData 对象，则可以将以下数据源添加到 MediatorLiveData 对象：

- 与本地数据关联的 LiveData
- 与网络数据关联的 LiveData

而 Activity 或 Fragment 只需观察 MediatorLiveData 对象即可接收来自两个来源的更新。

### Room 与 Live

数据库组件 Room 支持返回 LiveData 保证的数据。

### LiveDataReactiveStreams

LiveDataReactiveStreams 可以将 LiveData 输入和输出调整为 ReactiveStreams 规范。提供的方法如下：

- `static <T> LiveData<T> fromPublisher(Publisher<T> publisher)`：根据Publisher创建一个LiveData。当LiveData变为活动状态时，它开始订阅Publisher。因此，对于一个热的Observable，当添加新的LiveData 观察者时，它将自动通知LiveData中保存的最后一个值，该值可能不是发布者发出的最后一个值。LiveData不会处理异常，并且它期望错误体现在数据中(用数据表示状态)，如果发生一个异常，则异常将被传播到主线程并且应用程序将崩溃。
- `static <T> Publisher<T> toPublisher(LifecycleOwner lifecycle, LiveData<T> liveData)`：将给定的LiveData流调整为ReactiveStreams规范的Publisher。订阅Publisher时，观察者将附加到给定的LiveData。

---
## 3 ViweModel

ViewModel 类旨在以一种有生命周期的方式**存储和管理**与UI相关的数据。 ViewModel 类允许数据在屏幕旋转等配置更改后继续保持。

Android 框架管理 UI 控制器的生命周期，比如 Activities和 Fragments，系统会决定销毁或重新创建 UI 控制器，以响应完全不受开发者控制的某些用户操作或设备事件。如果UI组件销毁和重建的过程导致已有数据的丢失(比如一个列表或用户的输入)，这是很不好的体验，如果是小量的数据(比如一个 userId)可以使用 onSaveInstanceState()方法来保持，但是如果是一个数据量较大的列表则不能这么做。另一个问题是 UI 控制器经常需要进行异步调用，这可能需要一些时间才能返回，UI 控制器需要管理这些调用，并确保系统在销毁后清理它们以避免潜在的内存泄漏。这种管理需要大量的维护，并且在对配置更改重新创建对象的情况下，由于对象可能不得不重新发出已经做出的调用，所以这是浪费资源。

>这里可以理解为：当UI配置改变时，ViewModel 会被保存下来，并且 ViewModel 的异步调用继续，并且避免内存泄漏，当 UI 配置改变完成，可以直接使用之前 ViewModel 中的异步调用，从而避免每次UI配置改变都要执行异步调用。

总而言之，UI 控制器应该尽量保持简洁，只用来处理用户响应和系统特性相关的事件，如果把所有的逻辑都放在 UI 控制器中则会造成该类的异常臃肿和难以维护，且无法进行有效的测试。

### 实现一个ViewModel

ViewModel对象在配置更改期间自动保留，以便它们保存的数据立即可用于下一个活动或片段实例。

参考下面代码：

```java
//ViewModel用于保持和获取数据
public class MyViewModel extends ViewModel {

    private MutableLiveData<List<User>> users;
    public LiveData<List<User>> getUsers() {
        if (users == null) {
            users = new MutableLiveData<List<Users>>();
            loadUsers();
        }
        return users;
    }

    private void loadUsers() {
        // Do an asyncronous operation to fetch users.
    }
}

//在Activity中使用ViewModel获取列表数据
public class MyActivity extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState) {
        // Create a ViewModel the first time the system calls an activity's onCreate() method.
        // Re-created activities receive the same MyViewModel instance created by the first activity.

        MyViewModel model = ViewModelProviders.of(this).get(MyViewModel.class);
        model.getUsers().observe(this, users -> {
            // update UI
        });
    }
}
```

在上面例子中，如果重新创建 Activity，它将接收由第一个 Activity 创建的相同的 MyViewModel 实例。(MyViewModel 实例和它持有的数据都将得到保持)
当所有 Activity 结束后，框架会调用 ViewModel 对象的 `onCleared()` 方法，以便它可以清理资源。

### ViewModel实践

- ViewModel 能在重新创建 Activity 后继续保持不被销毁
- ViewModel 用于存储UI需要的数据
- ViewModel 对象被设计用于生存视图或 LifecycleOwners 的具体实例
- ViewModel 对象绝不能观察对生命周期感知的观察对象的更改，比如观察LiveData
- ViewModel 绝不能引用 Activity、Fragment 或任何可能持有对活动上下文的引用的类
- 如果 ViewModel 需要 Application 上下文，例如找到一个系统服务，它可以继承 **AndroidViewModel** 类，并有一个构造函数来接收 Application 对象

### ViewModel的生命周期

ViewModel 对象的范围是在获取 ViewModel 时传递给 ViewModelProvider 类的 Lifecycle 决定的。直到Lifecycle 的范围永久消失之前。ViewModel 都会保留在内存中，一个Activivy的 `finish()`、一个Fragment的 `detached` 都表示一个 Lifecycle 的范围永久消失。

![](index_files/viewmodel-lifecycle.png)

>Activity 的 `recreate()` 方法不会导致 ViewModel 的销毁

### 在 Fragment 之间共享 ViewModel

在一个Activity中的多个 Fragment 通讯是非常常见的，比如一个主 Fragment 用于显示一个列表，当 Item 被点击后另一个 Fragment 被加载用于
展示 Item 详情，通常的做法是使用接口进行通讯，而且 Activity 还需要负责连接这两个 Fragment，这其实是非常繁琐的，而利用 ViewModel 则可以很好的解决这个问题。参考下面代码：

```java
public class SharedViewModel extends ViewModel {
    private final MutableLiveData<Item> selected = new MutableLiveData<Item>();

    public void select(Item item) {
        selected.setValue(item);
    }

    public LiveData<Item> getSelected() {
        return selected;
    }
}

public class MasterFragment extends Fragment {
    private SharedViewModel model;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //通过Activity来获取共享的SharedViewModel
        model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        itemSelector.setOnClickListener(item -> {
            model.select(item);
        });
    }
}

public class DetailFragment extends Fragment {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //通过Activity来获取共享的SharedViewModel
        SharedViewModel model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        model.getSelected().observe(this, { item ->
           // Update the UI.
        });
    }
}
```

**注意：在Fragment 中是通过 ViewModelProviders 传入它们的 Activity 来获取ViewModel**

这种方法提供了以下好处：

- 这个 Activity 不需要做任何事情
- 避免定义繁复的接口通讯
- 除了 SharedViewModel 之外，Fragment 不需要了解彼此。如果其中一个 Fragment 消失，另一个片断继续照常工作。
- 每个 Fragment 都有自己的生命周期，不受其他生命周期的影响。如果一个 Fragment 替换另一个片段，则 UI 继续工作而没有任何问题

---
## 4 保存用户的状态

当因为配置改变或者系统重启(内存不足时)Activity时，用户希望界面恢复到它们期望的状态，这在提升用户体验上是很重要的一点，保持与恢复界面状态可以分为一下几点：

- 对于简单的数据(userId)可以使用 onSaveInstanceState 方法来保存
- 对于复杂的数据(列表或Bitmap)应该使用 ViewModel 来保存，同时也可使用 onSaveInstanceState 配合ViewModel 来保存界面状态，比如使用 onSaveInstanceState 来保存用于加载数据的 key，然后再把对于的 key 传递给 ViewModel

---
## 5 Room

Room 提供了一个 SQLite 的抽象层，以便在充分利用 SQLite 的同时流畅的访问数据库。Room 中的三个概念：

- Database：可以使用此组件创建数据库的持有者，通过获取获取 Dao。
- Dao：该组件表示一个数据访问对象（DAO）的类或接口，通过Dao可以操作数据库。
- Entity：该组件表示一个保存有数据库行的类。

Room教程可以参考[官方文档](https://developer.android.com/training/data-storage/room/index.html)

---
## 6 Paging Library

Paging 使程序更容易从数据源逐步加载所需的信息，而不会使设备过载或等待太长的加载时间。许多 App 在显示列表数据时都需要进行分页加载，虽然现有的 Android API 允许在内容中进行分页，但它们带来了明显的限制和缺陷：

- **CursorAdapter** 将数据库查询结果映射到 ListView 项中，但它在 UI 线程上运行数据库查询
- **AsyncListUtil** 允许将基于位置的数据分页展示到 RecyclerView 中，但不允许进行非定位分页，并强制在可数数据集中使用空值作为占位符。

而 Paging Library 有以下优点：

- 数据请求消耗更少的网络带宽和更少的系统资源。
- 即使在数据更新和刷新期间，应用程序也会继续快速响应用户输入。

### 6.1 Paging 库概览

分页库的关键组件是 PagedList类，这是一个异步加载的数据块或页面的集合。PagedList 类作为中介连接着各层。

- **数据层**：PagedList 的每个实例都会从其对应的 DataSource 中加载应用数据的最新快照。数据从应用程序的后端或数据库流入 PagedList 对象。Paging 支持各种应用体系结构，包括独立数据库和与后端服务器通信的数据库。
- **UI层**：PagedList 类与 PagedListAdapter 一起使用将数据加载到 RecyclerView 中。

Paging 实现了观察者模式。基于 DataSource 可以创建一个可以观察的 `LiveData <PagedList>`（或基于RxJava2的相同类）的数据实例，然后UI层可以通过观察这个可观察的示例来更新UI。

#### 定义如何获取数据(DataSource)

DataSource用于定义你需要从中提取分页数据的数据源，根据需要访问数据的方式，可以扩展其子类:

 - PageKeyedDataSource：适用于进行 上一下/下一页 切换的加载方式。根据上一页请求返回的**key**来加载下一页数据的场景
 - ItemKeyedDataSource：用于需要使用项目第`N`个的数据来获取项目`N + 1`的数据的情景。比如根据列表最后一条数据的id加载更多的列表数据。
 - PositionalDataSource：根据 position 加载数据，适用于固定 size 的数据源

如果使用Room来操作数据库，可以在 Dao 直接返回 DataSource 的工厂对象 Factory，Room 知道如果生成Factory 并返回

```
@Query("select * from users WHERE age > :age order by name DESC, id ASC")DataSource.Factory<Integer, User> usersOlderThan(int age);
```

#### 将数据加载到内存中(PagedList)

PagedList 用于从一个 DataSource 中加载数据。可以配置一次加载多少数据，以及应该预加载多少数据，以便最大限度地减少用户等待数据加载的时间。这个类可以向其他类提供更新信号，比如 RecyclerView.Adapter，允许在页面中加载数据时更新 RecyclerView 的内容。

#### 将数据展示到 UI(PagedListAdapter)

PagedListAdapter 是 RecyclerView.Adapter 的一个实现，用于展示 PagedList 中的数据。当一个新的页面被加载时，PagedListAdapter 向 RecyclerView发送数据已经到达的通知，PagedListAdapter 使用后台线程来计算(使用 DiffUtil)从一个 PagedList 到下一个的变化

>PagedListAdapter 继承自 RecyclerView.Adapter，通过监听适配器的方法调用(比如getItem)来判断是否应该加载更多 page。PagedListAdapter的创建需要提供一个 DiffUtil 的 Callback 或 AsyncDifferConfig 对象，用于判断列表中的Item是需要更新。

#### 观察数据的更新(LivePagedListBuilder)

PagingLibrary提供以下类来构建能够实时更新的PagedList容器：

- LivePagedListBuilder：使用LiveData包装的PagedList
- RxPagedListBuilder：使用 RxJava的 Flowable 包装的 PagedList

如果使用Room库的话，它支持自动生成 `DataSource.Factory` (这个Factory可以创建PositionalDataSource实例)实现，然后通过上面提供的构建器生成可观察的PagedList容器：

```java
LiveData<PagedList<Item>> pagedItems =
        LivePagedListBuilder(myDataSource, /* page size */ 50)
                .setFetchExecutor(myNetworkExecutor)
                .build();

//需要paging:rxjava2库
Flowable<PagedList<Item>> pagedItems =
        RxPagedListBuilder(myDataSource, /* page size */ 50)
                .setFetchScheduler(myNetworkScheduler)
                .buildFlowable(BackpressureStrategy.LATEST);
```

当我们创建了 PagedList 容器容器后，就可以将其提供给 UI：

```java
   //viewModel.allCheeses返回个ListData<PagedList<Object>>
   viewModel.allCheeses.observe(this, Observer {
            printPageInfo(it)
            //调用PagedListAdapter的submitList方法提交PagdList
            adapter.submitList(it)
        })
```

### 6.2 数据流

![](index_files/paging-threading.gif)
>图片来自[官方文档](https://developer.android.com/topic/libraries/architecture/paging)

PagingLibrary 组织来自后台线程生成器的数据流，并在UI线程中呈现。比如，当一个新的数据被插入到数据库中，之前的 DataSource 将会被废弃，`LiveData<PagedList>` 或 `Flowable<PagedList>`将会在工作线程创建一个新的 PagedList。新的 PagedList 被提交给 PagedListAdapter，PagedListAdapter 通过 DiffUtil 来计算列表条目的变化，然后调用 `notifyItemInserted()` 等方法来更新列表 UI。


### 6.3 选择一个加载数据的架构

- 从网络或者本地加载数据：当网络或者数据库作为单一的数据源
- 从网络和本地加载数据：网络和数据库都是数据源，但是只从数据库加载数据，网络加载完成后自动插入数据库，数据库更新后通知上层数据有更新。


### 6.4 支持不同的数据架构

Paging 支持从不同的数据源加载数据的模式，比如，`只从数据库加载、只从网络加载、同时从数据库和网络加载数据`。

- 如果只从网络加载数据，注意分页库的 DataSource 对象不提供任何错误处理，因为不同的应用程序以不同的方式处理和呈现错误 UI。
- 如果只从数据加载数据，可以使用 Room，Room 支持生成 DataSouce.Factory 并且自动处理数据更新并触发通知

#### 从网络和数据库加载数据

从两个数据源加载数据时，推荐使用单一数据源模型，比如把数据库当作单一的数据源，而从网络加载回来的数据更新到数据库，然后数据库由触发数据更新通知，在开始观察数据库之后，可以使用 `PagedList.BoundaryCallback` 监听数据库何时数据不足。然后，可以从网络中获取更多项目并将其插入数据库。

如果UI层正在观察数据库：

```java
class ConcertViewModel {
    fun search(query: String): ConcertSearchResult {
        val boundaryCallback =
                ConcertBoundaryCallback(query, myService, myCache)
        // 错误处理未在此代码中展示。
        val networkErrors = boundaryCallback.networkErrors
    }
}

class ConcertBoundaryCallback(
        private val query: String,
        private val service: MyService,
        private val cache: MyLocalCache
) : PagedList.BoundaryCallback<Concert>() {
    override fun onZeroItemsLoaded() {
        requestAndSaveData(query)
    }

    override fun onItemAtEndLoaded(itemAtEnd: Concert) {
        requestAndSaveData(query)
    }
}
```

### 6.5 UI组件和注意事项

#### 连接UI到ViewModel

UI 通过在 ViewModel 中提供的 `LiveData<PagedList>` 观察列表的变化，然后将 PagedList 绑定到 `PagedListAdapter`。PagedListAdapter 使用 PagedList.Callback 对象处理页面加载事件，PagedListAdapter 调用`PagedList.loadAround()` 为底层的 PagedList 提供提示，告诉它应该从 DataSource 中获取哪些项目。PagedList 是不可变的对象，当列表数据发生变化时，底层会产生新的 PagedList，然后新的 PagedList 通过 LiveData 重新绑定到 `PagedListAdapter`。

#### 实现Item数据差异的回调

当新的 PagedList 被提交给 PagedListAdapter，PagedListAdapter 会自动计算新数据的变化，然后通知 RecycleView 更新 UI，但是数据是未知的，所以需要告诉 PagedListAdapter 如何判断两个数据对象是否相同，比如传入 `DiffUtil.ItemCallback `或 `AsyncDifferConfig`。

数据类可以实现equals方法，这样可以通过`Object.equals()` 方法来判断数据对象是否一致，在kotlin中，`==`相当于调用 `equals` 方法

#### 使用不同的适配器类型进行扩展

如果因为某种元素不能使用 `PagedListAdapter`，也可以使用 Paging 的分页功能，此时可以使用 Paging 提供的 `AsyncPagedListDiffer`。

#### 提供空Item的占位符

如果希望UI在应用完成抓取数据之前显示列表，则可以向用户显示占位符列表项。 RecyclerView 通过将列表项本身临时设置为null来处理这种情况。使用占位符的要求：

- 需要一个可数数据集
- 需要相同尺寸的项目视图大小


### 6.6 数据组件和注意事项

#### 创建一个可观察的列表

通常，我们的UI观察一个 LiveData (或 RxJava中的 Flowable/Observable)来响应数据的变化，LiveData 被保存在 ViewModel 中，ViewMode 连接着 UI 层和数据持久层。为了创建一个 PagedList 对象，需要传递一个 `DataSource.Factory` 到 `LivePagedListBuilder` 或者 `RxPagedListBuilder`，DataSource.Factory 顾名思义，用于创建 DataSouce 的工厂，DataSource 对象为单个PagedList加载页面，当数据源的内容发生变化(列发生改变、网络刷新)时会创建一个新的 PagedList 来响应内容的变化，如果使用数据库框架 Room，则其可以根据声明自动创建一个 `DataSource.Factory`，或者我们可以自己实现一个 `DataSource.Factory`。

代码示例：

```java
//ConcertDao.kt

interface ConcertDao {
    // Integer类型参数告诉Room使用PositionalDataSource 对象，基于位置的加载。
    @Query("SELECT * FROM concerts ORDER BY date DESC")
    public abstract DataSource.Factory<Integer, Concert> concertsByDate()
}

//ConcertViewModel.kt

// Integer类型参数对应于一个PositionalDataSource对象。
val myConcertDataSource : DataSource.Factory<Integer, Concert> =
       concertDao.concertsByDate()

val myPagedList = LivePagedListBuilder(myConcertDataSource, /* page size */ 20)
        .build()
```

#### 定义分页配置

Paging 的分页功能是可以配置的，我们可以创建一个 `PagedList.Config` 对象，然后传递给 LivePagedListBuilder，可配置的项如下：

- **page size**：每页中的项目数量
- **Prefetch distance**：预加载的距离，距离最后一个 Item 多远时预加载下一页或上一页的数据
- **Placeholder presence**：是否使用占位符，如果使用占位符，则一次性返回所有页面数据的快照，但只当前页面展示的数据是非空的。

除了上面的配置外，还可以提供一个 Executor 给 LivePagedListBuilder，该Executor用于执行加载任务。

代码示例：

```java
//EventViewModel.kt

val myPagingConfig = PagedList.Config.Builder()
        .setPageSize(50)
        .setPrefetchDistance(150)
        .setEnablePlaceholders(true)
        .build()

// Integer类型参数对应于一个PositionalDataSource对象。
val myConcertDataSource : DataSource.Factory<Integer, Concert> =
        concertDao.concertsByDate()

val myPagedList = LivePagedListBuilder(myConcertDataSource, myPagingConfig)
        .setFetchExecutor(myExecutor)
        .build()
```

#### 选择正确的DataSource类型

选择正确的 DataSource 类型是非常重要的，Paging 内置了几种选择 DataSource类型，适用于不同的场景：PageKeyedDataSource、ItemKeyedDataSource、PositionalDataSource。

#### 当数据过期时发送通知

当表或行已过期时，由数据层决定通知应用程序的其他层，可以调用 DataSource 的 `invalidate()` 方法。UI 层可以使用 Swipe to Refresh 来触发数据失效的功能。

#### 构建自己的DataSource

如果使用自定义的数据方，或者直接从本地或者网络加载数据，则可以选择继承已有的 DataSource 的某些子类型，比如下面代码：

```java
class MyDataSource : ItemKeyedDataSource<String, Item>() {
    override fun getKey(item: Item) = item.name

    //初始加载
    override fun loadInitial(
            params: LoadInitialParams<string>,
            callback: LoadInitialCallback<Item>) {
        val items = fetchItems(params.requestedLoadSize)
        callback.onResult(items)
    }

    //加载后面的
    override fun loadAfter(
            params: LoadParams<String>,
            callback: LoadCallback<Item>) {
        val items = fetchItemsAfter(
            start = params.key,
            limit = params.requestedLoadSize)
        callback.onResult(items)
    }
}
```

#### 考虑如何处理内容更新

在构建PagedList时，需要考虑如何处理列表内容更新，当使用 Room 时，数据更新会自动更新到 UI，而如果直接从网络加载数据，通常UI层会有一个交互行为，比如`swipe to refresh`，当触发更新时，表示发送一个表示废弃当前数据并加载新的的信号给下层。

```java
class ConcertActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        ...

        concertViewModel.refreshState.observe(this, Observer {
            swipeRefreshLayout.isRefreshing =
                    it == NetworkState.LOADING
        })
        swipeRefreshLayout.setOnRefreshListener {
            concertViewModel.invalidateDataSource()
        }
    }
}
```

#### 提供数据表示之间的映射

对于由DataSource加载的项目，Paging支持基于项目和基于页面的转换。比如一个 name 和一个 date 字段被映射成一个单一的String字段：

```java
class ConcertViewModel : ViewModel() {
    val concertDescriptions : LiveData<PagedList<String>>
        init {
            //Executor
            val factory = database.allConcertsFactory()
                    .map { concert ->
                           concert.name + " - " + concert.date
                    }
            concerts = LivePagedListBuilder(factory, 30).build()
        }
    }
}
```

如果想在加载后**对项目进行换行，转换或准备项目**，这可能很有用。由于这项工作是在Executor中执行的，因此可以耗时工作，例如从磁盘读取数据或查询单独的数据库。

---
## 7 ACC实践

ACC提供的不仅仅是组件，而是一套框架思想，指导我们如何构建一个稳定的、灵活的、可测试的应用。所以更重要的是去学习这些知道思想

- lifecycle-aware 组件现在与 Support Library  的组合在一起，利用 `lifecycle-aware`可以开发一些自动感知生命周期的组件，比如 AutoClearedValue，在生命周期结束时自动销毁其保存的变量
- LiveData 和 ViewModel 是 lifecycle-aware 的扩展，引入这个组件用来实现 MVVM 还是不错的选择
- 有些网络操作不应该随着界面的销毁而取消，比如一个点赞行为，但是如果不做特殊的判断这可能造成内存泄漏或者NPE，现在使用 LiveData 即可以很方便的处理这种情况，让 UI 组件通过 LiveData 监听把操作的结果，LiveData 能处理好生命周期相关的操作和判断
- Room是数据库组件，如果项目中使用了数据库可以选择引入 Room，而且 Room 支持与 LiveData 和 Paging集成
- Paging库提供了分页加载功能，但是不能应对复杂的加载情况，现在官方的 Sample 还有很多 issue，以下场景可以考虑使用 Paging 库。
   - 只能数据库分页加载数据
   - 从网络和数据加载数据，而网络数据上的数据是不可变的，Paging 从 DataSource 加载数据时，只有加载不到更多的数据时才会触发 BoundaryCallback 的回调，这样的话保证数据实在是最新的存在一定的难度
