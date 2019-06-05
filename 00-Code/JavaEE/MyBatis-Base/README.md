# MyBatis 入门学习

---
## 1 代码说明

### 源码

以 test 形式学习 MyBatis。代码所在包：`me.ztiany.mybatis.test`。

- basic
    - MyBatisBasicTest：MyBatis 的基本用法
    - MyBatisDaoTest：MyBatis dao模式
    - MyBatisBasicTest：MyBatis 自动 Mapper 映射
- spring
    - SpringDaoTest：MyBatis 整合 Spring，dao模式
    - SpringMapperTest：MyBatis 整合 Spring，手动配置mapper模式 + MyBatis事务演示
    - SpringMapperScannerTest：MyBatis 整合 Spring，自动扫描Mapper
    - SpringTxTest：MyBatis 整合 Spring，使用 Spring 事务管理 MyBatis

### 插件

根据 SQL 生成POJO、Mapper、和xml映射文件，支持单表查询。

- 在 gradle 中添加依赖和配置插件，生成的 task 是`other/mbGenerator`
- 使用 `src/main/resource/generatorConfig.xml`配置插件
- 生成的代码配置在根目录 generate 目录下
- 运行 task (如果没有生成则手动创建必生成路径)即可生成代码