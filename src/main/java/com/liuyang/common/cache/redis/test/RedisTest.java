package com.liuyang.common.cache.redis.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liuyang.common.cache.redis.lua.LuaScript;
import com.liuyang.common.utils.encode.SHA1Utils;
import lombok.Data;
import lombok.experimental.Accessors;
import org.junit.Test;
import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.JedisNoScriptException;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RedisTest {

    private JedisPool jedisPool = new JedisPool("localhost", 6379);
    private Jedis j = jedisPool.getResource();
    private ObjectMapper mapper = new ObjectMapper();

    private JedisCluster cluster;

    {
        String ip = "192.168.0.102";
        Set<HostAndPort> nodes = Stream.of(6391, 6392, 6393, 6394, 6395, 6396).map(port -> new HostAndPort(ip, port)).collect(Collectors.toSet());
        cluster = new JedisCluster(nodes);
    }

    private boolean timeLimit(Integer userId) {

        String key = "time-limit-" + userId;

        Long time = this.writeAndReturn(jedis -> jedis.incr(key));

        if (time == 1) {
            this.write(jedis -> jedis.expire(key, 10));
        }

        return time <= 3;
    }

    @Test
    public void testPipe() {
        Pipeline pipelined = jedisPool.getResource().pipelined();
        pipelined.set("abc", "abc");
        pipelined.set("123", "123");
        pipelined.set("678", "678");
        pipelined.set("abc", "abc");

        pipelined.sync();

        
    }

    @Test
    public void testEmptySet() {
        Jedis jedis = jedisPool.getResource();
        String key = "emptySet";
        jedis.sadd(key);
        System.out.println(jedis.exists(key));
    }

    @Test
    public void testCluster() {
        System.out.println(123);
    }

    @Test
    public void testRecentLike() {
        String key = "recent-like-users";
        for (int i = 1; i <= 100; i++) {
            List<String> args = Stream.of(System.currentTimeMillis(), i, 20).map(String::valueOf).collect(Collectors.toList());
            Object o = this.writeAndReturn(jedis -> jedis.eval(LuaScript.RECENT_LIKE_USERS_ADD.getScript(), Collections.singletonList(key), args));
            System.out.println(o);
        }

    }

    @Test
    public void testRecentLikeDel() {
        String key = "recent-like-users";
        String userId = "98";
        Object o = this.writeAndReturn(jedis -> {
            Pipeline p = jedis.pipelined();
            p.zrem(key, userId);
            p.zcount(key, "0", String.valueOf(System.currentTimeMillis()));
            return p.syncAndReturnAll().get(1);
        });
        System.out.println(o);
    }

    @Test
    public void testPerformance() throws JsonProcessingException {
        int times = 2000;
        this.testPerformance(times, 5);
        this.testPerformance(times, 20);
        this.testPerformance(times, 50);
        this.testPerformance(times, 100);
        this.testPerformance(times, 200);
        this.testPerformance(times, 100);
        this.testPerformance(times, 50);
        this.testPerformance(times, 20);
        this.testPerformance(times, 5);
    }

    /**
     * @param times 总共的key个数
     * @param size  单个hash的field数量
     */
    private void testPerformance(int times, int size) throws JsonProcessingException {
        j.flushAll();
        //准备数据
        Map<String, Map<String, String>> hashes = new HashMap<>();
        Map<String, String> strs = new HashMap<>();

        //准备随机字符串
        char c = 'a';
        List<Character> charList = new ArrayList<>();
        for (int i = 0; i < 52; i++) {
            charList.add(c++);
        }

        System.out.println(String.format("times %d  size %d ", times, size));
        for (int i = 0; i < times; i++) {
            String hashKey = "hash:" + times + ":" + i;
            String strKey = "string" + times + ":" + i;

            Map<String, String> hashValue = new HashMap<>();
            for (int j = 0; j < size; j++) {
                Collections.shuffle(charList);
                ThreadLocalRandom current = ThreadLocalRandom.current();
                String value = charList.subList(0, current.nextInt(0, charList.size() - 1)).stream().map(String::valueOf).collect(Collectors.joining(","));
                hashValue.put("f" + j, value);
            }

            hashes.put(hashKey, hashValue);
            strs.put(strKey, mapper.writeValueAsString(hashValue));
        }

        //hash写
        System.out.print("hash");
        this.write(jedis -> {
            Pipeline p = jedis.pipelined();
            hashes.forEach((hashKey, hashValue) -> {
                p.hmset(hashKey, hashValue);
                p.expire(hashKey, 600);
            });
            p.sync();
        });
        //string 写
        System.out.print("string");
        this.write(jedis -> {
            Pipeline p = jedis.pipelined();
            strs.forEach((strKey, StrValue) -> p.setex(strKey, 600, StrValue));
            p.sync();
        });

        //hash全读
        System.out.print("hash全");
        Object read = this.read(jedis -> {
            Pipeline p = jedis.pipelined();
            for (String key : hashes.keySet()) {
                p.hmget(key);
            }
            return p.syncAndReturnAll();
        });
        //hash指定读
        System.out.print("hash指");
        Object read1 = this.read(jedis -> {
            Pipeline p = jedis.pipelined();
            hashes.forEach((hashKey, hashValue) -> p.hmget(hashKey, hashValue.keySet().toArray(new String[]{})));
            return p.syncAndReturnAll();
        });
        //string 读
        System.out.print("string");
        this.read(jedis -> jedis.mget(strs.keySet().toArray(new String[]{})));
        System.out.println();
    }

    /**
     * 测试结构
     */
    @Test
    public void testHashStruct() {
        Map<String, Map<String, String>> maps = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            Map<String, String> map = new HashMap<>();
            map.put(String.valueOf(i), String.valueOf(i));
            maps.put(String.valueOf(i), map);
        }
        maps.put("11", new HashMap<>());

        Pipeline p = j.pipelined();
        maps.forEach(p::hmset);
        p.sync();

        j.close();

        j = jedisPool.getResource();
        p = j.pipelined();
        for (int i = 0; i < 11; i++) {
            p.hgetAll(String.valueOf(i));
        }
        List<Object> objects = p.syncAndReturnAll();
        objects.forEach(System.out::println);
    }

    @Test
    public void testSadd() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 50000; i++) {
            list.add(String.valueOf(i));
        }
        String[] arr = list.toArray(new String[]{});
        Jedis jedis = jedisPool.getResource();
        Pipeline p = jedis.pipelined();
        String key = "testAdd";
        p.sadd(key, arr);
        p.expire(key, 20);
        long l = System.currentTimeMillis();
        List<Object> objects = p.syncAndReturnAll();
        System.out.println(System.currentTimeMillis() - l);
        System.out.println(objects);

        System.out.println(this.executeLua(LuaScript.SISMEMBER, key, 1));
        System.out.println(this.executeLua(LuaScript.SISMEMBER, key, 1000000));
        System.out.println(this.executeLua(LuaScript.SISMEMBER, key + ":", 10000));
    }

    /**
     * 测试lua是否会插队
     */
    @Test
    public void testLuaLineup() throws ExecutionException, InterruptedException {
        String lua = "local i1 =redis.call('incr', 'testLuaLineup') for num = 1,1998,1 do redis.call('incr', 'testLuaLineup') end local i2 = redis.call('incr', 'testLuaLineup') return i2 - i1";
        j.del("testLuaLineup");

        ExecutorService tpool = Executors.newFixedThreadPool(20);

        List<Future<Object>> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(tpool.submit(() -> {
                Jedis jedis = jedisPool.getResource();
                try {
                    return jedis.eval(lua);
                } finally {
                    jedis.close();
                }
            }));
        }
        for (Future<Object> f : list) {
            System.out.println(f.get());
        }

        System.out.println("最终总数：" + j.get("testLuaLineup"));
    }

    /**
     * 测试pipeline的命令是否会被打乱
     */
    @Test
    public void testPipelineOrder() {
        int num = 5_0000;

        String key = "test-pipeline-order";

        Pipeline p = j.pipelined();
        j.del(key);

        for (int i = 0; i < num; i++) {
            p.incrBy(key, 2);
        }
        for (int i = 0; i < num; i++) {
            p.incr(key);
        }
        List<Object> objects = p.syncAndReturnAll();
        long count = objects.stream().map(String::valueOf).map(Integer::valueOf).filter(i -> i % 2 == 0).count();
        System.out.println(count);

    }

    /**
     * 测试pipeline是否会插队
     */
    @Test
    public void testPipelinedLineup() throws Exception {

        int num = 2000;
        String key = "test-pipeline";

        ExecutorService tpool = Executors.newFixedThreadPool(20);

        List<Future<Long>> list = new ArrayList<>();
        j.del(key);
        for (int i = 0; i < 20; i++) {
            list.add(tpool.submit(() -> {

                Jedis jedis = jedisPool.getResource();
                Pipeline p = jedis.pipelined();

                for (int i1 = 0; i1 < num; i1++) {
                    p.incr(key);
                }
                List<Object> objects;
                try {
                    objects = p.syncAndReturnAll();
                } finally {
                    jedis.close();
                }
                Long l1 = (Long) objects.get(0);
                Long l2 = (Long) objects.get(num - 1);

                return l2 - l1;
            }));
        }
        for (Future<Long> f : list) {
            System.out.println(f.get());
        }

        System.out.println("最终总数：" + j.get(key));

    }

    @Test
    public void testSetDis() throws JsonProcessingException {
        Integer dishId = 1;
        String dishKey = "{" + dishId + "}dish";
        String imageIdsKey = "{" + dishId + "}imageIds";
        String textIdsKey = "{" + dishId + "}textIds";

        Pipeline pipe = j.pipelined();
        Map<String, String> dishInfo = new HashMap<>();
        dishInfo.put("id", dishId.toString());
        dishInfo.put("name", "dishName");
        pipe.hmset(dishKey, dishInfo);
        pipe.expire(dishKey, 600);
        pipe.setex(imageIdsKey, 600, mapper.writeValueAsString(Arrays.asList(1, 2, 3)));
        pipe.setex(textIdsKey, 600, mapper.writeValueAsString(Arrays.asList(4, 5, 6)));
        pipe.sync();
    }

    @Test
    public void testGetDish() {
        Integer dishId = 1;
        String dishKey = "{" + dishId + "}dish";
        String imageIdsKey = "{" + dishId + "}imageIds";
        String textIdsKey = "{" + dishId + "}textIds";

        Object eval = j.eval(LuaScript.DISH_INFOS.getScript(), Arrays.asList(dishKey, imageIdsKey, textIdsKey), Collections.emptyList());
        System.out.println(eval);
//        Map<String, Object> map = mapper.convertValue(eval, new TypeReference<Map<String, Object>>() {
//        });
//
//        System.out.println(map);

//        List<Object> list = (List<Object>) eval;
//        List<Object> dishInfoList = (List<Object>) list.get(0);
//
//        Map<Object, Object> dishInfoMap = new HashMap<>();
//        for (int i = 0; i < dishInfoList.size(); i += 2) {
//            dishInfoMap.put(dishInfoList.get(i), dishInfoList.get(i+1));
//        }
//
//        System.out.println(dishInfoMap);

    }

    @Test
    public void testPipelineHmget() {
        this.createHashData();
        Pipeline pipeline = j.pipelined();
        int num = 200;
        for (int i = 0; i < num; i++) {
            String key = this.getUserKey(i);
            pipeline.hmget(key, "id", "name", "nickName", "sex", "email", "hobby", "weight", "height", "city", "province", "nation");
        }
        long start = System.currentTimeMillis();
        List<Object> result = pipeline.syncAndReturnAll();
        System.out.println("批量花费时间：" + (System.currentTimeMillis() - start));
        start = System.currentTimeMillis();
        for (int i = 0; i < num; i++) {
            String key = this.getUserKey(i);
            List<String> hmget = j.hmget(key, "id", "name", "nickName", "sex", "email", "hobby", "weight", "height", "city", "province", "nation");
        }
        System.out.println("循环花费时间：" + (System.currentTimeMillis() - start));
    }

    @Test
    public void testPipelineHgetAll() {
        this.createHashData();
        Pipeline pipeline = j.pipelined();
        int num = 200;
        for (int i = 0; i < num; i++) {
            String key = this.getUserKey(i);
            pipeline.hgetAll(key);
        }
        long start = System.currentTimeMillis();
        List<Object> result = pipeline.syncAndReturnAll();
        System.out.println("花费时间：" + (System.currentTimeMillis() - start));
        start = System.currentTimeMillis();
        for (int i = 0; i < num; i++) {
            String key = this.getUserKey(i);
            Map<String, String> map = j.hgetAll(key);
        }
        System.out.println("循环花费时间：" + (System.currentTimeMillis() - start));
    }

    /**
     * 制造100条数据
     */
    @Test
    public void createHashData() {
        List<User> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            list.add(new User()
                    .setId(i)
                    .setName("name" + i)
                    .setNickName("nickname" + i)
                    .setSex(Byte.valueOf(String.valueOf(i % 2)))
                    .setEmail("asdfjasdkl" + i + "@qq.com")
                    .setHobby("hobby" + i)
                    .setHeight(150 + i)
                    .setWeight(100 + i)
                    .setCity("city" + i % 10)
                    .setProvince("province" + i % 15)
                    .setNation("nation" + i % 20)
            );
        }
        Pipeline pipelined = j.pipelined();
        list.forEach(user -> {
            String key = this.getUserKey(user.getId());
            pipelined.hmset(key, this.getMap(user));
            pipelined.expire(key, 600 + ThreadLocalRandom.current().nextInt(1, 600));
        });

        long start = System.currentTimeMillis();
        pipelined.sync();
        System.out.println("花费时间：" + (System.currentTimeMillis() - start));
    }

    private String getUserKey(Integer id) {
        return "user-" + id;
    }

    @Test
    public void testPipelineHmget2() {
        this.createHashData2();
        Pipeline pipeline = j.pipelined();
        String[] arr = new String[50];
        for (int j = 0; j < 50; j++) {
            arr[j] = String.valueOf(j);
        }
        int num = 200;
        for (int i = 0; i < num; i++) {
            String key = this.getUserKey2(i);
            pipeline.hmget(key, arr);
        }
        long start = System.currentTimeMillis();
        List<Object> result = pipeline.syncAndReturnAll();
        System.out.println("批量花费时间：" + (System.currentTimeMillis() - start));
        start = System.currentTimeMillis();
        for (int i = 0; i < num; i++) {
            String key = this.getUserKey2(i);
            List<String> hmget = j.hmget(key, arr);
        }
        System.out.println("循环花费时间：" + (System.currentTimeMillis() - start));
    }

    @Test
    public void testPipelineHgetAll2() {
        this.createHashData2();
        Pipeline pipeline = j.pipelined();
        int num = 200;
        for (int i = 0; i < num; i++) {
            String key = this.getUserKey2(i);
            pipeline.hgetAll(key);
        }
        long start = System.currentTimeMillis();
        List<Object> result = pipeline.syncAndReturnAll();
        System.out.println("花费时间：" + (System.currentTimeMillis() - start));
        start = System.currentTimeMillis();
        for (int i = 0; i < num; i++) {
            String key = this.getUserKey2(i);
            Map<String, String> map = j.hgetAll(key);
        }
        System.out.println("循环花费时间：" + (System.currentTimeMillis() - start));
    }

    /**
     * 制造100条数据
     */
    @Test
    public void createHashData2() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            Map<String, Object> map = new HashMap<>();
            for (int j = 50; j > 0; j--) {
                map.put(String.valueOf(j), "abcd" + j);
            }
            list.add(map);
        }
        Pipeline pipelined = j.pipelined();
        list.forEach(user -> {
            String key = this.getUserKey2(user.get("1"));
            pipelined.hmset(key, this.getMap(user));
            pipelined.expire(key, 600 + ThreadLocalRandom.current().nextInt(1, 600));
        });

        long start = System.currentTimeMillis();
        pipelined.sync();
        System.out.println("花费时间：" + (System.currentTimeMillis() - start));
    }

    private String getUserKey2(Object id) {
        return "user-2-" + id;
    }

    @Test
    public void testLua() throws JsonProcessingException {
        LuaScript script = LuaScript.SISMEMBER;
        String key = "names";
        System.out.println(this.executeLua(script, key, "a"));
        System.out.println(this.executeLua(script, key, "h"));
        System.out.println(this.executeLua(script, "name", "a"));
    }

    private Object executeLua(LuaScript script, String key, Object value) {
        String s = this.getString(value);
        String sha1 = SHA1Utils.sha1(script.getScript());
        try {
            //采用sha1调用，性能更高，但是可能存在此脚本未被执行的情况
            return j.evalsha(sha1, 1, key, s);
        } catch (JedisNoScriptException e) {
            return j.eval(script.getScript(), 1, key, s);
        }
    }

    private String getString(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof String) {
            return (String) obj;
        }
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    private Map<String, String> getMap(Object obj) {
        if (obj == null) {
            return new HashMap<>();
        }
        return mapper.convertValue(obj, new TypeReference<Map<String, String>>() {
        });
    }

    private <T> Object read(Function<Jedis, T> function) {
        Jedis j = jedisPool.getResource();
        try {
            long start = System.currentTimeMillis();
            T apply = function.apply(j);
            System.out.println(" 读：" + (System.currentTimeMillis() - start));
            return apply;
        } finally {
            j.close();
        }
    }

    private void write(Consumer<Jedis> consumer) {
        Jedis j = jedisPool.getResource();
        try {
            long start = System.currentTimeMillis();
            consumer.accept(j);
            System.out.println(" 写：" + (System.currentTimeMillis() - start));
        } finally {
            j.close();
        }
    }

    private <T> T writeAndReturn(Function<Jedis, T> function) {
        Jedis j = jedisPool.getResource();
        try {
            return function.apply(j);
        } finally {
            j.close();
        }
    }

    @Data
    @Accessors(chain = true)
    static class User {

        private Integer id;
        private String name;
        private String nickName;
        private String email;
        private Byte sex;
        private String hobby;
        private Integer height;
        private Integer weight;
        private String city;
        private String province;
        private String nation;
    }

}
