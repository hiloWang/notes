<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 18.4.21
  Time: 0:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
    <title>动态包含源</title>
</head>
<body>

我喜欢
<%String name = "傻师妹"; %>
<jsp:include page="/dynamic_inclued_target.jsp"/>
</body>
</html>
