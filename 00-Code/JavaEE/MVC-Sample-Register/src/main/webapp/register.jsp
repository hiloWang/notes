<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 18.4.22
  Time: 18:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>用户注册</title>
</head>
<body>

<form method="post" action="${pageContext.request.contextPath}/servlet/RegisterServlet"
      onsubmit="return checkForm()">

    <%--formBean为request中的对象，用于数据回显--%>

    <table border="1px" cellpadding="0dp">
        <caption>注册</caption>
        <tr>
            <td>用户名:</td>
            <td>
                <input id="username" type="text" name="username" value="${formBean.username}"/>
                <span id="susername">${formBean.errors.username}</span>
            </td>
        </tr>
        <tr>
            <td>密码:</td>
            <td>
                <input id="password" type="text" name="password" value="${formBean.password }"/>
                <span id="spassword">${formBean.errors.password }</span>
            </td>
        </tr>
        <tr>
            <td>确认密码:</td>
            <td>
                <input id="repassword" type="text" name="repassword"
                       value="${formBean.repassword }"/>
                <span id="srepassword">${formBean.errors.repassword }</span>
            </td>
        </tr>
        <tr>
            <td>邮箱:</td>
            <td>
                <input id="email" type="text" name="email" value="${formBean.email }"/>
                <span id="semail">${formBean.errors.email}</span>
            </td>
        </tr>
        <tr>
            <td>出生日期:</td>
            <td>
                <input id="birthday" type="text" name="birthday" value="${formBean.birthday }"/>
                <span id="sbirthday">${formBean.errors.birthday}</span>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <input type="submit" value="注册"/>
                <input type="reset" value="重置"/>
            </td>
        </tr>
    </table>

</form>


<script type="text/javascript">

    function checkForm() {
        var username = document.getElementById("username");
        var password = document.getElementById("password");
        var repassword = document.getElementById("repassword");
        var email = document.getElementById("email");
        var birthday = document.getElementById("birthday");

        if (username.value == null || !/^[a-zA-Z]{3,8}$/.test(username.value)) {
            alert("用户名为3-8位字母");
            return false;
        }

        if (password.value == null || !/^\d{3,8}$/.test(password.value)) {
            alert("密码为3-8位数字");
            return false;
        }

        if (password.value !== repassword.value) {
            alert("两次密码不一致");
            return false;
        }

        var emailReg = new RegExp("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
        if (email.value == null || !emailReg.test(email.value)) {
            alert("邮箱格式错误");
            return false;
        }

        var birthdayReg = new RegExp("((((19|20)\\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\\d|3[01]))|(((19|20)\\d{2})-(0?[469]|11)-(0?[1-9]|[12]\\d|30))|(((19|20)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-8])))");
        if (birthday.value == null || !birthdayReg.test(birthday.value)) {
            alert("生日格式为：1990-01-01");
            return false;
        }
        return true;
    }

</script>

</body>
</html>
