package clink.impl;


import java.io.IOException;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import clink.core.IoProvider;
import clink.utils.CloseUtils;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/11/8 22:57
 */
public class IoSelectorProvider implements IoProvider {

    private final AtomicBoolean isClosed = new AtomicBoolean(false);

    // 是否处于注册读过程
    private final AtomicBoolean inRegInput = new AtomicBoolean(false);
    // 是否处于注册写过程
    private final AtomicBoolean inRegOutput = new AtomicBoolean(false);

    //读选择器，读写分离
    private final Selector readSelector;
    //写选择器，读写分离
    private final Selector writeSelector;

    private final HashMap<SelectionKey, Runnable> inputCallbackMap = new HashMap<>();
    private final HashMap<SelectionKey, Runnable> outputCallbackMap = new HashMap<>();

    private final ExecutorService inputHandlePool;
    private final ExecutorService outputHandlePool;

    /**
     * 创建并启动IoSelectorProvider
     */
    public IoSelectorProvider() throws IOException {
        readSelector = Selector.open();
        writeSelector = Selector.open();

        inputHandlePool = Executors.newFixedThreadPool(20,
                new NameableThreadFactory("IoProvider-Input-Thread-"));

        outputHandlePool = Executors.newFixedThreadPool(20,
                new NameableThreadFactory("IoProvider-Output-Thread-"));

        // 开始输出输入的监听
        startRead();
        startWrite();
    }

    private void startRead() {
        Thread thread = new SelectThread("Clink IoSelectorProvider ReadSelector Thread",
                isClosed, inRegInput, readSelector,
                inputCallbackMap, inputHandlePool,
                SelectionKey.OP_READ);
        thread.start();
    }

    private void startWrite() {
        //这个线程只负责从选择器中获取可写的 Channel，然后交给线程池处理。
        Thread thread = new SelectThread("Clink IoSelectorProvider WriteSelector Thread",
                isClosed, inRegOutput, writeSelector,
                outputCallbackMap, outputHandlePool,
                SelectionKey.OP_WRITE);
        thread.start();
    }

    @Override
    public boolean registerInput(SocketChannel channel, HandleProviderCallback callback) {
        //注册关心可读的SocketChannel
        return registerSelection(channel, readSelector, SelectionKey.OP_READ, inRegInput, inputCallbackMap, callback) != null;
    }

    @Override
    public boolean registerOutput(SocketChannel channel, HandleProviderCallback callback) {
        //注册关心可写的SocketChannel
        return registerSelection(channel, writeSelector, SelectionKey.OP_WRITE, inRegOutput, outputCallbackMap, callback) != null;
    }

    @Override
    public void unRegisterInput(SocketChannel channel) {
        unRegisterSelection(channel, readSelector, inputCallbackMap, inRegInput);
    }

    @Override
    public void unRegisterOutput(SocketChannel channel) {
        unRegisterSelection(channel, writeSelector, outputCallbackMap, inRegOutput);
    }

    private void unRegisterSelection(SocketChannel channel, Selector selector, HashMap<SelectionKey, Runnable> map, AtomicBoolean locker) {
        //noinspection SynchronizationOnLocalVariableOrMethodParameter
        synchronized (locker) {
            locker.set(true);
            try {
                //重新唤醒一个 Selector，让其暂时停止select操作，以防止下面的   selectionKey.cancel(); 产生线程竞争。
                selector.wakeup();

                if (channel.isRegistered()) {
                    SelectionKey selectionKey = channel.keyFor(selector);
                    // 取消监听的方法
                    if (selectionKey != null) {
                        selectionKey.cancel();
                        map.remove(selectionKey);
                    }
                }
            } finally {
                locker.set(false);
                try {
                    //notifyAll 可能存在异常
                    locker.notifyAll();
                } catch (Exception ignore) {
                }
            }
        }
    }

    @Override
    public void close() {
        if (isClosed.compareAndSet(false, true)) {
            inputHandlePool.shutdown();
            outputHandlePool.shutdown();

            inputCallbackMap.clear();
            outputCallbackMap.clear();

            CloseUtils.close(readSelector, writeSelector);
        }
    }

    private static void handleSelection(SelectionKey selectionKey, int keyOps, HashMap<SelectionKey, Runnable> map, ExecutorService executorService, AtomicBoolean locker) {
        // 重点，取消继续对keyOps的监听，为什么要取消呢？因为获取一个可读/写的 Channel 后，是将其交给线程池执行，而不是直接处理，线程池的执行时机是不定的，
        // 如果这里不取消对keyOps的监听，那么轮询 Selector 的线程下一次又会读取获取到还没有被线程池处理的 Channel，又会重新把对应的操作提交给线程池，这就会导致重复任务大量堆积。

        //这里同步是因为，interestOps 也是对 Selector 队列进行操作 ，此时 Selector 虽然肯定不处于 select 状态，
        //但是还是要防止多个线程同时操作队列。
        //noinspection SynchronizationOnLocalVariableOrMethodParameter
        synchronized (locker) {
            try {
                //取消操作可能在其他的线程执行。如果key被取消，可能抛出异常，直接返回。
                selectionKey.interestOps(selectionKey.readyOps() & ~keyOps);
            } catch (CancelledKeyException e) {
                return;
            }
        }

        Runnable runnable = null;

        try {
            runnable = map.get(selectionKey);
        } catch (Exception ignored) {
        }

        // 异步调度
        if (runnable != null && !executorService.isShutdown()) {
            executorService.execute(runnable);
        }
    }

    private static void waitSelection(AtomicBoolean locker) {
        //noinspection SynchronizationOnLocalVariableOrMethodParameter
        synchronized (locker) {
            //如果处于 locker 所表示的状态，让该线程等待。
            if (locker.get()) {
                try {
                    locker.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 给SocketChannel注册对应的事件，该方法是线程安全的，因为还有另外的线程对 map 进行操作。
     */
    private SelectionKey registerSelection(SocketChannel channel, Selector selector, int registerOps,
                                           AtomicBoolean locker, Map<SelectionKey, Runnable> map, Runnable runnable) {
        //要求线程安全
        //noinspection SynchronizationOnLocalVariableOrMethodParameter
        synchronized (locker) {
            //设置位锁定状态
            locker.set(true);
            try {

                //当 Selector 在执行 select 操作时，其实就是对已经注册的 key 队列的扫描过程。在这个过程中，是不允许其他线程更改其内部队列的(比如调用
                // Selector 的 register 方法，获取调用 Selector 内部 Key 的 interestOps 方法)，如果在扫描过程中其他线程调用方法修改 Selector 内部的队列 ，
                // 可能导致线程阻塞导致阻塞，所以应该下先调用 Selector 的 wakeup 方法，让 Selector 立即从 select 方法返回（此时它可能只遍历了队列的一部分）。
                // 之后在进行修改修改。
                // 唤醒当前的selector，让selector不处于select()状态
                selector.wakeup();

                SelectionKey selectionKey = null;
                //如果注册过就获取Key后修改Key的组合值
                if (channel.isRegistered()) {
                    //获取表示通道向给定选择器注册的键。 当此通道是向给定选择器注册的最后一个通道时返回该键，如果此通道当前未向该选择器注册，则返回 null
                    selectionKey = channel.keyFor(selector);
                    if (selectionKey != null) {
                        // selectionKey 中的 readyOps 值是一个复合值，注册多个事件到 selectionKey 中，将以位的形式保存。
                        // readyOps 用于获取此键的 ready 操作集合。
                        // interestOps 用于将此键的 interest 集合设置为给定值。下面按位或的操作就是组合多个操作
                        selectionKey.interestOps(selectionKey.readyOps() | registerOps);
                    }
                }
                //如果没有注册过，则注册后添加
                if (selectionKey == null) {
                    selectionKey = channel.register(selector, registerOps);
                    map.put(selectionKey, runnable);
                }
                return selectionKey;
            } catch (ClosedChannelException
                    | ClosedSelectorException
                    | CancelledKeyException ignore) {
                return null;
            } finally {
                //设置为非锁定状态
                locker.set(false);
                try {
                    //通知被锁定的线程
                    locker.notify();
                } catch (Exception ignored) {
                }
            }
        }
    }

    static class SelectThread extends Thread {

        private final AtomicBoolean mLocker;
        private final Selector mSelector;
        private final HashMap<SelectionKey, Runnable> mCallbackMap;
        private final ExecutorService mHandlePool;
        private final int mKeyOps;
        private final AtomicBoolean mIsClosed;

        SelectThread(String name, AtomicBoolean isClosed, AtomicBoolean locker, Selector selector,
                     HashMap<SelectionKey, Runnable> mCallbackMap, ExecutorService handlePool, int ops) {
            super(name);

            this.mIsClosed = isClosed;
            this.mLocker = locker;
            this.mSelector = selector;
            this.mCallbackMap = mCallbackMap;
            this.mHandlePool = handlePool;
            this.mKeyOps = ops;

            //因为希望客户端得到最快的响应，所以设置读写为最高的优先级
            setPriority(Thread.MAX_PRIORITY);
        }

        @Override
        public void run() {
            AtomicBoolean isClosed = mIsClosed;
            AtomicBoolean locker = mLocker;
            Selector selector = mSelector;
            HashMap<SelectionKey, Runnable> map = mCallbackMap;
            ExecutorService service = mHandlePool;
            int keyOps = mKeyOps;

            while (!isClosed.get()) {
                try {
                    //阻塞等待可写
                    if (selector.select() == 0) {
                        waitSelection(locker);
                        continue;
                    } else if (locker.get()) {
                        //如果 locker 为 true，说明正在操作 Selector 中的队列，此时依然需要停止Selector的select操作，等待操作完成。
                        waitSelection(locker);
                    }

                    //获取到可写的 SelectionKey，readSelector
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    //这里使用迭代器，而不是for循环，因为正在迭代的 key 能被取消（remove）掉，从而导致可能的并发修改异常。
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        //有两种情况从select中返回：
                        //select遍历获取到了符合注册条件的key
                        //wakeup
                        //注册  selector 的 channel 被关闭的，这个 channel 的key 会被返回，进入到读写流程，出些读写异常，从而得知连接中断
                        if (key.isValid()) {
                            handleSelection(key, keyOps, map, service, locker);
                        }
                        iterator.remove();
                    }
                    //处理完后需要清理

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClosedSelectorException e) {
                    break;
                }//try end
            }//while end
        }//run end

    }

}
