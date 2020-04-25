package com.liuyang.common.cache.redis.test.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liuyang.common.cache.agg.RedisItem;
import com.liuyang.common.cache.redis.test.PipeTest;
import com.liuyang.common.cache.redis.test.pojo.Reward;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Function;

public class RewardService {

    public RedisItem<Reward> getByPerson(Integer personId) {
        //从缓存取数据的方法
        Consumer<Pipeline> reader = pipeline -> pipeline.get(this.key(personId));
        //拿到数据后解析的方式
        Function<Object, Reward> handler = obj -> {
            ObjectMapper mapper = new ObjectMapper();
            //缓存命中
            if (obj != null) {
                try {
                    return mapper.readValue((String) obj, new TypeReference<Reward>() {
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            //缓存不命中
            else {
                Reward r = new Reward();
                r.setId(personId + 100);
                r.setPersonId(personId);
                r.setName(personId + "的奖励");
                //写缓存
                try (Jedis jedis = PipeTest.jedisPool.getResource()) {
                    jedis.set(this.key(personId), mapper.writeValueAsString(r));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                return r;
            }
        };

        return new RedisItem<>(reader, handler);
    }

    private String key(Integer id) {
        return "reward-person-" + id;
    }
}
