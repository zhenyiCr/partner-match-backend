package com.yupi.usercenter;

import com.yupi.usercenter.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;

@SpringBootTest
public class RedisTest {

    @Resource
    private RedisTemplate redisTemplate;


    @Test
    void testRedis() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set("testKey", "testValue");
        User user = new User();
        user.setId(1L);
        user.setUsername("name114514");
        valueOperations.set("testUserKey", user);

        // 查
        String testValue = (String) valueOperations.get("testKey");
        Assertions.assertTrue("testValue".equals(testValue));
        User testUser = (User) valueOperations.get("testUserKey");
        Assertions.assertTrue("name114514".equals(testUser.getUsername()));
    }
}
