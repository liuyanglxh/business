package com.liuyang;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.*;
import com.liuyang.business.dao.impl.UserLoadingCacheImpl;
import com.liuyang.business.pojo.vo.UserVo;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CommonTest {

    @Test
    public void test4(){
        HashBiMap<String, Integer> map = HashBiMap.create();
        BiMap<Integer, String> inverse = map.inverse();

        map.put("1", 1);
        map.put("2", 2);
        map.put("3", 3);

        System.out.println(map.get("1"));
        System.out.println(inverse.get(1));
    }

    @Test
    public void test3() throws JsonProcessingException {
        ListMultimap<String, Integer> build = MultimapBuilder.hashKeys().arrayListValues().build();

        build.put("name1", 1);
        build.put("name1", 2);
        build.put("name1", 2);
        build.put("name2", 2);
        build.put("name2", 2);
        build.put("name2", 2);

        System.out.println(build);

        String json = new ObjectMapper().writeValueAsString(build.toString());
        System.out.println(json);
    }

    @Test
    public void test2() {
        Multiset<Integer> set = HashMultiset.create();

        List<Integer> lst = Arrays.asList(1, 1, 2, 2, 2, 3, 4, 4, 4, 5, 5, 6, 7);

        set.addAll(lst);
        set.add(1);

        set.setCount(1, 10);

        lst.stream().distinct().sorted().forEach(i -> System.out.println(i + ":" + set.count(i)));

        System.out.println("----------------");

        System.out.println(set.size());

    }

    @Test
    public void test1() throws ExecutionException, InterruptedException {

        UserLoadingCacheImpl cache = new UserLoadingCacheImpl();

        ExecutorService pool = Executors.newCachedThreadPool();

        Future<UserVo> future1 = pool.submit(() -> cache.get(1));
        Future<UserVo> future2 = pool.submit(() -> cache.get(1));

        UserVo vo1 = future1.get();
        UserVo vo2 = future2.get();

        System.out.println(vo1 == vo2);
        vo1.setId(10);

        System.out.println(cache.get(1).getId());

    }

}
