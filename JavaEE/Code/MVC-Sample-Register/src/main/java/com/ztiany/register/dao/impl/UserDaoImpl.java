package com.ztiany.register.dao.impl;

import com.ztiany.register.dao.UserDao;
import com.ztiany.register.domain.User;
import com.ztiany.register.utils.CommonUtil;
import com.ztiany.register.utils.Dom4JUtil;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

import java.util.Date;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.4.22 18:03
 */
public class UserDaoImpl implements UserDao {


    @Override
    public void save(User user) {
        Document doc;
        try {
            doc = Dom4JUtil.getDocument();

            Element rootElement = doc.getRootElement();

            rootElement.addElement("user")
                    .addAttribute("username", user.getUsername())
                    .addAttribute("password", user.getPassword())
                    .addAttribute("email", user.getEmail())
                    .addAttribute("birthday", CommonUtil.formatDate(user.getBirthday()));

            Dom4JUtil.write2xml(doc);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public User findUser(String name) {
        try {
            Document doc = Dom4JUtil.getDocument();
            Node node = doc.selectSingleNode("//user[@username='" + name + "']");
            if (node == null) {
                return null;
            }

            User user = new User();
            user.setUsername(node.valueOf("@username"));
            user.setPassword(node.valueOf("@password"));
            user.setEmail(node.valueOf("@email"));
            String sBirthday = node.valueOf("@birthday");

            Date birthday = null;
            if (sBirthday != null && !sBirthday.equals("")) {
                birthday = CommonUtil.parse(sBirthday);
            }
            user.setBirthday(birthday);

            return user;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User findUser(String name, String password) {
        try {
            Document doc = Dom4JUtil.getDocument();
            Node node = doc.selectSingleNode("//user[@username='" + name + "' and @password ='" + password + "']");
            if (node == null) {
                return null;
            }
            User user = new User();
            user.setUsername(node.valueOf("@username"));
            user.setPassword(node.valueOf("@password"));
            user.setEmail(node.valueOf("@email"));
            String sBirthday = node.valueOf("@birthday");
            Date birthday = null;

            if (sBirthday != null && !sBirthday.equals("")) {
                birthday = CommonUtil.parse(sBirthday);
            }
            user.setBirthday(birthday);
            return user;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
