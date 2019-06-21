# MVC示例：登录注册功能

- 三层架构
- 每一层的异常如何处理，抛出还是自己catch掉
    - dao层在操作数据时，有可能引发异常，dao不应该自己处理掉，而应该将异常包装后抛给service处理
    - service业务层在处理业务时可用引发异常，service层不应该自己处理掉，而应该将异常包装后抛给Controller处理
    - Controller调用业务层，根据业务调用是否成功来显示或调整不同的页面
- dom4j和xpath用法
- BeanUtils
