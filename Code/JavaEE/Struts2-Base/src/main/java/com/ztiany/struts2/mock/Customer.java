package com.ztiany.struts2.mock;

import java.io.Serializable;

public class Customer implements Serializable {

    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String addCustomer() {
        if ("wzhting".equals(name)) {
            return "success";
        } else {
            return "error";
        }
    }
}
