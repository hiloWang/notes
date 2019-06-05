package com.ztiany.principle.dip;


public class Client {
    public static void main(String[] args) {
        Driver driver = new Driver();
        driver.driver(new Benz());
    }
}
