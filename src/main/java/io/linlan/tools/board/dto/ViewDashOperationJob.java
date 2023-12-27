package io.linlan.tools.board.dto;

import io.linlan.tools.board.entity.DashOperationJob;
import io.linlan.tools.board.service.role.RolePermission;
import org.apache.commons.collections.map.HashedMap;
import io.linlan.commons.script.json.JsonUtils;

import java.util.Date;
import java.util.Map;

/**
 * 
 * Filename:ViewDashOperationJob.java
 * Desc:
 *
 * @author hcday
 * @author <a href="mailto:hcday@qq.com">hcday soo</a>
 * CreateTime:2017/12/21 0:21
 *
 * @version 1.0
 * @since 1.0
 *
 */
public class ViewDashOperationJob {

    private Long id;
    private String name;
    private String cronExp;
    private Map<String, Object> daterange;
    private String jobType;
    private Map<String, Object> config;
    private String userId;
    private Date lastExecTime;
    private String username;
    private Integer jobStatus;
    private String execLog;
    private boolean edit;
    private boolean delete;

    public static final Long STATUS_PROCESSING = 2L;
    public static final Long STATUS_FINISH = 1L;
    public static final Long STATUS_FAIL = 0L;

    public ViewDashOperationJob(DashOperationJob job) {
        this.id = job.getId();
        this.userId = job.getUserId();
        this.name = job.getName();
        this.cronExp = job.getCronExp();
        this.daterange = new HashedMap();
        this.daterange.put("startDate", job.getStartDate());
        this.daterange.put("endDate", job.getEndDate());
        this.jobType = job.getJobType();
        this.config = JsonUtils.parseJO(job.getConfig());
        this.lastExecTime = job.getLastExecTime();
        this.username = job.getUsername();
        this.jobStatus = job.getJobStatus();
        this.execLog = job.getExecLog();
        this.edit = RolePermission.isEdit(job.getPermission());
        this.delete = RolePermission.isDelete(job.getPermission());
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

    public String getExecLog() {
        return execLog;
    }

    public void setExecLog(String execLog) {
        this.execLog = execLog;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCronExp() {
        return cronExp;
    }

    public void setCronExp(String cronExp) {
        this.cronExp = cronExp;
    }

    public Map<String, Object> getDaterange() {
        return daterange;
    }

    public void setDaterange(Map<String, Object> daterange) {
        this.daterange = daterange;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public Map<String, Object> getConfig() {
        return config;
    }

    public void setConfig(Map<String, Object> config) {
        this.config = config;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getLastExecTime() {
        return lastExecTime;
    }

    public void setLastExecTime(Date lastExecTime) {
        this.lastExecTime = lastExecTime;
    }

    public Integer getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(Integer jobStatus) {
        this.jobStatus = jobStatus;
    }
}
