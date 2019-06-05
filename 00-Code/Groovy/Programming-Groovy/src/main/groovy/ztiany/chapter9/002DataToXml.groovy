package ztiany.chapter9

import groovy.sql.Sql
import groovy.xml.MarkupBuilder

//--------------------
//将数据转换为xml
//--------------------

def sql = Sql.newInstance('jdbc:mysql://localhost:3306/weatherinfo', 'root', '201314', 'com.mysql.jdbc.Driver')


bldr = new MarkupBuilder()

bldr.weather{
    sql.eachRow('SELECT * FROM  weather'){
        city(name:it.city,temperature:it.temperature)
    }
}

sql.close()

















