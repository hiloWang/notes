<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 18.4.21
  Time: 0:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
    <title>转发源</title>
</head>
<body>

<%--
   方式1：
  RequestDispatcher rd = request.getRequestDispatcher("/forward_target.jsp");
  rd.forward(request, response);
--%>

<!--方式2 -->
<jsp:forward page="/forward_target.jsp">
    <%--用于传递参数--%>
    <jsp:param value="wujing" name="username"/>
    <jsp:param value="123" name="password"/>
</jsp:forward>


</body>
</html>
