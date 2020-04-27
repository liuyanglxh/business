package com.liuyang.common.cache.agg;

import lombok.Getter;
import redis.clients.jedis.commands.RedisPipeline;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * redis任务
 *
 * @param <T> 业务线最终返回的数据类型
 */
@Getter
public class RedisTask<T> {

    //读取redis的方式
    private Consumer<RedisPipeline> reader;

    //处理redis数据的方式
    private Function<List<Object>, T> handler;

    public RedisTask(Consumer<RedisPipeline> reader, Function<List<Object>, T> handler) {
        this.reader = reader;
        this.handler = handler;
    }
}
