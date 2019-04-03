# 更新、删除、修改、销毁

---
## 1 update

update用于更新表中的数据，格式为：

```
    UPDATE table_name set update_list WHERE predicate;
```
例如：

```
    UPDATE students SET  tel_no = '18888888888' , name = 'jordan' WHERE name = 'kgyt';
```

需要注意的是更新操作也可能失败，例如，当我们要修改primary key的id字段且修改的新值在表中已经存在是，那么更新就会失败，例如：

![](index_files/7603dac2-cb9c-473c-9089-d41656e5190a.png)


---
## 2 delete

delete用于删除数据库中的数据，其语法为:

```
    delete from table_name where predicate;
```

如果不加where则表示删除表中所有的数据

---
## 3 修改表

随着项目的演进，最初建立的表可能会面临修改的情况，例如需要往表中添加一个字段，某个字段已经不再需要了，这种情况下通常需要修改表的结构，修改表的结构同alter命令，SQLite的alter并没有实现标致的SQL中所有的功能，**它只能修改表名和添加字段**，如果需要删除或者修改字段则需要创建新的表然后复制旧数据的迂回方式了。

```
alter table tableName {rename to newName | add column 新的字段}
```

修改表名和添加字段比较简单，但如果我们需要删除一个列呢？那么只能先创建一个临时表(该临时表比之前的表少创建了要删除的字段)，将之前的表的数据备份到临时表中(只备份需要的字段)，然后销毁之前的表，最后再将临时表重命名为之前的表。

比如要删除students表中的age字段
```
//创建临时表
create table stu_temp{
   id integer primary key autoincrement,
   name vachar(20) check(length(name) > 3),
   tel_no varchar(11) not null,
   cls_id integer not null
};

insert into stu_temp select id,name,tel_no,cls_id from sutdents;
drop table students;
alter table stu_temp rename to students;
```


---
## 4 drop命令

drop用于删除物理存储介质，例如删除表，视图，索引，触发器等，命令格式为：

```
    drop [table | view | index | trigger] name;
```








