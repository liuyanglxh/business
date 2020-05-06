package com.liuyang.common.cache.agg;

import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public abstract class RedisPipelineTaskManagerFactory {

    protected abstract Jedis getJedis();

    public RedisPipelineTaskManager getManager(List<RedisTask<?>> tasks) {
        return new RedisPipelineTaskManager(tasks, this::getJedis);
    }

    public RedisPipelineTaskManager getManager(RedisTask<?>... tasks) {
        return new RedisPipelineTaskManager(Stream.of(tasks).collect(Collectors.toList()), this::getJedis);
    }
}
