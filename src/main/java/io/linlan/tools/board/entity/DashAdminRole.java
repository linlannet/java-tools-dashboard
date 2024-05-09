package io.linlan.tools.board.entity;

import io.linlan.commons.core.RandomUtils;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;


/**
 *
 * Filename:DashAdminRole.java
 * Desc:
 *
 * @author Linlan
 * CreateTime:2017-12-18 20:35:17
 *
 * @version 1.0
 * @since 1.0
 *
 */
public class DashAdminRole implements Serializable {
    private static final long serialVersionUID = 1L;

    //角色ID
    private String id;
    //角色分类ID
    private String roletypeId;
    //管理员ID
    private String userId;
    //角色自定义名称
    private String name;
    //创建时间
    private Date createTime;
    //最后时间
    private Date lastTime;
    //描述
    private String description;


    /**
     * get id, 角色ID
     */
    public String getId() {
        return id;
    }

    /**
     * set id, 设置:角色ID
     */
    public void setId(String id) {
        this.id = id;
    }


    /**
     * get roletypeId, 角色分类ID
     */
    public String getRoletypeId() {
        return roletypeId;
    }

    /**
     * set roletypeId, 设置:角色分类ID
     */
    public void setRoletypeId(String roletypeId) {
        this.roletypeId = roletypeId;
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
     * get name, 角色自定义名称
     */
    public String getName() {
        return name;
    }

    /**
     * set name, 设置:角色自定义名称
     */
    public void setName(String name) {
        this.name = name;
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
        //添加对数据库或实体对象的默认值处理
        if(getId() == null){
            setId(RandomUtils.randomSid(12));
        }
        if(getCreateTime() == null){
            setCreateTime(new Date());
        }
        if (getLastTime() == null) {
            setLastTime(new Timestamp(System.currentTimeMillis()));
        }
    }
}
