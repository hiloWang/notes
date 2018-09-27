# Struts 2

---
## 1 Struts 2简介

Apache Struts 2是一个用于开发Java EE网络应用程序的开放源代码网页应用程序架构。它利用并延伸了Java Servlet API，鼓励开发者采用**MVC**架构。

Struts2以WebWork优秀的设计思想为基础，吸收了Struts1的部分优点，建立了一个兼容WebWork和Struts1的框架，**实现了MVC设计模式**。Struts2是Struts的下一代产品，它在Struts1和WebWork的技术基础上进行了合并，是一种全新的框架。Struts2以WebWork为核心，采用拦截器的机制来处理用户的请求，这使得业务逻辑控制器能够与ServletAPI完全脱离开。Struts2执行流程：

1. 客户端浏览器发出HTTP请求。
1. 根据web.xml配置，该请求被FilterDispatcher接收。
1. 根据struts.xml配置，找到需要调用的Action类和方法，并通过IoC方式，将值注入给Aciton。
1. Action调用业务逻辑组件处理业务逻辑，这一步包含表单验证。
1. Action执行完毕，根据struts.xml中的配置找到对应的返回结果result，并跳转到相应页面。
1. 返回HTTP响应到客户端浏览器。

---
## 2 Struts2环境配置

具体参考：[使用 Gradle 构建 Struts 2 Web 应用](https://maimieng.com/2016/21/)。

最后需要在web.xml中配置Struts2的过滤器：
```xml
<filter>
      <filter-name>struts2</filter-name>
      <filter-class>org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter</filter-class>
</filter>
<filter-mapping>
       <filter-name>struts2</filter-name>
       <url-pattern>/*</url-pattern>
</filter-mapping>
```

### Struts2入门示例

1、编写struts.xml配置文件

```xml
        <?xml version="1.0" encoding="UTF-8"?>
        <!DOCTYPE  struts PUBLIC
            "-//Apache Software Foundation//DTD Struts Configuration 2.1.7//EN"
            "http://struts.apache.org/dtds/struts-2.1.7.dtd">
        <struts><!--这是Struts2配置文件的根元素-->
            <package  name="sample"  namespace="/test"  extends="struts-default">
            <!--
            pageckage:方便管理动作元素
                name：必须有。包的名称，配置文件中必须保证唯一。
                namespace：该包的名称空间，一般是以"/"开头
                extends:集成的父包的名称。struts-default名称的包是struts2框架已经命名好的一个包。（在struts2-core库中有一个struts-default.xml中）
                abstract：是否是抽象包。没有任何action元素的包就是抽象包（java类）
            -->

                <action  name="helloworld"  class="cn.sample.action.HelloWorldAction"  method="sayHello">
                <!--
                action:代表一个请求动作
                    name：同包中必须唯一。动作的名称
                    class:负责处理的JavaBean的类全名
                    method：JavaBean中的对应处理方法。（动作方法：特点是，public String 方法名(){}）
                -->
                    <result  name="success">/1.jsp</result>
                    <!--
                    result:结果类型
                        name:动作方法返回的字符串
                        主体内容：View的具体地址。
                    -->
                </action>

            </package>
        </struts>
```

2、根据配置文件，创建需要的JavaBean和对应的动作方法，在动作方法中完成你的逻辑调用。

```java
    package cn.sample.action;
    public  class  HelloWorldAction  implements  Serializable {

        private  String  message;

        public  String  getMessage() {
            return message;
        }

        public  void  setMessage(String  message) {
            this.message = message;
        }
        public  String  sayHello(){
            message = "helloworld by struts2";
            return  "success";
        }
    }
```

3、编写View，在jsp中显示结果

```jsp
     ${message}
```

4、访问helloworld动作的方式

```
访问地址 http://localhost:8080/struts2_base/test/a/b/c/helloworld

即：应用名称/包的名称空间/动作的名称，默认情况下：访问动作名helloworld，可以直接helloworld，或者`helloworld.action`
/test/a/b/c:名称空间
helloworld：动作名称

搜索顺序：名称空间
/test/a/b/c 没有helloworld
/test/a/b    没有helloworld
/test/a 没有helloworld
/test 有了，调用执行
```


---
## 3 struts2.xml配置

- struts.xml中的package元素：方便管理action
- strutx.xml中的action元素：定义具体访问行为
- strutx.xml中的result元素 ：对访问行为的结果做处理
- 全局配置：使用包的继承性，配置全局错误结果

---
## 4 定义常量

- 常用常量
- 定义后缀
- 开发模式

---
## 5 Struts2的处理流程

Struts2使用Filter作为控制器，其处理流程如下：

```
StrutsPrepareAndExecuteFilter
|
|--Interceptor1：Struts2内置的和用户自定义的一些拦截器
|--Interceptor2
|--Interceptor3
|
|--Action：程序员编写的Action类
|
|--Result：结果处理器
|
|--Jsp/Html：最终的响应
```

---
## 6 多个struts配置文件

可以定义多个配置文件，然后在`struts.xml`中使用include导入，`struts.xml`是所有配置文件的入口

---
## 7 动态方法调用

- `!`符号
- Action通配符

---
## 8 接收请求参数

- 基本类型
- 复合类型
- JavaBean
- 数组
- 自定义类型转换器
- 如何定义转换器
- 局部配置
- 全局配置
- 2.1.6版本的Struts2接收中文表单乱码问题(POST和GET方式都乱码，Tomcat 6、7下)

---
## 9 Action中获取常用Servlet对象

- ActionContext、ServletActionContext、Servlet***Aware接口
- 原理：每一次请求都会创建对应的Action对象和ActionContext对象，方法调用在不同线程，所以可以使用ThreadLocal保存线程域的共享变量

---
## 10 文件上传

在Struts2中，文件上传内部使用也是common-fileupload库。

---
## 11 自定义拦截器

Struts2支持自定义拦截器

---
## 12 用户输入校验

- 手动校验，实习那validate方法
- 使用xml编写校验规则
- 内置校验器

---
## 13 国际化

Struts支持国际化，使用properties文件配置。

---
## 14 OGNL表达式

OGNL是Object Graphic Navigation Language(对象图导航语言)的缩写，它是一个开源项目。Struts2框架使用OGNL作为默认的表达式语言。

- 掌握常用表达式
- 利用ValueStack的含义

---
## 15 Struts标签

Struts2提供了很多有用的标签，比如防止表单重复提交

----
## 17 使用Struts插件

Struts2可以整合其他插件一起使用。
