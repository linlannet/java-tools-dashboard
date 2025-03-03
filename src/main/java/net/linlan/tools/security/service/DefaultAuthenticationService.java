package net.linlan.tools.security.service;

import net.linlan.tools.security.ShareAuthenticationToken;
import net.linlan.tools.security.User;
import net.linlan.tools.board.service.AuthService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;


/**
 * the implements AuthenticationService for current user info
 * Filename:DefaultAuthenticationService.java
 * Desc: implements AuthenticationService for current user info
 *
 * @author Linlan
 * CreateTime:2017/12/16 21:37
 *
 * @version 1.0
 * @since 1.0
 *
 */
public class DefaultAuthenticationService implements AuthService {

    @Override
    public User getCurrentUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) {
            return null;
        }
        Authentication authentication = context.getAuthentication();
        if (authentication == null) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        User user = null;
        if (principal instanceof UserDetails || principal instanceof User ) {
            user = (User) authentication.getPrincipal();
            //username = ((UserDetails)principal).getUsername();
        }else { // 默认登录 admin
            user = new User("admin", "admin", new ArrayList<>());
            //user.setUserId(sidCache.get(sid));
            user.setUserId("77f0shu0m3d110a1jiam03d23qi0001");
            context.setAuthentication(new ShareAuthenticationToken(user));
        }
        return user;

    }

}
