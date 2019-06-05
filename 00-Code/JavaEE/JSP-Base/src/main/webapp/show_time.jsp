<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 18.4.20
  Time: 23:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date" %>
<html>
<head>
    <title>当地时间</title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
</head>
<body>

<%
    out.write(new Date().toLocaleString());
%>


</body>
</html>
