<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 18.4.21
  Time: 0:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <title>转发目标</title>
</head>
<body>

<%
    String username = request.getParameter("username");
    out.write(username);
    String password = request.getParameter("password");
    out.write(password);
%>

</body>
</html>
