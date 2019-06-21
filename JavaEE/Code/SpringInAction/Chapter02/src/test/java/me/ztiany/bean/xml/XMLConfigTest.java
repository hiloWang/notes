package me.ztiany.bean.xml;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.6.12 23:51
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:xml_config.xml")
public class XMLConfigTest {

    @Autowired
    @Qualifier("beanA1")
    private BeanA mBeanA;
    @Autowired
    @Qualifier("beanB1")
    private BeanB mBeanB;
    @Autowired
    @Qualifier("beanC1")
    private BeanC mBeanC;

    @Test
    public void test() {
        Assert.assertNotNull(mBeanA);
        Assert.assertNotNull(mBeanB);
        Assert.assertNotNull(mBeanC);
    }

}
