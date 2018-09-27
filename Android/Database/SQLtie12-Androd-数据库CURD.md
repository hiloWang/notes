# Androd 数据库 CURD

数据库的操作包括`增删查改`，再进一步分的化，`查`不会触发数据的变更，而 `增删改` 会导致数据库的变更。所以：

- 对于`查`，可以使用 `SqliteOpenHelper.getReadableDatabase` 获取数据库实例，对应的方法为 `query` 或 `rawQuery`
- 对于`增删改`，可以使用 `SqliteOpenHelper.getWritableDatabase` 获取数据库实例，对应的方法有 `insert`、`delete`、`update`、`execSQL`，或使用 SQLiteStatement 操作数据库。

---
## 1 CURD API

对于 `增删改` 操作，Android 提供了三套 API，分别是：

- 使用 `原生的sql` 语句执行数据库操作，execSQL 方法可用于执行任意的 sql 语句。
- 使用 SQLiteDatabase 提供的 `增删改` API。
- 使用 SQLiteStatement。

对于 查询，对应的方法为 `query` 或 `rawQuery`


### 使用 SQLiteDatabase 提供的 `增删改` API

```
insert类方法，返回值为新插入行的行ID，如果发生错误，则为-1

    //默认冲突处理 CONFLICT_NONE
    public long insert(String table, String nullColumnHack, ContentValues values)
    ////默认冲突处理 CONFLICT_REPLACE
    public long insertOrThrow(String table, String nullColumnHack, ContentValues values)
    public long insertWithOnConflict(String table, String nullColumnHack,
            ContentValues initialValues, int conflictAlgorithm) {

replace（对应的sql语句为 INSERT OR REPLACE INTO）类方法，返回值为新插入行的行ID，如果发生错误，则为-1

    //替换一个表中原有的行，如果需要替换的行不存在，则插入新的行
    public long replace(String table, String nullColumnHack, ContentValues initialValues)
    public long replaceOrThrow(String table, String nullColumnHack,
            ContentValues initialValues) throws SQLException

update类方法，返回值：影响的行数

    public int update(String table, ContentValues values, String whereClause, String[] whereArgs)
    public int updateWithOnConflict(String table, ContentValues values,
            String whereClause, String[] whereArgs, int conflictAlgorithm) {

delete操作，返回值：影响的行数

    public int delete(String table, String whereClause, String[] whereArgs) {
```

nullColumnHack 的作用：这是要给可选值，可以为null，SQL不允许在`没有命名至少一个列名`的情况下插入完全的空行。如果你提供的  是空的，没有知道的列名称又不允许插入空行，如果 nullColumnHack 不为空，它将提供可为空的列名称的名称，以便在 ContentValues 为空的情况下将 NULL 显式插入。

### SQLiteStatement

SQLiteStatement 表示可以对数据库执行的语句。该语句不能返回多个行或列，但支持单值（1 x 1）结果集，这个类不是线程安全的。

其实，通过源码可以发现，SQLite执行的 SQL 语句最后都是创建为 SQLiteStatement 对象去执行。


---
## 2 解决冲突

---
### ON CONFLICT 子句

当插入新的数据时，与已有的数据发送冲突，比如已经存在 `primary key`、`union key`，这时在插入或更新时指定冲突解决方式，比如 `ON CONFLICT REPLACE`

>ON CONFLICT 子句不是独立的 SQL 命令。这是一条可以出现在许多其他 SQL 命令中的非标准的子句。由于它并**不是标准的 SQL 语言**，ON CONFLICT 子句的语法在如上的 CREATE TABLE 命令中示出。对于 INSERT 和 UPDATE，关键词 “ON CONFLICT” 由 “OR” 替代，这样语法显得自然。例如，不用写 “INSERT ON CONFLICT IGNORE” 而是 “INSERT OR IGNORE” 。二者表示相同的意思。

ON CONFLICT子句定义了解决约束冲突的算法。有五个选择：`ROLLBACK、ABORT、FAIL、IGNORE和REPLACE`，缺省方案是ABORT。选项含义如下：

- **ROLLBACK**：当发生约束冲突，立即ROLLBACK，即结束当前事务处理，命令中止并返回SQLITE_CONSTRAINT代码。若当前无活动事务（除了每一条命令创建的默认事务以外），则该算法与ABORT相同。
- **ABORT**：当发生约束冲突，命令收回已经引起的改变并中止返回SQLITE_CONSTRAINT。但由于不执行- ROLLBACK，所以前面的命令产生的改变将予以保留。缺省采用这一行为。
- **FAIL**：当发生约束冲突，命令中止返回SQLITE_CONSTRAINT。但遇到冲突之前的所有改变将被保留。例如，若一条UPDATE语句在100行遇到冲突100th，前99行的改变将被保留，而对100行或以后的改变将不会发生。
- **IGNORE**：当发生约束冲突，发生冲突的行将不会被插入或改变。但命令将照常执行。在冲突行之前或之后的行将被正常的插入和改变，且不返回错误信息。
- **REPLACE**：当发生UNIQUE约束冲突，先存在的，导致冲突的行在更改或插入发生冲突的行之前被删除。这样，更改和插入总是被执行。命令照常执行且不返回错误信息。当发生NOT NULL约束冲突，导致冲突的NULL值会被字段缺省值取代。若字段无缺省值，执行ABORT算法。

其他需要注意的是 INSERT 或 UPDATE 的 OR 子句定义的算法会覆盖 CREATE TABLE 所定义的。ABORT 算法将在没有定义任何算法时作为缺省使用。具体可以参考 [SQLite介绍及使用](http://www.cnblogs.com/txw1958/archive/2012/11/16/sqlite-basic.html)

---
### REPLACE INTO

如果不使用 ON CONFLICT 子句，我们也可以可以在编程上解决主键或唯一性约束，比如比插入时：

1. 首先判断数据是否存在
2. 如果不存在，则插入
3. 如果存在，则更新

程序逻辑如下：

```
if not exists (select 1 from t where id = 1)
      insert into t(id, update_time) values(1, getdate())
else
   update t set update_time = getdate() where id = 1
```

但是，有一种更更方便的方法，那就是 `replace into`，replace into 跟 insert 功能类似，不同点在于：`replace into` 首先尝试插入数据到表中，如果发现表中已经有此行数据（根据主键或者唯一索引判断）则先删除此行数据，然后插入新的数据。否则就直接插入新数据。

注意，除非表有一个 PRIMARY KEY 或 UNIQUE 索引，否则该语句会与 INSERT 相同，因为没有索引被用于确定是否新行复制了其它的行。

测试表
```sql
CREATE TABLE IF NOT EXISTS teacher(id TEXT,name TEXT NOT NULL,PRIMARY KEY(id));

CREATE TABLE IF NOT EXISTS student(
        "id"  TEXT,
        "name"  TEXT NOT NULL,
        "sex"  TEXT,
        "email"  TEXT UNIQUE,
        "fenshu" TEXT CHECK(fenshu > 0),
        "tecid"  TEXT REFERENCES teacher(id),
        "class"  TEXT, 
        PRIMARY KEY(id, name));

//插入两个老师
insert into teacher (id, name) values('1', 'tec1');
insert into teacher (id, name) values('2', 'tec2');

//插入一个学生 wangwu
insert into student (id, name, sex, email, fenshu, tecid, class) values
 ('1', 'wangwu', '*F', 'wangwu@qq.com', '85', '1', '1');
//插入一个学生 lisi
insert into student (id, name, sex, email, fenshu, tecid, class) values
 ('2', 'lisi', '*F', '123456@qq.com', '80', '2', '1');

下面语句将导致已有的 lisi 被删除，然后插入新的 lisi，邮箱变为 lisi@qq.com
    replace into student (id, name, sex, email, fenshu, tecid, class) values
 ('2', 'lisi', '*F', 'lisi@qq.com', '80', '1', '1');

下面语句将导致已有的 lisi 被删除，然后插入新的 lisi，同时 wangwu 也会被删除
replace into student (id, name, sex, email, fenshu, tecid, class) values
 ('2', 'lisi', '*F', 'wangwu@qq.com', '80', '1', '1');
```

REPLACE INTO 总结：

- replace 语句会删除原有的一条记录， 并且插入一条新的记录来替换原记录。
- 一般用 replace 语句替换一条记录的所有列， 如果在 replace 语句中没有指定某列， 在 replace 之后这列的值被置空。而 update 语句不会
- replace 根据主键确定被替换的是哪一条记录，replace 语句不能根据 where 子句来定位要被替换的记录。
- 如果执行 replace 语句时，不存在要替换的记录，那么就会插入一条新的记录。
- 如果新插入的或替换的记录中，有字段和表中的已有记录冲突（比如唯一性约束），那么也会删除那条其他记录。也就是说 replace into 可能导致一次删除多条记录，总之只要存在唯一性约束的旧记录都将被删除

---
### 主从冲突

当数据库中的表存在主从关系时，操作可能引发冲突，比如一个外键引用了另一张表的主键，直接删除主键对于的行时就会引发冲突。

对于这种情况，在创建表指定外键时，使用
```
[ON {DELETE|UPDATE} action
    [NOT] DEFERRABLE INITALLY {DEFERRED|IMMEDIATE}]
```
语句指定父表的记录被删除或者更新时，子表对应操作的的执行时机

---
### 使用触发器

利用触发器解决更新主键冲突的问题，也许也是一种可行的方案
