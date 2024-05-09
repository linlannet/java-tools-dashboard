package io.linlan.tools.board.dto;

import com.alibaba.fastjson.JSON;
import io.linlan.tools.security.User;

import java.util.Date;


/**
 * the Base Controller Log to record
 * Filename:DbUserDetailService.java
 * Desc: to write or record log of controller
 *
 * @author Linlan
 * CreateTime:2017/12/16 21:37
 *
 * @version 1.0
 * @since 1.0
 *
 */
public class BaseControllerLog {

    private User user;
    private String requestUrl;
    private Date actionTime = new Date();

    public BaseControllerLog() {}

    public BaseControllerLog(User user, String requestUrl) {
        this.user = user;
        this.requestUrl = requestUrl;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public Date getActionTime() {
        return actionTime;
    }

    public void setActionTime(Date actionTime) {
        this.actionTime = actionTime;
    }

    @Override
    public String toString() {
        return JSON.toJSONStringWithDateFormat(this, "yyyy-MM-dd HH:mm:ss.SSS");
    }
}
