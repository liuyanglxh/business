package com.liuyang.common.cache.redis.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liuyang.common.cache.agg.RedisItem;
import com.liuyang.common.cache.agg.RedisPipelineAgent;
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

    static class TestAgent extends RedisPipelineAgent {

        @Override
        protected Jedis getJedis() {
            return jedisPool.getResource();
        }
    }

    @Test
    public void pipeTest() throws JsonProcessingException {
        PersonVo vo = this.getUserVo(1);
        System.out.println(new ObjectMapper().writeValueAsString(vo));
    }

    private PersonVo getUserVo(Integer personId) {

        TestAgent agent = new TestAgent();

        RedisItem<Person> personItem = agent.addItem(personService.get(personId));
        RedisItem<Reward> rewardItem = agent.addItem(rewardService.getByPerson(personId));

        Person person = agent.get(personItem);
        Reward reward = agent.get(rewardItem);

        PersonVo vo = new PersonVo();
        vo.setId(person.getId());
        vo.setName(person.getName());
        vo.setReward(reward);

        return vo;
    }
}
