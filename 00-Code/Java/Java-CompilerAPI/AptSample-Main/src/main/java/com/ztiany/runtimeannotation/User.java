package com.ztiany.runtimeannotation;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Ztiany
 *         Date : 2017-02-15 22:11
 *         Email: ztiany3@gmail.com
 */
@ClassInfo(
        author = "Ztiany",
        createData = "2017-02-15 22:11",
        version = 11
)
public class User {

    private int age;
    private String name;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
