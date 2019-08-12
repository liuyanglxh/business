package com.liuyang.business.dao;

import com.liuyang.business.pojo.vo.UserVo;

public interface UserLoadingCache {

    UserVo get(Integer userId);

}
