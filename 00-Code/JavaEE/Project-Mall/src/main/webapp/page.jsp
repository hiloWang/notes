<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page pageEncoding="UTF-8" %>

<!-- 分页显示 -->

第${page.currentPage}页/共${page.totalPageNum}页&nbsp;&nbsp;

<a href="${pageContext.request.contextPath}">首页</a>&nbsp;&nbsp;

<a href="${pageContext.request.contextPath}${page.url}&currentPage=${page.previousPage}">上一页</a>&nbsp;&nbsp;

<c:forEach begin="${page.startPageNum}" end="${page.endPageNum}" var="num">
    <a href="${pageContext.request.contextPath}${page.url}&currentPage=${num}">${num }</a>
</c:forEach>

<a href="${pageContext.request.contextPath}${page.url}&currentPage=${page.nextPage}">下一页</a>&nbsp;&nbsp;

<a href="${pageContext.request.contextPath}${page.url}&currentPage=${page.totalPageNum}">尾页</a>&nbsp;&nbsp;

去：<input title="页码" type="text" size="3" onchange="jump(this)"/>页


<script type="text/javascript">
    function jump(inputObj) {
        var num = inputObj.value;
        if (!/^[1-9][0-9]*$/.test(num)) {
            alert("请输入正确的页码");
            return;
        }
        if (num >${page.totalPageNum}) {
            alert("页码不能超过总页数");
            return;
        }
        window.location.href = "${pageContext.request.contextPath}${page.url}&currentPage=" + num;
    }

</script>