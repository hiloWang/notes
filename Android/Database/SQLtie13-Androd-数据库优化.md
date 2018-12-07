# Android 数据库优化

---
## 1 根据选择使用包装类型还是基本类型

在定义数据库实体时，使用包装类型，有以下特点：

- 可以存放 null，null 值具有特殊的意义，在某些情况下让表的操作更加方便。（好处）
- 把 null 的整形包装类型赋值给基本类型时，会参数空指针异常。（缺陷）
- 在 Long 型 id 作为对象是否相等判断依据时，用在集合中容易出现垃圾数据（equals方法的实现）。（缺陷）
- 不能用于两个 Long 对象双等(`==`)的方式来判断两个对象的值相等，因为比较的是地址，会出现错误的。（缺陷）

在定义数据库实体时，使用基本类型，有以下特点：

- 计算效率高，因为是基本类型。（好处）
- 缺少了 null 所能表达的特殊含义。（缺陷）


---
## 2 并发环境下 android sqlite数据库操作

可能存在的异常：

- `SQLiteDatabaseLockedException: database is locked`，原因，多个线程打开了多个数据库连接，同时写数据，SQLite3是基于文件锁的，其对于并发的处理机制是允许同一个进程的多个线程同时读取一个数据库，但是任何时刻只允许一个线程/进程写入数据库。解决方案是多个线程共享一个全局的数据库连接。
- `java.lang.IllegalStateException: attempt to re-open an already-closed object`，多线程条件下，A 线程获取数据库实例对数据库进行操作，同时 B 线程也拿到了该数据库实例，加如当 A 线程操作完数据库后，关闭了数据库链接，而此时 B 线程还在使用数据库实例进行数据库操作，就会抛出次异常，解决方法可以使用`同步+引用计数`方法进行控制。

但是，这种情况下，我们必须规范，必须使用 DatabaseManager 进行 SQLiteDatabase 的关闭操作：

```java
public class DatabaseManager {

    private int mOpenCounter = 0;
    private volatile static DatabaseManager instance;
    private static DBHelper mDatabaseHelper;
    private SQLiteDatabase mDatabase;

    private DatabaseManager() {
        mDatabaseHelper = new DBHelper(App.getApp());
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            synchronized (DatabaseManager.class) {
                if (instance == null) {
                    instance = new DatabaseManager();
                }
            }
        }
        return instance;
    }

    public synchronized SQLiteDatabase openDatabase() {
        if (mOpenCounter == 0) {
            // Opening new database
            mDatabase = mDatabaseHelper.getWritableDatabase();
        }
        mOpenCounter++;//引用加1
        return mDatabase;
    }

    public synchronized void closeDatabase() {
        mOpenCounter--;
        if (mOpenCounter == 0) {
            // Closing database
            mDatabase.close();
        }
    }

}
```

### ENABLE_WRITE_AHEAD_LOGGING

SQLiteDataBase 在 API 11 引入了 ENABLE_WRITE_AHEAD_LOGGING 属性，对应的方法为 `enableWriteAheadLogging`，其作用为开启 WAL 模式，即允许从同一数据库上的多个线程（使用各自的数据库连接）并行执行查询。它通过打开与数据库的多个连接并为每个查询使用不同的数据库连接来完成此操作。还更改了数据库日志模式，以使写入与读取同时进行。实现原理是写操作其实是在一个单独的log文件，读操作读的是原数据文件，是写操作开始之前的内容，从而互不影响。当写操作结束后读操作将察觉到新数据库的状态。当然这样做的弊端是将消耗更多的内存空间。这有点类似于 CopyOnWrite 机制。

---
## 3 避免危险的 SQLite 操作

参考[深入SQLite，一网打尽“危险操作”](https://zhuanlan.zhihu.com/p/36218222)。

SQLite 的特点：

- 嵌入式的数据库，没有单独的进程。
- SQLite 的读写操作基于原始的磁盘文件操作
- SQLite 的操作可能存在一定的异常，甚至可能导致 DB 文件损坏

可能存在的异常：

- `SQLiteDatabaseLockedException`：DB引擎在执行job的时候发现获取不到数据库的锁就会抛出这个异常。所以应该：
    - 只使用一个 SQLOpenHelper 来访问和操作 database
- **文件锁**：SQLite 默认的锁是协同锁。假设有 A、B 线程同时访问数据库并写入数据。然后有一个线程C 不是使用数据库提供的 API 来操作数据库文件，这个时候数据库的锁会被取消，此时数据文件同时被A、B操作，可能导致文件损坏。所以应该:
    - 尽量通过数据库 API 来操作数据库
    - 如果由IO线程操作数据库，应该保证此时没有其他线程操作通过API操作数据库
- **文件sync失败**：避免使用`PRAGMA synchronous=OFF`。
- **多db操作**：包装一个 DB 对应一个 DataBaseOpenHelper，避免 DataBaseOpenHelper 跨 DB 操作。

---
## 4 其他优化操作

- 为某些查询频繁的列创建索引
- 使用事务
- 使用 SQLiteStatement 预编译 SQL 语句
- 只查询需要的数据
- 提前获取列索引，而不是通过`cursor.getColumnIndex()`
- 评估好 `ContentValues` 的容量，避免扩容

## 引用

- [性能优化之数据库优化](http://www.trinea.cn/android/database-performance/)
- [Android 中 SQLite 性能优化](https://droidyue.com/blog/2015/12/13/android-sqlite-tuning/)
- [Android SQLite 性能优化](http://www.mricefox.com/2016/03/28/android-sqlite-optimize/)