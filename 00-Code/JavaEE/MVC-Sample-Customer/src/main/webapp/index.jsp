<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
    <title>客户管理首页</title>
</head>
<body>

<%--请求转发--%>
<jsp:forward page="/servlet/ControllerServlet">
    <jsp:param value="showAll" name="op"/>
</jsp:forward>

</body>
</html>
