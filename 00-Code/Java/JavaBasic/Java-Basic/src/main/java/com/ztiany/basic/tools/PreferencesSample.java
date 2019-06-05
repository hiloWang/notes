package com.ztiany.basic.tools;

import java.util.prefs.Preferences;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Ztiany
 *         Date : 2017-02-18 21:45
 *         Email: ztiany3@gmail.com
 */
public class PreferencesSample {

    public static void main(String... args) {
        Preferences preferences = Preferences.systemRoot();
        preferences.addPreferenceChangeListener(evt -> {
            System.out.println(evt.getNewValue());
        });
        preferences.put("key", "Value");
        preferences.put("key1", "Value1");
        preferences.put("key2", "Value2");
        preferences.put("key3", "Value3");
    }
}
