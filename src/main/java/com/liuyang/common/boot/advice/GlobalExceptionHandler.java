package com.liuyang.common.boot.advice;

import com.liuyang.business.pojo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 统一处理controller抛出的异常
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ResponseBody
    @ExceptionHandler(Throwable.class)
    public Object handle(HttpServletRequest req, HttpServletResponse resp, Throwable cause) {
        String uri = req.getRequestURI();
        String method = req.getMethod();
        logger.error("error call {} {}", method, uri, cause);
        return Result.fail("稍后再试");
    }

}
