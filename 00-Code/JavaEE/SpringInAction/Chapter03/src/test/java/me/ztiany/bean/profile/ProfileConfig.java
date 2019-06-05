package me.ztiany.bean.profile;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.6.14 23:41
 */
@Configuration
@ComponentScan(basePackages = "me.ztiany.bean.profile")
@ImportResource(locations = "classpath:xml_profile.xml")
public class ProfileConfig {

}
