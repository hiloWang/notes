package com.ztiany.gradleforandroid;

import android.widget.TextView;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

/**
 * <br/>    Description  :
 * <br/>    Email    : ztiany3@gmail.com
 *
 * @author Ztiany
 *         <p>
 *         Date : 2016-11-23 23:50
 */
@RunWith(RobolectricTestRunner.class)
@Config(sdk = 21, manifest = "app/src/main/AndroidManifest.xml")
public class MainActivityTest {

    @Test
    public void testTextView() {
        MainActivity mainActivity = Robolectric.buildActivity(MainActivity.class).create().get();
        TextView textView = (TextView) mainActivity.findViewById(R.id.main_tv);
        Assert.assertEquals("Hello World!", textView.getText().toString());
    }

}