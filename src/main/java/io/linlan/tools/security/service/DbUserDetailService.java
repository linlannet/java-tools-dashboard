package io.linlan.tools.security.service;

import io.linlan.tools.security.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * the security service for user detail of db type
 * Filename:DbUserDetailService.java
 * Desc: to verify user info and detail of db info use springframework
 *
 * @author Linlan
 * CreateTime:2017/12/16 21:37
 *
 * @version 1.0
 * @since 1.0
 *
 */
public class DbUserDetailService extends JdbcDaoImpl {

    protected List<UserDetails> loadUsersByUsername(final String username) {
        return this.getJdbcTemplate().query(super.getUsersByUsernameQuery(), new String[]{username}, new RowMapper() {
            public UserDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
                String userId = rs.getString(1);
                String realName = rs.getString(2);
                String username = rs.getString(3);
                String password = rs.getString(4);
                boolean enabled = rs.getBoolean(5);
                User user = new User(username, password, enabled, true, true, true, AuthorityUtils.NO_AUTHORITIES);
                user.setUserId(userId);
                user.setName(realName);
                return user;
            }
        });
    }

    protected UserDetails createUserDetails(String username, UserDetails userFromUserQuery, List<GrantedAuthority> combinedAuthorities) {
        return userFromUserQuery;
    }

}
