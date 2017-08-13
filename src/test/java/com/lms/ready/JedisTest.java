package com.lms.ready;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class JedisTest {

    @Resource
    private JedisPool jedisPool;

    @Test
    public void testSet() {
        Jedis jedis = jedisPool.getResource();
        jedis.set("age", "23");
        jedis.close();
    }

}
