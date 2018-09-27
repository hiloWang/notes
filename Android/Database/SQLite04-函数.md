# 函数

---
## 1 常用函数

[参考](http://udn.yyuap.com/doc/wiki/project/sqlite/sqlite-functions.html)

---
## 2 日期与时间函数

[参考](http://udn.yyuap.com/doc/wiki/project/sqlite/sqlite-date-time.html)

时间戳：

```
create table test(
      _id integer primary key autoincrement ,
      name varchar(100) ,
      pwd varchar(100) ,
      time TimeStamp NOT NULL DEFAULT (datetime('now','localtime'))
);
```