package me.ztiany.aop.injectaspect;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.6.18 22:48
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:inject_aspect.xml")
public class InjectAspectTest {


    @Autowired()
    @Qualifier("benz")
    private Car mBenz;

    @Autowired()
    @Qualifier("bmw")
    private Car mBmw;

    @Test
    public void test() {
        System.out.println(mBenz);
        System.out.println(mBmw);
    }

}
