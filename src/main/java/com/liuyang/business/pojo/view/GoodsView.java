package com.liuyang.business.pojo.view;

import com.liuyang.business.pojo.entity.BrandEntity;
import com.liuyang.business.pojo.entity.CategoryEntity;
import com.liuyang.business.pojo.entity.GoodsEntity;
import lombok.Data;

import java.util.List;

@Data
public class GoodsView extends GoodsEntity {

    private List<BrandEntity> brands;

    private List<CategoryEntity> categories;
}
