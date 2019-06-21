package com.ztiany.session.domain;

import java.io.Serializable;

public class User implements Serializable {

    private static final long serialVersionUID = 32232L;


    private String username;
    private String password;
    private String nickname;

    public User() {
    }

    public User(String username, String password, String nickname) {
        super();
        this.username = username;
        this.password = password;
        this.nickname = nickname;
    }

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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

}
