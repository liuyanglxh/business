package com.liuyang.common.cache.redis;

import com.liuyang.common.cache.redis.lua.LuaScript;
import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 点赞测试
 */
public class LikeTest {

    RedisHelper redisHelper = new JedisHelper();

    @Test
    public void testAfterLike() {
        this.like(1, 100);
        System.out.println(this.getTotalLike(10));
    }

    private void like(Integer resId, Integer userId) {
        String key = "like-record-" + resId;
        int tm = ThreadLocalRandom.current().nextInt(600, 1200);
        redisHelper.eval(LuaScript.SADD_WHEN_EXISTS, key, Arrays.asList(tm, userId));
    }

    private Long getTotalLike(Integer resId) {
        String key = "like-record-" + resId;
        return (Long) redisHelper.eval(LuaScript.SCARD, key);
    }

}
