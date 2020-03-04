package com.liuyang.business.business.controller.client;

import com.liuyang.business.business.controller.BaseController;
import com.liuyang.business.pojo.Result;
import com.liuyang.business.pojo.vo.UserVo;
import com.liuyang.business.service.BusinessService;
import com.liuyang.common.utils.ThreadPools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
@RequestMapping("api/business/v1")
public class BusinessController extends BaseController {

    @Autowired
    private BusinessService businessService;

    @GetMapping("user-info")
    public Result getUserInfo(@RequestParam(value = "userId", required = false) Integer userId,
                              @RequestParam(value = "throwExc", defaultValue = "false") boolean throwExc) {
        System.out.println(businessService.getClass().getName());
        return Result.success(businessService.getUserInfo(userId, throwExc));
    }

    @GetMapping("by-user/{userId}")
    public Result getBizByUser(@PathVariable("userId") Integer userId) {

        return Result.success("business_" + userId);
    }

    @GetMapping("test-feign")
    public Result testFeign(@RequestParam("async") Boolean async,
                            @RequestParam("size") Integer size,
                            @RequestParam(value = "throwExc", defaultValue = "false") boolean throwExc) throws InterruptedException, ExecutionException {

        if (async) {
            UserVo vo = businessService.getUserInfo(1, throwExc);
            return Result.success(vo);
        } else {
            List<Future<UserVo>> list = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                list.add(ThreadPools.submit(() -> businessService.getUserInfo(1, throwExc)));
            }
            List<UserVo> vos = new ArrayList<>();
            for (Future<UserVo> future : list) {
                vos.add(future.get());
            }
            long count = vos.stream().filter(Objects::nonNull).count();
            return Result.success(count);
        }

    }

    private void showVO(Future<UserVo> future) throws InterruptedException, ExecutionException {
        UserVo userVo = future.get();
        System.out.println("userVo is not null " + (userVo != null) + " : " + System.currentTimeMillis());
    }

}
