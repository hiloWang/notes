package me.ztiany.bean.placeholder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.6.15 0:27
 */
@Configuration
@ComponentScan(basePackages = "me.ztiany.bean.placeholder")
@PropertySource("classpath:runtime_di.properties")
public class PlaceHolderConfig {

    /*用于开启占位符功能*/
    @Bean
    public static PropertySourcesPlaceholderConfigurer configurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}
