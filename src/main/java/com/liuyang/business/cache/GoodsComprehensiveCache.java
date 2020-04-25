package com.liuyang.business.cache;

import com.liuyang.business.pojo.entity.BrandEntity;
import com.liuyang.business.pojo.entity.CategoryEntity;
import com.liuyang.business.pojo.entity.GoodsEntity;
import com.liuyang.business.pojo.view.GoodsView;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 商品综合缓存
 */
public interface GoodsComprehensiveCache {

    @AllArgsConstructor
    @Getter
    public enum Fields {
        /**
         * 商品基础信息
         */
        GOODS_INFO("1"),
        /**
         * 商品总点赞数
         */
        GOODS_TOTAL_LIKE("11"),
        /**
         * 商品总分享数
         */
        GOODS_TOTAL_SHARE("12"),
        /**
         * 品牌id
         */
        BRAND_IDS("2"),
        /**
         * 分类id
         */
        CATEGORY_IDS("3"),
        ;
        /**
         * 在hash中的field
         */
        private String mark;
    }

    GoodsView getGoodsView(Integer id);

    Map<Integer, GoodsView> batchGetGoodsView(Collection<Integer> ids);

    void setGoodsInfo(GoodsEntity goods);

    void setBrandInfo(Integer goodsId, List<BrandEntity> brands);

    void setCategoryInfo(Integer goodsId, List<CategoryEntity> categories);

    void del(Fields field, Integer goodsId);

    void delAll(Integer goodsId);

}
