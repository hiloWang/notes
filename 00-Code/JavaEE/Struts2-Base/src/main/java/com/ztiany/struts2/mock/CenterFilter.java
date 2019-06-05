package com.ztiany.struts2.mock;

import org.apache.commons.beanutils.BeanUtils;
import org.dom4j.Document;
import org.dom4j.Element;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * <pre>
 * <filter>
 *      <filter-name>CenterFilter</filter-name>
 *      <filter-class>com.ztiany.struts2.mock.CenterFilter</filter-class>
 *      <init-param>
 *              <param-name>actionPostFix</param-name>
 *              <param-value>action,do</param-value>
 *      </init-param>
 * </filter>
 * <filter-mapping>
 *      <filter-name>CenterFilter</filter-name>
 *      <url-pattern>/*</url-pattern>
 * </filter-mapping>
 * </pre>
 */

/**
 * 模拟Struts2核心控制器
 */
public class CenterFilter implements Filter {

    //存取配置文件信息。key：对应action中的name value：Action对象
    private Map<String, Action> actions = new HashMap<>();
    private FilterConfig filterConfig;

    public void init(FilterConfig filterConfig) {
        initCfg();//初始化配置文件
        this.filterConfig = filterConfig;
    }

    //初始化配置文件
    private void initCfg() {
        //读取XML配置文件：把配置文件中的信息封装到对象中.再放到actions中

        Document document = Dom4JUtil.getDocument();
        Element root = document.getRootElement();
        //得到所有的action元素，创建Action对象，封装信息
        List<Element> actionElements = root.elements("action");
        if (actionElements != null && actionElements.size() > 0) {
            for (Element actionElement : actionElements) {
                //封装action信息开始
                Action action = new Action();
                action.setName(actionElement.attributeValue("name"));
                action.setClassName(actionElement.attributeValue("class"));
                String methodXmlAttrValue = actionElement.attributeValue("method");
                if (methodXmlAttrValue != null)
                    action.setMethod(methodXmlAttrValue);
                //封装action信息结束

                //得到每个action元素中的result元素，创建Result对象，封装信息
                //封装属于当前action的结果
                List<Element> resultElements = actionElement.elements("result");
                if (resultElements != null && resultElements.size() > 0) {
                    for (Element resultElement : resultElements) {
                        Result result = new Result();
                        result.setName(resultElement.attributeValue("name"));
                        result.setTargetUri(resultElement.getText().trim());
                        String typeXmlValue = resultElement.attributeValue("type");
                        if (typeXmlValue != null) {
                            result.setType(ResultType.valueOf(typeXmlValue));
                        }
                        action.getResults().add(result);
                    }
                }
                //封装属于当前action的结果

                //把Action对象都放到Map中
                actions.put(action.getName(), action);
            }
        }
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) {
        try {
            HttpServletRequest request = (HttpServletRequest) req;
            HttpServletResponse response = (HttpServletResponse) resp;
            //真正的控制器部分

            //取一个配置参数
            String actionPostFixArr[] = {"action", "", "do"};//你请求的地址以action\do\空结尾的话，才真正过滤。默认值
            String actionPostFix = filterConfig.getInitParameter("actionPostFix");
            if (actionPostFix != null) {
                actionPostFixArr = actionPostFix.split("\\,");
            }

            //解析用户请求的URI
            String uri = request.getRequestURI();//   /strutsDay01MyFramework/addCustomer.action

            //截取后缀名，看看是否需要我们的框架进行处理
            //切后缀名
            String extendFileName = uri.substring(uri.lastIndexOf(".") + 1);// /strutsDay01MyFramework/addCustomer.action   action
            // /strutsDay01MyFramework/addCustomer.do   do
            // /strutsDay01MyFramework/addCustomer   ""
            boolean needProcess = false;
            for (String s : actionPostFixArr) {
                if (extendFileName.equals(s)) {
                    needProcess = true;
                    break;
                }
            }

            if (needProcess) {
                //需要框架处理
                //解析uri中的动作名称
                String requestActionName = uri.substring(uri.lastIndexOf("/") + 1, uri.lastIndexOf("."));
                System.out.println("您的请求动作名是：" + requestActionName);
                //查找actions中对应的Action对象
                if (actions.containsKey(requestActionName)) {
                    Action action = actions.get(requestActionName);
                    //得到类名称的字节码
                    Class clazz = Class.forName(action.getClassName());
                    //封装数据到JavaBean中,利用BeanUtils框架
                    Object bean = clazz.newInstance();
                    BeanUtils.populate(bean, request.getParameterMap());
                    //实例化，调用其中指定的方法名称
                    Method m = clazz.getMethod(action.getMethod(), null);
                    //根据方法的返回值，遍历结果
                    String resultValue = (String) m.invoke(bean, null);

                    List<Result> results = action.getResults();
                    if (results != null && results.size() > 0) {
                        for (Result result : results) {

                            if (resultValue.equals(result.getName())) {
                                //根据结果中的type决定是转发还是重定向
                                //重定向的目标就是结果中的targetUri
                                if ("dispatcher".equals(result.getType().toString())) {
                                    //转发
                                    request.getRequestDispatcher(result.getTargetUri()).forward(request, response);
                                }
                                if ("redirect".equals(result.getType().toString())) {
                                    //重定向
                                    response.sendRedirect(request.getContextPath() + result.getTargetUri());
                                }
                            }
                        }
                    }
                } else {
                    throw new RuntimeException("The action " + requestActionName + " is not founded in your config files!");
                }

            } else {
                chain.doFilter(request, response);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void destroy() {

    }

}
