package com.liuyang.common.cache.redis.lua;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LuaScript {

    /**
     * sismember命令
     * 1 -- 包含元素
     * 0 -- 不包含元素
     * -1 -- key不存在
     */
    SISMEMBER("local e = redis.call('sismember', KEYS[1], ARGV[1]) if e == 1 then return 1 end return (redis.call('exists', KEYS[1]) - 1);"),
    /**
     * 调用sadd
     * 返回 1 -- 成功  0 -- key不存在，不执行操作
     */
    SADD_WHEN_EXISTS("local e = redis.call('expire', KEYS[1], ARGV[1]) if e == 1 then redis.call('sadd', KEYS[1], ARGV[2]) return 1 end return 0"),

    /**
     * 调用scard
     * 返回 >=0 key存在  -1 -- key不存在
     */
    SCARD("local e = redis.call('scard', KEYS[1]) if e > 0 then return e end return redis.call('exists', KEYS[1]) - 1"),

    /**
     * 推荐菜基础信息
     * 返回map结构：info、imageIds、textIds
     */
//    DISH_INFOS("local r = {redis.call('hgetall', KEYS[1]), redis.call('get', KEYS[2]), redis.call('get', KEYS[3])} return r")
    DISH_INFOS("local r = {info = redis.call('hgetall', KEYS[1]), imageIds = ('get', KEYS[2]), textIds = redis.call('get', KEYS[3])} return cjson.encode(r)"),

    /**
     * 用sortedset保存最近n个用户
     */
    RECENT_LIKE_USERS_ADD("local ex = redis.call('expire', KEYS[1], ARGV[4]) if ex == 0 then return -1 end redis.call('zadd', KEYS[1], ARGV[1], ARGV[2]) local count = redis.call('zcount', KEYS[1], 0, tonumber(ARGV[1]))  if count > tonumber(ARGV[3])  then local endIndex = count - tonumber(ARGV[3]) redis.call('zremrangebyrank', KEYS[1], 0, endIndex)  return count - endIndex end return count"),

    RECENT_LIKE_USERS_DEL("redis.call('zrem', KEYS[1], ARGV[1]) return count = redis.call('zcount', KEYS[1], 0, tonumber(ARGV[1]))"),
    ;


    private String script;
}
