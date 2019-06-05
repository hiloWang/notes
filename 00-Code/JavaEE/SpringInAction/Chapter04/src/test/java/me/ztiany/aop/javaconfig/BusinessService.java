package me.ztiany.aop.javaconfig;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.6.18 19:17
 */
public interface BusinessService {

    void addProduct(int id);

    void deleteProduct(int id);

    void setProductName(int id, String name);

    void setProductPrice(int id, float price);
}
