package io.linlan.tools.security.service;

import io.linlan.tools.board.dao.DashAdminUserDao;
import io.linlan.tools.security.User;
import org.jasig.cas.client.validation.Assertion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.cas.userdetails.AbstractCasAssertionUserDetailsService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;


/**
 * the security service for user detail
 * Filename:UserDetailsService.java
 * Desc: to verify user info and detail of User define
 *
 * @author hcday
 * @author <a href="mailto:hcday@qq.com">hcday soo</a>
 * CreateTime:2017/12/16 21:37
 *
 * @version 1.0
 * @since 1.0
 *
 */
@SuppressWarnings("deprecation")
public final class UserDetailsService extends AbstractCasAssertionUserDetailsService {

    private static final String NON_EXISTENT_PASSWORD_VALUE = "NO_PASSWORD";

    @SuppressWarnings("unused")
    private final String[] attributes;

    @SuppressWarnings("unused")
    private boolean convertToUpperCase = true;

    @Autowired
    private DashAdminUserDao dashAdminUserDao;

    public UserDetailsService(final String[] attributes) {
        Assert.notNull(attributes, "attributes cannot be null.");
        Assert.isTrue(attributes.length > 0, "At least one attribute is required to retrieve roles from.");
        this.attributes = attributes;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected UserDetails loadUserDetails(final Assertion assertion) {
        final List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        User user = new User(
                (String) assertion.getPrincipal().getAttributes().get("displayName"),
                NON_EXISTENT_PASSWORD_VALUE,
                true, true, true, true,
                grantedAuthorities);
        user.setCompany((String) assertion.getPrincipal().getAttributes().get("company"));
        user.setDepartment((String) assertion.getPrincipal().getAttributes().get("department"));
        user.setUserId((String) assertion.getPrincipal().getAttributes().get("employee"));
        user.setName(assertion.getPrincipal().getName());
        dashAdminUserDao.saveNewUser(user.getUserId(), user.getUsername(), user.getName());
        return user;
    }

    /**
     * Converts the returned attribute values to uppercase values.
     *
     * @param convertToUpperCase true if it should convert, false otherwise.
     */
    public void setConvertToUpperCase(final boolean convertToUpperCase) {
        this.convertToUpperCase = convertToUpperCase;
    }
}
