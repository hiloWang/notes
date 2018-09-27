# Listener(监听器)

监听器就是一个实现特定接口的普通java程序，这个程序专门用于监听一个java对象的方法调用或属性改变，当被监听对象发生上述事件后，监听器某个方法将立即被执行。

---
## 1 Servlet中的监听器

在Servlet规范中定义了多种类型的监听器，它们用于监听的事件源分别为SerlvetConext、HttpSession和ServletRequest这三个域对象。Servlet规范针对这三个对象上的操作，又把这多种类型的监听器划分为三种类型：

第一类(3个)：监听ServletRequest HttpSession ServletContext对象的创建和销毁的监听器

- `ServletRequestListener`：有什么用？统计页面的访问次数
- `HttpSessionListener`：有什么用？统计某个时间点的访问量
- `ServletContextListener`：有什么用？完成系统级别的对象的初始化。

第二类（3个）：监听ServletRequest HttpSession ServletContext中的属性（Map）变化的监听器(域中数据变化)

- `ServletRequestAttributeListener`
- `HttpSessionAttributeListener`
- `ServletContextAttributeListener`

第三类（2个）：HttpSessionBindingListener HttpSessionActivationListener 感知型监听器（不需要注册）

- `HttpSessionBindingListener` ：谁实现该接口，监测自己何时被放到HttpSession域中和何时从HttpSession域中删除。
- `HttpSessionActivationListener` 谁实现该接口，监测自己何时被钝化或激活

---
## 2 注册监听器


serlvet监听器的注册不是直接注册在事件源上，而是由WEB容器负责注册，开发人员只需在web.xml文件中使用`<listener>`标签配置好监听器，web容器就会自动把监听器注册到事件源中。一个web.xml文件中可以配置多个Servlet事件监听器，web服务器按照他们在web.xml文件中的注册顺序来加载和注册这些Servlet事件监听器。

```xml
    <listener>
        <listener-class>com.ztiany.filter.listener.AllRequesterListener</listener-class>
    </listener>
```

---
## 3 感知Session绑定的事件监听器

保存在Session域中的对象可以有多种状态：绑定到Session中；从Session域中解除绑定；随Session对象持久化到一个存储设备中；随Session对象从一个存储设备中恢复。

Servlet规范中定义了两个特殊的监听器接口来帮助JavaBean对象了解自己在Session域中的这些状态：HttpSessionBindingListener接口和HttpSessionActivationListener接口，实现这两个接口的类不需要在web.xml中注册

实现了HttpSessionBindingListener接口的JavaBean对象可以感知自己被绑定到Session中和从Session中删除的事件：
- 当对象被绑定到HttpSession对象中时，web服务器调用该对象的`void valueBound(HttpSessionBindingEvent event)`方法
- 当对象从HttpSession对象中解除绑定时，web服务器调用该对象的`void valueUnbound(HttpSessionBindingEvent event)`方法

实现了HttpSessionActivationListener接口的JavaBean(需要序列化)对象可以感知自己被活化和钝化的事件

- 当绑定到HttpSession对象中的对象将要随HttpSession对象被钝化之前，web服务器调用`sessionWillPassivate(HttpSessionBindingEvent event)`方法
- 当绑定到HttpSession对象中的对象将要随HttpSession对象被活化之后，web服务器调用该对象的`void sessionDidActive(HttpSessionBindingEvent event)`方法

```java
    public class User implements Serializable,HttpSessionBindingListener {
    
        public void valueBound(HttpSessionBindingEvent event) {
            HttpSession session = event.getSession();
        }
    
        public void valueUnbound(HttpSessionBindingEvent event) {
            
        }
        
    }
```