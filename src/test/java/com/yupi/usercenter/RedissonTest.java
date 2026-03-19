package com.yupi.usercenter;

import org.junit.jupiter.api.Test;
import org.redisson.api.RList;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class RedissonTest {

    @Resource
    private RedissonClient redissonClient;

    @Test
    void test() {

        // list
        List<String> list = new ArrayList<>();
        list.add("1");
        System.out.println("list = " + list);

        // 从 Redisson 中获取分布式列表
        RList<String> rList = redissonClient.getList("myList");
//        rList.add("2");
        System.out.println("rList = " + rList);
        rList.remove(0);

        // Map
        Map<String, Integer> map = new HashMap<>();
        map.put("1", 1);
        System.out.println("map = " + map);

        RMap<Object, Object> map1 = redissonClient.getMap("myMap");

    }

    @Test
    void testWatchDog() {
        RLock lock = redissonClient.getLock("zhenyi:preCacheJob:docache:lock");
        try {
            // 只有一个线程能获取到
            lock.tryLock(0, -1, TimeUnit.SECONDS);
            Thread.sleep(30000000);
            System.out.println("lock = " + Thread.currentThread().getId());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            // 只能释放当前线程的锁
            if (lock.isHeldByCurrentThread()) {
                System.out.println("unlock = " + Thread.currentThread().getId());
                lock.unlock();
            }
        }
    }





}
