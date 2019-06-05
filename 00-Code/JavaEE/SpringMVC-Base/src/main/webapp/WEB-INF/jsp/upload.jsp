<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 18.6.6
  Time: 23:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>上传图片</title>
</head>
<body>

<form action="${pageContext.request.contextPath}/upload/uploadFile.action"
      enctype="multipart/form-data" method="post">

    <ul>
        <li><input title="文件1" type="file" name="files"></li>
        <li><input title="文件2" type="file" name="files"></li>
        <li><input title="文件3" type="file" name="files"></li>
        <li><input type="submit"></li>
    </ul>
</form>

</body>
</html>
