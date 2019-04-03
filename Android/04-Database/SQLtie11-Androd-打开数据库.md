# 数据库操作 API

---
## 1 创建数据库

创建或打开数据库的方式有三种，一般我们使用 SqliteOpenHelper，三种方式分别是：

- 第一种方式：继承 SQLiteOpenHelper 打开或创建数据库，可以在升级数据库版本的时候在回调函数里面做升级需要的处理
- 第二种方式：使用 `Context.openOrCreateDatabase` 打开或创建数据库，可以指定数据库文件的操作模式
```
fun openOrCreateDatabase(context: Context, database: String) {
    /**指定数据库的名称为 info2.db，并指定数据文件的操作模式为MODE_PRIVATE */
    val sqLiteDatabase = context.openOrCreateDatabase(database, MODE_PRIVATE, null)
    /**查看改对象所创建的数据库 */
    showDataBase(sqLiteDatabase)
}
```
- 第三种方式：使用 `SQLiteDatabase.openOrCreateDatabase` 打开或创建数据库，可以指定数据库文件的路径
```
fun sQLiteDatabase(fileName: String) {
    val dataBaseFile = File(Environment.getExternalStorageDirectory(), "/sqlite$fileName")
    if (!dataBaseFile.parentFile.exists()) {
        dataBaseFile.mkdirs()
    }
    val sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(dataBaseFile, null)
    showDataBase(sqLiteDatabase)
}
```

查看应用内已经创建的数据库

```
/** 查看手机中由SQLiteDatabase创建的的数据库文件 */
fun showDataBase(sqLiteDatabase: SQLiteDatabase) {
    val ll = sqLiteDatabase.attachedDbs
    for (i in ll.indices) {
        val p = ll[i]
        Log.d(ORIGIN_TAG, p.first + "=" + p.second)
    }
}
```

---
## 2 SqliteOpenHelper 的 getReadableDatabase 与 getWritableDatabase 的区别

数据库的操作包括`增删查改`，再进一步分的化，`查`不会触发数据的变更，而 `增删改` 会导致数据库的变更。所以：

- 对于`查`，可以使用 `SqliteOpenHelper.getReadableDatabase` 获取数据库实例
- 对于`增删改`，可以使用 `SqliteOpenHelper.getWritableDatabase` 获取数据库实例

- getWritableDatabase：创建或打开将用于读写的数据库。第一次调用这个方法时，将打开数据库并调用 onCreate/onUpgrade/onOpen 方法，一旦数据库打开成功，数据库实例将会被缓存，所以当你需要读写数据库的时候就可以调用这个方法，当你不再需要操作数据库时请确保你调用了它的 close 方法，权限不足或磁盘满了等错误可能导致此方法调用失败（抛出SQLiteException），但如果问题得到解决，之后的尝试调用可能会成功。数据库更新可能需要很长的事件，所以你不应该在主线程调用它们。
- getReadableDatabase：创建或打开数据库，这个方法可能返回与 getWritableDatabase 方法一样的数据库实例，除非发生了某些问题，比如一个满了磁盘要求以只读的方式打开数据库，在那种情况下，一个只读的数据库实例将会被返回，如果问题被解决了(磁盘已满)，在之后调用 getWritableDatabase 方法可能会成功，在这种情况下，将关闭只读数据库对象，并在将来返回可读写的数据库对象。

getReadableDatabase 与 getWritableDatabase 都会调用 getDatabaseLocked 方法，只是传入的参数不同而已：

```java
    private SQLiteDatabase getDatabaseLocked(boolean writable) {
        if (mDatabase != null) {
            //如果数据库已经关闭了，就置空
            if (!mDatabase.isOpen()) {
                // Darn!  The user closed the database by calling mDatabase.close().
                mDatabase = null;
            //如果数据库是只读的，并且只需要一个只读的数据库，那么直接返回
            } else if (!writable || !mDatabase.isReadOnly()) {
                // The database is already open for business.
                return mDatabase;
            }
        }

        if (mIsInitializing) {
            throw new IllegalStateException("getDatabase called recursively");
        }
        //到这里的情况
        //原有的数据库是只读的，要升级为可读写的
        //原有的数据库是 null 的，要求一个只读或可读写的数据库

        SQLiteDatabase db = mDatabase;
        try {
            mIsInitializing = true;

            if (db != null) {
                if (writable && db.isReadOnly()) {
                    //之前打开了数据库，则升级为可读写的
                    db.reopenReadWrite();
                }
            } else if (mName == null) {
                //没有指定名称，就在内存中创建
                db = SQLiteDatabase.createInMemory(mOpenParamsBuilder.build());
            } else {
                final File filePath = mContext.getDatabasePath(mName);
                SQLiteDatabase.OpenParams params = mOpenParamsBuilder.build();
                try {
                    ////打开新的数据库
                    db = SQLiteDatabase.openDatabase(filePath, params);
                    setFilePermissionsForDb(filePath.getPath());//权限660
                } catch (SQLException ex) {
                    if (writable) {//如果要求获取可读写的，但是打开失败了，只能抛出异常
                        throw ex;
                    }
                   //尝试以只读的方式打开
                    params = params.toBuilder().addOpenFlags(SQLiteDatabase.OPEN_READONLY).build();
                    //打开新的数据库
                    db = SQLiteDatabase.openDatabase(filePath, params);
                }
            }

            //模板方法：配置数据库的特性
            onConfigure(db);

            //新数据库的 version 默认为 0
            final int version = db.getVersion();
            
            //如果新版本与旧的版本不一致
            if (version != mNewVersion) {
                //版本对比，如果版本要升级，但是数据库是只读的就不能进行升级了，只能抛出异常
                if (db.isReadOnly()) {
                    throw new SQLiteException("Can't upgrade read-only database from version " +
                            db.getVersion() + " to " + mNewVersion + ": " + mName);
                }
                //mMinimumSupportedVersion 最低支持版本
                //如果新版本与旧的版本不一致，且现在的版本小于最低支持版本，则先删除原有的数据库
                if (version > 0 && version < mMinimumSupportedVersion) {
                    File databaseFile = new File(db.getPath());
                    //模板方法：删除之前的回调
                    onBeforeDelete(db);
                    db.close();
                    //删除数据库
                    if (SQLiteDatabase.deleteDatabase(databaseFile)) {
                        mIsInitializing = false;
                        //重新去获取数据库
                        return getDatabaseLocked(writable);
                    } else {
                        //删除失败，抛出异常
                        throw new IllegalStateException("Unable to delete obsolete database "
                                + mName + " with version " + version);
                    }
                } 
                //如果新版本与旧的版本不一致，支持最低支持的版本，正常升级
                else {
                    db.beginTransaction();
                    try {
                        //版本为0，则调用onCreate用于创建表
                        if (version == 0) {
                            onCreate(db);
                        } else {
                            //降级
                            if (version > mNewVersion) {
                                onDowngrade(db, version, mNewVersion);
                            } else {
                            //升级
                                onUpgrade(db, version, mNewVersion);
                            }
                        }
                        db.setVersion(mNewVersion);
                        db.setTransactionSuccessful();
                    } finally {
                        db.endTransaction();
                    }
                }
            }

            //模板方法：已经打开了数据库
            onOpen(db);

            if (db.isReadOnly()) {
                Log.w(TAG, "Opened " + mName + " in read-only mode");
            }

            mDatabase = db;
            return db;
        } finally {
            mIsInitializing = false;
            if (db != null && db != mDatabase) {
                db.close();
            }
        }
    }
```


回调方法总结：

- onConfigure：在配置数据库连接时调用，以启用预写日志记录或外键支持等功能。
- onBeforeDelete：在删除数据库之前调用，比如设置的版本小于最低支持版本。
- onCreate：在第一次创建数据库时调用。这是表的创建和表的初始填充的地方。
- onUpgrade：在需要升级数据库时调用。实现应该使用此方法删除表、添加表、或执行升级到新架构版本所需的任何其他操作。
- onDowngrade：在数据库需要降级时调用。
- 打开数据库时调用。, 在更新数据库之前，实现应检查 SQLiteDatabase 是否是只读的。