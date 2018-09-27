[TOC]

# Redis数据存储

## 1 数据类型介绍

Redis是一种高级的key-value存储系统，支持以下数据类型：

- 二进制安全的**字符串**
- **Lists**: 按插入顺序排序的字符串元素的集合。他们基本上就是链表（linked lists）。
- **Sets**: 不重复且无序的字符串元素的集合。
- **Sorted sets**,类似Sets,但是每个字符串元素都关联到一个叫score浮动数值（floating number value）。里面的元素总是通过score进行着排序，所以不同的是，它是可以检索的一系列元素。（例如你可能会问：给我前面10个或者后面10个元素）。
- **Hashes**,由field和关联的value组成的map。field和value都是字符串的。这和Ruby、Python的hashes很像。
- **Bit arrays** (或者说 simply bitmaps): 通过特殊的命令，你可以将 String 值当作一系列 bits 处理：可以设置和清除单独的 bits，数出所有设为 1 的 bits 的数量，找到最前的被设为 1 或 0 的 bit，等等。
- **HyperLogLogs**: 这是被用于估计一个 set 中元素数量的概率性的数据结构。

关于key，需要注意几点：

- key不要太长，最好不要超过1024个字节，这不仅仅会消耗内存，还会降低查询效率
- key的命名要具有可读性
- 在项目中，key最好有一个统一的命名规范

## 2 数据操作

### 2.1 存储String

字符串时Redis中最为基础的数据存储类型，他在Redis中是二进制安全的，这意味着该类型存储或获取的数据相同，在Redis字符串类型的value最多可以容纳的数据长度是512M。

#### 常用命令

- `set key value` 设置key
- `get key` 获取key对应的字符串
- `del key` 删除指定的key
- `incr key` key对应的value自增1，如果key不存在，将其初始化为0，在incr之后值为1，如果key不能转变为整型，执行失败并返回错误信息
- `decr key` key对应的value自减1
- `incrby key increment` key对应的value自增increment
- `decrby key increment` key对应的value自减increment
- `append key value` 字符串，如果key不存在，则创建一个，key为value

---
### 2.2 存储Hash

Redis中Hash类型可以看成具有String Key和String Value的map容器，该类型非常适合存储值对象的信息，如果Hash中保存很少的字段，那么该类型的数据也将占据很少的内存，每个Hash可以存储4294967295个键值对。

#### 常用命令

- `hset key filed value` 添加key
- `hmset key filed value [field2 value2 ...]` 设置key中的多个field和value
- `hget key filed` 获取key中field对应的value
- `hmget key filed [filed2 file3]` 获取key中的多个filed
- `hgetall key` 获取key中所有的field和value
- `hdel key field [filed2]` 删除key中一个或多个field
- `del key` 删除整个key
- `hincby key field increment` 自增
- `hexists key field` 判断key中的field是否存在
- `hkeys key` 获取key中所有的field
- `havls key` 获取key中所有的value


### 存储List

在Redis中，List类型是按照插入顺序排序的字符串链表。和数据结构中的普通链表一样，我们可以在其头部(left)和尾部(right)添加新的元素。在插入时，如果该键并不存在，Redis将为该键创建一个新的链表。与此相反，如果链表中所有的元素均被移除，那么该键也将会被从数据库中删除。List中可以包含的最大元素数量是4294967295。

**Redis lists基于Linked Lists实现**。这意味着即使在一个list中有数百万个元素，在头部或尾部添加一个元素的操作，其时间复杂度也是常数级别的。用LPUSH 命令在十个元素的list头部添加新元素，和在千万元素list头部添加新元素的速度相同。那么同样的操作在linked list实现的list上没有那么快。

Redis Lists用linked list实现的原因是：对于数据库系统来说，至关重要的特性是：能非常快的在很大的列表上添加元素。另一个重要因素是，正如你将要看到的：Redis lists能在常数时间取得常数长度。

如果快速访问集合元素很重要，建议使用可排序集合(sorted sets)。

#### 常用命令


- `lpush key value1 [value2 ...]` 在链表的左边添加value，虽然可以一次添加多个value，但value都是从头部一个一个插入的，所以最前面的value到最后反而被挤到最后面了
- `rpush key value1 [value2 ...]` 在链表的右边添加value
- `lrage key start end` 获取链表中从start到end的元素的值，start、end从0开始计数，也可以为负数，-1表示链表尾部的第一个元素。
- `lpop key` 返回并弹出指定的key关联的链表中的第一个元素，如过key不存在返回`nil`
- `rpop key` 同上，从右边弹出
- `llen key` 返回指定的key关联的链表中元素的数量
- `lpushx key value` 仅当参数中指定的key存在时，向指定的key关联的链表的头部插入value
- `rpushx key value` 同上，右边插入
- `lrem key count value` 删除count个值为value的元素，如果count大于0，从头向尾遍历并删除，反之从尾部向头部遍历并删除，如果count等于0，则删除链表中所有值等于value的元素
- `lset key index value` 设置链表中的index的脚标的元素值，0代表头元素，-1代表尾元素，脚标不存在则抛出异常
- `linsert key before|after pivot value` 在pivot元素前|后插入value
- `rpoplpush resource destination` 将resource链表中的尾部元素弹出并添加到destination链表的头部，返回弹出的元素


#### rpoplpush的使用场景

Redis链表经常会被用于消息队列的服务，以完成多程序之间的消息交换。假设一个应用程序正 在执行LPUSH操作向链表中添加新的元素，我们通常将这样的程序称之为“生产者(Producer)",而另 外一个应用程序正在执行RPOP操作从链表中取出元素，我们称这样的程序为“消费者(Consumer)"。 如果此时，消费者程序在取出消息元素后立刻崩溃，由于该消息己经被取出且没有被正常处理，那 么我们就可以认为该消息己经丢失，由此可能会导致业务数据丢失，或业务状态的不一致等现象的 发生。然而通过使用RPOPLPUSH命令，消费者程序在从主消息队列中取出消息之后再将其插入到备 份队列中，直到消费者程序完成正常的处理逻辑后再将该消息从备份队列中删除。同时我们还可以 提供一个守护进程，当发现备份队列中的消息过期时，可以重新将其再放回到主消息队列中，以便 其它的消费者程序继续处理。

---
### 存储Set

在Redis中，我们可以将Set类型看作为没有排序的字符集合，和List类型一样，我们也可以在 该类型的数据值上执行添加、删除或判断某一元素是否存在等操作。需要说明的是，这些操作的时间复杂度为0(1)，即常量时间内完成次操作。Set可包含的最大元素数量是4294967295。和List类型不同的是，Set集合中不允许出现重复的元素，这一点和C++标准库中的set容器是完全相同的。换句话说，如果多次添加相同元素，Set中将仅保留该元素的一份拷贝。和List类型相比，Set类型在功能上还存在着一个非常重要的特性，即在服务器端完成多个Sets之间的聚合计算操作，如unions、intersections和differences。由于这些操作均在服务端完成，因此效率极高，而且也节省了大量的网络10开销。

#### 常用命令

- `sadd key value1 [value2 ...]` 添加key对应的set已有该值，则不会重复添加
- `srem key menber1 [menber2 ...]` 删除set中指定的成员
- `smembers key` 获取set中所有的成员
- `sismenber key member` 判断参数中指定的成员是否存在该set中，1表示存在，0表示不存在或该key本身就不存在
- `sdiff key1 key2` 返回 **属于key1** 并且 **不属于key2** 的元素构成的集合
- `sinter key1 key2 [key3 ...]` 取交集
- `sunion key key2 [key3 ...]` 取并集
- `scard key` 获取成员数
- `srandmember key` 随机返回一个成员
- `sdiffstore destination_key key1 key2` 将key1 key2取差集后的成员存储在destination_key上
- `sinterstore destination_key key1 key2 [key3 ...]` 将返回的交集存储在destination_key上
- `sunionstore destination_key key1 key2 [key3 ...]` 将返回的并集存储在destination_key上

#### 应用场景

1. 可以使用Redis的Set数据类型跟踪一些唯一性数据，比如访问某一博客的唯一IP地址信息。对于此场景，我们仅需在每次访问该博客时将访问者的IP存入Redis中，Set数据类型会自动保证IP 地址的唯一性。
2. 充分利用Set类型的服务端聚合操作方便、高效的特性，可以用于维护数据对象之间的关联关系。 比如所有购买某一电子设备的客户ID被存储在一个指定的Set中，而购买另外一种电子产品的客户 ID被存储在另外一个Set中，如果此时我们想获取有哪些客户同时购买了这两种商品时，Set的insersections命令就可以充分发挥它的方便和效率的优势了。

---
### 存储Sorted-Set

Sorted-Set和Set类型极为相似，它们都是字符串的集合，都不允许重复的成员出现在一个Set 中。它们之间的主要差别是Sorted-Set中的每一个成员都会有一个分数(score)与之关联，Redis正是通过分数来为集合中的成员进行从小到大的排序。然而需要额外指出的是，尽管Sorted-Set中的成员必须是唯一的，但是分数(score)却是可以重复的。 在Sorted-Set中添加、删除或更新一个成员都是非常快速的操作，其时间复杂度为集合中成员数量的对数。由于Sorted-Set中的成员在集合中的位置是有序的，因此，即便是访问位于集合中部 的成员也仍然是非常高效的。事实上，Redis所具有的这一特征在很多其它类型的数据库中是很难实 现的，换句话说，在该点上要想达到和Redis同样的高效，在其它数据库中进行建模是非常困难的。例如：游戏排名、微博热点话题等使用场景。


#### 常用命令

- `zadd key score member score2 member2 score2 [...]` 将所有成员以及所有成员对应的分数存放到sorted-set中，**如果该元素已存在则会用新的分数替换原来的分数**。返回值是新添加到集合中元素的个数。
- `zscore key member` 返回指定成员的分数
- `zcard key` 获取成员数量
- `zrem key member1 [member2 ...]` 移除元素
- `zrange key start end [withscores]` 获取集合中脚标尾start-end的成员，[withscores]参数表示返回的成员包含其分数。
- `zrevrange key start stop [withscores]` 按照元素分数从大到小的顺序返回索引从start到stop之间的所有元素，包含两端的元素
- `zremrangebyrank key start stop` 按照排名范围删除元素
- `zremrangebyscore key min max` 按照分数范围删除元素
- `zrangebyscore key min max [withscores] [limit offset count]` 按照分数从低到高，返回在[min ,max]区间的元素，limit offset count中，offset表示从该脚标的元素开始返回count个元素(起点索引不在count计数中)
- `zincrby key increment(要增加的分数) member` 设置指定成员的增加的分数，返回更改后的分数
- `zrank key member` 返回成员在集合中的排名(小到大)
- `zrevrank key member` 返回成员在结合中的排名(大到小)


#### 应用场景

1. 可以用于一个大型在线游戏的积分排行榜。每当玩家的分数发生变化时，可以执行ZADD命令更新玩家的分数，此后再通过ZRANGE命令获取积分TOPTEN的用户信息。当然我们也可以利用ZRANK命令通过usemame来获取玩家的排行信息。最后我们将组合使用ZRANGE和ZRANK命令快速的获取 和某个玩家积分相近的其他用户的信息。
2. Sorted-Set类型还可用于构建索引数据。

----
### 通用的key操作

- `keys pattern` 获取所有的与pattern匹配的key，返回所有与该key匹配的keys，`*`表示任意一个或多个字符，`?`表示任意一个字符
- `del key1 [key2 ...]` 删除指定的key
- `exists key` 判断key是否存在，1表示存在，0表示不存在
- `rename key newkey` 重命名key
- `expire key` 设置过期时间，单位：秒
- `ttl key` 获取只当key所剩的超时时间，如果key不存在则返回-2，如果key没有超时时间则返回-1
- `type key` 获取指定key的类型，该命令以字符串的形式返回：string、list、set、hash、zset，如果key不存在则返回none

----
## 3 Redis特性

### 3.1 多数据库

一个Redis示例可以包含多个数据库，客户端可以指定连接某个Reids实例的哪个数据库，就好比MySQL中创建了多个数据库，客户端连接时指定连接哪个数据库一样。

一个Redis实例最多可以提供16个数据库，下标从0到15，客户端默认连接第0号数据库，也可以通过`select`选择连接哪个数据库。比如：`select 1`。

不同数据库间可以移植数据，比如将newkey移植到1号库：
```
move myset 1
```

### 3.2 服务端命令

- `ping` 测试连接是否存活，如果存活则返回PONG
- `echo` 在命令行打印一些内容
- `select` 切换数据库
- `quit`
- `dbsize` 返回当前数据库中key的数目
- `info` 获取服务器的信息和统计
- `flushdb` 删除当前选择的数据库的所有数据
- `flushall` 删除所有数据库中的所有数据

### 3.3 订阅与发布

- subscribe channel 订阅频道，比如`subscribe mychat`，订阅mychat频道
- psubscribe channel* 批量订阅频道，比如`psubscribe s*`，订阅所有s开头的频道
- publish channel content 在指定的频道中发布消息，比如`publish mychat 'today is a newday'`


### 3.4 事务

和众多其他数据库一样，Redis作为NoSQL数据库也同样提供了事务机制，在Redis中，`MULTI、EXEC、DISCARD`这三个命令是使用事务的基石。

Redis事务特性

1. 在事务中的所有命令都将会被串行化的顺序执行，事务执行期间，Redis不会再为其它客户端的请求提供任何服务，从而保证了事物中的所有命令被原子的执行
2. 和关系型数据库中的事务相比，**在Redis事务中如果有某一条命令执行失败，其后的命令仍然会被继续执行。**
3. 我们可以通过MULTI命令开启一个亨务，有关系型数据库开发经验的人可以将其理解为"BEGINTRANSACTION"语句。在该语句之后执行的命令都将被视力事务之内的操作，最后我们可以通过 执行EXEC/DISCARD命令来提交/回滚该事务内的所有操作。这两个Redis命令可被视为等同于关系型数据库中的COMMIT/ROLLBACK语句。
4. 在事务开启之前，如果客户端与服务器之间出现通讯故障并导致网络断开，其后所有待执行的语句都将不会被服务器执行。然而如果网络屮断事件是发生在客户端执行EXEC命令之后，那么该事务中的所有命令都会被服务器执行。
5. 当使用Append-Only模式时，Redis会通过调用系统函数write将该事务内的所有与操作在本次调用中全部写入磁盘。然而如果在写入的过程中出现系统朋溃，如电源故障导致的宕机，那么此时也许只有部分数据被写入到磁盘，而另外一部分数据却己经丢失。Redis服务器会在重新启动时执行一系列必要的一致性检测，一旦发现类似问题，就会立即退出并给出相应的错误提示。此时，我们就要充分利用Redis工具包中提供的redis-check-aof工具，该工具可以帮助我们定位到数据不一致的错误，并将已经写入的部分数据进行回滚。修复之后我们就可以再次重新启动Redis服务器了。

命令解释

- `multi` 开启事务用于标记事务的开始，其后执行的命令都将被存入命令队列，直到执行EXEC 
时，这些命令才会被原子的执行，类似与关系型数据库中的：begin transaction 
- `exec` 提交事务，类似ij关系型数据库中的：commit 
- `discard` 事务回滚，类似勹关系型数据库巾的：rollback

----
## 4 持久化

Redis的高性能是由于其将所有数据都存储在了内存中，为了使Redis在重启之后仍能保证数据不丢失，需要将数据从内存中冋步到硬盘中，这一过程就是持久化。
Redis支持两种方式的持久化，一种是RDB方式，一种是AOF方式。可以单独使用其中一种或 将二者结合使用。 

1. RDB持久化（默认支持，无需配置)：该机制是指在指定的时间间隔内将内存中的数据集快照写入磁盘。
2. AOF持久化：该机制将以**日志**的形式记录服务器所处理的每一个写操作，在Redis服务器启动之初会读取该文件来重新构建数据库，以保证启动后数据库中的数据是完整的。
3. 无持久化 我们可以通过配置的方式禁用Redis服务器的持久化功能，这样我们就可以将Redis视为一个功能加强版的memcached了。
4. redis可以同时使用RDB和AOF

### 4.1  RDB

#### 优势

1. 一旦采用该方式，那么你的整个Redis数据库将只包含一个文件，这对于文件备份而言是非常完美的。比如，你可能打算每个小时归挡一次最近24小时的数据，同时还要毎天归裆一次最近30天的数据。通过这样的备份策略，一旦系统出现灾难性故障，我们可以非常容易的进行恢复。
2. 对于灾难恢复而言，RDB是非常不错的选择。因为我们可以非常轻松的将一个单独的文件压缩后再转移到其它存储介质上
3. 性能最大化。对于Redis的服务进程而言，在开始持久化时，它唯一需要做的只是fork(分叉)出子进程，之后再由子进程完成这些持久化的工作，这样就可以极大的避免服务进程执行I/O操作了。
4. 相比于A0F机制，如果数据集很大，RDB的启动效率会更高。


#### 劣势

1、如果你想保证数据的髙可用性，即最大限度的避免数据丢失，那么RDB将不是一个很好的选择。因为系统一旦在定时持久化之前出现宕机现象，此前没有来得及写入磁盘的数据都将丢失。
2、由于RDB是通过fork子进程来协助完成数据持久化工作的，因此，如果当数据集较大时，可能会导致整个服务器停止服务几百毫秒，甚至是1秒钟

#### 配置说明 Snapshotting

```shell
save 900 1 #毎900秒(1S分钟)至少有1个key发生变化，则dump内存快照。
save 300 10 #毎300秒(5分钟)至少有10个key犮生变化，则dump内存快照
save 60 10000 #每60秒(1分钟)至少有10000个key发生变化，则dump内存快照

dbfilename dump.db #快照文件名

dir ./ #快照保存位置
```

###  4.2 AOF

#### 优势

1. 该机制可以带来更高的数据安全性，即数据持久性。Redis中提供3中同步策略，即毎秒同步、 每修改同步和不同步。事实上,每秒同步也是异步完成的，其效率也是非常高的，所差的是一旦系统出现宕机现象，那么这一秒钟之内修改的数据将会丢失。而每修改同步，我们可以将其视为同步持久化，即每次发生的数据变化都会被立即记录到磁盘中。可以预见，这种方式在效率上是最低的。至于无同步，无需多言。
2. 由于该机制对日志文件的写入操作釆用的是append模式，因此在写入过程中即使出现宕机现象， 也不会破坏曰志文件中己经存在的内容。然而如果我们本次操作只是写入了一半数据就出现了 系统崩溃问题，不用担心，在Redis下一次启动之前，我们可以通过redis-check-aof工具来帮助 我们解决数据一致性的问题。
3. 如果日志过大，Redis可以自动启用rewrite机制，即Redis以append模式不断的将修改数据写入到老的磁盘文件中，同时Redis还会创建一个新的文件用于记录此期间有哪些修改命令被执行。 因此在进行rewrite切换时可以更好的保证数据安全性。
4. AOF包含一个格式清晰、易于理解的日志文件用于记录所有的修改操作。事实上，我们也可以通 过该文件完成数据的重建。

#### 劣势

1. 对于相同数量的数据集而言，AOF文件通常要大于RDB文件
2. 根据同步策略的不同，AOF在运行效率上往往会慢于RDB，总之，毎秒同步策略的效率是比较高的，同步禁用策略的效率和RDB一样高效。

#### 配置AOF

```shell
# 配置是否开启AOF，AOF文件在Reids启动时被加载，必须保证AOF文件的完整性
appendonly on #默认关闭

# 配置策略
appendfsync always #每秒钟同步一次，该策略为AOF的缺省策略
# appendfsync everysec #每次有数据修改发生时都会写入AOF文件
# appendfsync no #从不同步。高效但是数据不会被持久化

appendfilename appendonly.aof # AOF文件名
```

重写AOF：如果不满足重写条件时，可以手动重写，命令：`bgrewriteaof`

---
## 提问

- 为什么要有缓存？
- 缓存和应用程序是在一个进程内还是不同的进程？
- 缓存在本机还是网络上？
- 缓存里能放什么东西？
- 数据是什么格式？
- 缓存和数据库的一致性怎么保证？
- 如何实现高性能？
- 一个redis服务器满了怎么办？
- 一个redis服务器挂了怎么办？
- 是不是有了Redis就不需要关系型数据库了？
- 如何实现Redis分布式？