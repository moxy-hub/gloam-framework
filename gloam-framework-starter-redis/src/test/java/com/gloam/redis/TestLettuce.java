package com.gloam.redis;

import com.gloamframework.data.redis.GloamRedisTemplate;
import com.gloamframework.data.redis.RedisUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author 晓龙
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class TestLettuce {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class User {
        private String name;
        private int age;
        private Long phone = null;
    }

    @Autowired
    private GloamRedisTemplate redisTemplate;

    @Test
    public void contextLoads() {
        redisTemplate.opsForValue().set("test:aa", new User("name", 18, null));
        User user = (User) redisTemplate.opsForValue().get("test:aa");
        System.out.println(user);
        System.out.println(redisTemplate.getConnectionFactory());
    }

    @Test
    public void testUtil() {
        RedisUtil.Ops.set("test:aa2", new User("name", 28, null));
        User user = (User) RedisUtil.Ops.get("test:aa2");
        System.out.println(user);
    }
}
