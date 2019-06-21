package com.ztiany.android.pthread;

import java.util.UUID;

public class UUIDUtils {
    public static String get() {
        return UUID.randomUUID().toString();
    }
}
