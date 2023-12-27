package io.linlan.tools.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;


/**
 * the security service for user use of ShareAuthenticationToken
 * Filename:ShareAuthenticationToken.java
 * Desc: the ShareAuthenticationToken to provide token operation
 *
 * @author hcday
 * @author <a href="mailto:hcday@qq.com">hcday soo</a>
 * CreateTime:2017/12/16 21:37
 *
 * @version 1.0
 * @since 1.0
 *
 */
public class ShareAuthenticationToken extends AbstractAuthenticationToken {

    private User user;

    public ShareAuthenticationToken(User user) {
        super(AuthorityUtils.NO_AUTHORITIES);
        this.user = user;
    }


    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return user;
    }
}
