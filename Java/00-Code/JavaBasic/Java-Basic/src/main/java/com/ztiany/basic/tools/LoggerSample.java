package com.ztiany.basic.tools;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * java.util.Logger 日志工具
 *
 * @author Ztiany
 *         Date : 2016-12-10 15:19
 */
public class LoggerSample {
    public static void main(String... args) {
        Logger.getGlobal().log(Level.INFO, "LoggerSample");
    }
}
