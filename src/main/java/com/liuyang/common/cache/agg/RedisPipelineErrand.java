package com.liuyang.common.cache.agg;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * redis跑腿人
 */
public abstract class RedisPipelineErrand {

    private List<RedisTask<?>> items;

    private Map<RedisTask, List<Object>> objectMap;

    private boolean synced;

    protected abstract Jedis getJedis();

    public <T> RedisTask<T> recieve(RedisTask<T> item) {
        if (item == null) {
            return null;
        }
        if (synced) {
            throw new RuntimeException("the agent has already synced,please call the method before sync");
        }
        if (items == null) {
            items = new ArrayList<>();
        }
        items.add(item);
        return item;
    }

    public <T> T getObject(RedisTask<T> item) {

        if (item == null) {
            return null;
        }

        sync();

        List<Object> list = objectMap.get(item);
        return item.getHandler().apply(list);
    }

    private void sync() {
        if (synced) {
            return;
        }
        try (Jedis jedis = getJedis()) {
            Pipeline pipeline = jedis.pipelined();
            PipelineProxy counter = new PipelineProxy(pipeline);

            objectMap = new HashMap<>();
            Map<RedisTask, Integer> itemCountMap = new HashMap<>();

            for (RedisTask<?> item : items) {
                //调用获取redis数据的方法
                item.getReader().accept(counter);
                itemCountMap.put(item, counter.count());
                counter.move();
            }

            List<Object> objects = pipeline.syncAndReturnAll();

            int index = 0;
            for (RedisTask<?> item : items) {
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
