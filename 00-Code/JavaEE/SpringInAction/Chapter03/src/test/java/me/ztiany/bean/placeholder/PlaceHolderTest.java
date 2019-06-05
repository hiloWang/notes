package me.ztiany.bean.placeholder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.6.15 0:27
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PlaceHolderConfig.class)
public class PlaceHolderTest {

    @Autowired
    private Worker mWorker;

    @Test
    public void test() {
        System.out.println("mWorker=" + mWorker);
    }
}
