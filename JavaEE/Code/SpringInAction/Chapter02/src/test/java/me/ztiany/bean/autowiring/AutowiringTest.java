package me.ztiany.bean.autowiring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.6.12 23:21
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AutowiringConfig.class)
public class AutowiringTest {

    @Autowired
    private Service mService;

    @Test
    public void testAutowiring() {
        mService.start();
    }

}
