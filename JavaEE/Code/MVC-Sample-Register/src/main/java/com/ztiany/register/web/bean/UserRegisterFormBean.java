package com.ztiany.register.web.bean;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 属性和表单输入域名称的保持一致;字段都是String类型，完成用户输入的验证；验证错误的记住错误提示信息。
 */
public class UserRegisterFormBean {

    private String username;
    private String password;
    private String repassword;
    private String email;
    private String birthday;

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

    public String getRepassword() {
        return repassword;
    }

    public void setRepassword(String repassword) {
        this.repassword = repassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    //封装错误信息提示：key，表单字段名；value：提示信息
    private Map<String, String> errors = new HashMap<String, String>();

    public Map<String, String> getErrors() {
        return errors;
    }

    /**
     * 用户输入信息的验证
     *
     * @return
     */
    public boolean validate() {
        if (username == null) {
            errors.put("username", "必须输入用户名");
        } else if (!username.matches("[a-zA-Z]{3,8}")) {
            errors.put("username", "用户名必须是3~8位字母组成");
        }

        if (password == null) {
            errors.put("password", "必须输入密码");
        } else if (!password.matches("\\d{3,8}")) {
            errors.put("password", "密码必须是3~8位数字组成");
        }

        if (password != null) {
            if (!password.equals(repassword)) {
                errors.put("repassword", "两次密码必须一致");
            }
        }
        if (email == null || email.trim().equals("")) {
            errors.put("email", "请输入邮箱");
        } else if (!email.matches("\\b^['_a-z0-9-\\+]+(\\.['_a-z0-9-\\+]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)*\\.([a-z]{2}|aero|arpa|asia|biz|com|coop|edu|gov|info|int|jobs|mil|mobi|museum|name|nato|net|org|pro|tel|travel|xxx)$\\b")) {
            errors.put("email", "请输入正确的邮箱");
        }
        if (birthday == null) {
            errors.put("birthday", "必须输入生日");
        } else {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            try {
                df.parse(birthday);
            } catch (ParseException e) {
                errors.put("birthday", "生日必须符合格式");
            }
        }
        return errors.isEmpty();
    }
}
