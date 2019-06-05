package me.ztiany.bean.proxyMode;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.6.16 15:04
 */
@Component
@Scope(
        value = WebApplicationContext.SCOPE_SESSION,/*范围*/
        proxyMode = ScopedProxyMode.TARGET_CLASS/*代理模式*/
)
public class ShoppingCart {

    private List<String> mList;
    {
        mList = new ArrayList<>();
        mList.add("笔记本电脑");
    }

    public void setList(List<String> list) {
        mList = list;
    }

    public List<String> getList() {
        return mList;
    }

}
