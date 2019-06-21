<%@ page import="com.ztiany.jspbase.domain.Person" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 18.4.21
  Time: 16:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>EL 操作符</title>
</head>
<body>

<%
    pageContext.setAttribute("s1", "");

    List<String> list = new ArrayList<String>();
    pageContext.setAttribute("list", list);

    list.add("aaa");
    pageContext.setAttribute("gender", "male");
%>

${empty p1}<br/>
${empty s1}<br/>
${empty list}<br/><!-- 如果一个集合本身不是null，但是没有任何元素，返回true -->
${gender=='male'?"男性":"女性"}<br/>

<%
    session.setAttribute("user", new Person("乔丹"));//设置登录标记
%>

${user==null?'请先登录':'欢迎您：'}${user.name}


</body>
</html>
