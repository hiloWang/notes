package com.ztiany.jspbase.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Person implements Serializable {

    private static final long serialVersionUID = 422L;


    private String name;
    private String gender;
    private int age;
    private Date birthday;
    private Address address = new Address();//默认地址
    private List<Address> addresses = new ArrayList<>();//其他地址

    public Person() {
    }

    public Person(String name) {
        this.name = name;
    }

    public String getName() {//读属性，属性名name。
        return name;
    }

    public void setName(String name) {//写属性
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

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                ", birthday=" + birthday +
                ", address=" + address +
                ", addresses=" + addresses +
                '}';
    }
}
