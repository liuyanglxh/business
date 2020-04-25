package com.liuyang.common.cache.agg;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import redis.clients.jedis.Pipeline;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * redis操作单位
 *
 * @param <T> 业务线最终返回的数据类型
 */
@Getter
@AllArgsConstructor
public class RedisItem<T> {

    //读取redis的方式
    private Consumer<Pipeline> reader;

    //处理redis数据的方式
    private Function<Object, T> handler;

}
