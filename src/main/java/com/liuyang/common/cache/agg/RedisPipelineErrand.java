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

    private List<RedisTask<?>> tasks;

    private Map<RedisTask, List<Object>> objectMap;

    private boolean synced;

    protected abstract Jedis getJedis();

    public <T> void receive(RedisTask<T> task) {
        if (synced) {
            throw new UnsupportedOperationException("the agent has already synced,please call the method before sync");
        }
        if (task == null) {
            return;
        }
        if (tasks == null) {
            tasks = new ArrayList<>();
        }
        tasks.add(task);
    }

    public <T> T getObject(RedisTask<T> task) {

        if (task == null) {
            return null;
        }

        sync();

        List<Object> list = objectMap.get(task);
        return task.getHandler().apply(list);
    }

    private void sync() {
        if (synced) {
            return;
        }
        try (Jedis jedis = getJedis()) {
            Pipeline pipeline = jedis.pipelined();
            PipelineProxy pipelineProxy = new PipelineProxy(pipeline);

            objectMap = new HashMap<>();
            Map<RedisTask, Integer> taskCountMap = new HashMap<>();

            for (RedisTask<?> task : tasks) {
                //调用获取redis数据的方法
                task.getReader().accept(pipelineProxy);
                taskCountMap.put(task, pipelineProxy.count());
                pipelineProxy.move();
            }

            List<Object> objects = pipeline.syncAndReturnAll();

            int index = 0;
            for (RedisTask<?> task : tasks) {
                Integer count = taskCountMap.get(task);
                for (Integer i = 0; i < count; i++) {
                    objectMap.putIfAbsent(task, new ArrayList<>());
                    Object obj = objects.get(index++);
                    objectMap.get(task).add(obj);
                }
            }
        }
        synced = true;
    }

}
