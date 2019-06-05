package spittr.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/*
WebConfig类扩展了WebMvcConfigurerAdapter并重写了其configureDefaultServletHandling()方法
 */
@Configuration
@EnableWebMvc
//添加@ComponentScan注解，扫描spitter.web包来查找组件
@ComponentScan("spittr.web")
public class WebConfig extends WebMvcConfigurerAdapter {

    /*
    配置视图解析器：添加一个ViewResolver bean，它会查找JSP文件，在查找的时候，它会在视图名称上加一个特定的前缀和后缀
    （例如，名为home的视图将会解析为WEB-INFviews/home.jsp）

    如果没有配置视图解析器，Spring默认会使用BeanNameView-Resolver，这个视图解析器会查找ID与视图名称匹配的bean，并且查找的bean要实现View接口，
    它以这样的方式来解析视图。
     */
    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        return resolver;
    }

    /*
    通过调用DefaultServlet-HandlerConfigurer的enable()方法，要求DispatcherServlet将对静态资源的请求转发到Servlet容器中默认的Servlet上，
    而不是使用DispatcherServlet本身来处理此类请求。

    如果不调用enable()方法，DispatcherServlet会映射为应用的默认Servlet，所以它会处理所有的请求，包括对静态资源的请求。
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        super.addResourceHandlers(registry);
    }

}
