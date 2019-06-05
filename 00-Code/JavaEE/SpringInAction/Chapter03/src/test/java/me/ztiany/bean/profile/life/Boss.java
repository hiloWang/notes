package me.ztiany.bean.profile.life;

import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.6.14 23:46
 */
@Component
@Profile("life")
//Conditional 用于配置条件化的 bean，也可以使用在 @Bean 方法上
@Conditional(BossCondition.class)
public class Boss {
}
