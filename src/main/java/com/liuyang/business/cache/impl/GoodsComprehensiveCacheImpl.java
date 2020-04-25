package com.liuyang.business.cache.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liuyang.business.cache.GoodsComprehensiveCache;
import com.liuyang.business.pojo.entity.BrandEntity;
import com.liuyang.business.pojo.entity.CategoryEntity;
import com.liuyang.business.pojo.entity.GoodsEntity;
import com.liuyang.business.pojo.view.GoodsView;
import com.liuyang.common.cache.redis.RedisHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component
public class GoodsComprehensiveCacheImpl implements GoodsComprehensiveCache {

    @Autowired
    private RedisHelper redisHelper;

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public GoodsView getGoodsView(Integer id) {

        Map<String, String> map = redisHelper.hgetAll(this.key(id));

        if (map == null) {
            return null;
        }

        return mapper.convertValue(map, GoodsView.class);
    }

    @Override
    public Map<Integer, GoodsView> batchGetGoodsView(Collection<Integer> ids) {
        return null;
    }

    @Override
    public void setGoodsInfo(GoodsEntity goods) {

    }

    @Override
    public void setBrandInfo(Integer goodsId, List<BrandEntity> brands) {

    }

    @Override
    public void setCategoryInfo(Integer goodsId, List<CategoryEntity> categories) {

    }

    @Override
    public void del(Fields field, Integer goodsId) {

    }

    @Override
    public void delAll(Integer goodsId) {

    }

    private String key(Integer goodsId) {
        return "goods-" + goodsId;
    }
}
