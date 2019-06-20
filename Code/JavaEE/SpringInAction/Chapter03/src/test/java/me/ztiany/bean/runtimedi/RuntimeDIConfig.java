package me.ztiany.bean.runtimedi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.6.15 0:05
 */
@Configuration
@PropertySource("classpath:runtime_di.properties")
public class RuntimeDIConfig {

    @Autowired
    private Environment mEnvironment;

    @Bean
    public Person createPerson() {
        String age = mEnvironment.getProperty("age");
        String name = mEnvironment.getProperty("name");
        return new Person(Integer.parseInt(age), name);
    }
}
