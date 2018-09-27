
# 多表连接

数据关系通常需要跨过多个表，因此多表查询非常重要，比如students中的cls_id字段代表班级id，通过这个id到classes表中可以查询到这个班级的专业，年份字段。由于这些信息不是在一个表中，所以就需要进行多表查询：

例如：`SELECT * FROM students,classes WHERE students.cls_id = classes.id;`：

![](index_files/a8f1397b-cbe2-4e04-9461-c7e5267d8fe6.png)

sql支持除了右外连接、全外连接之外的所有多表查询，对于这些多表查询在前面的MySQL中已经学习过，而这些都是遵循sql标准的，就不再重复了。

