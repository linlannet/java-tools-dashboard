package io.linlan.tools.security.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.commons.lang.StringUtils;
import io.linlan.tools.security.User;
import io.linlan.tools.security.ShareAuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


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
public class LocalSecurityFilter implements Filter {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static String context = "";
    private static LoadingCache<String, String> sidCache = CacheBuilder.newBuilder().expireAfterAccess(3, TimeUnit.MINUTES).build(new CacheLoader<String, String>() {
        @Override
        public String load(String key) throws Exception {
            return null;
        }
    });

    public static void put(String sid, String uid) {
        sidCache.put(sid, uid);
    }

    public static String getContext() {
        return context;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest hsr = (HttpServletRequest) servletRequest;
        if (StringUtils.isBlank(context)) {
            context = hsr.getContextPath();
        }
        if ("/render.html".equals(hsr.getServletPath())) {
            String sid = hsr.getParameter("sid");
            try {
                String uid = sidCache.get(sid);
                if (StringUtils.isNotEmpty(uid)) {
                    User user = new User("shareUser", "", new ArrayList<>());
                    user.setUserId(sidCache.get(sid));
                    SecurityContext context = SecurityContextHolder.getContext();
                    context.setAuthentication(new ShareAuthenticationToken(user));
                    hsr.getSession().setAttribute("SPRING_SECURITY_CONTEXT", context);
                }
            } catch (Exception e) {
                logger.error("", e);
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
