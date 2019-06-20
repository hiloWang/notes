import com.ztiany.serbase.domain.User;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.4.16 0:58
 */
public class TestUser {

    @Test
    @SuppressWarnings("all")
    public void testUser() {
        User user = new User();
        System.out.println("封装前：" + user);

        Map<String, String[]> map = new HashMap<>();
        map.put("username", new String[]{"name"});
//        map.put("password", new String[]{"123456"});
        map.put("password", new String[]{"123456", "123456"});
        map.put("age", new String[]{"28"});

        for (Map.Entry<String, String[]> me : map.entrySet()) {
            String paramName = me.getKey();
            String paramValues[] = me.getValue();
            try {
            /*     PropertyDescriptor pd = new PropertyDescriptor(paramName, User.class);
                Method writerMethod = pd.getWriteMethod();//得到setter方法
                if(paramValues.length==1){
                    writerMethod.invoke(user, paramValues);
                }else{
                    //writerMethod.invoke(user, (Object)paramValues);//方法一
                    writerMethod.invoke(user, new Object[]{paramValues});//方法一
                }*/
                BeanUtils.setProperty(user, paramName, paramValues);

            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        System.out.println("封装后：" + user);
    }
}
