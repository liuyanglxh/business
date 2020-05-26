package com.liuyang.common.cache.agg;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * redis任务管理者
 */
public class RedisPipelineTaskManager {

    private List<RedisTask<?>> tasks;

    private Supplier<Jedis> jedisSupplier;

    private Map<RedisTask, List<Object>> objectMap;

    private boolean synced;

    public RedisPipelineTaskManager(List<RedisTask<?>> tasks, Supplier<Jedis> jedisSupplier) {
        this.tasks = tasks.stream().filter(Objects::nonNull).collect(Collectors.toList());
        this.jedisSupplier = jedisSupplier;
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
        try (Jedis jedis = jedisSupplier.get()) {
            Pipeline pipeline = jedis.pipelined();
            PipelineProxy pipelineProxy = new PipelineProxy(pipeline, this.tasks.size());

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
