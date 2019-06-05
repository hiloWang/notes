<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>客户管理首页</title>
    <style type="text/css">


        .page, table {
            margin: auto;
            width: 80%;
        }

        .odd {
            background-color: #c3f3c3;
        }

        .even {
            background-color: #f3c3f3;
        }

        .middle {
            vertical-align: middle;
            text-align: center;
        }

    </style>

    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">

    <script type="text/javascript" src="<c:url value="/js/jquery-1.8.3.js"/>"></script>

</head>

<body>

<div class="content">

    <form method="post"
          action="${pageContext.request.contextPath}/servlet/ControllerServlet?op=deleteMultiCustomer"
          onsubmit="return confirmDeleteAll()">

        <table border="1px" cellpadding="0px">

            <caption>客户列表</caption>

            <tr>
                <td class="middle">
                    <a href="${pageContext.request.contextPath}/add.jsp">添加</a>
                    <a href="javascript:confirmDeleteAll()">删除</a>
                </td>
            </tr>

            <tr>
                <th>
                    <input title="全选" type="checkbox" id="selectAll"/>全选
                </th>
                <th>姓名</th>
                <th>性别</th>
                <th>生日</th>
                <th>电话</th>
                <th>邮箱</th>
                <th>爱好</th>
                <th>类型</th>
                <th>描述</th>
                <th>操作</th>
            </tr>

            <c:forEach items="${page.record}" var="customer" varStatus="vs">

                <tr class="${vs.index%2==0?'odd':'even' }">
                    <th>
                            <%--ids由表单自动提交--%>
                        <input title="选择" type="checkbox" name="ids" value="${customer.id}"/>
                    </th>

                    <th>${customer.name}</th>
                    <th>${customer.gender=='male'?'男性':'女性'}</th>
                    <th>${customer.birthday}</th>
                    <th>${customer.cellphone}</th>
                    <th>${customer.email}</th>
                    <th>${customer.hobbies}</th>
                    <th>${customer.type}</th>
                    <th>${customer.description}</th>
                    <th>
                        <a href="${pageContext.request.contextPath}/servlet/ControllerServlet?op=editCustomerUI&customerId=${customer.id}">修改</a>
                        <a href="javascript:deleteCustom('${customer.id}')">删除</a>
                    </th>
                </tr>

            </c:forEach>
        </table>
    </form>

    <div class="page">
        <jsp:include page="page.jsp"/>
    </div>

</div>


<script type="text/javascript">

    $(function () {

        $("#selectAll").click(function () {
            var c = this.checked;

            $("input[name = ids]").each(function () {
                $(this).attr("checked", c);
            })
        });

    });


    function deleteCustom(customerId) {
        var sure = window.confirm("确定要删除吗?");
        if (sure) {
            window.location.href = "${pageContext.request.contextPath}/servlet/ControllerServlet?op=deleteCustomer&customerId=" + customerId;
        }
    }

    function confirmDeleteAll() {
        //判断用户是否选择了
        var selected = false;

        var ids = $("input[name='ids']");

        for (var i = 0; i < ids.length; i++) {
            if (ids[i].checked) {
                selected = true;
                break;
            }
        }

        if (selected) {
            //二次提示
            var sure = window.confirm("确定要删除所选记录吗?");
            if (sure) {
                document.forms[0].submit();
            }
        } else {
            alert("请先选择要删除的记录");
        }
    }

</script>

</body>


</html>
