package io.linlan.tools.board.entity;

import io.linlan.commons.core.RandomUtils;

import java.io.Serializable;


/**
 *
 * Filename:DashBoardparam.java
 * Desc:
 *
 * @author hcday
 * @author <a href="mailto:hcday@qq.com">hcday</a>
 * CreateTime:2017-12-18 20:35:18
 *
 * @version 1.0
 * @since 1.0
 *
 */
public class DashBoardParam implements Serializable {
    private static final long serialVersionUID = 1L;

    //仪表盘子项ID
    private Long id;
    //管理员ID
    private String userId;
    //仪表盘ID
    private String boardId;
    //配置内容
    private String content;


    /**
     * get id, 仪表盘子项ID
     */
    public Long getId() {
        return id;
    }

    /**
     * set id, 设置:仪表盘子项ID
     */
    public void setId(Long id) {
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
     * get boardId, 仪表盘ID
     */
    public String getBoardId() {
        return boardId;
    }

    /**
     * set boardId, 设置:仪表盘ID
     */
    public void setBoardId(String boardId) {
        this.boardId = boardId;
    }


    /**
     * get content, 配置内容
     */
    public String getContent() {
        return content;
    }

    /**
     * set content, 设置:配置内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    public void init() {
        if (getId() == null){
            setId(RandomUtils.randomLid());
        }
    }
}
