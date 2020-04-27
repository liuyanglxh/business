package com.liuyang.common.cache.agg;

import redis.clients.jedis.*;
import redis.clients.jedis.commands.RedisPipeline;
import redis.clients.jedis.params.GeoRadiusParam;
import redis.clients.jedis.params.SetParams;
import redis.clients.jedis.params.ZAddParams;
import redis.clients.jedis.params.ZIncrByParams;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PipelineProxy implements RedisPipeline {

    private Pipeline pipeline;

    private List<Integer> counts;

    private int index;

    public void move() {
        index++;
    }

    public int count() {
        return counts.get(index);
    }

    public void add() {
        if (index >= counts.size()) {
            counts.add(1);
        } else {

            counts.set(index, counts.get(index) + 1);
        }
    }

    public PipelineProxy(Pipeline pipeline) {
        this.pipeline = pipeline;
        counts = new ArrayList<>();
    }

    @Override
    public Response<Long> append(String key, String value) {
        add();
        return pipeline.append(key, value);
    }

    @Override
    public Response<List<String>> blpop(String arg) {
        add();
        return pipeline.blpop(arg);
    }

    @Override
    public Response<List<String>> brpop(String arg) {
        add();
        return pipeline.brpop(arg);
    }

    @Override
    public Response<Long> decr(String key) {
        add();
        return pipeline.decr(key);
    }

    @Override
    public Response<Long> decrBy(String key, long decrement) {
        add();
        return pipeline.decrBy(key, decrement);
    }

    @Override
    public Response<Long> del(String key) {
        add();
        return pipeline.del(key);
    }

    @Override
    public Response<Long> unlink(String key) {
        add();
        return pipeline.unlink(key);
    }

    @Override
    public Response<String> echo(String string) {
        add();
        return pipeline.echo(string);
    }

    @Override
    public Response<Boolean> exists(String key) {
        add();
        return pipeline.exists(key);
    }

    @Override
    public Response<Long> expire(String key, int seconds) {
        add();
        return pipeline.expire(key, seconds);
    }

    @Override
    public Response<Long> pexpire(String key, long milliseconds) {
        add();
        return pipeline.pexpire(key, milliseconds);
    }

    @Override
    public Response<Long> expireAt(String key, long unixTime) {
        add();
        return pipeline.expireAt(key, unixTime);
    }

    @Override
    public Response<Long> pexpireAt(String key, long millisecondsTimestamp) {
        add();
        return pipeline.expireAt(key, millisecondsTimestamp);
    }

    @Override
    public Response<String> get(String key) {
        add();
        return pipeline.get(key);
    }

    @Override
    public Response<Boolean> getbit(String key, long offset) {
        add();
        return pipeline.getbit(key, offset);
    }

    @Override
    public Response<String> getrange(String key, long startOffset, long endOffset) {
        add();
        return pipeline.getrange(key, startOffset, endOffset);
    }

    @Override
    public Response<String> getSet(String key, String value) {
        add();
        return pipeline.getSet(key, value);
    }

    @Override
    public Response<Long> hdel(String key, String... field) {
        add();
        return pipeline.hdel(key, field);
    }

    @Override
    public Response<Boolean> hexists(String key, String field) {
        add();
        return pipeline.hexists(key, field);
    }

    @Override
    public Response<String> hget(String key, String field) {
        add();
        return pipeline.hget(key, field);
    }

    @Override
    public Response<Map<String, String>> hgetAll(String key) {
        add();
        return pipeline.hgetAll(key);
    }

    @Override
    public Response<Long> hincrBy(String key, String field, long value) {
        add();
        return pipeline.hincrBy(key, field, value);
    }

    @Override
    public Response<Set<String>> hkeys(String key) {
        add();
        return pipeline.hkeys(key);
    }

    @Override
    public Response<Long> hlen(String key) {
        add();
        return pipeline.hlen(key);
    }

    @Override
    public Response<List<String>> hmget(String key, String... fields) {
        add();
        return pipeline.hmget(key, fields);
    }

    @Override
    public Response<String> hmset(String key, Map<String, String> hash) {
        add();
        return pipeline.hmset(key, hash);
    }

    @Override
    public Response<Long> hset(String key, String field, String value) {
        add();
        return pipeline.hset(key, field, value);
    }

    @Override
    public Response<Long> hset(String key, Map<String, String> hash) {
        add();
        return pipeline.hset(key, hash);
    }

    @Override
    public Response<Long> hsetnx(String key, String field, String value) {
        add();
        return pipeline.hsetnx(key, field, value);
    }

    @Override
    public Response<List<String>> hvals(String key) {
        add();
        return pipeline.hvals(key);
    }

    @Override
    public Response<Long> incr(String key) {
        add();
        return pipeline.incr(key);
    }

    @Override
    public Response<Long> incrBy(String key, long increment) {
        add();
        return pipeline.incrBy(key, increment);
    }

    @Override
    public Response<String> lindex(String key, long index) {
        add();
        return pipeline.lindex(key, index);
    }

    @Override
    public Response<Long> linsert(String key, ListPosition where, String pivot, String value) {
        add();
        return pipeline.linsert(key, where, pivot, value);
    }

    @Override
    public Response<Long> llen(String key) {
        add();
        return pipeline.llen(key);
    }

    @Override
    public Response<String> lpop(String key) {
        add();
        return pipeline.lpop(key);
    }

    @Override
    public Response<Long> lpush(String key, String... string) {
        add();
        return pipeline.lpush(key, string);
    }

    @Override
    public Response<Long> lpushx(String key, String... string) {
        add();
        return pipeline.lpushx(key, string);
    }

    @Override
    public Response<List<String>> lrange(String key, long start, long stop) {
        add();
        return pipeline.lrange(key, start, stop);
    }

    @Override
    public Response<Long> lrem(String key, long count, String value) {
        add();
        return pipeline.lrem(key, count, value);
    }

    @Override
    public Response<String> lset(String key, long index, String value) {
        add();
        return pipeline.lset(key, index, value);
    }

    @Override
    public Response<String> ltrim(String key, long start, long stop) {
        add();
        return pipeline.ltrim(key, start, stop);
    }

    @Override
    public Response<Long> move(String key, int dbIndex) {
        add();
        return pipeline.move(key, dbIndex);
    }

    @Override
    public Response<Long> persist(String key) {
        add();
        return pipeline.persist(key);
    }

    @Override
    public Response<String> rpop(String key) {
        add();
        return pipeline.rpop(key);
    }

    @Override
    public Response<Long> rpush(String key, String... string) {
        return null;
    }

    @Override
    public Response<Long> rpushx(String key, String... string) {
        return null;
    }

    @Override
    public Response<Long> sadd(String key, String... member) {
        return null;
    }

    @Override
    public Response<Long> scard(String key) {
        return null;
    }

    @Override
    public Response<Boolean> sismember(String key, String member) {
        return null;
    }

    @Override
    public Response<String> set(String key, String value) {
        return null;
    }

    @Override
    public Response<Boolean> setbit(String key, long offset, boolean value) {
        return null;
    }

    @Override
    public Response<String> setex(String key, int seconds, String value) {
        return null;
    }

    @Override
    public Response<Long> setnx(String key, String value) {
        return null;
    }

    @Override
    public Response<Long> setrange(String key, long offset, String value) {
        return null;
    }

    @Override
    public Response<Set<String>> smembers(String key) {
        return null;
    }

    @Override
    public Response<List<String>> sort(String key) {
        return null;
    }

    @Override
    public Response<List<String>> sort(String key, SortingParams sortingParameters) {
        return null;
    }

    @Override
    public Response<String> spop(String key) {
        return null;
    }

    @Override
    public Response<Set<String>> spop(String key, long count) {
        return null;
    }

    @Override
    public Response<String> srandmember(String key) {
        return null;
    }

    @Override
    public Response<Long> srem(String key, String... member) {
        return null;
    }

    @Override
    public Response<Long> strlen(String key) {
        return null;
    }

    @Override
    public Response<String> substr(String key, int start, int end) {
        return null;
    }

    @Override
    public Response<Long> touch(String key) {
        return null;
    }

    @Override
    public Response<Long> ttl(String key) {
        return null;
    }

    @Override
    public Response<Long> pttl(String key) {
        return null;
    }

    @Override
    public Response<String> type(String key) {
        return null;
    }

    @Override
    public Response<Long> zadd(String key, double score, String member) {
        return null;
    }

    @Override
    public Response<Long> zadd(String key, double score, String member, ZAddParams params) {
        return null;
    }

    @Override
    public Response<Long> zadd(String key, Map<String, Double> scoreMembers) {
        return null;
    }

    @Override
    public Response<Long> zadd(String key, Map<String, Double> scoreMembers, ZAddParams params) {
        return null;
    }

    @Override
    public Response<Long> zcard(String key) {
        return null;
    }

    @Override
    public Response<Long> zcount(String key, double min, double max) {
        return null;
    }

    @Override
    public Response<Long> zcount(String key, String min, String max) {
        return null;
    }

    @Override
    public Response<Double> zincrby(String key, double increment, String member) {
        return null;
    }

    @Override
    public Response<Double> zincrby(String key, double increment, String member, ZIncrByParams params) {
        return null;
    }

    @Override
    public Response<Set<String>> zrange(String key, long start, long stop) {
        return null;
    }

    @Override
    public Response<Set<String>> zrangeByScore(String key, double min, double max) {
        return null;
    }

    @Override
    public Response<Set<String>> zrangeByScore(String key, String min, String max) {
        return null;
    }

    @Override
    public Response<Set<String>> zrangeByScore(String key, double min, double max, int offset, int count) {
        return null;
    }

    @Override
    public Response<Set<String>> zrangeByScore(String key, String min, String max, int offset, int count) {
        return null;
    }

    @Override
    public Response<Set<Tuple>> zrangeByScoreWithScores(String key, double min, double max) {
        return null;
    }

    @Override
    public Response<Set<Tuple>> zrangeByScoreWithScores(String key, double min, double max, int offset, int count) {
        return null;
    }

    @Override
    public Response<Set<String>> zrevrangeByScore(String key, double max, double min) {
        return null;
    }

    @Override
    public Response<Set<String>> zrevrangeByScore(String key, String max, String min) {
        return null;
    }

    @Override
    public Response<Set<String>> zrevrangeByScore(String key, double max, double min, int offset, int count) {
        return null;
    }

    @Override
    public Response<Set<String>> zrevrangeByScore(String key, String max, String min, int offset, int count) {
        return null;
    }

    @Override
    public Response<Set<Tuple>> zrevrangeByScoreWithScores(String key, double max, double min) {
        return null;
    }

    @Override
    public Response<Set<Tuple>> zrevrangeByScoreWithScores(String key, String max, String min) {
        return null;
    }

    @Override
    public Response<Set<Tuple>> zrevrangeByScoreWithScores(String key, double max, double min, int offset, int count) {
        return null;
    }

    @Override
    public Response<Set<Tuple>> zrevrangeByScoreWithScores(String key, String max, String min, int offset, int count) {
        return null;
    }

    @Override
    public Response<Set<Tuple>> zrangeWithScores(String key, long start, long stop) {
        return null;
    }

    @Override
    public Response<Long> zrank(String key, String member) {
        return null;
    }

    @Override
    public Response<Long> zrem(String key, String... members) {
        return null;
    }

    @Override
    public Response<Long> zremrangeByRank(String key, long start, long stop) {
        return null;
    }

    @Override
    public Response<Long> zremrangeByScore(String key, double min, double max) {
        return null;
    }

    @Override
    public Response<Long> zremrangeByScore(String key, String min, String max) {
        return null;
    }

    @Override
    public Response<Set<String>> zrevrange(String key, long start, long stop) {
        return null;
    }

    @Override
    public Response<Set<Tuple>> zrevrangeWithScores(String key, long start, long stop) {
        return null;
    }

    @Override
    public Response<Long> zrevrank(String key, String member) {
        return null;
    }

    @Override
    public Response<Double> zscore(String key, String member) {
        return null;
    }

    @Override
    public Response<Long> zlexcount(String key, String min, String max) {
        return null;
    }

    @Override
    public Response<Set<String>> zrangeByLex(String key, String min, String max) {
        return null;
    }

    @Override
    public Response<Set<String>> zrangeByLex(String key, String min, String max, int offset, int count) {
        return null;
    }

    @Override
    public Response<Set<String>> zrevrangeByLex(String key, String max, String min) {
        return null;
    }

    @Override
    public Response<Set<String>> zrevrangeByLex(String key, String max, String min, int offset, int count) {
        return null;
    }

    @Override
    public Response<Long> zremrangeByLex(String key, String min, String max) {
        return null;
    }

    @Override
    public Response<Long> bitcount(String key) {
        return null;
    }

    @Override
    public Response<Long> bitcount(String key, long start, long end) {
        return null;
    }

    @Override
    public Response<Long> pfadd(String key, String... elements) {
        return null;
    }

    @Override
    public Response<Long> pfcount(String key) {
        return null;
    }

    @Override
    public Response<List<Long>> bitfield(String key, String... arguments) {
        return null;
    }

    @Override
    public Response<Long> hstrlen(String key, String field) {
        return null;
    }

    @Override
    public Response<byte[]> dump(String key) {
        return null;
    }

    @Override
    public Response<String> restore(String key, int ttl, byte[] serializedValue) {
        return null;
    }

    @Override
    public Response<String> restoreReplace(String key, int ttl, byte[] serializedValue) {
        return null;
    }

    @Override
    public Response<String> migrate(String host, int port, String key, int destinationDB, int timeout) {
        return null;
    }

    @Override
    public Response<Long> geoadd(String key, double longitude, double latitude, String member) {
        return null;
    }

    @Override
    public Response<Long> geoadd(String key, Map<String, GeoCoordinate> memberCoordinateMap) {
        return null;
    }

    @Override
    public Response<Double> geodist(String key, String member1, String member2) {
        return null;
    }

    @Override
    public Response<Double> geodist(String key, String member1, String member2, GeoUnit unit) {
        return null;
    }

    @Override
    public Response<List<String>> geohash(String key, String... members) {
        return null;
    }

    @Override
    public Response<List<GeoCoordinate>> geopos(String key, String... members) {
        return null;
    }

    @Override
    public Response<List<GeoRadiusResponse>> georadius(String key, double longitude, double latitude, double radius, GeoUnit unit) {
        return null;
    }

    @Override
    public Response<List<GeoRadiusResponse>> georadiusReadonly(String key, double longitude, double latitude, double radius, GeoUnit unit) {
        return null;
    }

    @Override
    public Response<List<GeoRadiusResponse>> georadius(String key, double longitude, double latitude, double radius, GeoUnit unit, GeoRadiusParam param) {
        return null;
    }

    @Override
    public Response<List<GeoRadiusResponse>> georadiusReadonly(String key, double longitude, double latitude, double radius, GeoUnit unit, GeoRadiusParam param) {
        return null;
    }

    @Override
    public Response<List<GeoRadiusResponse>> georadiusByMember(String key, String member, double radius, GeoUnit unit) {
        return null;
    }

    @Override
    public Response<List<GeoRadiusResponse>> georadiusByMemberReadonly(String key, String member, double radius, GeoUnit unit) {
        return null;
    }

    @Override
    public Response<List<GeoRadiusResponse>> georadiusByMember(String key, String member, double radius, GeoUnit unit, GeoRadiusParam param) {
        return null;
    }

    @Override
    public Response<List<GeoRadiusResponse>> georadiusByMemberReadonly(String key, String member, double radius, GeoUnit unit, GeoRadiusParam param) {
        return null;
    }

    @Override
    public Response<Long> bitpos(String key, boolean value) {
        return null;
    }

    @Override
    public Response<Long> bitpos(String key, boolean value, BitPosParams params) {
        return null;
    }

    @Override
    public Response<String> set(String key, String value, SetParams params) {
        return null;
    }

    @Override
    public Response<List<String>> srandmember(String key, int count) {
        return null;
    }

    @Override
    public Response<Set<Tuple>> zrangeByScoreWithScores(String key, String min, String max) {
        return null;
    }

    @Override
    public Response<Set<Tuple>> zrangeByScoreWithScores(String key, String min, String max, int offset, int count) {
        return null;
    }

    @Override
    public Response<Long> objectRefcount(String key) {
        return null;
    }

    @Override
    public Response<String> objectEncoding(String key) {
        return null;
    }

    @Override
    public Response<Long> objectIdletime(String key) {
        return null;
    }

    @Override
    public Response<Double> incrByFloat(String key, double increment) {
        return null;
    }

    @Override
    public Response<String> psetex(String key, long milliseconds, String value) {
        return null;
    }

    @Override
    public Response<Double> hincrByFloat(String key, String field, double increment) {
        return null;
    }
}
