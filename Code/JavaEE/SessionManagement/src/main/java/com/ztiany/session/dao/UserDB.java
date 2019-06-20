package com.ztiany.session.dao;

import com.ztiany.session.domain.User;

import java.util.ArrayList;
import java.util.List;

public class UserDB {

    private static List<User> users = new ArrayList<User>();

    static {
        users.add(new User("wujing", "123", "傻师妹"));
        users.add(new User("zhangshengli", "123", "八戒"));
    }

    /**
     * 如果找不到返回null
     *
     * @param username
     * @param password
     * @return
     */
    public static User findUser(String username, String password) {
        for (User u : users) {
            if (username.equals(u.getUsername()) && password.equals(u.getPassword())) {
                return u;
            }
        }
        return null;
    }
}
