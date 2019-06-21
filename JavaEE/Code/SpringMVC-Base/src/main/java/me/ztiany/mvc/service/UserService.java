package me.ztiany.mvc.service;

import java.util.List;

import me.ztiany.mvc.pojo.User;

public interface UserService {

    List<User> selectUserList();

    User selectUserById(Integer id);

    void updateUserById(User items);

}
