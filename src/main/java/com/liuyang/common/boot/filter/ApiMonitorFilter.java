package com.liuyang.common.boot.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class ApiMonitorFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String uri = ((HttpServletRequest) request).getRequestURI();
        long start = System.currentTimeMillis();
        chain.doFilter(request, response);
        long end = System.currentTimeMillis();
        System.out.println(uri + ":" + (end - start));
    }

    @Override
    public void destroy() {

    }
}
