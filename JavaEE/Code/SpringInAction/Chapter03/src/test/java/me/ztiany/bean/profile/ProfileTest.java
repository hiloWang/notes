package me.ztiany.bean.profile;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import me.ztiany.bean.profile.life.Boss;
import me.ztiany.bean.profile.life.Job;
import me.ztiany.bean.profile.war.General;
import me.ztiany.bean.profile.war.Tank;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.6.14 23:40
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ProfileConfig.class)
//使用@ActiveProfiles注解，我们可以使用它来指定运行测试时要激活哪个
@ActiveProfiles("life")
public class ProfileTest {

    @Autowired(required = false)
    private Boss mBoss;
    @Autowired(required = false)
    private Job mJob;
    @Autowired(required = false)
    private General mGeneral;
    @Autowired(required = false)
    private Tank mTank;

    @Test
    public void test() {
        System.out.println("mBoss: " + mBoss);
        System.out.println("mJob: " + mJob);
        System.out.println("mGeneral: " + mGeneral);
        System.out.println("mTank: " + mTank);
    }

}
