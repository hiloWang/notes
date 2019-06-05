<%@ page import="com.ztiany.jspbase.domain.Address" %>
<%@ page import="com.ztiany.jspbase.domain.Person" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %><%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 18.4.21
  Time: 16:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>EL 操作属性</title>
</head>
<body>

<hr/>
<br/>-----------------------------------1 EL获取普通类的属性---------------------------------------<br/>

<%
    Person person = new Person("张学友");
    pageContext.setAttribute("p", person);
%>

${p}<br/>

<%--
模拟原理
Object obj = pageContext.findAttribute("p1");//从page、request、session、application范围中找名称为p1的那个对象
out.write(obj);
--%>

${p.name}=${p['name']}=${p["name"]}<br/>
${p.class}<br/>
${p.birthday}<br/><!-- EL表达式如果打印的值是null，则什么都不显示 -->
${p.birthday.time}<br/><!-- EL表达式中没有空指针异常 -->
<%--${p.province}<br/><!-- 如果属性不存在，会报错 -->--%>





<hr/>
<br/>-----------------------------------2 EL获取List中的数据---------------------------------------<br/>
<%
    Address add1 = new Address();
    add1.setProvince("北京市");
    add1.setCity("海淀");

    Address add2 = new Address();
    add2.setProvince("广东省");
    add2.setCity("深圳");

    person.getAddresses().add(add1);
    person.getAddresses().add(add2);
%>

${p.addresses[0]['province']}==${p.addresses[0].province}<br/>
${p.addresses[1]['province']}==${p.addresses[1].province}<br/>




<hr/>
<br/>-----------------------------------3 EL获取Map中的数据---------------------------------------<br/>
<%
    /*
    泛型不能省
    Apache Tomcat Version Supported Java Versions
    9.0.x                                    8 and later
    8.0.x (superseded)                7 and later
    7.0.x                                    6 and later
    6.0.x (archived)                    5 and later
     */
    Map<String,String> map = new HashMap<String,String>();
    map.put("a", "aaa");
    map.put("b", "bbb");
    map.put("c", "ccc");
    pageContext.setAttribute("map",map);
%>
${map}<br/>
${map.b}<br/><!-- 如果对象时一个map，按照key名称来取的 -->

</body>
</html>
