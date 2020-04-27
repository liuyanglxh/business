package com.liuyang.common.cache.redis.test.service;

import com.liuyang.common.cache.agg.RedisTask;
import com.liuyang.common.cache.redis.test.pojo.Reward;

public interface RewardService {

    RedisTask<Reward> getByPerson(Integer personId);
}
