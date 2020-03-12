package com.liuyang.common.boot.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class ApiMonitorFilter implements Filter {

    private final Logger logger = LoggerFactory.getLogger(ApiMonitorFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String uri = ((HttpServletRequest) request).getRequestURI();
        long start = System.currentTimeMillis();
        chain.doFilter(request, response);
        String method = ((HttpServletRequest) request).getMethod();
        logger.info("{} {}:{}ms", method, uri, System.currentTimeMillis() - start);
    }

    @Override
    public void destroy() {

    }
}
