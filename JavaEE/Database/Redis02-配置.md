# Redis配置

Redis支持命令行配置，语法：

```
获取配置的变量
CONFIG GET 配置key
示例：CONFIG GET loglevel
示例，获取所有配置变量：CONFIG GET *

设置新的变量
CONFIG SET 配置key 配置value
示例：CONFIG SET loglevel "notice"
```

具体参考[Redis 配置](http://www.runoob.com/redis/redis-conf.html)
