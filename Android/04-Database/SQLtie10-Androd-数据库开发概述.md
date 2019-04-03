# Android数据库程序开发实践

---
## 1 Android数据库基本介绍

Android自带的数据库就是 SQLlite，由于 SQLite 是 C 和 C++ 实现的，因此 Android 在Framework 层封装了一层Java接口，使得开发人员可以更方便的操作数据库，相关类库：

- SQLiteOpenHelper
- SQLiteDatabase
- Cursor
- ContentObserver
- SQLiteStatement

---
## 2 数据库基本类型与接口

在 Android 中使用数据库，我们通常都会使用SQLiteOpenHelper类，该类封装了 SQLite 数据库的创建与升级工作，代码大致如下：

```java
    public class DBHelper extends SQLiteOpenHelper {
    
        private static final String TAG = DBHelper.class.getSimpleName();
        private static final String DB_NAME = "study.db";
        private static final int DB_VERSION = 1;
    
        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }
    
        public DBHelper(Context context, DatabaseErrorHandler errorHandler) {
            super(context, DB_NAME, null, DB_VERSION, errorHandler);
        }
    
        @Override
        public void onOpen(SQLiteDatabase db) {
            super.onOpen(db);
    
            db.execSQL(SqlHolder.OPEN_FOREIGN_KEY);
        }
    
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(SqlHolder.CREATE_CLASSES);
            sqLiteDatabase.execSQL(SqlHolder.CREATE_STUDENTS);
        }
    
        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
            switch (oldVersion) {
                case 1:{
                    //do sth
                    break;
                }
                case 2:{
                    // do sth
                    break;
                }
            }
        }
    }
```


---
## 3 插入数据

通过 SQLiteDAtebase 插入数据的接口为 insertWithOnConflict，它的声明如下：

```
public long insertWithOnConflict(String table, String nullColumnHack, ContentValues initialValues, int conflictAlgorithm)
```

- table 表示要操作的表名
- 参数2当参数3为空时将表中的字段置为null
- 参数3 表示表中各字段的键值对
- 参数4 插入数据发生冲突时如何解决

ContentValues的使用方式：

```
     ContentValues values = new ContentValues();
                values.put( "name", name);
                values.put( "phone", phone);
```

conflictAlgorithm 的可选值有六种，这些策略声明在 SQLiteDatabase 中：

- CONFLICT_ABORT 当执行sql语句违反限制条件时，会停止执行当前sql语句，并将数据恢复到操作之前的状态。不过当前事务下先执行sql语句造成的数据变动并不会受到影响
- CONFLICT_FAIL 当执行sql语句违反限制条件时，会停止执行当前sql语句，不过先前执行sql语句造成的数据变动并不会收到影响，而后面的sql语句不会执行
- CONFLICT_IGNORE 当插入或者修改内容是违反了限制条件时，那么这次数据将不会生效，但是后面的sql语句会被继续执行
- CONFLICT_NONE  当执行sql语句违反限制条件时，忽略该错误，继续执行后续的sql语句
- CONFLICT_REPLACE 当执行sql语句违反了唯一性的限制原则，新的数据将会替换旧的数据
- CONFLICT_ROLLBACK 当执行sql语句违反限制条件时，会停止执行当前sql语句，并将数据恢复到操作之前的状态。

除了使用 insertWithOnConflict 外，我们也可以在创建表的使用指定发送冲突时的行为，具体参考：

- [on conflict 官方文档](https://www.sqlite.org/lang_conflict.html)


---
## 4 删除数据

原生的sql语句删除数据语法为：

```sql
    delete form table_name where column1= value1 , column2 = value2;
```

而使用Android封装的的delete函数为：

```sql
int delete(String table, String whereClause, String[] whereArgs);

实例：
    db.delete("students","name = ? and cls_id = ?", new String[]{"jake","2"});
```


---
## 5 修改数据

Android封装的修改数据的函数为:

```
    update(String table, ContentValues values, String whereClause, String[] whereArgs)
    updateWithOnConflict(String table, ContentValues values, String whereClause, String[] whereArgs, int conflictAlgorithm) 

```

- conflictAlgorithm 与 insertWithOnConflict 中的 conflictAlgorithm意义一致。

---
## 6 查询数据

下面是 Android 中参数最为复杂的一个 query 函数：

```
    Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit)
```

参数分别代表如下：

- table 操作的表明
- columns 要获取的字段
- selection 条件语句，where部分
- selectionArgs where语句中的字段值
- groupBy 分组字段
- having groupBy 中的having部分
- OrderBy 排序
- limit 限制返回的数据数量

使用示例：

```
    Cursor c =  db.query( "person", new String[]{ "phone"}, "name=?", new String[]{name}, null, null, null);
```

---
## 7 Cursor

从上面的查询函数可以看到，query 函数返回了一个 Cursor，它提供了随机读写访问数据库查询结果集的接口，Cursor并不是线程安全的，因此，当多线程中访问Cursor对象时要手动实现同步，避免出现线程安全问题。

Cursor常用的函数如下：

| 函数名  |  作用 |
| ------------ | ------------ |
| getString(int index)  | 通过字段的索引获取一个String类型的字段，通过Index获取其他类型的字段与getString类似，只是函数名不一样而已，如getInt(int index)  |
| getColumnIndex(String columnName)  | 根据字段名获取字段的索引值，通过索引获取字段值的效率较高。  |
| getCount() | 该Cursor中有多少条数据  |
| moveToFirst()  | 将光标移动到第一个数据的位置  |
| moveToLast()  | 将光标移动到最后一个数据的位置  |
| moveToNext()  | 将光标移动到下一个数据的位置  |
| isClosed()  | 判断游标是否关闭  |
| isLast()|  判断光标是否在一个一个数据的位置上，对应还有isFirst判断是否在第一个数据上|
| close()|  关闭游标，使用完Cursor之后一定要记得调用此函数  |

一般的游标使用示例如下：

```
        Cursor dataCursor = resolver.query(datauri, new String[] {
                            "data1", "mimetype" }, "raw_contact_id=?",
                            new String[] { id }, null);
    
                    while (dataCursor.moveToNext()) {
                        String data1 = dataCursor.getString(0);
                        String mimetype = dataCursor.getString(1);
                        //doSth
                    }
                    dataCursor.close();
```

### 少用 cursor.getColumnIndex

在使用 cursor 时，我们往往会如下编码：

```
public static final String NAME_COLUMN = "name"

...

cursor.getString(cursor.getColumnIndex(NAME_COLUMN));
```
实际上，这包含了两个操作，getColumnIndex 和 getString，而如果我们能直接记住列的位置，就可以减少一次操作：

```
public static final String NAME_COLUMN_INDEX = 3;

...

cursor.getString(NAME_COLUMN_INDEX);
```



---
## 8 Android 中数据库事务

使用事务的两大好处是院子提交和性能更加优越，院子提交意味着同一事物内的所有修改要么都完成要么都不完成，如果某一个修改失败了，会自动回滚到修改之前的状态，SQLite会为每一个插入、更新操作创建一个事务，并且每次更新和插入后立即提交，如果连续的操作1000次，那么这样的过程就会重复1000次，如果使用事务的话就可以很大程度上优化性能，开启事务后的操作是这样的，创建事务-->提交1000次-->提交，这样创建事务和提交就只有一次。

Android中使用事务非常简单：

```
            database.beginTransaction();
            try {
                //这个执行数据库事务的代码，比如插入数据
                database.setTransactionSuccessful();
            } finally{
                database.endTransaction();
            }
```

---
## 9 SQLite 操作与异步

Android 数据的使用就是上面介绍的几个类了，需要注意的是使用 SQLiteDatabase 和 Curosr 一定要记得关闭，特使是 Cursor 时，关于数据的操作和表的升级前面都已经讲过了，现在主要考虑一下 Android 数据操作与多线程。

SQLite 是一个内嵌的数据库，数据库服务器就寄宿在应用程序中，无须网络配置和管理，数据服务器和客户端运行在同一个进程中，减少了网络访问消耗，简化了数据库管理，保证了执行效率，但是SQLite在多线程并发和数据大小方面存在局限性，并且它的锁机制的粒度为表级锁，所以不能，也没有必要通过多线程操作数据，如果通过多线程去操作数据库，往往会发生意想不到的异常，为了保证UI的流畅性，且又不会因为数据所引发异常，可以将数据的操作放在一个独立的线程中执行，最后通过将结果通过Handler返回给UI，当然也可以使用独立的轮询线程封装一个RxJava的Scheduler来配合RxJava操作数据库。另外，Android 也提供了 AsyncQueryHandler 类来帮我们实现类似功能。

例如使用 Handler 简单的封装一个 DbCommand:

```java
    public abstract class DbCOmmand<T> {

        private static ExecutorService sExecutorService =  Executors.newSingleThreadExecutor();
    
        private final static android.os.Handler sUiHandler = new android.os.Handler(Looper.getMainLooper());
    
        public final void execute() {
            sExecutorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                postResult(doBackground());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    
        private void postResult(final T t) {
            sUiHandler.post(new Runnable() {
                @Override
                public void run() {
                    opPostExecute(t);
                }
            });
        }
    
        protected  void opPostExecute(T t){}
        
        protected abstract T doBackground();
    
    
    }
```

---
## 10 使用ORM

ORM即对象关系映射，即对sql的封装，面向对象的操作数据库，使用上有很多orm框架，如果对sql使用熟练，并且能够熟悉框架的实现原理，使用orm框架也是不错的选择。







