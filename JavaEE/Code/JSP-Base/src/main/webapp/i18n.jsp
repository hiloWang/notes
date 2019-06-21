<%@ page import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
<head>
    <fmt:setLocale value="${pageContext.request.locale}"/>
    <fmt:setBundle basename="com.ztiany.jspbase.resources.msg" var="msg" scope="page"/>
    <title><fmt:message key="jsp.login.title" bundle="${msg}"/></title>

    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">

</head>

<body>

<form action="">
    <fmt:message key="jsp.login.username" bundle="${msg}"/>：<input title="username"
                                                                   name="username"/><br/>
    <fmt:message key="jsp.login.password" bundle="${msg}"/>：<input title="password"
                                                                   name="password"/><br/>
    <input type="submit" value="<fmt:message key="jsp.login.submit" bundle="${msg}"/>"/>
</form>

<%
    pageContext.setAttribute("birthday", new Date());
%>

<fmt:formatDate value="${birthday}"/><br/>
<fmt:formatDate value="${birthday}" type="time"/><br/>
<fmt:formatDate value="${birthday}" type="both"/><br/>
<fmt:formatDate value="${birthday}" type="both" dateStyle="long" timeStyle="full"/><br/>
<fmt:formatDate value="${birthday}" type="both" pattern="yyyy-MM-dd HH:mm:ss"/><br/>

</body>

</html>
