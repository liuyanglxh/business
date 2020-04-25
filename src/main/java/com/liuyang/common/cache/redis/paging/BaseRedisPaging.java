package com.liuyang.common.cache.redis.paging;

import com.liuyang.common.BusinessException;
import com.liuyang.common.pojo.PageList;
import com.liuyang.common.cache.redis.RedisHelper;
import com.liuyang.common.utils.ObjectConvertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * redis分页基类，负责使用redis存储和获取分页数据
 */
public abstract class BaseRedisPaging {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private RedisHelper redisHelper;

    private final String TOTAL = "total";

    private final int MAX_SIZE = 500;

    /**
     * 获取分页数据
     *
     * @param key
     * @param page
     * @param size
     * @return
     */
    protected <T> PageList<T> getPage(String key, int page, int size, Class<T> clz) {
        if (StringUtils.isEmpty(key)) {
            throw new BusinessException("empty key!");
        }
        //限制最大分页数
        if (size > MAX_SIZE) {
            logger.warn("size [" + size + "] is too big!");
            size = MAX_SIZE;
        }
        //计算需要的位置
        int from = (page - 1) * size;
        List<String> fields = new ArrayList<>(size + 1);
        for (int i = 0; i < size; i++) {
            fields.add(String.valueOf(from + i));
        }
        fields.add(TOTAL);

        Map<String, Object> cacheData = this.getRedisHelper().hmget(key, Object.class, fields);

        //如果没有获取total，则视为缓存未命中
        Long total = Optional.ofNullable(cacheData)
                .map(m -> m.get(TOTAL))
                .map(Object::toString)
                .map(Long::valueOf).orElse(null);

        if (total == null) {
            return null;
        }

        //获取数据
        fields.remove(fields.size() - 1);
        long maxField = total - 1;
        fields.removeIf(field -> Integer.parseInt(field) > maxField);
        List<T> dataList = fields.stream().map(cacheData::get).filter(Objects::nonNull)
                .map(obj -> ObjectConvertUtil.convertValue(obj, clz))
                .collect(Collectors.toList());

        //所有数据中，如果有一条未命中，则视为整页数据未命中
        boolean hitAll = dataList.size() == fields.size();
        boolean hasMore = total > page * size;
        return hitAll ? new PageList<>(total, hasMore, dataList) : new PageList<>(total, hasMore, null);
    }

    /**
     * 设置分页数据
     *
     * @param key
     * @param data
     * @param total 总数
     * @param page
     */
    protected void setPage(String key, List<?> data, long total, int page, int size) {
        if (StringUtils.isEmpty(key)) {
            throw new BusinessException("empty key!");
        }
        if (data == null) {
            data = Collections.emptyList();
        }
        //计算数据起始位置
        int from = (page - 1) * size;
        Map<String, Object> toCache = new HashMap<>();
        //总数
        toCache.put(TOTAL, total);
        for (int i = 0; i < data.size(); i++) {
            toCache.put(String.valueOf(from + i), data.get(i));
        }
        this.getRedisHelper().hmset(key, toCache, RedisHelper.DEFAULT_TIMEOUT);
    }

    protected RedisHelper getRedisHelper() {
        if (this.redisHelper == null) {
            //todo 这里需要获取到redisHelp这个bean
//            this.redisHelper = SpringContextHolder.getBean(RedisHelper.class);
        }
        return this.redisHelper;
    }
}
