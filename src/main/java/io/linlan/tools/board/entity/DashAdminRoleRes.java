package io.linlan.tools.board.entity;

import io.linlan.commons.core.RandomUtils;

import java.io.Serializable;



/**
 *
 * Filename:DashAdminRoleRes.java
 * Desc:
 *
 * @author Linlan
 * CreateTime:2017-12-18 20:35:18
 *
 * @version 1.0
 * @since 1.0
 *
 */
public class DashAdminRoleRes implements Serializable {
    private static final long serialVersionUID = 1L;

    //角色资源ID
    private Long id;
    //角色ID
    private String roleId;
    //资源类型
    private String resType;
    //资源ID
    private String resId;
    //权限URL
    private String permission;


    /**
     * get id, 角色资源ID
     */
    public Long getId() {
        return id;
    }

    /**
     * set id, 设置:角色资源ID
     */
    public void setId(Long id) {
        this.id = id;
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


    /**
     * get resType, 资源类型
     */
    public String getResType() {
        return resType;
    }

    /**
     * set resType, 设置:资源类型
     */
    public void setResType(String resType) {
        this.resType = resType;
    }


    /**
     * get resId, 资源ID
     */
    public String getResId() {
        return resId;
    }

    /**
     * set resId, 设置:资源ID
     */
    public void setResId(String resId) {
        this.resId = resId;
    }


    /**
     * get permission, 权限URL
     */
    public String getPermission() {
        return permission;
    }

    /**
     * set permission, 设置:权限URL
     */
    public void setPermission(String permission) {
        this.permission = permission;
    }


    public void init() {
        if (getId() == null){
            setId(RandomUtils.randomLid());
        }
    }
}
