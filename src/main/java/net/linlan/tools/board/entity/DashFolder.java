package net.linlan.tools.board.entity;

import net.linlan.commons.core.RandomUtils;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;


/**
 *
 * Filename:DashFolder.java
 * Desc:
 *
 * @author Linlan
 * CreateTime:2018-05-05 15:11:56
 *
 * @version 1.0
 * @since 1.0
 *
 */
public class DashFolder implements Serializable {
    private static final long serialVersionUID = 1L;

    //目录ID
    private String id;
    //父目录ID
    private String parentId;
    //应用ID
    private String appId;
    //用户ID
    private String userId;
    //名称
    private String name;
    //是否私有1是0否
    private Boolean isPrivate;
    //创建时间
    private Date createTime;
    //最后时间
    private Date lastTime;
    //描述
    private String description;


    /**
     * get id, 目录ID
     */
    public String getId() {
        return id;
    }

    /**
     * set id, 设置:目录ID
     */
    public void setId(String id) {
        this.id = id;
    }


    /**
     * get parentId, 父目录ID
     */
    public String getParentId() {
        return parentId;
    }

    /**
     * set parentId, 设置:父目录ID
     */
    public void setParentId(String parentId) {
        this.parentId = parentId;
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
     * get isPrivate, 是否私有1是0否
     */
    public Boolean getIsPrivate() {
        return isPrivate;
    }

    /**
     * set isPrivate, 设置:是否私有1是0否
     */
    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }


    /**
     * get createTime, 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * set createTime, 设置:创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


    /**
     * get lastTime, 最后时间
     */
    public Date getLastTime() {
        return lastTime;
    }

    /**
     * set lastTime, 设置:最后时间
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
        if(getId() == null){
            setId(RandomUtils.randomSid(12));
        }
        if (getIsPrivate() == null){
            setIsPrivate(true);
        }
        if (getCreateTime() == null) {
            setCreateTime(new Date());
        }
        if (getLastTime() == null) {
            setLastTime(new Timestamp(System.currentTimeMillis()));
        }
    }
}
