package com.liuyang.common.cache.redis.paging;

import com.liuyang.common.pojo.PageList;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 对BaseRedisPaging做进一步封装
 *
 * @param <T> 唯一标识类型
 * @param <R> 数据类型
 */
public abstract class RedisPagingHandler<T, R> extends BaseRedisPaging {

    private Class<R> dataType;

    protected RedisPagingHandler() {
        this.setDataType();
    }

    /**
     * 获取key
     *
     * @param t 唯一标识
     * @return 必须非空
     */
    protected abstract String getKey(T t);

    /**
     * 加载总数
     *
     * @param t 唯一标识
     * @return 必须非空
     */
    protected abstract Long loadTotal(T t);

    /**
     * 加载分页数据
     *
     * @param t    唯一标识
     * @param page
     * @param size
     * @return 必须非空
     */
    protected abstract List<R> loadData(T t, Integer page, Integer size);

    /**
     * 获取分页数据
     *
     * @param t    唯一标识
     * @param page
     * @param size
     * @return always non null
     */
    public PageList<R> getPage(T t, int page, int size) {

        String key = this.getKey(t);
        if (key == null) {
            throw new NullPointerException();
        }

        PageList<R> pageList = super.getPage(key, page, size, dataType);
        Long total = pageList == null ? null : pageList.getTotal();
        List<R> data = pageList == null ? null : pageList.getData();

        if (data == null) {
            if (total == null) {
                total = this.loadTotal(t);
            }
            if (total == 0) {
                data = new ArrayList<>();
            } else {
                data = this.loadData(t, page, size);
            }
            super.setPage(key, data, total, page, size);
            pageList = new PageList<>(total, data);
            pageList.setHasMore(total > page * size);
        }

        return pageList;
    }

    /**
     * 获取总数
     *
     * @param t
     * @return
     */
    public long getTotal(T t) {
        String key = this.getKey(t);
        if (key == null) {
            throw new NullPointerException();
        }

        PageList<R> pageList = super.getPage(key, 1, 0, dataType);

        Long total = pageList == null ? null : pageList.getTotal();

        if (total == null) {
            total = this.loadTotal(t);
        }

        return total;
    }

    public void del(T t) {
        if (t != null) {
            super.getRedisHelper().del(this.getKey(t));
        }
    }

    public void del(Collection<T> collection) {
        if (!CollectionUtils.isEmpty(collection)) {
            Set<String> keys = collection.stream().map(this::getKey).collect(Collectors.toSet());
            super.getRedisHelper().del(keys);
        }
    }

    /**
     * 获取当前对象的第二个泛型的真实类型
     */
    private void setDataType() {
        ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
        this.dataType = (Class<R>) pt.getActualTypeArguments()[1];
    }


}
