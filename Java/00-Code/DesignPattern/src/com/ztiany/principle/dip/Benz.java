package com.ztiany.principle.dip;


public class Benz implements ICar{
    @Override
    public void run() {
        System.out.println("奔驰车子启动");
    }
}
