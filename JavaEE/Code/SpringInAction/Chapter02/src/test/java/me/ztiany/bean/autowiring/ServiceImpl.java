package me.ztiany.bean.autowiring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.6.12 23:18
 */
//@Component注解用于表明该类会作为组件类
@Component("service")
public class ServiceImpl implements Service {

    @Autowired
    @Qualifier("worker")//指定bean的id
    private Worker mWorker;

    @Override
    public void start() {
        mWorker.doWork();
    }

}
