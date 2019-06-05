package com.ztiany.mysql.multi_table.many_to_many;

import java.util.ArrayList;
import java.util.List;

public class Student {

    private int id;
    private String name;
    private String grade;
    private List<Teacher> ts = new ArrayList<Teacher>();

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

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public List<Teacher> getTs() {
        return ts;
    }

    public void setTs(List<Teacher> ts) {
        this.ts = ts;
    }

    @Override
    public String toString() {
        return "Student [id=" + id + ", name=" + name + ", grade=" + grade
                + ", ts=" + ts + "]";
    }

}
