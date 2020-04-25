package com.liuyang.common.cache.redis.test.service.impl;

import com.liuyang.common.cache.agg.RedisItem;
import com.liuyang.common.cache.redis.test.PipeTest;
import com.liuyang.common.cache.redis.test.pojo.Person;
import com.liuyang.common.cache.redis.test.service.PersonService;
import com.liuyang.common.utils.ObjectConvertUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PersonServiceImpl implements PersonService {

    @Override
    public RedisItem<Person> get(Integer id) {

        //从缓存取数据的方法
        Consumer<Pipeline> reader = pipeline -> pipeline.get(this.key(id));

        //拿到数据后解析的方式
        Function<List<Object>, Person> handler = obj -> {
            //缓存不命中
            if (obj == null) {
                Person r = new Person();
                r.setId(id);
                r.setName(id + "的名字");
                //写缓存
                try (Jedis jedis = PipeTest.jedisPool.getResource()) {
                    jedis.set(this.key(id), ObjectConvertUtil.writeAsString(r));
                }
                return r;
            }
            //缓存命中
            else {
                return ObjectConvertUtil.readValue((String) obj.get(0), Person.class);
            }

        };

        return new RedisItem<>(reader, handler);
    }

    @Override
    public RedisItem<Map<Integer, Person>> getAll(Collection<Integer> ids) {

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
}
