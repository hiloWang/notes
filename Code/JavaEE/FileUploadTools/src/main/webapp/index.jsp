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

<form action="${pageContext.request.contextPath}/servlet/FileUploadServlet" method="post"
      enctype="multipart/form-data">

    <ul>
        <li><input title="文件描述" type="text" name="description" value="文件描述"></li>
        <li><input type="file" name="file1"/> :选择上传文件1</li>
        <li><input type="file" name="file2"/> :选择上传文件2</li>
        <li><input type="file" name="file3"/> :选择上传文件3</li>
        <li><input type="submit"></li>
    </ul>

</form>

</body>
</html>
