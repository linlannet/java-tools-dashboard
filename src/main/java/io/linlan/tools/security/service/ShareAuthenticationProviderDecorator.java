package io.linlan.tools.security.service;

import io.linlan.tools.security.ShareAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;


/**
 * the implements AuthenticationProvider for current user info
 * Filename:ShareAuthenticationProviderDecorator.java
 * Desc: the implements AuthenticationProvider for current user info
 *
 * @author Linlan
 * CreateTime:2017/12/16 21:37
 *
 * @version 1.0
 * @since 1.0
 *
 */
public class ShareAuthenticationProviderDecorator implements AuthenticationProvider {

    private AuthenticationProvider authenticationProvider;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication instanceof ShareAuthenticationToken) {
            return authentication;
        } else {
            return authenticationProvider.authenticate(authentication);
        }
    }

    @Override
    public boolean supports(Class<?> aClass) {
        if (aClass.equals(ShareAuthenticationToken.class)) {
            return true;
        } else {
            return authenticationProvider.supports(aClass);
        }
    }

    public void setAuthenticationProvider(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }
}
