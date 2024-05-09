package io.linlan.tools.board.entity;

import io.linlan.commons.core.RandomUtils;

import java.io.Serializable;


/**
 *
 * Filename:AdminUserRole.java
 * Desc:
 *
 * @author Linlan
 * CreateTime:2017-12-18 20:35:18
 *
 * @version 1.0
 * @since 1.0
 *
 */
public class DashAdminUserRole implements Serializable {
    private static final long serialVersionUID = 1L;

    //用户角色ID
    private Long id;
    //用户ID
    private String userId;
    //角色ID
    private String roleId;


    /**
     * get id, 用户角色ID
     */
    public Long getId() {
        return id;
    }

    /**
     * set id, 设置:用户角色ID
     */
    public void setId(Long id) {
        this.id = id;
    }


    /**
     * get userId, 用户ID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * set userId, 设置:用户ID
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }


    /**
     * get roleId, 角色ID
     */
    public String getRoleId() {
        return roleId;
    }

    /**
     * set roleId, 设置:角色ID
     */
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public void init() {
        if (getId() == null){
            setId(RandomUtils.randomLid());
        }
    }
}
