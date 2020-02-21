package com.liuyang.common.cloud;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * hystrix是异步调用，此类可以将父线程的上线文传递到子线程
 */
@Component
public class HeaderInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        template.header("Content-Type", "application/json");
    }
}
