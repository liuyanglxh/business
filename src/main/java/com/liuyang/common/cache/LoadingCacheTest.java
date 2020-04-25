package com.liuyang.common.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.junit.Test;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class LoadingCacheTest {

    private Object lock = new Object();
    private Long now = System.currentTimeMillis();

    private LoadingCache<Integer, String> cache1 = CacheBuilder.newBuilder()
            .expireAfterAccess(5, TimeUnit.SECONDS)
            .maximumSize(2)
            .build(new CacheLoader<Integer, String>() {
                @Override
                public String load(Integer key) {
                    System.out.println("调用了");
                    return key.toString();
                }
            });

    private LoadingCache<Integer, String> cache2 = CacheBuilder.newBuilder()
            .concurrencyLevel(5)
            .build(new CacheLoader<Integer, String>() {
                @Override
                public String load(Integer key) throws InterruptedException {
                    Thread.sleep(2000);
                    System.out.println((System.currentTimeMillis() - now) / 1000);
                    return key.toString();
                }
            });

    private LoadingCache<Integer, String> cache3 = CacheBuilder.newBuilder()
            .concurrencyLevel(5)
            .build(new CacheLoader<Integer, String>() {
                @Override
                public String load(Integer key) throws InterruptedException {
                    System.out.println("调用了");
                    Thread.sleep(1000);
                    return key.toString();
                }
            });

    @Test
    public void testBreakDown() throws ExecutionException, InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(10);
        List<Future<String>> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(pool.submit(() -> cache3.get(1)));
        }
        int sum = 0;
        for (Future<String> stringFuture : list) {
            String s = stringFuture.get();
            sum++;
        }
        System.out.println(sum);
    }

    @Test
    public void testConcurrent() throws InterruptedException, ExecutionException {
        ExecutorService pool = Executors.newFixedThreadPool(100);
        List<Future<String>> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            int j = i;
            list.add(pool.submit(() -> cache2.get(j)));
        }
        Set<String> set = new HashSet<>();
        for (Future<String> stringFuture : list) {
            String s = stringFuture.get();
            set.add(s);
        }

        System.out.println(set.size());

    }

    @Test
    public void testExpire() throws Exception {
        System.out.println(cache1.get(1));
        Thread.sleep(3000);
        System.out.println(cache1.get(1));
        Thread.sleep(3000);
        System.out.println(cache1.get(1));
        Thread.sleep(3000);
        System.out.println(cache1.get(1));
        Thread.sleep(3000);
    }
}
