package com.ztiany.mall.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Log级别：
 * <pre>
 *          fatal：非常严重的错误，导致系统中止。期望这类信息能立即显示在状态控制台上。
 *          error：其它运行期错误或不是预期的条件。期望这类信息能立即显示在状态控制台上。
 *          warn：使用了不赞成使用的API、非常拙劣使用API, '几乎就是'错误, 其它运行时不合需要和不合预期的状态但还没必要称为 "错误"。期望这类信息能立即显示在状态控制台上。
 *          info：运行时产生的有意义的事件。期望这类信息能立即显示在状态控制台上。
 *          debug：系统流程中的细节信息。期望这类信息仅被写入log文件中。
 *          trace：更加细节的信息。期望这类信息仅被写入log文件中。
 * </pre>
 */
public class LogUtils {

    private static Log LOG = LogFactory.getLog(LogUtils.class);

    public static void error(Object message, Throwable throwable) {
        LOG.error(message, throwable);
    }

    public static void info(Object message, Throwable throwable) {
        LOG.info(message, throwable);
    }

    public static void info(Object message) {
        LOG.info(message);
    }

    public static void debug(Object message) {
        LOG.debug(message);
    }

    public static void warn(Object message, Throwable throwable) {
        LOG.warn(message, throwable);
    }


} 