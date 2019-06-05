package me.ztiany.bean.placeholder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.6.15 0:27
 */
@Component
public class Worker {

    private String salary;
    private String job;

    public Worker(@Value("${salary}") String salary, @Value("${job}") String job) {
        this.salary = salary;
        this.job = job;
    }

    @Override
    public String toString() {
        return "Worker{" +
                "salary=" + salary +
                ", job='" + job + '\'' +
                '}';
    }
}
