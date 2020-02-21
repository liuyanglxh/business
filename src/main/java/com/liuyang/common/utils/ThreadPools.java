package com.liuyang.common.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadPools {

    private static ExecutorService executorService = Executors.newCachedThreadPool();

    public static <T> Future<T> submit(Callable<T> callable) {
        return executorService.submit(callable);
    }

}
