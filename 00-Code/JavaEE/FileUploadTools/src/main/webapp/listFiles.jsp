<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>已上传文件列表</title>
    <style type="text/css">
        li {
            list-style: none;
        }
    </style>
</head>
<body>

<h1>已上传的文件</h1>

<ul>
    <c:forEach items="${map}" var="entry">
        <li>
                ${entry.value} <a
                href="${pageContext.request.contextPath}/servlet/DownloadServlet?filename=${entry.key}">下载</a>
        </li>
    </c:forEach>
</ul>

</body>

</html>
