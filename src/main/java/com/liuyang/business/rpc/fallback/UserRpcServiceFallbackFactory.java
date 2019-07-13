package com.liuyang.business.rpc.fallback;

import com.liuyang.business.pojo.Result;
import com.liuyang.business.pojo.vo.UserVo;
import com.liuyang.business.rpc.UserRpcService;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class UserRpcServiceFallbackFactory implements FallbackFactory<UserRpcService> {
    @Override
    public UserRpcService create(Throwable cause) {
        return new UserRpcService() {
            @Override
            public Result<UserVo> getUserInfo(Integer id) {
                return null;
            }
        };
    }
}
