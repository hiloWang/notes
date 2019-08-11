package me.ztiany.jcipg.chapter05;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2019/5/12 17:25
 */
public class Main {

    public static void main(String... args) {
        Account accountA = new Account(300);
        Account accountB = new Account(400);
        accountA.transfer(accountB, 100);
        System.out.println("accountA " + accountA.balance);
        System.out.println("accountB " + accountB.balance);
    }

    /**
     * 每个用户对应一个 Account 对象。
     */
    static class Account {

        private int balance;

        Account(int balance) {
            this.balance = balance;
        }

        /**
         * 转账
         *
         * @param target 转入账户
         * @param amt    转入金额
         */
        void transfer(Account target, int amt) {
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
        }

    }

}
