package io.linlan.tools.board.entity;

import io.linlan.commons.core.RandomUtils;

import java.io.Serializable;
import java.util.Date;


/**
 *
 * Filename:DashAdminUser.java
 * Desc:
 *
 * @author hcday
 * @author <a href="mailto:hcday@qq.com">hcday</a>
 * CreateTime:2018-05-19 14:55:01
 *
 * @version 1.0
 * @since 1.0
 *
 */
public class DashAdminUser implements Serializable {
    private static final long serialVersionUID = 1L;

    //用户ID
    private String id;
    //登录名
    private String username;
    //名称
    private String name;
    //密码
    private String password;
    //混淆字符
    private String salt;
    //创建时间
    private Date createTime;
    //状态0未生效1正常2受限3锁定
    private Integer status;
    //备用1
    private String spare1;
    //备用2
    private String spare2;


    /**
     * get id, 用户ID
     */
    public String getId() {
        return id;
    }

    /**
     * set id, 设置:用户ID
     */
    public void setId(String id) {
        this.id = id;
    }


    /**
     * get username, 登录名
     */
    public String getUsername() {
        return username;
    }

    /**
     * set username, 设置:登录名
     */
    public void setUsername(String username) {
        this.username = username;
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
     * get password, 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * set password, 设置:密码
     */
    public void setPassword(String password) {
        this.password = password;
    }


    /**
     * get salt, 混淆字符
     */
    public String getSalt() {
        return salt;
    }

    /**
     * set salt, 设置:混淆字符
     */
    public void setSalt(String salt) {
        this.salt = salt;
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
     * get status, 状态0未生效1正常2受限3锁定
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * set status, 设置:状态0未生效1正常2受限3锁定
     */
    public void setStatus(Integer status) {
        this.status = status;
    }


    /**
     * get spare1, 备用1
     */
    public String getSpare1() {
        return spare1;
    }

    /**
     * set spare1, 设置:备用1
     */
    public void setSpare1(String spare1) {
        this.spare1 = spare1;
    }


    /**
     * get spare2, 备用2
     */
    public String getSpare2() {
        return spare2;
    }

    /**
     * set spare2, 设置:备用2
     */
    public void setSpare2(String spare2) {
        this.spare2 = spare2;
    }


    //初始化方法
    public void init() {
        //添加对数据库或实体对象的默认值处理
        if(getId() == null){
            setId(RandomUtils.randomSid(12));
        }
        if(getCreateTime() == null){
            setCreateTime(new Date());
        }
    }

}
