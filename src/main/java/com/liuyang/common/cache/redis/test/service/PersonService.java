package com.liuyang.common.cache.redis.test.service;

import com.liuyang.common.cache.agg.RedisItem;
import com.liuyang.common.cache.redis.test.pojo.Person;

import java.util.Collection;
import java.util.Map;

public interface PersonService {

    RedisItem<Person> get(Integer id);

    RedisItem<Map<Integer, Person>> getAll(Collection<Integer> ids);
}
