<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>修改客户</title>

    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">

</head>

<body>
<form action="${pageContext.request.contextPath}/servlet/ControllerServlet?op=updateCustomer"
      method="post">

    <input type="hidden" name="id" value="${customer.id}">

    <table border="1" width="438">
        <tr>
            <td>姓名：</td>
            <td>
                <input type="text" name="name" value="${customer.name}"/>
            </td>
        </tr>
        <tr>
            <td>性别：</td>
            <td>
                <input type="radio" name="gender"
                       value="male" ${customer.gender=='male'?'checked="checked"':'' }>男
                <input type="radio" name="gender"
                       value="female" ${customer.gender=='female'?'checked="checked"':'' }>女<br/>
            </td>
        </tr>
        <tr>
            <td>生日：(yyyy-MM-dd)</td>
            <td>
                <input name="birthday" value="${customer.birthday }"/>
            </td>
        </tr>
        <tr>
            <td>电话：</td>
            <td>
                <input name="cellphone" value="${customer.cellphone }"/>
            </td>
        </tr>
        <tr>
            <td>邮箱：</td>
            <td>
                <input name="email" value="${customer.email }"/>
            </td>
        </tr>
        <tr>
            <td>爱好：</td>
            <td>
                <input type="checkbox" name="hobby"
                       value="吃饭" ${fn:contains(customer.hobbies,'吃饭')?'checked="checked"':'' }>吃饭
                <input type="checkbox" name="hobby"
                       value="睡觉" ${fn:contains(customer.hobbies,'睡觉')?'checked="checked"':'' }>睡觉
                <input type="checkbox" name="hobby"
                       value="学Java" ${fn:contains(customer.hobbies,'学Java')?'checked="checked"':'' }>学Java
            </td>
        </tr>
        <tr>
            <td>类型：</td>
            <td>
                <input type="radio" name="type"
                       value="普通客户" ${customer.type=='普通客户'?'checked="checked"':'' } />普通客户
                <input type="radio" name="type"
                       value="VIP"  ${customer.type=='VIP'?'checked="checked"':'' }/>VIP
            </td>
        </tr>
        <tr>
            <td>描述：</td>
            <td>
                <textarea rows="3" cols="38" name="description">${customer.description}</textarea>
            </td>
        </tr>

        <tr>
            <td colspan="2">
                <input type="submit" value="保存"/>
            </td>
        </tr>

    </table>
</form>

</body>
</html>
