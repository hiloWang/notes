package spittr.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import spittr.web.WebConfig;

/*
在Servlet 3.0环境中，容器会在类路径中查找实现javax.servlet.ServletContainerInitializer接口的类，如果能发现的话，就会用它来配置Servlet容器。
Spring提供了这个接口的实现，名为SpringServletContainerInitializer，这个类反过来又会查找实现WebApplicationInitializer的类并将配置的任务交给它们来完成。
Spring 3.2引入了一个便利的WebApplicationInitializer基础实现，也就是AbstractAnnotationConfigDispatcherServletInitializer。
因为我们的SpittrWebAppInitializer扩展了AbstractAnnotationConfigDispatcherServletInitializer（同时也就实现了WebApplicationInitializer），
因此当部署到Servlet 3.0容器中的时候，容器会自动发现它，并用它来配置Servlet上下文。

AbstractAnnotationConfigDispatcherServletInitializer会同时创建DispatcherServlet和ContextLoaderListener。
 */
public class SpitterWebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    /*
    getRootConfigClasses()方法返回的带有@Configuration注解的类将会用来配置ContextLoaderListener创建的应用上下文中的bean。

    Spring Web应用中，通常还会有另外一个应用上下文。另外的这个应用上下文是由ContextLoaderListener创建的。

    ContextLoaderListener加载应用中的其他bean。这些bean通常是驱动应用后端的中间层和数据层组件。
     */
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{RootConfig.class};
    }

    /*
    getServletConfigClasses方法返回的带有@Configuration注解的类将会用来定义DispatcherServlet应用上下文中的bean：

    当DispatcherServlet启动的时候，它会创建Spring应用上下文，并加载配置文件或配置类中所声明的bean。
    getServletConfigClasses()方法中，我们要求DispatcherServlet加载应用上下文时，使用定义在WebConfig配置类（使用Java配置）中的bean。

    DispatcherServlet加载包含Web组件的bean，如控制器、视图解析器以及处理器映射
     */
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{WebConfig.class};
    }

    /*
    将一个或多个路径映射到DispatcherServlet上
     */
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

}