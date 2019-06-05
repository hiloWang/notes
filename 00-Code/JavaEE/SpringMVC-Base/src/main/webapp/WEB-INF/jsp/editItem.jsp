<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>修改商品信息</title>
</head>
<body>

<form id="itemForm" action="${pageContext.request.contextPath }/item/updateItem.action">

    <input type="hidden" name="id" value="${item.id }"/> 修改商品信息：

    <table width="100%" border=1>

        <tr>
            <td>商品名称</td>
            <td><input title="商品名称" type="text" name="name" value="${item.name }"/></td>
        </tr>

        <tr>
            <td>商品价格</td>
            <td><input title="商品价格" type="text" name="price" value="${item.price }"/></td>
        </tr>

        <tr>
            <td>商品简介</td>
            <td>
                <label>
                    <textarea rows="3" cols="30" name="detail">${item.detail }</textarea>
                </label>
            </td>
        </tr>

        <tr>
            <td colspan="2" align="center">
                <input type="submit" value="提交"/>
            </td>
        </tr>

    </table>
</form>
</body>

</html>