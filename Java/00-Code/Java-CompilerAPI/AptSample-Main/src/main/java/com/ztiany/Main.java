package com.ztiany;

import com.ztiany.runtimeannotation.ClassInfo;
import com.ztiany.runtimeannotation.User;
import  com.ztiany.compilerannotation.UseCodeAutogenerate;

public class Main {

    public static void main(String... args) {
        useRuntimeAnnotation(new User());
        userAPT();
    }

    private static void userAPT() {
        new UseCodeAutogenerate().message();
    }

    private static void useRuntimeAnnotation(Object object) {
        if (object instanceof User) {
            User user = (User) object;

            ClassInfo annotation = user.getClass().getAnnotation(ClassInfo.class);

            if (annotation != null) {
                System.out.println(annotation.author());
                System.out.println(annotation.version());
                System.out.println(annotation.createData());
            }
        }
    }


}
