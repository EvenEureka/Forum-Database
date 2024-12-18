package com.zhihu.zhihubackend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

@SpringBootTest  //如果在测试类中添加了该注解，在单元测试方法执行之前会初始化spring容器
public class RedisTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void testSet()
    {
        // 往redis中存储一个键值对
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.set("key1", "value1");
        operations.set("username","zhangsan",15, TimeUnit.SECONDS);
    }

    @Test
    public void testGet()
    {
        //从redis中获取一个键值对
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        String value = operations.get("key1");
        System.out.println(value);
    }

    @Test
    public void testDelte()
    {
        stringRedisTemplate.delete("key1");
    }
}
