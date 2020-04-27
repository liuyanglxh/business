package com.liuyang.common.cache.redis.test.service;

import com.liuyang.common.cache.agg.RedisTask;
import com.liuyang.common.cache.redis.test.pojo.Person;

import java.util.Collection;
import java.util.Map;

public interface PersonService {

    RedisTask<Person> get(Integer id);

    RedisTask<Map<Integer, Person>> getAll(Collection<Integer> ids);
}
