package me.ztiany.bean.spel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.6.15 0:08
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpELConfig.class)
public class SpELTest {

    @Autowired
    private MathUtils mMathUtils;

    @Test
    public void test() {
        System.out.println("mMathUtils: " + mMathUtils);
    }
}
