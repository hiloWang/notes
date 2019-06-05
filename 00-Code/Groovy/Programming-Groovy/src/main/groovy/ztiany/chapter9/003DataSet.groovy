package ztiany.chapter9

import groovy.sql.Sql



//--------------------
//使用DataSet
//--------------------

def sql = Sql.newInstance('jdbc:mysql://localhost:3306/weatherinfo', 'root', '201314', 'com.mysql.jdbc.Driver')

/*
DataSet表示一个查询后的结果集，sql的dataSet方法返回哟个虚拟代理，知道迭代时才去实际获取数据
 */

dataSet = sql.dataSet('weather')
println dataSet.each {
    println it
}
println dataSet.rows()



//--------------------
//插入数据
//--------------------

//使用dataSet来插入数据
dataSet.add(city:"ChangSha",temperature:3)

/*
结果：
mysql> select * from weather;
+-------------+-------------+
| city        | temperature |
+-------------+-------------+
| Austin      |          48 |
| Boton Rouge |          21 |
| Jsckson     |           2 |
| Montgomery  |          32 |
| Sacramento  |          22 |
| Santa       |           1 |
| Tallahassee |          23 |
| Shenzhen    |          17 |
| ChangSha    |           3 |
+-------------+-------------+
 */

//使用sql来插入数据
sql.execute(
        """INSERT INTO  weather VALUES ('haha',100)"""
)
/*
结果：
mysql> select * from weather;
+-------------+-------------+
| city        | temperature |
+-------------+-------------+
| Austin      |          48 |
| Boton Rouge |          21 |
| Jsckson     |           2 |
| Montgomery  |          32 |
| Sacramento  |          22 |
| Santa       |           1 |
| Tallahassee |          23 |
| Shenzhen    |          17 |
| ChangSha    |           3 |
| haha        |         100 |
+-------------+-------------+

 */












