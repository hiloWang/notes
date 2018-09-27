# Reids


---
## 1 NoSQL简介

NoSQL的意思是Not Only SQL，即不仅仅是SQL，是一项全新的数据库理念，泛指非关系型数据库。

### 为什么需要NoSQL

随着互联Mweb2.〇M站的兴起，非关系型的数据库现在成了一个极其热门的新领域，非关系数据库产品的发展非常迅速。而传统的关系数据库在应付**web2.0**网站，特别是超大规模和高并发的SNS类型的web2.0纯动态M站己经显得力不从心，暴露了很多难以克服的问题，例如：

**1、Highperformance-对数据库高并发读写的需求**

web2.0网站要根据用户个性化信息来实时生成动态页而和提供动态信息，所以基本上无法使用动态页面静态化技术，因此数据库并发负载非常高，往往要达到每秒上万次读写请求。关系数据库应付上万次SQL查询还勉强顶得住，但是应付上万次SQL写数据请求，硬盘10就己经无法承受了。其实对于普通的BBSM站，往往也存在对高并发写请求的需求，例如M站的实时统计在线用户状态，记录热门帖子的点击次数，投票计数等，因此这是一个相当普遍的需求。

**2、HugeStorage-对海M数据的高效率存储和访问的需求**

类似Facebook，twitter，Friendfeed这样的SNS网站，每天用户产生海量的用户动态，以Friendfeed为例，一个月就达到了2.5亿条用户动态，对于关系数据库来说，在一张2.5亿条记录的表里面进行SQL查询，效率是极其低下乃至不讨忍受的。再例如大型web网站的用户登录系统，例如腾讯，盛大，动辄数以亿计的帐号，关系数据库也很难应付。

**3、HighScalability和HighAvailability-对数据库的高可扩展性和高可用性的需求**

在基于web的架构当中，数据库是最难进行横向扩M的，当一个应用系统的用户量和访问量与曰俱增的时候，你的数据库却没有办法像webserver和appserver那样简单的通过添加更多的硬件和服务节点来扩展性能和负载能力。对于很多需要提供24小时不间断服务的网站来说，对数据库系统进行升级和扩展是非常痛苦的事情，往往需要停机维护和数据迁移，为什么数据库不能通过不断的添加服务器节点来实现扩展呢？

**NoSQL数据库的产生就是为了解决大规模数据集合多重数据种类带来的挑战，尤其是大数据应用难题。**


### NoSQL产品

- MongoDB
- Redis

### NoSQL四大分类

键值(Key-Value)存储数据库：

- 相关产品：TokyoCabinet/Tyrant、Redis、Voldemort、BerkeleyDB
- 典型应用：内容缓存，卞要用于处理大M数据的高访问负载.
- 数据模型：一系列键值对
- 优势：快速查询
- 劣势：存储的数据缺少结构化


列存储数据库

- 相关产品：Cassandra、HBase、Riak
- 典型应用：分布式的文件系统
- 数据模型：以列簇式存储，将一列数椐存在一起
- 优势：查找速度快，可扩展性强，容易进行分布式扩展
- 劣势：功能相对局限

文档型数据库

- 相关产品：CouchDB、MongoDB
- 典型应用：Web应用（与Key-Value类似，Value是结构化的）
- 数据模型：一系列键值对
- 优势：数据结构要求不严格
- 劣势：查询性能不高，而且缺乏统一的查询语法

图形(Graph)数据库

- 相关数据库：Neo4J、InfoGrid、InfiniteGraph
- 典戢应用：社交网络
- 数据模型：图结构
- 优势：利用图结构相关筧法。
- 劣势：要对整个图做计算才能得出结果，不容易做分布式的集群方案。


### NoSQL特点

在大数据存取上具备关系型数据库无法比拟的性能优势，例如：

1. **易扩展**：NoSQL数据库种类繁多，但是一个共同的特点都是去掉关系数据库的关系型特性。数据之间无关系，这样就非常容易扩展。也无形之间，在架构的层面上带来了讨扩展的能力。
2. **大数据量，高性能**：NoSQL数据库都具有非常高的读写性能，尤其在大数据量下，同样表现优秀。这得益于它的无关系性，数据库的结构简单。
3. **灵活的数据模型**：NoSQL无需事先为要存储的数据建立字段，随时可以存储自定义的数据格式。而在关系数据库里，增删字段是一件非常麻烦的事情。如果是非常大数据量的表，增加字段简直就是一个噩梦。这点在大数据量的Web2.0时代尤其明显。
4. **高可用**：NoSQL在不太影响性能的情况下，就可以方便的实现髙可用的架构。比如Cassandra、HBase模型，通过复制模型也能实现高可用。

综上所述，NoSQL的非关系特性使其成为/后Web2.0时代的宠儿，助力大型Web2.0网站的再次起飞，是一项全新的数据库革命性运动。


---
## 2 Redis简介

### Redis由来

2008年，意大利的一家创业公司Merzia推出了一款基于MySQL的M站实时统计系统LLOOGG，然而没过多久该公司的创始人Salvatore Sanfilippo便对MySQL的性能感到失望，于是他决定亲自为LLOOGG童身定做一个数据库，并于2009年开发完成，这个数据库就是Redis。
不过Salvatore Sanfilippo并不满足只将Redis用于LLOOGG这一款产品，而是希望更多的人使用它，于是在同一年Salvatore Sanfilippo将Redis开源发布，并开始和Redis的另一名主要的代码贡献者Pieter Noordhuis一起继续着Redis的开发，直到今天。

Salvatore Sanfilippo自己也没有想到，短短的儿年时间，Redis就拥有了庞大的用户群体。Hacker News在2012年发布了一份数据库的使用情况调查，结果显示有近12%的公司在使用Redis。国内如新浪微博、街旁网、知乎网，闽外如GitHub、StackOverflow、Flickr等都是Redis的用户。VMware公司从2010年开始赞助Redis的开发，Salvatore Sanfilippo和Pieter Noordhuis也分别在3月和5月力口入VMware，全职开发Redis


### 什么是Redis


Redis是用C语言开发的一个开源的高性能键值对（key-value)数据库。它通过提供多种键值数据类型来适应不同场景下的存储需求，它通常被称为数据结构服务器，目前为止Redis支持的键值数据类型如下：

1. 字符串类型
2. 散列类型
3. 列表类型
4. 集合类型
5. 有序集合类型。

官方提供测试数据：50个并发执行100000个请求,读的速度是110000次/S，写的速度是81000次/s。数据仅供参考，根据服务器配置会有不同结果。

### Redis特点

- Redis运行在内存中但是可以持久化到磁盘，即Redis支持数据的持久化，可以将内存中的数据保存在磁盘中，重启的时候可以再次加载进行使用。
- Redis不仅仅支持简单的key-value类型的数据，同时还提供list，set，zset，hash等数据结构的存储。
- Redis支持数据的备份，即master-slave模式的数据备份。
- 原子 – Redis的所有操作都是原子性的，意思就是要么成功执行要么失败完全不执行。单个操作是原子性的。多个操作也支持事务。
- 丰富的特性 – Redis还支持 publish/subscribe, 通知, key 过期等等特性。

### Redis的应用场景

- 缓存（数据查询、短连接、新闻内容、商品内容等等），使用最多
- 聊天室的在线好友列表。
- 任务队列。（秒杀、抢购、12306等等）
- 应用排行榜。
- 网站访问统计。
- 数据过期处理（可以精确到毫秒）
- 分布式集群架构中的session分离。

---
## 3 安装Reids

环境ubuntu，[参考文章](http://blog.topspeedsnail.com/archives/4967)。

### 安装依赖

```
- sudo apt-get update
- sudo apt-get install build-essential tcl
```

### 下载编译并安装

```
- curl -O http://download.redis.io/redis-stable.tar.gz //下载地址可以从官网获取最新的
- tar xzvf redis-stable.tar.gz
- cd redis的解压目录
- make
- make test
- make install
```

### 常规配置

redis的配置可以通过修改配置文件修改(需要重启服务)，也可以通过redis提供的命令进行修改。


拷贝解压的redis文件中的redis.conf到etc/redis目录下，并编辑

```
#设置日志文件路径(稍后要添加权限)
logfile /var/log/redis/redis.log

#设置数据保存目录(稍后要添加权限)
dir /var/lib/redis

#注释掉本地监听
bind 127.0.0.1
```

### 以后台服务启动Reids

使用redis-server启动redis时，Reids默认以前台服务启动，以后台服务的方式启动redis的方法为：

```
#配置文件中，打开后台运行选项
daemonize yes

#然后以配置文件的方法启动redis(后面加上配置文件即可)
redis-server /etc/redis/redis.conf
```

### 把Redis配置成系统服务

编辑配置文件
```
#supervised改为systemd
supervised systemd
```

创建systemd Unit文件
```
vim /etc/systemd/system/redis.service

#redis.service中的内容
[Unit]
Description=Redis In-Memory Data Store
After=network.target

[Service]
User=redis
Group=redis
ExecStart=/usr/local/bin/redis-server /etc/redis/redis.conf
ExecStop=/usr/local/bin/redis-cli shutdown
Restart=always

[Install]
WantedBy=multi-user.target
```

创建用户组并设置权限(**根据上面redis.service中的内容创建用户**)
```
sudo adduser --system --group --no-create-home redis
mkdir /var/lib/redis

//数据库目录读写权限
chown redis:redis /var/lib/redis
chmod 770 /var/lib/redis

//日志目录读写权限
chown redis:redis /var/log/redis/redis.log
chmod 770 /var/log/redis/redis.log
```

以系统服务的方式启动redis
```
启动服务：systemctl start redis
重启服务：systemctl restart redis

开启redis：service redis start
停止redis： service redis stop
重启redis： service redis restart
查看服务状态：service redis status
```

### 查看状态

```
#命令行客户端连接Redis服务：
redis-cli
redis-cli --raw //自动转码(查看中文)
auth password //如果设置了密码
redis-cli -h host -p port -a password //连接远程redis服务

关闭redis：redis-cli shutdown
```

### 开放端口

如需远程连接redis，需配置redis端口6379在linux防火墙中开放

```
/sbin/iptables -I INPUT -p tcp --dport 6379 -j ACCEPT
iptables-save
```

### 测试

```
使用客户端命令行连接redis
redis-cli

保存一个值
set a b

获取a
get a
```

### jedis连接redis

redis默认没有启动密码，但是开启了保护模型，要让jedis连接到redis需要为redis设置密码：
```
打开配置文件，找到requirepass，关闭注释
requurepass 201314
```
jedis连接到redis
```
        //1、获得连接对象
        Jedis jedis = new Jedis("39.108.56.76", 6379);
        jedis.auth("201314");

        //2、获得数据
        String username = jedis.get("username");
        System.out.println(username);

        //3、存储
        jedis.set("address", "北京");
        System.out.println(jedis.get("address"));
```