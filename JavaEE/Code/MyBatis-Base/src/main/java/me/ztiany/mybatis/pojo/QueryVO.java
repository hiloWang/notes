package me.ztiany.mybatis.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.5.27 15:38
 */
public class QueryVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private User user;

    private List<Integer> idList;

    private Integer[] idArray;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Integer> getIdList() {
        return idList;
    }

    public void setIdList(List<Integer> idList) {
        this.idList = idList;
    }

    public Integer[] getIdArray() {
        return idArray;
    }

    public void setIdArray(Integer[] idArray) {
        this.idArray = idArray;
    }
}
