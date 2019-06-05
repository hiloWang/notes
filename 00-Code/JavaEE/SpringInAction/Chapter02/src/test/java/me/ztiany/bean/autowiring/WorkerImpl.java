package me.ztiany.bean.autowiring;

import org.springframework.stereotype.Component;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.6.12 23:33
 */
@Component("worker")
public class WorkerImpl implements Worker {
    @Override
    public void doWork() {
        System.out.println("I am working");
    }
}
