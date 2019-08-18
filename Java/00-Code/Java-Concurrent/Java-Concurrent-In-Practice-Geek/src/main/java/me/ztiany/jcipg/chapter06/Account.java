package me.ztiany.jcipg.chapter06;

class Account {

    Account(Integer balance) {
        this.balance = balance;
    }

    private Integer balance;

    void transactionToTarget(Integer money, Account target) {//转账方法
        Allocator.getInstance().apply(this, target);

        this.balance -= money;
        target.setBalance(target.getBalance() + money);
        Allocator.getInstance().release(this, target);

    }

    Integer getBalance() {
        return balance;
    }

    private void setBalance(Integer balance) {
        this.balance = balance;
    }

}