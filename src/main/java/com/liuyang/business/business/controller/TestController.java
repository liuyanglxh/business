package com.liuyang.business.business.controller;

import com.liuyang.business.pojo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/business/v1/test")
public class TestController {

    @GetMapping("test-throw")
    public Result testThrow(@RequestParam(value = "throwExc", defaultValue = "false") Boolean throwExc) throws Exception {
        if (throwExc) {
            throw new Exception("你让我报错的");
        }
        return Result.success("success");
    }
}
