package io.linlan.tools.board.entity;

import io.linlan.commons.core.RandomUtils;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;


/**
 *
 * Filename:DashDatasource.java
 * Desc:
 *
 * @author Linlan
 * CreateTime:2017-12-18 20:35:17
 *
 * @version 1.0
 * @since 1.0
 *
 */
public class DashDatasource implements Serializable {
    private static final long serialVersionUID = 1L;

    //数据源ID
    private String id;
    //管理员ID
    private String userId;
    //应用ID
    private String appId;
    //数据源名称
    private String name;
    //数据源类型
    private String type;
    //配置JSON
    private String content;
    //创建时间
    private Date createTime;
    //最后时间
    private Date lastTime;

    //真实姓名
    private String realName;
    //用户名
    private String username;
    //权限
    private String permission;

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
     * get id, 数据源ID
     */
    public String getId() {
        return id;
    }

    /**
     * set id, 设置:数据源ID
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
     * get name, 数据源名称
     */
    public String getName() {
        return name;
    }

    /**
     * set name, 设置:数据源名称
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * get type, 数据源类型
     */
    public String getType() {
        return type;
    }

    /**
     * set type, 设置:数据源类型
     */
    public void setType(String type) {
        this.type = type;
    }


    /**
     * get content, 配置JSON
     */
    public String getContent() {
        return content;
    }

    /**
     * set content, 设置:配置JSON
     */
    public void setContent(String content) {
        this.content = content;
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


    public void init(){
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
}
