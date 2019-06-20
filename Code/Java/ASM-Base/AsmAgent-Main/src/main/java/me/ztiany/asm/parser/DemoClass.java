package me.ztiany.asm.parser;

/**
 * @author ztiany
 * Email: ztiany3@gmail.com
 */
public class DemoClass {

    private int age;
    private String name;
    private static String sName;
    private static final String FLAG = "flag";

    public DemoClass() {
    }

    public DemoClass(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "DemoClass{" +
                "age=" + age +
                ", name='" + name + '\'' +
                '}';
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }

}
