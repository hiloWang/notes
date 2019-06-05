package com.ztiany.runtimeannotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Ztiany
 *         Date : 2017-02-15 22:07
 *         Email: ztiany3@gmail.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ClassInfo {

    String author();

    int version() default 1;

    String createData();

}
