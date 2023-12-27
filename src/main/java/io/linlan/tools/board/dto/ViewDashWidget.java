package io.linlan.tools.board.dto;

import io.linlan.tools.board.entity.DashWidget;
import com.google.common.base.Function;
import io.linlan.tools.board.service.role.RolePermission;
import io.linlan.commons.script.json.JsonUtils;

import javax.annotation.Nullable;
import java.util.Map;

/**
 *
 * Filename:ViewDashWidget.java
 * Desc:
 *
 * @author hcday
 * @author <a href="mailto:hcday@qq.com">hcday soo</a>
 * CreateTime:2018/1/3 11:59
 *
 * @version 1.0
 * @since 1.0
 *
 */
public class ViewDashWidget {

    private String id;
    private String userId;
    private String appId;
    private String folderId;
    private String datasourceId;
    private String datasetId;
    private String name;
    private String cataName;
    private String realName;
    private String username;
    private String createTime;
    private String lastTime;
    private Map<String, Object> content;
    private String description;
    private boolean edit;
    private boolean delete;

    public static final Function TO = new Function<DashWidget, ViewDashWidget>() {
        @Nullable
        @Override
        public ViewDashWidget apply(@Nullable DashWidget input) {
            return new ViewDashWidget(input);
        }
    };

    public ViewDashWidget(DashWidget widget) {
        this.id = widget.getId();
        this.userId = widget.getUserId();
        this.appId = widget.getAppId();
        this.folderId = widget.getFolderId();
        this.datasourceId = widget.getDatasourceId();
        this.datasetId = widget.getDatasetId();
        this.name = widget.getName();
        this.cataName = widget.getCataName();
        this.realName = widget.getRealName();
        this.username = widget.getUsername();
        this.createTime = widget.getCreateTime().toString();
        this.lastTime = widget.getLastTime().toString();
        this.content = JsonUtils.parseJO(widget.getContent());
        this.description = widget.getDescription();
        this.edit = RolePermission.isEdit(widget.getPermission());
        this.delete = RolePermission.isDelete(widget.getPermission());
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

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDatasourceId() {
        return datasourceId;
    }

    public void setDatasourceId(String datasourceId) {
        this.datasourceId = datasourceId;
    }

    public String getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(String datasetId) {
        this.datasetId = datasetId;
    }
}
