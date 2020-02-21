package com.liuyang.business.utils.cocurrent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * 创建一个阻塞任务，避免类似"缓存穿透"的问题发生
 */
public class BlockJob<T> {

    private ExecutorService pool = Executors.newFixedThreadPool(10);
    private Map<String, Future<T>> map = new HashMap<>();

    public T get(String key, Supplier<T> supplier) {
        Future<T> future = map.get(key);
        if (future == null) {
            future = this.tryGetFuture(key, supplier);
        }

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    private synchronized Future<T> tryGetFuture(String key, Supplier<T> supplier) {
        Future<T> future = map.get(key);
        if (future == null) {
            future = pool.submit(() -> {
                T result = null;
                try {
                    result = supplier.get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                map.remove(key);
                System.out.println("map has removed a key");
                return result;
            });
            map.put(key, future);
        }
        return future;
    }

    public static void main(String[] args) {
        BlockJob<String> job = new BlockJob<>();

        String key = "user." + 1;

        Supplier<String> c = () -> {
            String name = Thread.currentThread().getName();
            System.out.println(name + "   start to sleep");
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("finish sleep");
            return "jackson";
        };

        List<Thread> lst = new ArrayList<>();

        int tName = 0;
        for (int i = 0; i < 10; i++) {
            Thread t = new Thread(() -> {
                String str = job.get(key, c);
                String name = Thread.currentThread().getName();
                System.out.println(name);
            });
            t.setName("t" + tName++);
            lst.add(t);
        }

        lst.forEach(Thread::start);

    }

}
