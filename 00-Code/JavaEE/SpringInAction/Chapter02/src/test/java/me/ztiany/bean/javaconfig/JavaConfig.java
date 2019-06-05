package me.ztiany.bean.javaconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.6.12 23:40
 */
//@Configuration注解表明这个类是一个配置类，其内部的添加了@Bean 的方法将会作为作为Bean的工厂方法
@Configuration
public class JavaConfig {

    @Bean
    public Car createCar() {
        return new BMWCar();
    }

    @Bean
    public Driver createDriver(Car car) {
        NormalDriver normalDriver = new NormalDriver();
        normalDriver.setCar(car);
        return normalDriver;
    }

}
