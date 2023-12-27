package io.linlan.tools.board.dto;

import java.io.Serializable;

/**
 * 
 * Filename:DataProviderResult.java
 * Desc:
 *
 * @author hcday
 * @author <a href="mailto:hcday@qq.com">hcday soo</a>
 * CreateTime:2018/1/3 11:58
 *
 * @version 1.0
 * @since 1.0
 *
 */
public class DataProviderResult implements Serializable{

    private String[][] data;
    private String[] columns;
    private String msg;
    private int resultCount = 0;

    public DataProviderResult() {}

    public DataProviderResult(String[][] data, String msg) {
        this.data = data;
        this.msg = msg;
    }

    public DataProviderResult(String[][] data, String msg, int resultCount) {
        this.data = data;
        this.msg = msg;
        this.resultCount = resultCount;
    }

    public String[][] getData() {
        return data;
    }

    public void setData(String[][] data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getResultCount() {
        return resultCount;
    }

    public void setResultCount(int resultCount) {
        this.resultCount = resultCount;
    }

    public String[] getColumns() {
        return columns;
    }

    public void setColumns(String[] columns) {
        this.columns = columns;
    }
}
