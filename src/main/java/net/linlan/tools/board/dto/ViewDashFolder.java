package net.linlan.tools.board.dto;

import com.google.common.base.Function;
import net.linlan.tools.board.entity.DashFolder;

import javax.annotation.Nullable;

public class ViewDashFolder {
    private String id;
    private String name;
    private String parentId;
    private Boolean isPrivate;
    private String userId;
    private String createTime;
    private String lastTime;

    public static final Function TO = new Function<DashFolder, ViewDashFolder>() {
        @Nullable
        @Override
        public ViewDashFolder apply(@Nullable DashFolder input) {
            return new ViewDashFolder(input);
        }
    };

    public ViewDashFolder(DashFolder folder) {
        this.id = folder.getId();
        this.name = folder.getName();
        this.parentId = folder.getParentId();
        this.isPrivate = folder.getIsPrivate();
        this.userId = folder.getUserId();
        this.createTime = folder.getCreateTime().toString();
        this.lastTime = folder.getLastTime().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Boolean getPrivate() {
        return isPrivate;
    }

    public void setPrivate(Boolean aPrivate) {
        isPrivate = aPrivate;
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
}
