package me.ztiany.aop.javaconfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.6.18 19:32
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AOPJavaConfig.class)
public class AOPJavaTest {


    @Autowired
    private BusinessService mBusinessService;

    @Test
    public void test() {
        System.out.println("----------------------------------");
        mBusinessService.addProduct(10);
        System.out.println("----------------------------------");
        mBusinessService.deleteProduct(10);
        System.out.println("----------------------------------");
        mBusinessService.setProductName(10, "电脑");
        mBusinessService.setProductPrice(10, 12222.8F);
        System.out.println();
        System.out.println("----------------------------------");
        System.out.println(mBusinessService.getClass());
        /*
        [
            interface me.ztiany.aop.javaconfig.BusinessService,
            interface me.ztiany.aop.javaconfig.EnhanceInterface,
            interface org.springframework.aop.SpringProxy,
            interface org.springframework.aop.framework.Advised,
            interface org.springframework.core.DecoratingProxy
        ]
         */
        System.out.println(Arrays.toString(mBusinessService.getClass().getInterfaces()));
        ((EnhanceInterface)mBusinessService).printAllInfo();
    }

}
