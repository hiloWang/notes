# Hibernate

---
## 1 Hibernate简介

Hibernate是处于开发中的持久层框架，他是一个ORM映射工具（Object/Relation Mapping，`对象-关系映射`，就是通过将Java对象映射到数据库表，通过操作Java对象，就可以完成对数据表的操作）。是轻量级JavaEE应用的持久层解决方案。

数据库框架：

- **JPA** Java Persistence API
- **Hibernate**：高度封装的ORM框架，通过`对象-关系映射`配置，可以完全脱离底层SQL
- **MyBatis**：本是apache的一个开源项目 iBatis，支持普通 SQL查询、存储过程和高级映射的优秀持久层框架
- Apache DBUtils：初级封装
- Spring JDBCTemplate

---
## 2 Hibernate配置

- `hibernate.cfg.xml` 配置Hibernate
- `JavaBean.hbm.xml` 配置实体映射

---
## 3 Hibernate API

- Configuration
- SessionFactory
- Session
- Transaction

---
## 4 实体规则

实体类创建的注意事项：

- 实体需要无参构造方法
- 成员私有变量提供setter和getter
- 提供一个标识属性，映射数据表主键字段
- **标识属性应尽量使用基本数据类型的包装类型**，可以多表达一个值`null`
- 不要用final修饰class （将无法生成代理对象。进行优化），Hibernate使用cglib代理生成代理对象，代理对象是继承被代理对象，如果被final修饰.将无法生成代理。

持久对象标识OID：

- Java按地址区分同一个类的不同对象
- 关系数据库用主键区分同一条记录
- Hibernate使用OID来建立内存中的对象和数据库中记录的对应关系
- 对象的OID和数据库的表的主键对应。为保证OID的唯一性，应该让Hibernate来为OID赋值

主键类型：

- 自然主键(少见)：表的业务列中，有某业务列符合，必须有，并且不重复的特征时，该列可以作为主键使用。
- 代理主键(常见)：表的业务列中，没有某业务列符合，必须有，并且不重复的特征时，创建一个没有业务意义的列作为主键

主键生成策略：

- 代理主键，identity：主键自增。由数据库来维护主键值。录入时不需要指定主键。
- sequence: Oracle中的主键生成策略。
- increment(了解): 主键自增。由hibernate来维护。每次插入前会先查询表中id最大值。然后`+1`作为新主键值。
- hilo(了解): 高低位算法。主键自增。由hibernate来维护。开发时不使用。
- native：hilo+sequence+identity 自动三选一策略。
- uuid: 产生随机字符串作为主键。 主键类型必须为string 类型。
- 自然主键，assigned：自然主键生成策略。 hibernate不会管理主键值。由开发人员自己录入。

---
## 5 实体对象的状态转换

- 瞬时状态：没有id，没有在session缓存中
- 持久化状态：有id，在session缓存中，持久化状态对象的任何变化都会自动同步到数据库中
- 游离|托管状态：有id，没有在session缓存中
- Session的 saveOrUpdate 方法：该方法同时包含save和update方法，如果对象是瞬时对象就调用save方法，如果是游离状态就调用update发，如果是持久状态就直接返回。

---
## 6 Hibernate的一级缓存

- 缓存:提高效率.hibernate中的一级缓存也是为了提高操作数据库的效率
- 提高效率手段：1 提高查询效率；2 减少不必要的修改语句发送

---
## 7 事务

在hibernate中指定数据库的隔离级别。在项目中如何管理事务：

- 业务开始之前打开事务，业务执行之后提交事务. 执行过程中出现异常则回滚事务.
- 在dao层操作数据库需要用到session对象，在service控制事务也是使用session对象完成，我们要确保dao层和service层使用的使用同一个session对象
- 在hibernate中，确保使用同一个session的问题,hibernate已经帮我们解决了，我们开发人员只需要调用`sf.getCurrentSession()`方法即可获得与当前线程绑定的session对象
- 调用getCurrentSession方法必须配合主配置中的一段配置
- 通过getCurrentSession方法获得的session对象，当事务提交时，session会自动关闭，不要手动调用close关闭

---
## 8 hibernate中的批量查询(概述)

- HQL：HQL查询-hibernate Query Language(多表查询，但不复杂时使用)，Hibernate独家查询语言，属于面向对象的查询语言
- Criteria查询(单表条件查询)：Hibernate自创的无语句面向对象查询
- 原生SQL查询(复杂的业务查询)：执行原生SQL语句

---
## 9 Hibernate多表操作

- 一对一
- 一对多
- 多对多