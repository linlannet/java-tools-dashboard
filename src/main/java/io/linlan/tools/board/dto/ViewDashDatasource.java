package io.linlan.tools.board.dto;

import io.linlan.tools.board.entity.DashDatasource;
import io.linlan.tools.board.service.role.RolePermission;
import com.google.common.base.Function;
import io.linlan.commons.script.json.JsonUtils;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * 
 * Filename:ViewDashDatasource.java
 * Desc:
 *
 * @author Linlan
 * CreateTime:2017/12/21 0:21
 *
 * @version 1.0
 * @since 1.0
 *
 */
public class ViewDashDatasource {

    private String id;
    private String userId;
    private String appId;
    private String name;
    private String type;
    private Map<String, Object> content;
    private boolean edit;
    private boolean delete;
    private String realName;
    private String username;
    private String createTime;
    private String lastTime;

    public static final Function TO = new Function<DashDatasource, ViewDashDatasource>() {
        @Nullable
        @Override
        public ViewDashDatasource apply(@Nullable DashDatasource input) {
            return new ViewDashDatasource(input);
        }
    };

    public ViewDashDatasource(DashDatasource datasource) {
        this.id = datasource.getId();
        this.userId = datasource.getUserId();
        this.appId = datasource.getAppId();
        this.name = datasource.getName();
        this.type = datasource.getType();
        this.content = JsonUtils.parseJO(datasource.getContent());
        this.edit = RolePermission.isEdit(datasource.getPermission());
        this.delete = RolePermission.isDelete(datasource.getPermission());
        this.realName = datasource.getRealName();
        this.username = datasource.getUsername();
        this.createTime = datasource.getCreateTime().toString();
        this.lastTime = datasource.getLastTime().toString();
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Object> getContent() {
        return content;
    }

    public void setContent(Map<String, Object> content) {
        this.content = content;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

}
