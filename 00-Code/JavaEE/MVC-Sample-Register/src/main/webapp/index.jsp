<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 18.4.22
  Time: 16:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<html>
<head>
    <title>系统首页</title>
</head>
<body>


<div>
    <h1>项目首页</h1>
</div>

<div>
    <%--user为session的User对象--%>

    <c:if test="${sessionScope.USER == null}">
        <a href="${pageContext.request.contextPath}/login.jsp">登录</a>
        <a href="${pageContext.request.contextPath}/register.jsp">注册</a>
    </c:if>

    <c:if test="${sessionScope.USER != null}">
        欢迎您：${sessionScope.USER.username}，<a href="${pageContext.request.contextPath}/servlet/LogoutServlet">注销</a>
    </c:if>
</div>

</body>
</html>
