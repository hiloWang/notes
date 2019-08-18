package me.ztiany.jcipg.chapter06;

import java.util.ArrayList;
import java.util.List;


/**
 * 使用`等待-通知`机制优化过后的 Allocator：
 */
class Allocator {

    private Allocator() {
    }

    private List<Account> locks = new ArrayList<>();

    synchronized void apply(Account src, Account tag) {
        while (locks.contains(src) || locks.contains(tag)) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        locks.add(src);
        locks.add(tag);
    }

    synchronized void release(Account src, Account tag) {
        locks.remove(src);
        locks.remove(tag);
        this.notifyAll();
    }

    static Allocator getInstance() {
        return AllocatorSingle.install;
    }

    static class AllocatorSingle {
        static Allocator install = new Allocator();
    }

}