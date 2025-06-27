package net.linlan.tools.board.service.persist;

import com.alibaba.fastjson2.JSONObject;

/**
 * 
 * Filename:PersistContext.java
 * Desc:
 *
 * @author Linlan
 * CreateTime:2018/1/3 12:04
 *
 * @version 1.0
 * @since 1.0
 *
 */
public class PersistContext {
    private String dashboardId;
    private JSONObject data;

    public PersistContext(String dashboardId) {
        this.dashboardId = dashboardId;
    }

    public String getDashboardId() {
        return dashboardId;
    }

    public void setDashboardId(String dashboardId) {
        this.dashboardId = dashboardId;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }
}
