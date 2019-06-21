package com.ztiany.jspbase.domain;

import java.io.Serializable;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.4.20 23:14
 */
public class Student implements Serializable{

    private static final long serialVersionUID = 432L;

    private String name;
    private String gender;
    private int age;

    public Student() {
    }

    public Student(String name, String gender, int age) {
        super();
        this.name = name;
        this.gender = gender;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                '}';
    }
}
