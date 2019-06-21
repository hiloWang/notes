<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 18.4.22
  Time: 18:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>用户登录</title>
</head>
<body>


<form method="post" action="${pageContext.request.contextPath}/servlet/LoginServlet"
      onsubmit="return checkForm()">

    <table border="1px" cellpadding="0dp">
        <caption>登录</caption>
        <tr>
            <td>用户名:</td>
            <td>
                <input id="username" type="text" name="username"/>
            </td>
        </tr>
        <tr>
            <td>密码:</td>
            <td>
                <input id="password" type="text" name="password"/>
            </td>

        <tr>
            <td colspan="2">
                <input type="submit" value="登录"/>
            </td>
        </tr>

    </table>

</form>


<script type="text/javascript">

    function checkForm() {

        var username = document.getElementById("username");
        var password = document.getElementById("password");

        if (username.value == null || !/^[a-zA-Z]{3,8}$/.test(username.value)) {
            alert("用户名为3-8位字母");
            return false;
        }

        if (password.value == null || !/^\d{3,8}$/.test(password.value)) {
            alert("密码为3-8位数字");
            return false;
        }

        return true;
    }
</script>

</body>
</html>
