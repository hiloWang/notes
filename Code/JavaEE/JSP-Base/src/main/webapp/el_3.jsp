<%@ page import="com.ztiany.jspbase.domain.Person" %><%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 18.4.21
  Time: 17:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>EL 隐式对象</title>
</head>
<body>


<%-- ----------------------------------PageContext---------------------------------- --%>
${pageContext}<br/>
当前应用名称：${pageContext.request.contextPath}<br/>
${pageContext.response.characterEncoding}<br/>
HttpSession的ID：<%=session.getId()%><br/>
HttpSession的ID：${pageContext.session.id}<br/>


<hr/>
<%
    pageContext.setAttribute("p", "pp");
    request.setAttribute("p", "rp");
    session.setAttribute("p", "sp");
    application.setAttribute("p", "ap");
%>
${p}<br/><!-- pageContext.findAttribute("p") -->
${pageScope.p}<br/><!-- pageContext.getAttribute("p",Page_Scope) -->
${requestScope.p}<br/>
${sessionScope.p}<br/>
${applicationScope.p}<br/>


<%-- ----------------------------------获取session中的数据---------------------------------- --%>
<%
    //pageContext.setAttribute("user", "haha");
    session.setAttribute("user", new Person());
%>
${user.name}<br/>
${sessionScope.user.name}<br/>


<%-- ----------------------------------获取消息头和请求参数的值---------------------------------- --%>
<hr/>
获取消息头的值：Accept-Encoding<br/>
JSP:<%=request.getHeader("Accept-Encoding") %><br/>
EL:${header['Accept-Encoding']}<br/>
${headerValues['Accept-Encoding'][0]}<br/>
<!-- el_3.jsp?name=abc&password=123&password=456 -->
${param.name}:${param.password }<br/>
${paramValues.name[0]}:${paramValues.password[0] }:${paramValues.password[1] }<br/>


<%-- ----------------------------------获取cookie的值---------------------------------- --%>
${cookie.JSESSIONID.value}<br/>


<%-- ----------------------------------获取初始化参数的值---------------------------------- --%>
${initParam.code}


</body>
</html>
