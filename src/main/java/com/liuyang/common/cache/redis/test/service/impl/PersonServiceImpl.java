package com.liuyang.common.cache.redis.test.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liuyang.common.cache.agg.RedisItem;
import com.liuyang.common.cache.redis.test.PipeTest;
import com.liuyang.common.cache.redis.test.pojo.Person;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Function;

public class PersonService {

    public RedisItem<Person> get(Integer id) {

        //从缓存取数据的方法
        Consumer<Pipeline> reader = pipeline -> pipeline.get(this.key(id));

        //拿到数据后解析的方式
        Function<Object, Person> handler = obj -> {
            //缓存命中
            ObjectMapper mapper = new ObjectMapper();
            if (obj != null) {
                try {
                    return mapper.readValue((String) obj, Person.class);
                } catch (IOException e) {
                    return null;
                }
            }
            //缓存不命中
            else {
                Person r = new Person();
                r.setId(id);
                r.setName(id + "的名字");
                //写缓存
                try (Jedis jedis = PipeTest.jedisPool.getResource()) {
                    jedis.set(this.key(id), mapper.writeValueAsString(r));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                return r;
            }
        };

        return new RedisItem<>(reader, handler);
    }


    private String key(Integer id) {
        return "person-" + id;
    }
}
