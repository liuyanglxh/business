package com.liuyang.business.service.impl;

import com.liuyang.business.cache.GoodsComprehensiveCache;
import com.liuyang.business.pojo.view.GoodsView;
import com.liuyang.business.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsComprehensiveCache cache;

    @Override
    public GoodsView getView(Integer id) {

        GoodsView view = cache.getGoodsView(id);

        if (view == null) {
            view  = new GoodsView();
        }


        return null;
    }
}
