package com.ztiany.gradleforandroid.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * <br/>    Description  :运行gradlew test
 * <br/>    Email    : ztiany3@gmail.com
 *
 * @author Ztiany
 *         <p>
 *         Date : 2016-11-23 23:18
 */
public class StringUtilsTest {



    @Test
    public void getLength() throws Exception {
        Assert.assertEquals(4, StringUtils.getLength("我读"));
    }

}