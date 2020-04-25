package com.liuyang.common.cache.agg;

import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * redis代理人
 */
public abstract class RedisPipelineAgent {

    private List<RedisItem<?>> items;

    private Map<RedisItem, List<Object>> objectMap;

    private Map<RedisItem, Integer> itemCountMap;

    private boolean synced;

    protected abstract Jedis getJedis();

    public <T> RedisItem<T> addItem(RedisItem<T> item) {
        if (synced) {
            throw new RuntimeException("the agent has already synced,please call the method before sync");
        }
        if (items == null) {
            items = new ArrayList<>();
        }
        items.add(item);
        return item;
    }

    public <T> T get(RedisItem<T> item) {

        sync();

        List<Object> list = objectMap.get(item);
        return item.getHandler().apply(list);
    }

    private void sync() {
        if (synced) {
            return;
        }
        try (Jedis jedis = getJedis()) {
            PipelineCounter p = new PipelineCounter(jedis.pipelined());
            objectMap = new HashMap<>();

            for (RedisItem<?> item : items) {
                //调用获取redis数据的方法
                item.getReader().accept(p);
                itemCountMap.put(item, p.getCount());
                p.move();
            }

            List<Object> objects = p.syncAndReturnAll();

            int index = 0;
            for (RedisItem<?> item : items) {
                Integer count = itemCountMap.get(item);
                for (Integer i = 0; i < count; i++) {
                    objectMap.putIfAbsent(item, new ArrayList<>());
                    Object obj = objects.get(index++);
                    objectMap.get(item).add(obj);
                }
            }
        }
        synced = true;
    }

}
