package io.linlan.tools.board.dto;

import com.google.common.base.Function;
import io.linlan.tools.board.entity.DashDataset;
import io.linlan.tools.board.service.role.RolePermission;
import io.linlan.commons.script.json.JsonUtils;

import javax.annotation.Nullable;
import java.util.Map;

/**
 *
 * Filename:ViewDashDataset.java
 * Desc:
 *
 * @author Linlan
 * CreateTime:2018/1/3 11:58
 *
 * @version 1.0
 * @since 1.0
 *
 */
public class ViewDashDataset {
    private String id;
    private String userId;
    private String appId;
    private String folderId;
    private String folderPath;
    private String datasourceId;
    private String name;
    private String cataName;
    private Map<String, Object> content;
    private boolean edit;
    private boolean delete;
    private String createTime;
    private String lastTime;
    private String description;

    private String realName;
    private String username;

    public static final Function TO = new Function<DashDataset, ViewDashDataset>() {
        @Nullable
        @Override
        public ViewDashDataset apply(@Nullable DashDataset input) {
            return new ViewDashDataset(input);
        }
    };

    public ViewDashDataset(DashDataset dataset) {
        this.id = dataset.getId();
        this.userId = dataset.getUserId();
        this.appId = dataset.getAppId();
        this.folderId = dataset.getFolderId();
        this.folderPath = dataset.getFolderPath();
        this.datasourceId = dataset.getDatasourceId();
        this.name = dataset.getName();
        this.cataName = dataset.getCataName();
        this.content = JsonUtils.parseJO(dataset.getContent());
        this.realName = dataset.getRealName();
        this.username = dataset.getUsername();
        this.createTime = dataset.getCreateTime().toString();
        this.lastTime = dataset.getLastTime().toString();
        this.description = dataset.getDescription();
        this.edit = RolePermission.isEdit(dataset.getPermission());
        this.delete = RolePermission.isDelete(dataset.getPermission());
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

    public Map<String, Object> getContent() {
        return content;
    }

    public void setContent(Map<String, Object> content) {
        this.content = content;
    }

    public String getCataName() {
        return cataName;
    }

    public void setCataName(String cataName) {
        this.cataName = cataName;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public String getDatasourceId() {
        return datasourceId;
    }

    public void setDatasourceId(String datasourceId) {
        this.datasourceId = datasourceId;
    }
}
