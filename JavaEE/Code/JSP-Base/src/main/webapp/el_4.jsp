<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 18.4.21
  Time: 17:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="mfn" uri="http://www.ztiany.com/functions" %>
<html>
<head>
    <title>EL 函数</title>
</head>
<body>



<%--contains、substring函数--%>
${fn:contains('abcdefg','a')}<br/>
<%pageContext.setAttribute("s", "Tomcat"); %>
${fn:contains(s,'catttt') }<br/>
${fn:substring('Tomcat',2,4) }<br/>
${fn:substring('Tomcat',2,4000) }<br/><!-- EL没有数组越界 -->

<%
    String s1[] = {"a", "b", "c"};
    pageContext.setAttribute("s1", s1);
%>



<%--join函数--%>
${fn:join(s1,"-") }<br/>
<%
    pageContext.setAttribute("s2", "<h1>ITHEIMA</h1>");
%>


<%--escape为转为--%>
${fn:escapeXml(s2)}<br/>


<%--自定义标签函数--%>
${mfn:change("abcedfghijk")}<br/>


</body>
</html>
