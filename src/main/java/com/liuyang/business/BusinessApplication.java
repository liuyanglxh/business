package com.liuyang.business;

import com.liuyang.common.boot.filter.ApiMonitorFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = "com.liuyang")
@ComponentScan("com.liuyang")
public class BusinessApplication {

    public static void main(String[] args) {
        SpringApplication.run(BusinessApplication.class, args);
    }

    @Bean
    public FilterRegistrationBean registrationBean(){
        FilterRegistrationBean bean = new FilterRegistrationBean(new ApiMonitorFilter());
        bean.addUrlPatterns("/*");

        return bean;
    }

}
