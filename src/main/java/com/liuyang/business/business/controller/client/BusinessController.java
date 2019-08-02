package com.liuyang.business.business.controller.client;

import com.liuyang.business.business.controller.BaseController;
import com.liuyang.business.pojo.Result;
import com.liuyang.business.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/business/v1")
public class BusinessController extends BaseController {

    @Autowired
    private BusinessService businessService;

    @GetMapping("user-info")
    public Result getUserInfo(@RequestParam(value = "userId", required = false) Integer userId) {
        return Result.success(businessService.getUserInfo(userId));
    }

    @GetMapping("by-user/{userId}")
    public Result getBizByUser(@PathVariable("userId") Integer userId) {

        return Result.success("business_" + userId);
    }
}
