package clink.core;

import java.io.IOException;

/**
 * 上下文，用于初始化框架。
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/11/8 22:41
 */
public class IoContext {

    private static IoContext INSTANCE;
    private final IoProvider ioProvider;

    private IoContext(IoProvider ioProvider) {
        this.ioProvider = ioProvider;
    }

    public IoProvider getIoProvider() {
        return ioProvider;
    }

    public static IoContext get() {
        return INSTANCE;
    }

    /**
     * 调用此方法获取 StartedBoot 用于启动 IoContext。
     */
    public static StartedBoot setup() {
        return new StartedBoot();
    }

    public static void close() throws IOException {
        if (INSTANCE != null) {
            INSTANCE.callClose();
        }
    }

    private void callClose() throws IOException {
        ioProvider.close();
    }

    public static class StartedBoot {

        private IoProvider ioProvider;

        private StartedBoot() {
        }

        /**
         * 向 IoContext 提供 IoProvider 实现
         */
        public StartedBoot ioProvider(IoProvider ioProvider) {
            this.ioProvider = ioProvider;
            return this;
        }

        /**
         * 启动 IoContext
         */
        public IoContext start() {
            INSTANCE = new IoContext(ioProvider);
            return INSTANCE;
        }
    }
}
