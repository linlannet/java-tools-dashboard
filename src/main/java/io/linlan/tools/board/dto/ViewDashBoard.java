package io.linlan.tools.board.dto;

import io.linlan.tools.board.service.role.RolePermission;
import io.linlan.tools.board.entity.DashBoard;
import com.google.common.base.Function;
import io.linlan.commons.script.json.JsonUtils;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * 
 * Filename:ViewDashBoard.java
 * Desc:
 *
 * @author Linlan
 * CreateTime:2018/1/3 11:58
 *
 * @version 1.0
 * @since 1.0
 *
 */
public class ViewDashBoard {

    private String id;
    private String userId;
    private String appId;
    private String folderId;
    private String folderName;
    private Boolean folderIsPrivate;
    private String name;
    private String realName;
    private String username;
    private String createTime;
    private String lastTime;
    private Map<String, Object> layout;
    private String cataName;
    private boolean edit;
    private boolean delete;

    public static final Function TO = new Function<DashBoard, ViewDashBoard>() {
        @Nullable
        @Override
        public ViewDashBoard apply(@Nullable DashBoard input) {
            return new ViewDashBoard(input);
        }
    };

    public ViewDashBoard(DashBoard board) {
        this.id = board.getId();
        this.userId = board.getUserId();
        this.appId = board.getAppId();
        this.folderId = board.getFolderId();
        this.name = board.getName();
        this.realName = board.getRealName();
        this.username = board.getUsername();
        // 增加 两个没有赋值的对象
        this.folderName = board.getFolderName();
        this.folderIsPrivate = board.getFolderIsPrivate();
        this.createTime = board.getCreateTime() != null ? board.getCreateTime().toString() : "" ;
        this.lastTime = board.getLastTime() != null ? board.getLastTime().toString() : "";
        this.layout = JsonUtils.parseJO(board.getLayout());
        this.cataName = board.getCataName();
        this.edit = RolePermission.isEdit(board.getPermission());
        this.delete = RolePermission.isDelete(board.getPermission());
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

    public Map<String, Object> getLayout() {
        return layout;
    }

    public void setLayout(Map<String, Object> layout) {
        this.layout = layout;
    }

    public String getCataName() {
        return cataName;
    }

    public void setCataName(String cataName) {
        this.cataName = cataName;
    }

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public Boolean getFolderIsPrivate() {
        return folderIsPrivate;
    }

    public void setFolderIsPrivate(Boolean folderIsPrivate) {
        this.folderIsPrivate = folderIsPrivate;
    }
}
