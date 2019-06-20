package com.ztiany.springf.test.aop;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.5.24 0:19
 */

//帮我们创建容器
@RunWith(SpringJUnit4ClassRunner.class)
//指定创建容器时使用哪个配置文件
@ContextConfiguration(locations = "classpath:aspectj_aop.xml")
public class AOPTest {

    @Resource(name = "service")
    private AOPService mAOPService;

    @Test
    public void fun1() {
        mAOPService.save();
    }

}
