package io.linlan.tools.security.service;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;

/**
 * the Local Security Filter service for security
 * Filename:LocalSecurityFilter.java
 * Desc: to filter path or actions of security
 *
 * @author Linlan
 * CreateTime:2017/12/16 21:37
 *
 * @version 1.0
 * @since 1.0
 *
 */
public class LogFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {


        HttpServletRequest request = ((HttpServletRequest) servletRequest);
        Enumeration<String> e = request.getHeaderNames();
        do {
            String s = e.nextElement();
            System.out.println(s + " : " + request.getHeader(s));
        } while (e.hasMoreElements());
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
