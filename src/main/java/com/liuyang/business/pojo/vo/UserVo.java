package com.liuyang.business.pojo.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserVo {

    private Integer id;
    private String name;
    private Integer age;
}
