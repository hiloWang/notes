package me.ztiany.lombok;

/**
 * 运行前安装 Lombok 插件
 *
 * @author ztiany
 * Email: ztiany3@gmail.com
 */
public class LombokMain {

    public static void main(String... args) {
        Person person = new Person();
        person.setName("haha");
        System.out.println(person.getName());
    }

}
