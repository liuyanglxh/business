package com.liuyang.common.cache.redis.test.service.impl;

import com.liuyang.common.cache.agg.RedisTask;
import com.liuyang.common.cache.redis.test.PipeTest;
import com.liuyang.common.cache.redis.test.pojo.Person;
import com.liuyang.common.cache.redis.test.service.PersonService;
import com.liuyang.common.utils.ObjectConvertUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.commands.RedisPipeline;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PersonServiceImpl implements PersonService {

    @Override
    public RedisTask<Person> get(Integer id) {

        String legalStatus = "legalStatus";

        //从缓存取数据的方法
        Consumer<RedisPipeline> reader = pipeline -> {
            pipeline.get(this.key(id));
            pipeline.hget(this.legalKey(id), legalStatus);
        };

        //拿到数据后解析的方式
        Function<List<Object>, Person> handler = objects -> {
            Object obj0 = objects.get(0);
            Person p;
            //缓存不命中
            if (obj0 == null) {

                //查数据库 + 写缓存
                p = new Person();
                p.setId(id);
                p.setName("小明");

                try (Jedis jedis = PipeTest.jedisPool.getResource()) {
                    jedis.set(this.key(id), ObjectConvertUtil.writeAsString(p));
                }

            }
            //缓存命中
            else {
                p = ObjectConvertUtil.readValue((String) obj0, Person.class);
            }

            Object obj1 = objects.get(1);
            Boolean ok;
            if (obj1 == null) {

                // 查数据库 + 写缓存
                ok = Boolean.TRUE;
                //写缓存
                try (Jedis jedis = PipeTest.jedisPool.getResource()) {
                    jedis.hset(this.legalKey(id), legalStatus, ok.toString());
                }

            } else {
                ok = Boolean.valueOf(obj1.toString());
            }
            return ok ? p : null;
        };

        return new RedisTask<>(reader, handler);
    }

    @Override
    public RedisTask<Map<Integer, Person>> getAll(Collection<Integer> ids) {

        List<String> keys = ids.stream().map(this::key).collect(Collectors.toList());

        //缓存reader
        Consumer<Pipeline> reader = pipeline -> pipeline.mget(keys.toArray(new String[]{}));

        //缓存handler
        Function<Object, Map<Integer, Person>> handler = obj -> {
            //缓存命中

            //缓存缺失

            return null;
        };

        return null;
    }

    private Map<Integer, Person> missing(Collection<Integer> missingIds) {
        return missingIds.stream().map(missingId -> {
            Person p = new Person();
            p.setId(missingId);
            p.setName(missingId + "的名字");
            return p;
        }).collect(Collectors.toMap(Person::getId, Function.identity()));
    }

    private String key(Integer id) {
        return "person-" + id;
    }

    private String legalKey(Integer id) {
        return "person-legal-" + id;
    }

}
