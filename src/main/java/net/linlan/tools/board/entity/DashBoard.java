package net.linlan.tools.board.entity;

import net.linlan.commons.core.RandomUtils;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;


/**
 *
 * Filename:DashBoard.java
 * Desc:
 *
 * @author Linlan
 * CreateTime:2017-12-18 20:35:17
 *
 * @version 1.0
 * @since 1.0
 *
 */
public class DashBoard implements Serializable {
    private static final long serialVersionUID = 1L;

    //仪表盘ID
    private String id;
    //管理员ID
    private String userId;
    //应用ID
    private String appId;
    //目录ID
    private String folderId;
    private String folderName;
    private Boolean folderIsPrivate;
    //名称
    private String name;
    //布局JSON
    private String layout;
    //创建时间
    private Date createTime;
    //最后时间
    private Date lastTime;
    //分类名称
    private String cataName;
    //真实姓名
    private String realName;
    //用户名
    private String username;
    //权限
    private String permission;
    //描述
    private String description;

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

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    /**
     * get id, 仪表盘ID
     */
    public String getId() {
        return id;
    }

    /**
     * set id, 设置:仪表盘ID
     */
    public void setId(String id) {
        this.id = id;
    }


    /**
     * get userId, 管理员ID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * set userId, 设置:管理员ID
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }


    /**
     * get appId, 应用ID
     */
    public String getAppId() {
        return appId;
    }

    /**
     * set appId, 设置:应用ID
     */
    public void setAppId(String appId) {
        this.appId = appId;
    }


    /**
     * get folderId, 目录ID
     */
    public String getFolderId() {
        return folderId;
    }

    /**
     * set folderId, 设置:目录ID
     */
    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    /**
     * get name, 名称
     */
    public String getName() {
        return name;
    }

    /**
     * set name, 设置:名称
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * get content, 布局JSON
     */
    public String getLayout() {
        return layout;
    }

    /**
     * set content, 设置:布局JSON
     */
    public void setLayout(String layout) {
        this.layout = layout;
    }


    /**
     * get createTime,
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * set createTime, 设置:
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


    /**
     * get lastTime,
     */
    public Date getLastTime() {
        return lastTime;
    }

    /**
     * set lastTime, 设置:
     */
    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }

    /**
     * get description, 描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * set description, 设置:描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    public void init() {
        if (getId() == null){
            setId(RandomUtils.randomSid(12));
        }
        if (getCreateTime() == null) {
            setCreateTime(new Date());
        }
        if (getLastTime() == null) {
            setLastTime(new Timestamp(System.currentTimeMillis()));
        }
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
