# MySQL

----
## 1 简介

MySQL是流行的开放源码SQL数据库管理系统。

简介：

-   MySQL是一种数据库管理系统。
-   MySQL软件是一种开放源码软件。
-   MySQL数据库服务器具有快速、可靠和易于使用的特点。
-   MySQL服务器工作在客户端/服务器模式下，或嵌入式系统中。
-   有大量可用的共享MySQL软件。

MySQL特性：

- 内部构件和可移植性
- 使用C和C++编写
- 提供了用于C、C++、Eiffel、Java、Perl、PHP、Python、Ruby和Tcl的API
- 采用核心线程的完全多线程 如果有多个CPU，它能方便地使用这些CPU。

---
###  使用MySQL

MySQL命令行的基本语法：

- 连接MySql：mysql命令格式： `mysql -h 主机地址 -u 用户名 －p用户密码`，比如在本地主机的命令行输入：`mysql -h localhost -u root -p`
- 退出数据库：`exit`
- 修改密码：`mysqladmin -u root -p oldpassword password newpassword`
- `SELECT User, Host FROM mysql.user;`：查看所有用户

---
## 2 SQL语言分类

SQL：Structured Query Language的缩写，结构化查询语言， 作用是一种定义、操作、管理关系数据库的句法。大多数关系型数据库都支持。结构化查询语言的工业标准由ANSI(美国国家标准学会，ISO的成员之一)维护。

- 数据定义语言：简称DDL(Data Definition Language)，用来定义数据库对象：数据块、表、列等，常用关键字：create、alter、drop
- 数据操作语言：简称DDL(Data Manipulation Language)，用来对数据库中表的记录进行更新，常用关键字：insert、delete、update
- 数据查询语言：简称DQL(Data Query Language)，用来查询数据库中表的记录，常用关键字：select、from、where
- 数据控制语言：简称DCL(Data Control Language)，用来定数库的访问权限和安全级别和创建用户，常用关键字：grant等
- 事务处理语言：TPL
- 指针控制语言：CCL


---
## 3 关于Windows MySQL编码问题

查看MySQL编码：`SHOW VARIABLES LIKE 'char%';`

![](index_files/409d1fd4-27fd-4660-a496-a7eb43611415.png)

因为当初安装时指定了字符集为UTF8，所以所有的编码都是UTF8。

- character_set_client：你发送的数据必须与client指定的编码一致！！！服务器会使用该编码来解读客户端发送过来的数据；
- character_set_connection：通过该编码与client一致！该编码不会导致乱码！当执行的是查询语句时，客户端发送过来的数据会先转换成connection指定的编码。但只要客户端发送过来的数据与client指定的编码一致，那么转换就不会出现问题；
- character_set_database：数据库默认编码，在创建数据库时，如果没有指定编码，那么默认使用database编码；
- character_set_server：MySQL服务器默认编码；
- character_set_results：响应的编码，即查询结果返回给客户端的编码。这说明客户端必须使用result指定的编码来解码；

### 控制台编码

修改`character_set_client、character_set_results、character_set_connection`为GBK，就不会出现乱码了。但其实只需要修改`character_set_client和character_set_results`。

控制台的编码只能是GBK，而不能修改为UTF8，这就出现一个问题。客户端发送的数据是GBK，而character_set_client为UTF8，这就说明客户端数据到了服务器端后一定会出现乱码。既然不能修改控制台的编码，那么只能修改character_set_client为GBK了。

服务器发送给客户端的数据编码为character_set_result，它如果是UTF8，那么控制台使用GBK解码也一定会出现乱码。因为无法修改控制台编码，所以只能把character_set_result修改为GBK。

- 修改character_set_client变量：set character_set_client=gbk;
- 修改character_set_results变量：set character_set_results=gbk;

设置编码只对当前连接有效，这说明每次登录MySQL提示符后都要去修改这两个编码，但可以通过修改配置文件来处理这一问题：配置文件路径：`D:\Program Files\MySQL\MySQL Server 5.1\ my.ini`，**一般不推荐这么做**。

![](index_files/c32edc20-c3f7-43a9-a3ea-e9bdf89c741e.png)


----
## 教程

- [w3school-sql](http://www.w3school.com.cn/sql/index.asp)
- 《SQL必知必会》
- 《sql权威指南第4版》
- 《MySQL从入门到精通》
- 《MySQL开发者SQL权威指南》