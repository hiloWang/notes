<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 18.4.21
  Time: 0:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
    <title>静态包含目标</title>
</head>
<body>

芙蓉姐姐<br/>
<%--由于是静态包含，目标和源被编译成同一个Java类，所以目标可以引用源中的变量--%>
<%=name %>
</body>
</html>
