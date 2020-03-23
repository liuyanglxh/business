package com.liuyang.common.pojo;

import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
public class PageList<T> {
    private boolean success = true;
    private long total; //总条数
    private Boolean hasMore;
    private List<T> data; //当前页数据集合

    public static <S> PageList<S> emptyList() {
        return new PageList<>(0, false, new ArrayList<>());
    }

    public static <S> PageList<S> fail() {
        PageList<S> p = new PageList<>();
        p.setSuccess(false);
        return p;
    }

    public PageList() {
    }

    public PageList(long total, List<T> data) {
        this.total = total;
        this.data = data;
    }

    public PageList(long total, Boolean hasMore, List<T> data) {
        this.total = total;
        this.hasMore = hasMore;
        this.data = data;
    }

    /**
     * 转换data字段并获取一个新的PageList
     *
     * @param func 转换方法
     * @param <R>
     * @return
     */
    public <R> PageList<R> map(Function<List<T>, List<R>> func) {
        return new PageList<>(total, hasMore, func.apply(data));
    }

    /**
     * 转换data字段并获取一个新的PageList
     *
     * @param func 转换方法
     * @param <R>
     * @return
     */
    public <R> PageList<R> mapListItem(Function<T, R> func) {
        if (CollectionUtils.isEmpty(data)) {
            return new PageList<>(total, hasMore, new ArrayList<>());
        }

        List<R> list = data.stream().map(func).collect(Collectors.toList());

        return new PageList<>(total, hasMore, list);
    }
}
