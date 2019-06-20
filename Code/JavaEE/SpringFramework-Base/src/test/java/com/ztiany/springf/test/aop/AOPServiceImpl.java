package com.ztiany.springf.test.aop;

/**
 * 该服务实现将被AOP切入功能
 */
public class AOPServiceImpl implements AOPService {

    @Override
    public void save() {
        System.out.println("保存用户!");
    }

    @Override
    public void delete() {
        System.out.println("删除用户!");
    }

    @Override
    public void update() {
        System.out.println("更新用户!");
    }

    @Override
    public void find() {
        System.out.println("查找用户!");
    }

}
