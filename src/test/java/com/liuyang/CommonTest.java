package com.liuyang;

import com.liuyang.business.dao.impl.UserLoadingCacheImpl;
import com.liuyang.business.pojo.vo.UserVo;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CommonTest {

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
