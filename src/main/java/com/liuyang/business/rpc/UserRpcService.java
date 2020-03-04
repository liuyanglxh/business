package com.liuyang.business.rpc;

import com.liuyang.business.pojo.Result;
import com.liuyang.business.pojo.vo.UserVo;
import com.liuyang.business.rpc.fallback.UserRpcServiceFallbackFactory;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

//@DefaultProperties(
//         threadPoolProperties = {
//                @HystrixProperty(name = "coreSize", value = "20"),
//                @HystrixProperty(name = "maxQueueSize", value = "30"),
//                @HystrixProperty(name = "queueSizeRejectionThreshold", value = "30")
//        })
//@FeignClient(value = "${usercenter.name}", fallbackFactory = UserRpcServiceFallbackFactory.class, configuration = HeaderInterceptor.class)
@FeignClient(name = "ucenter", fallbackFactory = UserRpcServiceFallbackFactory.class)
public interface UserRpcService {

    @RequestMapping(value = "api/user/v1/info", method = RequestMethod.GET)
    Result<UserVo> getUserInfo(@RequestParam("id") Integer id, @RequestParam("throwExc") boolean throwExc);

    @RequestMapping(value = "api/user/v1/info", method = RequestMethod.GET)
    Result<Map> getUserInfo2(@RequestParam("id") Integer id);
}
