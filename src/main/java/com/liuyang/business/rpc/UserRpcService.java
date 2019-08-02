package com.liuyang.business.rpc;

import com.liuyang.business.pojo.Result;
import com.liuyang.business.pojo.vo.UserVo;
import com.liuyang.business.rpc.fallback.UserRpcServiceFallbackFactory;
import com.liuyang.common.HeaderInterceptor;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

//@FeignClient(value = "${usercenter.name}", fallbackFactory = UserRpcServiceFallbackFactory.class, configuration = HeaderInterceptor.class)
@FeignClient(value = "ucenter", fallbackFactory = UserRpcServiceFallbackFactory.class, configuration = HeaderInterceptor.class)
@RequestMapping("api/user/v1")
public interface UserRpcService {

    @RequestMapping(value = "info", method = RequestMethod.GET, consumes = "application/json")
    Result<UserVo> getUserInfo(@RequestParam("id") Integer id);
}
