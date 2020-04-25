package com.liuyang.common.cache.agg;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * redis代理人
 */
public abstract class RedisPipelineAgent {

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

        trySync();

        Object o = objectMap.get(item);
        return item.getHandler().apply(o);
    }

    private void trySync() {
        if (objectMap != null) {
            return;
        }
        try (Jedis jedis = getJedis()) {
            Pipeline p = jedis.pipelined();
            objectMap = new HashMap<>();

            for (RedisItem<?> item : items) {
                item.getReader().accept(p);
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
