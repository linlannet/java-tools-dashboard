package io.linlan.tools.board.dto;

import io.linlan.tools.board.entity.DashAdminRoleRes;
import io.linlan.tools.board.service.role.RolePermission;

/**
 * 
 * Filename:ViewDashAdminRoleRes.java
 * Desc:
 *
 * @author Linlan
 * CreateTime:2018/1/3 11:59
 *
 * @version 1.0
 * @since 1.0
 *
 */
public class ViewDashAdminRoleRes {

    private Long roleResId;
    private String roleId;
    private String resId;
    private String resType;
    private boolean edit;
    private boolean delete;


    public ViewDashAdminRoleRes(DashAdminRoleRes dashboardRoleRes) {
        this.roleResId = dashboardRoleRes.getId();
        this.roleId = dashboardRoleRes.getRoleId();
        this.resId = dashboardRoleRes.getResId();
        this.resType = dashboardRoleRes.getResType();
        this.edit = RolePermission.isEdit(dashboardRoleRes.getPermission());
        this.delete = RolePermission.isDelete(dashboardRoleRes.getPermission());
    }

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public Long getRoleResId() {
        return roleResId;
    }

    public void setRoleResId(Long roleResId) {
        this.roleResId = roleResId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public String getResType() {
        return resType;
    }

    public void setResType(String resType) {
        this.resType = resType;
    }
}
