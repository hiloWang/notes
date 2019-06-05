<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 18.4.20
  Time: 23:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
    <title>注释的编译</title>
</head>
<body>


<%!
    /*public String name;//不要用实例变量。线程安全问题

    public void m1(){
        //out.write("abc");
    }

    static{

    }*/
%>

<%--
jsp注释掉的代码，JSP引擎不会翻译
String name = "沙师弟";
out.write(name);
 --%>

<!--
    <%
    String name = "沙师弟";
    out.write(name);
     %>
-->

</body>
</html>
