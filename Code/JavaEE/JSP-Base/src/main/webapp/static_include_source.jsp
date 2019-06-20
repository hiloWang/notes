<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 18.4.21
  Time: 0:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
    <title>静态包含源</title>
</head>
<body>
我喜欢
<%String name = "傻师妹"; %>
<%@include file="/static_include_target.jsp" %>
</body>
</html>
