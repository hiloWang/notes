# Aspectj 学习

---
## 1 基本概念

| 名词 |  含义 |
| --- | --- |
| 切面（方面，Aspect） | 一个关注点的模块化，这个关注点实现可能横切（crosscutting）多个对象切面的例子包括：事务控制、日志记录、权限控制等在AspectJ中，切面表现为Java类，其源码具有AspectJ的特殊语法增强，使用ajc编译器编译，我们可以针对不同的需求做出不同的切面。|
| 连接点（JoinPoint） | 程序中可能作为代码注入目标的特定的点和入口。例如方法的调用开始、结束，或者特定的异常被抛出，横切（crosscutting）在连接点发生 |
| 通知（Advice） | 在特定的连接点，AOP框架执行的操作 |
| 切入点（Pointcut） | 切入点是这样的一种程序构造：包含**一个连接点的集合、以及收集的连接点的上下文信息**（例如方法参数、被执行方法和对象）。通知在这些切入点被触发。AOP框架允许开发者以多种方式定义切入点，简单理解为切入点告诉AspectJ对那些连接点进行切入。 |
| 引入（Introduction） | 添加方法、字段、接口、注解等到被通知的类 |
| 目标对象（Target Object） | 包含连接点的对象，也被称作被通知或被代理对象 |
| AOP代理（AOP Proxy） | AOP框架创建的对象，以目标对象为基础，织入了通知逻辑代理主要包括动态代理、字节码增强两种类型 |


### Join Points

JointPoint就是程序运行时的一些连接点。在连接点可以插入切面功能代码。
**构造方法调用、调用方法、方法执行、异常、设置一个变量，或者读取一个变量等等**，这些都是Join Points

AspectJ中具体的连接点表现为：

Join Points | 说明 | 语法
---|---|---
method call | 函数调用 | execution(方法签名)
method execution|函数执行|call(方法签名)
constructor call|构造函数调用|call(构造器签名)
constructor execution|构造函数执行|execution(构造器签名)
field get|获取某个变量|get(字段签名)
field set|设置某个变量|set(字段签名)
static initialization|类初始化，包括静态成员初始化部分|staticinitialization(类型签名)
initialization|对象初始化，在构造器中的对象初始化。包含：从一个父构造器返回，到当前构造器执行完成的部分（不包括调用父构造器的部分）|initialization(构造器签名)
pre-initialization|对象预初始化，在构造器中的对象初始化之前：从当前构造器调用，到父构造器调用结束为止|preinitialization(构造器签名)
handler|异常处理|handler(类型签名)
advice execution|通知的执行：环绕某个通知的整个执行。可以对通知进行通知|adviceexecution()
 
 注意call和execution的区别：
 
 1. method call是调用某个函数的地方。而execution是某个函数执行的内部。
 2. call捕获的JoinPoint是签名方法的调用点，而execution捕获的则是执行点
 
### Pointcuts(切入点)

不是所有类型的JointPoint都是我们关注的。Pointcuts的目标是提供一种方法使得开发者能够选择自己感兴趣的JoinPoints。

pointcut选择基于正则的语法，Pointcuts的主要类型有：

- Methods and Constructors
    - call(Signature): 方法和构造函数的调用点
    - execution(Signature): 方法和构造函数的执行点
- Fields
    - get(Signature): 属性的读操作
    - set(Signature): 属性的写操作
- Exception Handlers
    - 异常处理执行
- Advice
    - adviceexecution(): Advice执行
- Initialization
    - staticinitialization(TypePattern)    : 类初始化
    - initialization(Signature): 对象初始化
    - preinitialization(Signature): 对象预先初始化
- Lexical
    - within(TypePattern): 捕获在指定类或者方面中的程序体中的所有连接点，包括内部类
    - withincode(Signature): 用于捕获在构造器或者方法中的所有连接点，包括在其中的本地类
- Instanceof checks and context exposure
    - this(Type or Id): 所有Type or id 的实例的执行点，匹配所有的连接点，如方法调用，属性设置
    - target(Type or Id): 配所有的连接点，目标对象为Type或Id
    - args(Type or Id): 参数类型为Type
- Control Flow
    - cflow(Pointcut): 捕获所有的连接点在指定的方法执行中，包括执行方法本身
    - cflowbelow(Pointcut): 捕获所有的连接点在指定的方法执行中，除了执行方法本身
- Conditional
    - if(Expression)
- Combination(多Pointcut的逻辑结合操作)
    - ! Pointcut
    - Pointcut0 && Pointcut1
    - Pointcut0 || Pointcut1

### Advice

通过pointcuts来选择合适的JointPoint后，就可以在JointPoint处插入的代码，Advice用于指定在JPoint之前还是之后插入代码，具体包括以下具体的Advice：

- After包括三个Advice
    - After：在切入点执行之后执行，不论其结果
    - AfterReturning：在切入点执行成功后执行
    - AfterThrowing：在切入点执行失败后执行
- Before：在切入点前插入代码
- Around：环绕切入点的执行过程，具有修改连接点执行上下文的能力
    - 在连接点之前/之后添加额外的逻辑，例如性能分析
    - 跳过原先逻辑还执行备选的逻辑，例如缓存。只要不调用proceed()，即不执行原有的逻辑
    - 使用try-catch包裹原先逻辑，提供异常处理策略，例如事务管理
- AfterRunning: 返回通知, 在方法返回结果之后执行
- AfterThrowing: 异常通知, 在方法抛出异常之后

注意：
 
- Advice为Before和After时，切入方法的参数应该是JoinPoint
- Advice为Around时，方法参数应该为ProceedingJoinPoint，ProceedingJoinPoint继承JoinPoint，多了proceed功能，此时如果我们不调用proceed方法，被切入的方法将不会被调用，
- Around和After是不能同时作用在同一个方法上的，会产生重复切入的问题。

---
## 2 Pointcuts语法

Pointcuts语法支持两种语法：1、完全使用AspectJ的语言；2、使用纯Java语言开发，然后使用AspectJ注解。

Pointcuts语法包括：

- AspectJ切入点声明语法
- AspectJ通知定义语法：由通知声明、切入点定义、通知体三部分组成

具体的语法内容：

- 通配符
- 类型签名语法
    - 基于注解的类型签名
    - 基于泛型的类型签名
    - 联合类型签名
- 方法和构造器签名语法
    - 基本方法签名
    - 基于注解的方法签名
- 字段签名语法

### 通配符

通配符用于匹配一系列的连接点

### Java语法示例

#### call和execution

语法结构：`execution([修饰符] 返回值类型 方法名(参数)［异常模式］)`，其中修饰符和异常模式可选

示例|说明
---|---
`execution(public *.*(..))` | 所有的public方法。
`execution(* hello(..))` | 所有的hello()方法
`execution(String hello(..))` | 所有返回值为String的hello方法。
`execution(* hello(String))` | 所有参数为String类型的hello()
`execution(* hello(String..))` | 至少有一个参数，且第一个参数类型为String的hello方法
`execution(* com.aspect..*(..))` | 所有com.aspect包，以及子孙包下的所有方法
`execution(* com..*.*Dao.find*(..))` | com包下的所有一Dao结尾的类的一find开头的方法

#### within和withincode 

- within用于捕获类型，示例`within(HelloAspectDemo)`表示在HelloAspectDemo类中
- withincode用于捕获在构造器或者方法中的所有连接点，用法与within类似，`withcode()`接受的 signature 是方法

#### cflow

cflow获取的是一个控制流程。一般与其他的pointcut 进行`&&`运算。
