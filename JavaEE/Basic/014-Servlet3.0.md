# Servlet3.0新特性

运行环境要求：
- Tomcat7.0及以上版本
- JDK6.0及以上版本


---
## 1 添加注解的支持

Servlet3.0，可以不用配置xml，直接使用注解配置Servlet的访问路径。

- `@WebServlet`
- `@WebInitParam`
- `@WebListener`
- `@WebFilter`

示例：
```
@WebServlet(value = { "/servlet/ServletDemo1", "/servlet/ServletDemo11" }, initParams = {
        @WebInitParam(name = "encoding", value = "UTF-8"),
        @WebInitParam(name = "XXX", value = "YYY") })
public class ServletDemo1 extends HttpServlet {

}
```

## 2 Fragment

Servlet 3.0 引入了称之为“Web 模块部署描述符片段”的 web-fragment.xml 部署描述文件，该文件必须存放在 JAR 文件的 META-INF 目录下，该部署描述文件可以包含一切可以在 web.xml 中定义的内容。JAR 包通常放在 WEB-INF/lib 目录下，除此之外，所有该模块使用的资源，包括 class 文件、配置文件等，只需要能够被容器的类加载器链加载的路径上，比如 classes 目录等。



---
## 3 文件上传支持

使用`@MultipartConfig`注解标注的Servlet将文件上传，读取和保存上传文件将变得异常简单：

```
@WebServlet("/servlet/UploadServlet")
@MultipartConfig/
public class UploadServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        System.out.println(name);
        //上传字段
        Part photoPart = request.getPart("photo");
        //取文件名  Content-Disposition: form-data; name="photo"; filename="2.jpg"

        String value = photoPart.getHeader("Content-Disposition");
        int filenameIndex = value.indexOf("filename=")+10;
        String filename = value.substring(filenameIndex, value.length()-1);
        photoPart.write(getServletContext().getRealPath("/WEB-INF/files")+"/"+filename);
    }

}
```


---
## 4 异步处理

```
//首先要配置Servlet，添加asyncSupported=true
 @WebServlet(urlPatterns="/AServlet", asyncSupported=true)
public class AsyncDemoServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
    throws IOException, ServletException {
        //在子线程中执行业务调用，并由其负责输出响应，主线程退出
        AsyncContext ctx = req.startAsync();
        //在异步线程中通过ctx可以继续向客户端写数据
        new Thread(new Executor(ctx)).start();
    }

}


//AsyncContext的complete()方法用于通知Tomcat我们异步线程已经执行结束了，Tomcat才会及时的断开与浏览器的连接！
```



---
## 5  Http-Only

Cookie是如果处理不好，是有安全隐患的，3.0为Cookie添加了Http-Only属性，如果Cookie的HttpOnly为true，则客户端的脚本无法读取该cookie。

```
//服务端
        Cookie c = new Cookie("c1", "wj");
        c.setPath(request.getContextPath());
        c.setMaxAge(Integer.MAX_VALUE);
        c.setHttpOnly(true);
        response.addCookie(c);
        

//客户端
  <script type="text/javascript">
        alert(document.cookie);
  </script>
```


---
## 附不同版本的servlet标准的web.xml配置

```xml
servlet 3.1
<?xml version="1.0" encoding="UTF-8"?>  
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"   
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"  
        xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee  
         http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"  
        version="3.1">

</web-app>

servlet 3.0
<?xml version="1.0" encoding="UTF-8"?>  
<web-app xmlns="http://java.sun.com/xml/ns/javaee"  
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"  
          xsi:schemaLocation="http://java.sun.com/xml/ns/javaee  
          http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"  
          version="3.0">

</web-app>

servlet 2.5
<?xml version="1.0" encoding="UTF-8"?>  
<web-app xmlns="http://java.sun.com/xml/ns/javaee"  
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"  
          xsi:schemaLocation="http://java.sun.com/xml/ns/javaee  
          http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd"  
          version="2.5">

</web-app>
```

---
## 引用

- [Servlet 3.0 新特性详解 - IBM](https://www.ibm.com/developerworks/cn/java/j-lo-servlet30/index.html)