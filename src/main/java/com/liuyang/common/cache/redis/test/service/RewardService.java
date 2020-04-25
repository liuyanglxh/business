package com.liuyang.common.cache.redis.test.service;

import com.liuyang.common.cache.agg.RedisItem;
import com.liuyang.common.cache.redis.test.pojo.Reward;

public interface RewardService {

    RedisItem<Reward> getByPerson(Integer personId);
}
