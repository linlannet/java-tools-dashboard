package io.linlan.tools.board.entity;

import io.linlan.commons.core.RandomUtils;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;


/**
 *
 * Filename:DashOperationJob.java
 * Desc:
 *
 * @author Linlan
 * CreateTime:2017-12-18 20:35:17
 *
 * @version 1.0
 * @since 1.0
 *
 */
public class DashOperationJob implements Serializable {
    private static final long serialVersionUID = 1L;

    //计划编号
    private Long id;
    //用户ID
    private String userId;
    //应用ID
    private String appId;
    //任务名称
    private String name;
    //cron任务表达式
    private String cronExp;
    //开始时间
    private Date startDate;
    //结束时间
    private Date endDate;
    //任务类型
    private String jobType;
    //任务详情
    private String config;
    //任务状态:0正常1暂停
    private Integer jobStatus;
    //最后执行时间
    private Date lastExecTime;
    //执行日志
    private String execLog;
    //备用1
    private String spare1;
    //备用2
    private String spare2;
    //用户名称
    private String username;
    //权限
    private String permission;

    /**
     * get id, 计划编号
     */
    public Long getId() {
        return id;
    }

    /**
     * set id, 设置:计划编号
     */
    public void setId(Long id) {
        this.id = id;
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
     * get name, 任务名称
     */
    public String getName() {
        return name;
    }

    /**
     * set name, 设置:任务名称
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * get cronExp, cron任务表达式
     */
    public String getCronExp() {
        return cronExp;
    }

    /**
     * set cronExp, 设置:cron任务表达式
     */
    public void setCronExp(String cronExp) {
        this.cronExp = cronExp;
    }


    /**
     * get startDate, 开始时间
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * set startDate, 设置:开始时间
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }


    /**
     * get endDate, 结束时间
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * set endDate, 设置:结束时间
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }


    /**
     * get jobType, 任务类型
     */
    public String getJobType() {
        return jobType;
    }

    /**
     * set jobType, 设置:任务类型
     */
    public void setJobType(String jobType) {
        this.jobType = jobType;
    }


    /**
     * get config, 任务详情
     */
    public String getConfig() {
        return config;
    }

    /**
     * set config, 设置:任务详情
     */
    public void setConfig(String config) {
        this.config = config;
    }


    /**
     * get jobStatus, 任务状态:0正常1暂停
     */
    public Integer getJobStatus() {
        return jobStatus;
    }

    /**
     * set jobStatus, 设置:任务状态:0正常1暂停
     */
    public void setJobStatus(Integer jobStatus) {
        this.jobStatus = jobStatus;
    }


    /**
     * get lastExecTime, 最后执行时间
     */
    public Date getLastExecTime() {
        return lastExecTime;
    }

    /**
     * set lastExecTime, 设置:最后执行时间
     */
    public void setLastExecTime(Date lastExecTime) {
        this.lastExecTime = lastExecTime;
    }


    /**
     * get execLog, 执行日志
     */
    public String getExecLog() {
        return execLog;
    }

    /**
     * set execLog, 设置:执行日志
     */
    public void setExecLog(String execLog) {
        this.execLog = execLog;
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

    public void init() {
        if (getId() == null){
            setId(RandomUtils.randomLid());
        }
        if (getStartDate() == null) {
            setStartDate(new Date());
        }
        if (getLastExecTime() == null) {
            setLastExecTime(new Timestamp(System.currentTimeMillis()));
        }
    }
}
