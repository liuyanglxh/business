package com.liuyang.common.cache.redis.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liuyang.common.cache.agg.RedisPipelineTaskManager;
import com.liuyang.common.cache.agg.RedisPipelineTaskManagerFactory;
import com.liuyang.common.cache.agg.RedisTask;
import com.liuyang.common.cache.redis.test.pojo.Person;
import com.liuyang.common.cache.redis.test.pojo.PersonVo;
import com.liuyang.common.cache.redis.test.pojo.Reward;
import com.liuyang.common.cache.redis.test.service.PersonService;
import com.liuyang.common.cache.redis.test.service.RewardService;
import com.liuyang.common.cache.redis.test.service.impl.PersonServiceImpl;
import com.liuyang.common.cache.redis.test.service.impl.RewardServiceImpl;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class PipeTest {

    public static JedisPool jedisPool = new JedisPool("localhost", 6379);

    private PersonService personService = new PersonServiceImpl();
    private RewardService rewardService = new RewardServiceImpl();

    static class TestFactory extends RedisPipelineTaskManagerFactory {

        @Override
        protected Jedis getJedis() {
            return jedisPool.getResource();
        }
    }

    @Test
    public void pipeTest() throws JsonProcessingException {

        PersonVo vo = this.getUserVo(1);
        System.out.println(new ObjectMapper().writeValueAsString(vo));

        PersonVo vo2 = this.getUserVo(2);
        System.out.println(new ObjectMapper().writeValueAsString(vo2));
    }

    private PersonVo getUserVo(Integer personId) {

        TestFactory factory = new TestFactory();

        RedisTask<Person> personTask = personService.get(personId);
        RedisTask<Reward> rewardTask = rewardService.getByPerson(personId);

        RedisPipelineTaskManager manager = factory.getManager(personTask, rewardTask);

        Person person = manager.getObject(personTask);
        Reward reward = manager.getObject(rewardTask);

        PersonVo vo = PersonVo.of(person);

        vo.setReward(reward);

        return vo;
    }
}
