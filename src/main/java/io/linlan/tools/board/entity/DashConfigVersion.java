package io.linlan.tools.board.entity;

import io.linlan.commons.core.RandomUtils;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;


/**
 *
 * Filename:ConfigVersion.java
 * Desc:
 *
 * @author Linlan
 * CreateTime:2018-05-05 15:11:56
 *
 * @version 1.0
 * @since 1.0
 *
 */
public class DashConfigVersion implements Serializable {
    private static final Long serialVersionUID = 1L;
    /**
     * 确实状态为正常
     */
    private static final Integer STATUS_DEFAULT = 1;

    //分类ID
    private String id;
    //创建人ID
    private String userId;
    //应用ID
    private String appId;
    //名称
    private String name;
    //代码
    private String code;
    //状态0缺省1正常
    private Integer status;
    //创建时间
    private Date createTime;
    //最后时间
    private Date lastTime;
    //描述
    private String description;


    /**
     * get id, 分类ID
     */
    public String getId() {
        return id;
    }

    /**
     * set id, 设置:分类ID
     */
    public void setId(String id) {
        this.id = id;
    }


    /**
     * get userId, 创建人ID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * set userId, 设置:创建人ID
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
     * get code, 代码
     */
    public String getCode() {
        return code;
    }

    /**
     * set code, 设置:代码
     */
    public void setCode(String code) {
        this.code = code;
    }


    /**
     * get status, 状态0缺省1正常
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * set status, 设置:状态0缺省1正常
     */
    public void setStatus(Integer status) {
        this.status = status;
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
            setId(RandomUtils.UUID());
        }
        if (getCode() == null){
            setCode(RandomUtils.randomCode());
        }
        if (getStatus() == null){
            setStatus(STATUS_DEFAULT);
        }
        if (getCreateTime() == null) {
            setCreateTime(new Date());
        }
        if (getLastTime() == null) {
            setLastTime(new Timestamp(System.currentTimeMillis()));
        }
    }
}
