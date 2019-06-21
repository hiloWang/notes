package me.ztiany.mvc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import me.ztiany.mvc.dao.UserMapper;
import me.ztiany.mvc.pojo.User;

/**
 * 查询商品信息
 *
 * @author lx
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper itemsMapper;

    @Autowired
    public UserServiceImpl(UserMapper itemsMapper) {
        this.itemsMapper = itemsMapper;
    }

    @Override
    public List<User> selectUserList() {
        return itemsMapper.selectByExample(null);
    }

    @Override
    public User selectUserById(Integer id) {
        return itemsMapper.selectByPrimaryKey(id);
    }

    @Override
    public void updateUserById(User items) {
        itemsMapper.updateByPrimaryKey(items);
    }
}
