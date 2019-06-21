<%@ page import="com.ztiany.jspbase.domain.Student" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>

<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 18.4.20
  Time: 23:15
  To change this template use File | Settings | File Templates.
--%>
<html>
<head>
    <title>展示学生表格</title>
</head>
<body>

<%
    List<Student> list = new ArrayList<Student>();
    list.add(new Student("傻师妹", "女性", 26));
    list.add(new Student("张胜利", "男性", 27));
    list.add(new Student("何明明", "女性", 28));
    list.add(new Student("小熊", "男性", 29));
    list.add(new Student("邹阳", "女性", 30));
%>

<table border="1px">
    <tr>
        <th>姓名</th>
        <th>性别</th>
        <th>年龄</th>
    </tr>

    <%
        for (Student student : list) {
    %>

    <tr>
        <td>
            <%=student.getName() %>
        </td>
        <td>
            <%=student.getGender() %>
        </td>
        <td>
            <%=student.getAge() %>
        </td>
    </tr>

    <%
        }
    %>

</table>

</body>
</html>
