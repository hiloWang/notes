<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>上传文件</title>
    <style type="text/css">
        li {
            list-style: none;
        }
    </style>
</head>

<body>

<form method="post" enctype="multipart/form-data"
      action="${pageContext.request.contextPath}/servlet/UploadServlet">

    <ul>
        <li><input title="照片" type="file" name="photo"></li>
        <li><input title="名字" type="text" name="name"></li>
        <li><input type="submit"></li>
    </ul>

</form>

</body>
</html>
