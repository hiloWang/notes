package com.ztiany.mysql.multi_table.many_to_many;

import java.util.ArrayList;
import java.util.List;

public class Teacher {

    private int id;
    private String name;
    private float salary;

    private List<Student> stus = new ArrayList<Student>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getSalary() {
        return salary;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }

    public List<Student> getStus() {
        return stus;
    }

    public void setStus(List<Student> stus) {
        this.stus = stus;
    }

    @Override
    public String toString() {
        return "Teacher [id=" + id + ", name=" + name + ", salary=" + salary
                + ", stus=" + stus + "]";
    }

}
