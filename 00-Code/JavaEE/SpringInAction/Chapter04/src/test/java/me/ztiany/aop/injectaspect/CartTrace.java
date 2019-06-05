package me.ztiany.aop.injectaspect;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.6.18 22:33
 */
@Component
public class CartTrace {

    private final Map<String, Integer> startCount = new HashMap<>();

    public void addTrace(Car car) {
        if (!startCount.containsKey(car.getName())) {
            startCount.put(car.getName(), 1);
        } else {
            int count = startCount.get(car.getName()) + 1;
            startCount.put(car.getName(), count);
        }
    }

}
