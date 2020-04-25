package com.liuyang.common.cache.agg;

import redis.clients.jedis.*;
import redis.clients.jedis.params.GeoRadiusParam;
import redis.clients.jedis.params.SetParams;
import redis.clients.jedis.params.ZAddParams;
import redis.clients.jedis.params.ZIncrByParams;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PipelineCounter extends Pipeline{

    private List<Integer> counts;

    private int index = 0;

    private Pipeline pipeline;

    public PipelineCounter(Pipeline pipeline) {
        this.pipeline = pipeline;
        counts = new ArrayList<>();
    }

    public void move() {
        index++;
    }

    public int getCount() {
        return counts.get(index);
    }

    private void add(int value) {
        Integer count = counts.get(index);
        if (count == null) {
            counts.set(index, value);
        } else {
            counts.set(index, count + value);
        }
    }


}
