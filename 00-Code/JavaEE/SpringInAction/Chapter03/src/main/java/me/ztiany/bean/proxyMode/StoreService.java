package me.ztiany.bean.proxyMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.6.16 15:04
 */
@Component
public class StoreService {

    @Autowired
    private ShoppingCart mShoppingCart;

    public void listCart() {
        System.out.println("购物车商品如下：--------------:" + mShoppingCart);
        System.out.println(mShoppingCart.getList());
        System.out.println(mShoppingCart.getClass());
    }
}
