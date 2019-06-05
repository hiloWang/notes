package com.ztiany.mysql.multi_table.one_to_one;

public class Person {
    private int id;
    private String name;
    private IdCard idCard;

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

    public IdCard getIdCard() {
        return idCard;
    }

    public void setIdCard(IdCard idCard) {
        this.idCard = idCard;
    }

    @Override
    public String toString() {
        return "Person [id=" + id + ", name=" + name + ", idCard=" + idCard
                + "]";
    }

}
