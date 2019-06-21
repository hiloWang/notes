<%@ page import="com.ztiany.jspbase.domain.Student" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="e" uri="http://www.sample.com/jsp/taglib/example" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
    <title>自定义标签</title>
</head>
<body>


<%
    pageContext.setAttribute("grade", "B");
%>

<e:choose>
    <e:when test="${grade=='A'}">
        优秀
    </e:when>
    <e:otherwise>
        尚需努力
    </e:otherwise>
</e:choose>


<%
    List list = new ArrayList();
    list.add(new Student("WJ", "女性", 23));
    list.add(new Student("HMM", "女性", 23));
    list.add(new Student("ZSL", "男性", 25));
    pageContext.setAttribute("list", list);
%>

<e:forEach items="${list}" var="s">
    ${s.name}:${s.gender}<br/>
</e:forEach>

<hr/>

</body>
</html>
