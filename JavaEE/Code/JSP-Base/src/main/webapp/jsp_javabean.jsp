<%@ page import="com.ztiany.jspbase.domain.Student" %><%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 18.4.21
  Time: 15:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>JSP 操作JavaBean标签</title>
</head>
<body>

<%
    Student student = new Student("张三", "男", 28);
    pageContext.setAttribute("key1", student);
%>

<jsp:useBean id="key1" class="com.ztiany.jspbase.domain.Student" scope="page"/>
<jsp:useBean id="key2" class="com.ztiany.jspbase.domain.Student" scope="page"/>
<%--输出key2--%>
<hr/>
输出key2：<%=key2%>

<%--输出key1--%>
<hr/>
key1的名字
<jsp:getProperty name="key1" property="name"/>
<br/>
key1的性别
<jsp:getProperty name="key1" property="gender"/>
<br/>
key1的年龄
<jsp:getProperty name="key1" property="age"/>
<br/>

<%--设置key2--%>
<jsp:setProperty name="key2" property="name" value="王五"/>
<jsp:setProperty name="key2" property="gender" value="男"/>
<jsp:setProperty name="key2" property="age" value="55"/>
<hr/>
key2的名字
<jsp:getProperty name="key2" property="name"/>
<br/>
key2的性别
<jsp:getProperty name="key2" property="gender"/>
<br/>
key2的年龄
<jsp:getProperty name="key2" property="age"/>
<br/>

</body>
</html>
