package com.ztiany.mysql.multi_table.one_to_one;

public class IdCard {
    private int id;
    private String num;
    private Person person;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public String toString() {
        return "IdCard [id=" + id + ", num=" + num + ", person=" + person + "]";
    }

}
