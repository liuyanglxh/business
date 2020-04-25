package com.liuyang.common.cache.agg;

import com.fasterxml.jackson.databind.ObjectMapper;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class RedisPipeOperator {

    private List<RedisItem<?>> items;

    private Map<RedisItem, Object> objectMap;

    protected abstract Jedis getJedis();

    public void addItem(RedisItem item) {
        if (items == null) {
            items = new ArrayList<>();
        }
        items.add(item);
    }

    public <T> T syncAndGet(RedisItem<T> item) {

        sync();

        Object o = objectMap.get(item);
        if (o == null) {
            return null;
        }

        return new ObjectMapper().convertValue(o, item.getReference());
    }

    private void sync() {
        if (objectMap == null) {
            Pipeline p = getJedis().pipelined();
            objectMap = new HashMap<>();

            for (RedisItem<?> item : items) {
                item.getReader().apply(p);
            }

            List<Object> objects = p.syncAndReturnAll();

            for (int i = 0; i < items.size(); i++) {
                RedisItem<?> item = items.get(i);
                Object obj = objects.get(i);
                objectMap.put(item, obj);
            }
        }
    }

}
