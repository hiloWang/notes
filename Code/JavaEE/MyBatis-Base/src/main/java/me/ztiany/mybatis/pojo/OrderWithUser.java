package me.ztiany.mybatis.pojo;

/**
 * 仅为演示多表查询(Orders本身不应该定义User)
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.5.27 16:28
 */
public class OrderWithUser extends Orders {

    private static final long serialVersionUID = 12L;

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
