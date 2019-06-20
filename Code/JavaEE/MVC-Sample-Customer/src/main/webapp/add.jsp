<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>添加客户</title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">

    <script type="text/javascript" src="<c:url value="/js/jquery-1.8.3.js"/>"></script>

    <style type="text/css">
        table {
            margin: auto;
        }
    </style>

</head>

<body>

<form action="${pageContext.request.contextPath}/servlet/ControllerServlet?op=addCustomer"
      method="post" onsubmit="return submitChecked()">

    <table border="1" cellpadding="0dp">
        <tr>
            <td>姓名：</td>
            <td>
                <input title="姓名" type="text" name="name"/>
            </td>
        </tr>
        <tr>
            <td>性别：</td>
            <td>
                <input title="男" type="radio" name="gender" value="male" checked="checked">男
                <input title="女" type="radio" name="gender" value="female">女<br/>
            </td>
        </tr>
        <tr>
            <td>生日：(yyyy-MM-dd)</td>
            <td>
                <input title="生日" name="birthday"/>
            </td>
        </tr>
        <tr>
            <td>电话：</td>
            <td>
                <input title="电话" name="cellphone"/>
            </td>
        </tr>
        <tr>
            <td>邮箱：</td>
            <td>
                <input title="邮箱" name="email"/>
            </td>
        </tr>
        <tr>
            <td>爱好：</td>
            <td>
                <input title="吃饭" type="checkbox" name="hobby" value="吃饭">吃饭
                <input title="睡觉" type="checkbox" name="hobby" value="睡觉">睡觉
                <input title="学Java" type="checkbox" name="hobby" value="学Java">学Java
            </td>
        </tr>
        <tr>
            <td>类型：</td>
            <td>
                <input title="普通客户" type="radio" name="type" value="普通客户" checked="checked"/>普通客户
                <input title="VIP" type="radio" name="type" value="VIP"/>VIP
            </td>
        </tr>
        <tr>
            <td>描述：</td>
            <td>
                <textarea title="描述" rows="3" cols="38" name="description"></textarea>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <input type="submit" value="添加"/>
            </td>
        </tr>
    </table>

</form>

<script type="text/javascript">

    function submitChecked() {
        //表单校验，略
        return true;
    }

</script>

</body>
</html>
