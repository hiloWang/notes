package me.ztiany.bean.runtimedi;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.6.15 0:06
 */
public class Person {

    private int age;
    private String name;

    public Person(int age, String name) {
        this.age = age;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Person{" +
                "age=" + age +
                ", name='" + name + '\'' +
                '}';
    }
}
