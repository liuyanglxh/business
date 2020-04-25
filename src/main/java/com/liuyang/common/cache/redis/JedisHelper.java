package com.liuyang.common.cache.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liuyang.common.cache.redis.lua.LuaScript;
import com.liuyang.common.utils.encode.SHA1Utils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisNoScriptException;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class JedisHelper implements RedisHelper {

    private Jedis jedis = new JedisPool("127.0.0.1", 6379).getResource();

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public String get(String key) {
        return null;
    }

    @Override
    public <T> T get(String key, Class<T> clazz) {
        return null;
    }

    @Override
    public <T> T get(String key, TypeReference<T> valueTypeRef) {
        return null;
    }

    @Override
    public void set(String key, Object o, int timeout) {

    }

    @Override
    public Long setnx(String key, Object o) {
        return null;
    }

    @Override
    public Long setnx(String key, Object o, int timeout) {
        return null;
    }

    @Override
    public void del(String... keys) {

    }

    @Override
    public <V> void del(Function<V, String> keySwapper, V... keys) {

    }

    @Override
    public void del(Collection<String> keys) {

    }

    @Override
    public <V> void del(Function<V, String> keySwapper, Collection<V> keys) {

    }

    @Override
    public <T> T hget(String key, String field, TypeReference<T> valueTypeRef) {
        return null;
    }

    @Override
    public <T> T hget(String key, String field, Class<T> classType) {
        return null;
    }

    @Override
    public void hdel(String key, String... fields) {

    }

    @Override
    public void hdel(String key, Collection<String> fields) {

    }

    @Override
    public Long hlen(String key) {
        return null;
    }

    @Override
    public <T> void hdel(String key, Function<T, String> fieldSwapper, T... fields) {

    }

    @Override
    public <T> void hdel(String key, Function<T, String> fieldSwapper, Collection<T> fields) {

    }

    @Override
    public void hset(String key, String field, Object o, int timeout) {

    }

    @Override
    public void hmset(String key, Map<String, ?> data, int timeout) {

    }

    @Override
    public <K> void hmset(String key, Map<K, ?> data, int timeout, Function<K, String> keySwapper) {

    }

    @Override
    public <K> void hmset(String key, Collection<K> data, int timeout, Function<K, String> keySwapper) {

    }

    @Override
    public void hset(String key, String field, Object o) {

    }

    @Override
    public Set<String> keys(String pattern) {
        return null;
    }

    @Override
    public boolean exists(String key) {
        return false;
    }

    @Override
    public Long incr(String key) {
        return null;
    }

    @Override
    public Long incrby(String key, long increment) {
        return null;
    }

    @Override
    public Long expire(String key, int seconds) {
        return null;
    }

    @Override
    public Long ttl(String key) {
        return null;
    }

    @Override
    public Long lpush(String listName, Object o) {
        return null;
    }

    @Override
    public Long llen(String s) {
        return null;
    }

    @Override
    public Long hincrby(String key, String field, long increment) {
        return null;
    }

    @Override
    public <T> Map<String, T> hgetAll(String key, Class<T> clazz) {
        return null;
    }

    @Override
    public <T> Map<String, T> hgetAll(String key, TypeReference<T> type) {
        return null;
    }

    @Override
    public Map<String, String> hgetAll(String key) {
        Map<String, String> map = jedis.hgetAll(key);
        return map;
    }

    @Override
    public <T> List<T> lrange(String key, Integer start, Integer end, Class<T> classType) {
        return null;
    }

    @Override
    public Long getListSize(String key) {
        return null;
    }

    @Override
    public List<Object> pipelineRpush(String listName, List<?> list) {
        return null;
    }

    @Override
    public <T> List<T> ramdomList(String key, int size, Class<T> classType) {
        return null;
    }

    @Override
    public <T> Map<String, T> mget(Class<T> clazz, Collection<String> keys) {
        return null;
    }

    @Override
    public <T> Map<String, T> mget(TypeReference<T> typeReference, Collection<String> keys) {
        return null;
    }

    @Override
    public <K, T> Map<K, T> mget(Class<T> clazz, Collection<K> keys, Function<K, String> keySwapper) {
        return null;
    }

    @Override
    public <K, T> Map<K, T> mget(TypeReference<T> typeReference, Collection<K> keys, Function<K, String> keySwapper) {
        return null;
    }

    @Override
    public <T> Map<String, T> hmget(String key, Class<T> clazz, Collection<String> fields) {
        return null;
    }

    @Override
    public <T> Map<String, T> hmget(String key, TypeReference<T> typeReference, Collection<String> fields) {
        return null;
    }

    @Override
    public <K, T> Map<K, T> hmget(String key, Class<T> clazz, Collection<K> fields, Function<K, String> fieldSwapper) {
        return null;
    }

    @Override
    public <K, T> Map<K, T> hmget(String key, TypeReference<T> typeReference, Collection<K> fields, Function<K, String> fieldSwapper) {
        return null;
    }

    @Override
    public void mset(Map<String, ?> map) {

    }

    @Override
    public void msetEx(Map<String, ?> map, int timeout) {

    }

    @Override
    public void hmset(String key, Map<String, ?> map) {

    }

    @Override
    public <K> void mset(Map<K, ?> map, Function<K, String> keySwapper) {

    }

    @Override
    public <V> void mset(Collection<V> data, Function<V, String> keySwapper) {

    }

    @Override
    public <K> void msetEx(Map<K, ?> map, Function<K, String> keySwapper, int timeout) {

    }

    @Override
    public <V> void msetEx(Collection<V> data, Function<V, String> keySwapper, int timeout) {

    }

    @Override
    public <K> void hmset(String key, Map<K, ?> map, Function<K, String> fieldSwapper) {

    }

    @Override
    public <V> void hmset(String key, Collection<V> data, Function<V, String> fieldSwapper) {

    }

    @Override
    public Object eval(LuaScript script, String key, List<Object> args) {
        String sha1 = SHA1Utils.sha1(script.getScript());
        List<String> strs = args.stream().map(this::getString).collect(Collectors.toList());
        try {
            return jedis.evalsha(sha1, Collections.singletonList(key), strs);
        } catch (JedisNoScriptException e) {
            return jedis.eval(script.getScript(), Collections.singletonList(key), strs);
        }
    }

    @Override
    public Object eval(LuaScript script, String key) {
        String sha1 = SHA1Utils.sha1(script.getScript());
        try {
            return jedis.evalsha(sha1, 1, key);
        } catch (JedisNoScriptException e) {
            return jedis.eval(script.getScript(), 1, key);
        }
    }

    @Override
    public Object eval(LuaScript script) {
        String sha1 = SHA1Utils.sha1(script.getScript());
        try {
            return jedis.evalsha(sha1);
        } catch (JedisNoScriptException e) {
            return jedis.eval(script.getScript());
        }
    }

    private String getString(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof String) {
            return (String) obj;
        }
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
