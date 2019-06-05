package me.ztiany.aop.javaconfig;

import org.springframework.stereotype.Component;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.6.18 19:17
 */
@Component("business")
public class BusinessServiceImpl implements BusinessService {

    @Override
    public void addProduct(int id) {
        System.out.print("BusinessServiceImpl.addProduct: ");
        System.out.println("id = [" + id + "]");
    }

    @Override
    public void deleteProduct(int id) {
        System.out.print("BusinessServiceImpl.deleteProduct: ");
        System.out.println("id = [" + id + "]");
    }

    @Override
    public void setProductName(int id, String name) {
        System.out.print("BusinessServiceImpl.setProductName: ");
        System.out.println("id = [" + id + "], name = [" + name + "]");
    }

    @Override
    public void setProductPrice(int id, float price) {
        System.out.print("BusinessServiceImpl.setProductPrice: ");
        System.out.println("id = [" + id + "], price = [" + price + "]");
    }

}
