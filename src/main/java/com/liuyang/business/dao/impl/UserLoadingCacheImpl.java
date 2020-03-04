package com.liuyang.business.dao.impl;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.liuyang.business.dao.UserLoadingCache;
import com.liuyang.business.pojo.Result;
import com.liuyang.business.pojo.vo.UserVo;
import com.liuyang.business.rpc.UserRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Component
public class UserLoadingCacheImpl implements UserLoadingCache {

    @Autowired
    private UserRpcService userRpcService;

    private LoadingCache<Integer, UserVo> userCache;

    {
        userCache = CacheBuilder.newBuilder()
                .maximumSize(5)
                .expireAfterAccess(1, TimeUnit.SECONDS)
                .concurrencyLevel(5)
                .build(new CacheLoader<Integer, UserVo>() {
                    @Override
                    public UserVo load(Integer userId) {
                        return Optional.ofNullable(userRpcService.getUserInfo(userId, false)).map(Result::getData).orElse(null);
                    }
                });
    }


    @Override
    public UserVo get(Integer userId) {
        try {
            return userCache.get(userId);
        } catch (ExecutionException e) {
            return null;
        }
    }


}
