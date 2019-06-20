package com.ztiany.jedis.base;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.5.6 15:40
 */
public class Connection {

    public static void main(String... args) {
        //testConnection();
        testPool();
    }

    /**
     * 获得单一的jedis对象操作数据库
     */
    public static void testConnection() {
        //1、获得连接对象
        Jedis jedis = new Jedis("xxx", 6379);
        jedis.auth("xxx");

        //2、获得数据
        String username = jedis.get("username");
        System.out.println(username);

        //3、存储
        jedis.set("address", "北京");
        System.out.println(jedis.get("address"));
    }

    /**
     * 通过jedis的pool获得jedis连接对象
     */
    public static void testPool() {

        //0、创建池子的配置对象
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(30);//最大闲置个数
        poolConfig.setMinIdle(10);//最小闲置个数
        poolConfig.setMaxTotal(50);//最大连接数

        //1、创建一个redis的连接池
        JedisPool pool = new JedisPool(poolConfig, "xxx", 6379);

        //2、从池子中获取redis的连接资源
        Jedis jedis = pool.getResource();
        jedis.auth("xxx");

        //3、操作数据库
        jedis.set("xxx", "yyyy");
        System.out.println(jedis.get("xxx"));

        //4、关闭资源
        jedis.close();
        pool.close();
    }
}
