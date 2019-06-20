<%@ page import="com.ztiany.jspbase.domain.Person" %>
<%@ page import="com.ztiany.jspbase.domain.Student" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.HashSet" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Set" %><%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 18.4.21
  Time: 18:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>JSTL if和for表达式</title>
</head>
<body>

<%-- ----------------------------------if的使用---------------------------------- --%>
<hr/>

<%
    Person person = new Person("张学友");
    pageContext.setAttribute("user", person);

    List<String> cartList = new ArrayList<String>();
    cartList.add("颈椎康复指南");
    cartList.add("JavaScript权威指南");
    cartList.add("Python高级编程");
    cartList.add("充气娃娃");
    session.setAttribute("cart", cartList);
%>

<core:if test="${user==null}">
    请先登录
</core:if>
<core:if test="${user != null}">

    欢迎您：${user.name}<br/>

    <core:if test="${empty cart}">
        您还没有购买商品
    </core:if>
    <core:if test="${!empty cart}">
        您购买的商品如下：<br/>

        <core:forEach items="${cart}" var="product" varStatus="vs">
            ${vs.count}：${product}<br/>
        </core:forEach>

    </core:if>

</core:if>

<%-- ---------------------------------- foreach的使用 ---------------------------------- --%>
<hr/>
<%
    List<Student> students = new ArrayList<Student>();
    students.add(new Student("张学友", "女性", 24));
    students.add(new Student("张胜利", "男性", 23));
    students.add(new Student("张三丰", "女性", 24));
    students.add(new Student("张无忌", "男性", 24));
    students.add(new Student("张家辉", "男性", 24));
    students.add(new Student("张曼玉", "男性", 24));

    pageContext.setAttribute("students", students);
%>
<table border="1" width="60%">
    <tr>
        <th>姓名</th>
        <th>性别</th>
        <th>年龄</th>
        <th>第一个</th>
        <th>最后一个</th>
        <th>索引</th>
        <th>计数</th>
    </tr>

    <!--
        varStatus:指定一个变量名，引用一个对象。该对象记录着当前遍历的元素的一些信息
            boolean isFirst():是否是第一条记录
            boolean isLast()
            int getIndex():获取索引，从0开始
            int getCount():计数，从1开始
     -->
    <core:forEach items="${students}" var="s" varStatus="vs">
        <tr class="${vs.index%2==0?'odd':'even' }">
            <td>${s.name}</td>
            <td>${s.gender}</td>
            <td>${s.age}</td>
            <td>${vs.first}</td>
            <td>${vs.last}</td>
            <td>${vs.index}</td>
            <td>${vs.count}</td>
        </tr>
    </core:forEach>
</table>

<%-- ---------------------------------- foreach 编译字符串、List、Set、Map ---------------------------------- --%>
<hr/>
<%
    String str[] = {"a", "b", "c"};
    pageContext.setAttribute("str", str);
%>

<core:forEach items="${str}" var="s" step="2">
    ${s}<br/>
</core:forEach>

<hr/>
<%
    List<String> list = new ArrayList<String>();
    list.add("aa");
    list.add("bb");
    list.add("cc");
    pageContext.setAttribute("list", list);
%>
<core:forEach items="${list}" var="s">
    ${s}<br/>
</core:forEach>

<hr/>
<%
    Set<String> set = new HashSet<String>();
    set.add("aaa");
    set.add("bbb");
    set.add("ccc");
    pageContext.setAttribute("set", set);
%>
<core:forEach items="${set}" var="s">
    ${s}<br/>
</core:forEach>


<hr/>
<%
    Map<String, String> map = new HashMap<String, String>();
    map.put("a", "aaaa");
    map.put("b", "bbbb");
    map.put("c", "cccc");
    pageContext.setAttribute("map", map);
%>

<!-- me:指向的类型Map.Entry -->
<core:forEach items="${map}" var="me">
    ${me.key}=${me.value}<br/>
</core:forEach>


<%-- ---------------------------------- foreach 实现递增遍历 ---------------------------------- --%>
<hr/>
<core:forEach begin="1" end="100" var="s">
    ${s}
</core:forEach>
<hr/>
<core:forEach begin="1" end="100" var="s" step="2">
    ${s}
</core:forEach>

</body>
</html>
