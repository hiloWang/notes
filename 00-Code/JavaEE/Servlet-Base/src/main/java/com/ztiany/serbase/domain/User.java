package com.ztiany.serbase.domain;

import java.util.Arrays;

public class User {

    private String username;//字段
    private String[] password;//字段
    private String age;

    public String getUsername() {//读属性
        return username;
    }

    public void setUsername(String username) {//写属性，属性名.去掉set，剩余的首字母小写 username
        this.username = username;
    }

    public String[] getPassword() {
        return password;
    }

    public void setPassword(String[] password) {
        this.password = password;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User [username=" + username + ", password="
                + Arrays.toString(password) + ", age=" + age + "]";
    }


}
