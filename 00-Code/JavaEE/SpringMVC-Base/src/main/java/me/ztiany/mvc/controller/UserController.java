package me.ztiany.mvc.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

import javax.servlet.http.HttpSession;

import me.ztiany.mvc.pojo.User;
import me.ztiany.mvc.service.UserService;

@Controller
public class UserController {

    private final UserService mUserService;

    @Autowired
    public UserController(UserService userService) {
        this.mUserService = userService;
    }

    //入门程序 第一   包类 + 类包 + 方法名
    @RequestMapping(value = "/user/userList.action")
    public ModelAndView itemList() {
        ModelAndView mav = new ModelAndView();

        mav.addObject("userList", mUserService.selectUserList());
        mav.setViewName("userList");

        return mav;
    }

    //去登陆的页面
    @RequestMapping(value = "/user/json/login.action", method = RequestMethod.GET)
    public @ResponseBody
    User loginJson() {
        User user = new User();
        user.setAddress("深圳");
        user.setBirthday(new Date());
        user.setId(3);
        user.setSex("男");
        user.setUsername("Ztiany");
        return user;
    }

    //去登陆的页面
    @RequestMapping(value = "/user/login.action", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    //登录操作
    @RequestMapping(value = "/user/login.action", method = RequestMethod.POST)
    public String login(String username, HttpSession httpSession) {
        httpSession.setAttribute("USER_SESSION", username);
        return "redirect:/item/itemList.action";
    }

}
