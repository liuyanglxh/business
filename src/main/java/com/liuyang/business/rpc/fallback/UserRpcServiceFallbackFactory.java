package com.liuyang.business.rpc.fallback;

import com.liuyang.business.pojo.Result;
import com.liuyang.business.pojo.vo.UserVo;
import com.liuyang.business.rpc.UserRpcService;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UserRpcServiceFallbackFactory implements FallbackFactory<UserRpcService> {

    @Override
    public UserRpcService create(Throwable cause) {
        return new UserRpcService() {
            @Override
            public Result<UserVo> getUserInfo(Integer id) {
                if (cause != null) {
                    System.out.println("error ~~~");
                    cause.printStackTrace();
                } else {
                    System.out.println("cause is null");
                }
                return null;
            }

            @Override
            public Result<Map> getUserInfo2(Integer id) {
                if (cause != null) {
                    System.out.println("error ~~~");
                    cause.printStackTrace();
                } else {
                    System.out.println("cause is null");
                }
                return null;
            }
        };
    }
}
