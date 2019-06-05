package me.ztiany.bean.profile.life;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Arrays;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.6.14 23:57
 */
public class BossCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Environment environment = context.getEnvironment();
        String[] activeProfiles = environment.getActiveProfiles();
        System.out.println(Arrays.toString(activeProfiles));
        return false;
    }

}
