package com.ztiany.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * @author Ztiany
 * Date : 2017-02-16 00:01
 * Email: ztiany3@gmail.com
 */
@Retention(CLASS)
@Target(METHOD)
public @interface Code {

    String author();

    String date() default "";
}
