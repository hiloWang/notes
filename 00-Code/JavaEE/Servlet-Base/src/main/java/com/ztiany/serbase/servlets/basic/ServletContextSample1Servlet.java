package com.ztiany.serbase.servlets.basic;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 通过ServletContext获取全局配置参数和设置参数，并读取配置文件属性
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.4.15 0:29
 */
public class ServletContextSample1Servlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        getGlobalProperties();
        readConfigFile();
    }

    private void readConfigFile() throws IOException {
        System.out.println("---------readConfig1---------");
        readConfig1();
        System.out.println("---------readConfig2---------");
        readConfig2();
//        System.out.println("---------readConfig3---------");
//        readConfig3();
//        System.out.println("---------readConfig4---------");
//        readConfig4();
        System.out.println("---------readConfig5---------");
        readConfig5();
//        System.out.println("---------readConfig6---------");
//        readConfig6();
        System.out.println("---------readConfig7---------");
        readConfig7();

        //使用IDEA都不到c.properties
    }

    private void readConfig7() throws IOException {
        //利用类加载器读取类路径中的配置文件。b.properties
        URL url = ServletContextSample1Servlet.class.getClassLoader().getResource("b.properties");
        if (url != null) {
            System.out.println(url.getPath());//绝对路径
            Properties props = new Properties();
            props.load(new FileInputStream(url.getPath()));
            System.out.println(props.getProperty("key"));
        }
    }

    private void readConfig6() throws IOException {
        //利用类加载器读取类路径中的配置文件。c.properties
        InputStream in = ServletContextSample1Servlet.class.getClassLoader().getResourceAsStream("com/ztiany/serbase/c.properties");
        Properties props = new Properties();
        props.load(in);
        System.out.println(props.getProperty("key"));
    }

    private void readConfig5() throws IOException {
        //利用类加载器读取类路径中的配置文件。b.properties
        InputStream in = ServletContextSample1Servlet.class.getClassLoader().getResourceAsStream("b.properties");
        Properties props = new Properties();
        props.load(in);
        System.out.println(props.getProperty("key"));
    }

    private void readConfig4() {
        //ResourceBundle:专门读properties文件的。只能读取类路径中的properties文件
        ResourceBundle rbB = ResourceBundle.getBundle("b");//baseName 基名
        System.out.println(rbB.getString("key"));

        ResourceBundle rbC = ResourceBundle.getBundle("com.ztiany.serbase.c");//baseName 基名
        System.out.println(rbC.getString("key"));
    }

    private void readConfig3() throws IOException {
        //利用ServletContext的getRealPath来读:c.properties
        ServletContext sc = getServletContext();
        String realPath = sc.getRealPath("/WEB-INF/classes/com/ztiany/serbase/c.properties");
        Properties props = new Properties();
        props.load(new FileInputStream(realPath));
        System.out.println(props.getProperty("key"));
    }

    private void readConfig2() throws IOException {
        //利用ServletContext的getRealPath来读:b.properties
        ServletContext sc = getServletContext();
        String realPath = sc.getRealPath("/WEB-INF/classes/b.properties");
        Properties props = new Properties();
        props.load(new FileInputStream(realPath));
        System.out.println(props.getProperty("key"));
    }

    private void readConfig1() throws IOException {
        //利用ServletContext的getRealPath来读:a.properties
        ServletContext sc = getServletContext();
        String realPath = sc.getRealPath("/WEB-INF/a.properties");
        Properties props = new Properties();
        props.load(new FileInputStream(realPath));
        System.out.println(props.getProperty("key"));
    }

    private void getGlobalProperties() {
        System.out.println("---------getGlobalProperties---------");
        //打印全局参数
        ServletContext sc = getServletContext();
        Enumeration e = sc.getInitParameterNames();
        while (e.hasMoreElements()) {
            String paramName = (String) e.nextElement();
            System.out.println(paramName + "=" + sc.getInitParameter(paramName));
        }
        //设置一个全局参数
        getServletContext().setAttribute("global_key2", "global_value2");
    }
}
