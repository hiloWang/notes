package me.ztiany.jcipg.chapter05;

import java.util.ArrayList;
import java.util.List;

/**
 * 破坏占用且等待条件
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2019/8/11 11:55
 */
public class BreakDeathLock01 {

    public static void main(String... args) {
        Account accountA = new Account(300);
        Account accountB = new Account(400);
        accountA.transfer(accountB, 100);
        System.out.println("accountA " + accountA.getBalance());
        System.out.println("accountB " + accountB.getBalance());
    }

    static class Allocator {

        private static Allocator instance = new Allocator();

        static Allocator getInstance() {
            return instance;
        }

        private Allocator() {
        }

        private List<Object> als = new ArrayList<>();

        // 一次性申请所有资源
        synchronized boolean apply(Object from, Object to) {
            if (als.contains(from) || als.contains(to)) {
                return false;
            } else {
                als.add(from);
                als.add(to);
            }
            return true;
        }

        // 归还资源
        synchronized void free(Object from, Object to) {
            als.remove(from);
            als.remove(to);
        }
    }

    static class Account {

        private Allocator allocator = Allocator.getInstance();
        private int balance;

        Account(int balance) {
            this.balance = balance;
        }

        int getBalance() {
            return balance;
        }

        // 转账
        void transfer(Account target, int amt) {
            // 一次性申请转出账户和转入账户，直到成功
            while (!allocator.apply(this, target)) ;

            //我们使用while循环获取锁，后面的 synchronized(this)与synchronized(target)是不是就可以去掉了呢?
            //这个例子里可以，因为除此之外没有其他路径能够访问到this。如果还有别的方法，例如取款操作，那就不可以了。
            try {
                // 锁定转出账户
                synchronized (this) {
                    // 锁定转入账户
                    synchronized (target) {
                        if (this.balance > amt) {
                            this.balance -= amt;
                            target.balance += amt;
                        }
                    }
                }
            } finally {
                allocator.free(this, target);
            }
        }

    }

}

