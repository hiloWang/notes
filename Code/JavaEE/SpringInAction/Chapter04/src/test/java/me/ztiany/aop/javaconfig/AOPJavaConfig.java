package me.ztiany.aop.javaconfig;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.6.18 19:18
 */
@Configuration
@ComponentScan(basePackages = "me.ztiany.aop.javaconfig")
@EnableAspectJAutoProxy
public class AOPJavaConfig {

}
