# Servlet——Response&Request

- Web服务器收到客户端的http请求，会针对每一次请求，分别创建一个用于代表请求的request对象、和代表响应的response对象。
- request和response对象即然代表请求和响应，那我们要获取客户机提交过来的数据，只需要找request对象就行了。要向容器输出数据，只需要找response对象就行了。

---
## 1 Response

- HttpServletResponse对象代表服务器的响应。这个对象中封装了向客户端发送数据、发送响应头，发送响应状态码的方法。

### 1.1 Response编码问题

#### 用OutputStream(字节流)发送数据

1. `response.getOutputStream().write(“中国”.getBytes());`以默认编码发送数据
2. `response.getOutputStream().write("中国".getBytes("UTF-8"));`以UTF-8编码发送数据，浏览器如果默认用GB2312则会出现乱码

解决办法：

- 让用户通过更改浏览器的编码方式(不可取，不可能要求用户修改)
- 通过设置响应头告知客户端编码方式：`response.setHeader(“Content-type”, “text/html;charset=UTF-8”);`告知浏览器数据类型及编码
- 通过meta标签模拟请求头:`out.write("<meta http-equiv='Content-Type' content='text/html; charset=utf-8' />".getBytes());`
- 通过`response.setContentType("text/html;charset=UTF-8");`方式

总结：程序以什么编码输出，就需要告知客户端以什么编码显示。

>输出字符“1”：用response.getOutputStream().write(1);出现的问题？输出的是1的ASSCII编码

#### 用PrintWriter(字符流)发送数据

```
//用PrintWriter(字符流)发送数据，有没有乱码？
response.getWriter().write(“中国” );
```

答案是有，因为以默认编码发送数据ISO-8859-1（没有中国二字编码），此时会发生乱码。

解决办法：
```java
response.setCharacterEncoding(“UTF-8”);//更改编码为UTF-8
response.setHead(“Context-type”,”text/html;charset=UTF-8”);//告诉客户端编码方式
```

由于经常改动编码，**response提供了一种更简单的方式**：`response. setContentType(“text/html;charset=UTF-8”);`，其作用相当于以上两条代码。即更改字符流使用的码表为UTF-8并且通知浏览器用UTF-8进行显示


#### 响应头的字段需要使用URL编码

示例：
```
//通知浏览器以下载的方式打开，中文文件名文件下载有问题：如果不使用URL编码文件名没有了
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
        response.setContentType("application/octet-stream");
```



### 1.2 response控制缓存

```
//控制客户端不要缓存
response.setHeader("Expires", "-1");
response.setHeader("Cache-Control", "no-cache");
response.setHeader("Pragma", "no-cache");

//控制资源的缓存时间
response.setDateHeader("Expires", System.currentTimeMillis()+1*60*60*1000);//单位是毫秒值，相对于当前时间的一个时间
```


### 1.3 response细节

- getOutputStream和getWriter方法分别用于得到输出二进制数据、输出文本数据的ServletOuputStream、Printwriter对象。
- getOutputStream和getWriter这两个方法互相排斥，调用了其中的任何一个方法后，就不能再调用另一方法。  会抛异常。
- Servlet程序向ServletOutputStream或PrintWriter对象中写入的数据将被Servlet引擎从response里面获取，Servlet引擎将这些数据当作响应消息的正文，然后再与响应状态行和各响应头组合后输出到客户端。
- Serlvet的service方法结束后，Servlet引擎将检查getWriter或getOutputStream方法返回的输出流对象是否已经调用过close方法，如果没有，Servlet引擎将调用close方法关闭该输出流对象。

### 1.4 重定向

重定向机制的运作流程

1. 用户在浏览器端输入特定URL，请求访问服务器端的某个组件
2. 服务器端的组件返回一个状态码为302的响应结果。
3. 当浏览器端接收到这种响应结果后，再立即自动请求访问另一个web组件
4. 浏览器端接收到来自另一个web组件的响应结果。

```
方式1
response.setStatus(302);//设置响应码
response.setHeader("Location", "http://www.google.cn");

//方式2
response.sendRedirect("http://www.itcast.cn");
```


特点：

- Servlet源组件生成的响应结果不会被发送到客户端。`response.sendRedirect(String location)`方法一律返回状态码为302的响应结果。原理为使用302/307状态码和location头即可实现重定向
- 如果源组件在进行重定向之前，已经提交了响应结果，会抛出IllegalStateException。为了避免异常，不应该在源组件中提交响应结果。
- 在Servlet源组件重定向语句后面的代码也会执行。
- 源组件和目标组件不共享同一个ServletRequest对象。
- 对于`sendRedirect(String location)`方法的参数，如果以“/”开头，表示相对于当前服务器根路径的URL。以“http"//”开头，表示一个完整路径。
- 目标组件不必是同一服务器上的同一个web应用的组件，它可以是任意一个有效网页。
- 浏览器地址栏会变，并发送2次请求，增加服务器负担

---
## 2 Request

HttpServletRequest对象代表客户端的请求，当客户端通过HTTP协议访问服务器时，HTTP请求头中的所有信息都封装在这个对象中，开发人员通过这个对象的方法，可以获得客户这些信息。

### 2.1 获得客户机信息

```
getRequestURL方法返回客户端发出请求时的完整URL。
getRequestURI方法返回请求行中的资源名部分。
getQueryString 方法返回请求行中的参数部分。
getRemoteAddr方法返回发出请求的客户机的IP地址
getRemoteHost方法返回发出请求的客户机的完整主机名
getRemotePort方法返回客户机所使用的网络端口号
getLocalAddr方法返回WEB服务器的IP地址。
getLocalName方法返回WEB服务器的主机名
getMethod得到客户机请求方式

//获得客户机请求头
getHead(name)方法 
getHeaders(String name)方法 
getHeaderNames方法 

//获得客户机请求参数(客户端提交的数据)
getParameter(name)方法
getParameterValues（String name）方法
getParameterNames方法 
getParameterMap方法  //做框架用，非常实用
```

### 2.2 请求乱码问题

**浏览器是什么编码就以什么编码传送数据**，针对各种情况的解决方法如下：

#### POST方式

请求参数在请求正文中，首先尝试从Request对象获取编码，然后根据获取的值设置查询的码表：

```
request.getContentType();//可能为null
request.getCharacterEncoding();//可能为null
```
但是这两个方法可能返回null，所以不应该以此作为依据，如果不设置编码，服务器就会以服务器默认的编码读取数据(Tomcat 6/7 默认编码为ISO-8859-1)，可行的解决方法为在response中告知浏览器服务器使用的编码，然后浏览器默认就会以什么编码传输数据。假如response默认全都使用UTF-8，则对于post方式的请求，设置request使用UTF-8的码表即可：

```
request.setCharacterEncoding(“UTF-8”);对POST方式有效
```

#### GET

GET的请求参数放在URL中，浏览器会对其进行URL编码，`request.setCharacterEncoding`方法是针对请求正文设置的，同样，如果不设置编码，服务器就会以服务器默认的编码读取数据(Tomcat 6/7 默认编码为ISO-8859-1)，因此解决方案是先根据默认码表查出字符串，再重新以UTF-8编码：

```
String username = request.getParameter("username");//ISO-8859-1
byte b[] = username.getBytes("ISO-8859-1");
username = new String(b, "UTF-8");
```

#### 更改服务器的默认编码

更改Tomcat的配置解决URL编码问题：`<Connector URIEncoding=“UTF-8”/>`，但是这样仅仅是针对Tomcat，而我们编写的应用应该是适用于其他服务器的，所以不建议使用此种方式。


### 2.3 request常见应用

- request对象实现请求转发：请求转发指一个web资源收到客户端请求后，通知服务器去调用另外一个web资源进行处理。
- request对象提供了一个getRequestDispatcher方法，该方法返回一个RequestDispatcher对象，调用这个对象的forward方法可以实现请求转发。
- request对象同时也是一个域对象，开发人员通过request对象在实现转发时，把数据通过request对象带给其它web资源处理。
  - setAttribute方法
  - getAttribute方法
  - removeAttribute方法
  - getAttributeNames方法


### 2.4 转发和包含

一个Servlet对象无法获得另一个Servelt对象的引用；如果需要多个Servet组件共同协作(数据传递)，只能使用Servelt规范为我们提供的两种方式：

- 请求转发：Servlet(源组件)先对客户请求做一些预处理操作，然后把请求转发给其他web组件(目标组件)来完成包括生成响应结果在内的后续操作。
- 包含：Servelt(源组件)把其他web组件(目标组件)生成的响应结果包含到自身的响应结果中。

转发和请求的共同点

- 源组件和目标组件处理的都是同一个客户请求，源组件和目标组件共享同一个ServeltRequest和ServletResponse对象
- 目标组件都可以为Servlet、JSP或HTML文档
- 都依赖 `javax.servlet.RequestDispatcher`接口

#### RequestDispather

表示请求分发器，它有两个方法：
```java
//forward():把请求转发给目标组件
public void forward(ServletRequest request,ServletResponse response)
             throws ServletException,java.io.IOException

//include():包含目标组件的响应结果
public void include(ServletRequest request,ServletResponse response)
             throws ServletException,java.io.IOException
```

得到RequestDispatcher对象，RequestDispatcher的转发时的路径(假设应用根路径为`/ServletBase`)：

1. `ServletContext对象的getRequestDispather(String path)`：path必须用绝对路径，即以`/`开头，若用相对路径会抛出异常IllegalArgumentException。，表示当前应用`/ServletBase`（绝对路径）
2. `ServletRequest对象的getRequestDispatcher(String path)`：path可以用绝对路径也可以用相对路径，若以绝对路径等作用与方式1等效。如使用相对则相对于当前Servlet的路径


#### 转发

转发会清空用于存放响应正文数据的缓冲区，如果目标组件为Servlet或JSP，就调用它们，把它们产生的响应结果发送到客户端；如果目标组件为文件系统中的静态HTML文档，就读取文档中的数据并把它发送给客户端。

特点：

1. 由于forward()方法先清空用于存放响应正文数据的缓冲区，因此源组件生成的响应结果不会被发送到客户端，只有目标组件生成的响应结果才会被送到客户端。
2. 如果源组件在进行请求转发之前，已经提交了响应结果（如调用了response的flush或close方法），那么`forward()`方法会抛出IllegalStateException。为了避免该异常，不应该在源组件中提交响应结果。
3. 源组件与目标组件的request不是同一个对象，但是可以通过setAttribute来传递数据

#### 包含

如果包含的目标组件为Servlet或JSP，就执行它们，并把它们产生的响应正文添加到源组件的响应结果中；如果目标组件为HTML文档，就直接把文档的内容添加到源组件的响应结果中。返回到源组件的服务方法中，继续执行后续代码块。

特点：

1. 源组件与被包含的目标组件的输出数据都会被添加到响应结果中。
2. 在目标组件中对响应状态代码或者响应头所做的修改都会被忽略。
3. 源组件与目标组件的response不是同一个对象


### 2.5 生命周期

ServletRequest对象的生命周期：用户发出请求时创建，响应结束销毁。

### 2.6 请求范围

- **web应用范围**内的共享数据作为ServeltContext对象的属性而存在(setAttribute)，只要共享ServletContext对象也就共享了其数据。
- **请求范围**内的共享数据作为ServletRequest对象的属性而存在(setAttribute)，只要共享ServletRequest对象也就共享了其数据。


---
## 3 路径的写法

路径的写法总结：

- 相对路径
- 绝对路径（建议）
- 使用绝对路径时，什么时候要加上项目名称？地址给客户端用的就要加

---
## 4 乱码问题总结

以上乱码问题主要针对Tomcat6，因为：
- Tomcat6和Tomcat7使用的默认编码为IOS8859-1
- Tomcat8以后使用默认编码为UTF-8