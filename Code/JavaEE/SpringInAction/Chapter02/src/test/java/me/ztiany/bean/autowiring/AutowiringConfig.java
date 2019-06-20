package me.ztiany.bean.autowiring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 这是一个自动装配配置类
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.6.12 23:21
 */
//Configuration表示这是一个配置类
@Configuration
//ComponentScan 开启自动装配，默认规则，以配置类所在的包作为基础包（base package）来扫描组件
@ComponentScan(basePackages = "me.ztiany.bean.autowiring")
// 使用basePackageClasses 指定扫描的组件
//@ComponentScan(basePackageClasses = Service.class)
public class AutowiringConfig {
}
