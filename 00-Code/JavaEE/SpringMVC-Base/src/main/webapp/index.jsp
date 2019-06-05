<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
    <title>SpringMVC sample index</title>
</head>
<body>

<ul>
    <li>
        <a href="${pageContext.request.contextPath}/item/itemList.action">ItemList</a>
    </li>
    <li>
        <a href="${pageContext.request.contextPath}/user/userList.action">UserList</a>
    </li>
    <li>
        <a href="${pageContext.request.contextPath}/upload/uploadPage.action">uploadFile</a>
    </li>
    <li>
        <a href="${pageContext.request.contextPath}/user/json/login.action">请求json</a>
    </li>
</ul>

</body>
</html>
