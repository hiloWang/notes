package me.ztiany.jcipg.chapter05;

/**
 * 破坏循环等待条件
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2019/8/11 12:09
 */
public class BreakDeathLock02 {

    public static void main(String... args) {

    }

    static class Account {

        private int id;
        private int balance;

        public Account(int id, int balance) {
            this.id = id;
            this.balance = balance;
        }

        // 转账
        void transfer(Account target, int amt) {

            Account left = this;
            Account right = target;
            if (this.id > target.id) {
                left = target;
                right = this;
            }

            // 锁定序号小的账户
            synchronized (left) {
                // 锁定序号大的账户
                synchronized (right) {
                    if (this.balance > amt) {
                        this.balance -= amt;
                        target.balance += amt;
                    }
                }
            }
        }
    }

}