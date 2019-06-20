package com.ztiany.register.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.4.22 18:02
 */
public class User implements Serializable {

    private static final long serialVersionUID = 42L;

    private String username;
    private String password;
    private String email;
    private Date birthday;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

}
