package com.ztiany.mysql.multi_table.one_to_many;

import java.util.ArrayList;
import java.util.List;

/*
use day18;

 */
public class Department {
    private int id;
    private String name;
    private String floor;

    private List<Employee> emps = new ArrayList<Employee>();

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

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public List<Employee> getEmps() {
        return emps;
    }

    public void setEmps(List<Employee> emps) {
        this.emps = emps;
    }

    @Override
    public String toString() {
        return "Department [id=" + id + ", name=" + name + ", floor=" + floor
                + ", emps=" + emps + "]";
    }

}
