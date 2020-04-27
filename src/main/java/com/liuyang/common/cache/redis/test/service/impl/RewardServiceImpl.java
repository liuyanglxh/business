package com.liuyang.common.cache.redis.test.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.liuyang.common.cache.agg.RedisTask;
import com.liuyang.common.cache.redis.test.PipeTest;
import com.liuyang.common.cache.redis.test.pojo.Reward;
import com.liuyang.common.cache.redis.test.service.RewardService;
import com.liuyang.common.utils.ObjectConvertUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.commands.RedisPipeline;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class RewardServiceImpl implements RewardService {

    @Override
    public RedisTask<Reward> getByPerson(Integer personId) {

        //从缓存取数据的方法
        Consumer<RedisPipeline> reader = pipeline -> pipeline.get(this.key(personId));

        //拿到数据后解析的方式
        Function<List<Object>, Reward> handler = objects -> {

            Object obj = objects.get(0);
            //缓存不命中
            if (obj == null) {

                //模拟从数据库查数据
                Reward r = new Reward();
                r.setId(personId + 100);
                r.setPersonId(personId);
                r.setName(personId + "的奖励");
                //写缓存
                try (Jedis jedis = PipeTest.jedisPool.getResource()) {
                    jedis.set(this.key(personId), ObjectConvertUtil.writeAsString(r));
                }

                return r;
            }
            //缓存命中
            else {
                return ObjectConvertUtil.readValue((String) obj, new TypeReference<Reward>() {
                });
            }

        };

        return new RedisTask<>(reader, handler);
    }

    private String key(Integer id) {
        return "reward-person-" + id;
    }
}
