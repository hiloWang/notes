# Spring Framework 入门


## 1 代码

在test中演示 Spring Framework 基础功能

- basic：Spring容器中Bean的配置
    - ConfigTest：Spring 手动加载配置文件，手动从容器中获取对象。
    - JUnitSpringTest：使用 Spring 提供的测试框架，简化Spring 单元测试
    - ProxyTest：演示 Proxy 和 CGLib 代理
    
- aop ：Spring 切面功能
    - AOPTest：xml方式配置aop
    - AOPAnnotationTest：注解方式开启aop

- jdbc：Spring 的 JDBC 模板
    - JDBCTest：jdbc模板的使用
  
- tx：spring 事务
    - Tx1Test：手动调用 TransactionTemplate 使用事务
    - Tx2Test：Spring AOP 事务配置
    - Tx3Test：Spring AOP 事务注解式配置