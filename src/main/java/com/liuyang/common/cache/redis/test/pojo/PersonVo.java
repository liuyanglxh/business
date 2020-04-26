package com.liuyang.common.cache.redis.test.pojo;

import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class PersonVo extends Person {

    public static PersonVo of(Person p) {
        if (p == null) {
            return null;
        }
        PersonVo vo = new PersonVo();
        BeanUtils.copyProperties(p, vo);
        return vo;
    }

    private Reward reward;
}
