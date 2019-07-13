package com.liuyang.business.service.impl;

import com.liuyang.business.pojo.Result;
import com.liuyang.business.pojo.vo.UserVo;
import com.liuyang.business.rpc.UserRpcService;
import com.liuyang.business.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BusinessServiceImpl implements BusinessService {

    @Autowired
    private UserRpcService userRpcService;

    @Override
    public UserVo getUserInfo(Integer id) {
        Result<UserVo> result = userRpcService.getUserInfo(id);
        return result.getData();
    }
}
