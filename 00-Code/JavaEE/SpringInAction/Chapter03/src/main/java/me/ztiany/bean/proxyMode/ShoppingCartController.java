package me.ztiany.bean.proxyMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.6.16 15:27
 */
@Controller
public class ShoppingCartController {

    @Autowired
    private  StoreService mStoreService;

    @RequestMapping(value = "cart.action")
    public String cart() {
        mStoreService.listCart();
        return "cart";
    }
}
