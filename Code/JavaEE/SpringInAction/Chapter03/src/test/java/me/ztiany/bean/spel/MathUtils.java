package me.ztiany.bean.spel;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.6.15 0:36
 */
@Component
public class MathUtils {

    private double pi;

    public MathUtils(@Value("#{T(java.lang.Math).PI}") double pi) {
        this.pi = pi;
    }

    @Override
    public String toString() {
        return "MathUtils{" +
                "pi=" + pi +
                '}';
    }
}
