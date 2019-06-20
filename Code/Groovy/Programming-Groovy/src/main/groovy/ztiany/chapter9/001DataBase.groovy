package ztiany.chapter9

import groovy.sql.Sql

//--------------------
//创建数据库
//--------------------

//mysql导入groovy.xml





//--------------------
//使用Groovy连接数据库
//--------------------

def sql = Sql.newInstance('jdbc:mysql://localhost:3306/weatherinfo', 'root', '201314', 'com.mysql.jdbc.Driver')

println sql.connection.catalog
//select操作
println "city                   temperature"
sql.eachRow('SELECT * FROM  weather') {
    printf " %s        %s   \n", it.city, it[1]
}

/*
eachRow的另外要给重载，接受两个闭包，一个用于元数据，另一个用于数据，元数据的闭包调用一次
并以一个ResultSetMetaData实例为参数，而另一个闭包会对结果中的每一行都调用一次
 */

processMeta = {  metaData ->
    metaData.columnCount.times{
        i ->
            printf "%s      " , metaData.getColumnLabel(i + 1)

    }
    println ''
}

sql.eachRow('SELECT * FROM  weather', processMeta){
    printf " %s        %s   \n", it.city, it[1]
}

//使用rows方法返一个数据的结果集ArrayList
rows = sql.rows('SELECT * FROM weather')
println  rows.size()
//使用firstRow仅返回第一行
//可以使用sql的call方法执行存储过程，使用withStatement方法可以设置一个将在查询之前调用的闭包，如果想在执行之前拦截
//并修改Sql查询，该方法会有所帮助。
sql.close()//记得关闭sql





















