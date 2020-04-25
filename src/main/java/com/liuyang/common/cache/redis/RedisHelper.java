package com.liuyang.common.cache.redis;

import com.fasterxml.jackson.core.type.TypeReference;
import com.liuyang.common.cache.redis.lua.LuaScript;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public interface RedisHelper {
    int DEFAULT_TIMEOUT = 10 * 60;

    String get(String key);

    <T> T get(String key, Class<T> clazz);

    <T> T get(String key, TypeReference<T> valueTypeRef);

    void set(String key, Object o, int timeout);

    Long setnx(String key, Object o);

    Long setnx(String key, Object o, int timeout);

    void del(String... keys);

    <V> void del(Function<V, String> keySwapper, V... keys);

    void del(Collection<String> keys);

    <V> void del(Function<V, String> keySwapper, Collection<V> keys);

    <T> T hget(String key, String field, TypeReference<T> valueTypeRef);

    <T> T hget(String key, String field, Class<T> classType);

    void hdel(String key, String... fields);

    void hdel(String key, Collection<String> fields);

    Long hlen(String key);

    <T> void hdel(String key, Function<T, String> fieldSwapper, T... fields);

    <T> void hdel(String key, Function<T, String> fieldSwapper, Collection<T> fields);

    void hset(String key, String field, Object o, int timeout);

    void hmset(String key, Map<String, ?> data, int timeout);

    /**
     * @param key
     * @param data
     * @param timeout
     * @param keySwapper 写入redis的field是字符串形式，此函数用于将给定的类型转换成字符串
     */
    <K> void hmset(String key, Map<K, ?> data, int timeout, Function<K, String> keySwapper);

    <K> void hmset(String key, Collection<K> data, int timeout, Function<K, String> keySwapper);

    void hset(String key, String field, Object o);

    /**
     * 线上Codis集群不支持KEYS命令（慎用）
     *
     * @param pattern
     * @return
     */
    Set<String> keys(String pattern);

    boolean exists(String key);

    Long incr(String key);

    Long incrby(String key, long increment);

    Long expire(String key, int seconds);

    Long ttl(String key);

    Long lpush(String listName, Object o);

    Long llen(String s);

    Long hincrby(String key, String field, long increment);

    <T> Map<String, T> hgetAll(String key, Class<T> clazz);

    <T> Map<String, T> hgetAll(String key, TypeReference<T> type);

    Map<String, String> hgetAll(String key);

    <T> List<T> lrange(String key, Integer start, Integer end, Class<T> classType);

    Long getListSize(String key);

    List<Object> pipelineRpush(String listName, List<?> list);

    <T> List<T> ramdomList(String key, int size, Class<T> classType);

    <T> Map<String, T> mget(Class<T> clazz, Collection<String> keys);

    <T> Map<String, T> mget(TypeReference<T> typeReference, Collection<String> keys);

    <K, T> Map<K, T> mget(Class<T> clazz, Collection<K> keys, Function<K, String> keySwapper);

    <K, T> Map<K, T> mget(TypeReference<T> typeReference, Collection<K> keys, Function<K, String> keySwapper);

    <T> Map<String, T> hmget(String key, Class<T> clazz, Collection<String> fields);

    <T> Map<String, T> hmget(String key, TypeReference<T> typeReference, Collection<String> fields);

    <K, T> Map<K, T> hmget(String key, Class<T> clazz, Collection<K> fields, Function<K, String> fieldSwapper);

    <K, T> Map<K, T> hmget(String key, TypeReference<T> typeReference, Collection<K> fields, Function<K, String> fieldSwapper);

    void mset(Map<String, ?> map);

    void msetEx(Map<String, ?> map, int timeout);

    void hmset(String key, Map<String, ?> map);

    <K> void mset(Map<K, ?> map, Function<K, String> keySwapper);

    <V> void mset(Collection<V> data, Function<V, String> keySwapper);

    <K> void msetEx(Map<K, ?> map, Function<K, String> keySwapper, int timeout);

    <V> void msetEx(Collection<V> data, Function<V, String> keySwapper, int timeout);

    <K> void hmset(String key, Map<K, ?> map, Function<K, String> fieldSwapper);

    <V> void hmset(String key, Collection<V> data, Function<V, String> fieldSwapper);

    Object eval(LuaScript script, String key, List<Object> args);

    Object eval(LuaScript script, String key);

    Object eval(LuaScript script);

    default String generateKey(Object... params) {
        StringBuilder builder = new StringBuilder();
        for (Object param : params) {
            builder.append(param).append("_");
        }
        if (builder.length() > 0) {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }


}
