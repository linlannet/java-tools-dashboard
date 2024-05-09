package io.linlan.tools.board.dto;

/**
 *
 * Filename:DashMenu.java
 * Desc:
 *
 * @author Linlan
 * CreateTime:2017-12-18 20:35:17
 *
 * @version 1.0
 * @since 1.0
 *
 */
public class ViewDashMenu {

    private long menuId;
    private long parentId;
    private String menuName;
    private String menuCode;

    public ViewDashMenu() {
    }
    public ViewDashMenu(long menuId, long parentId, String menuName, String menuCode) {
        this.menuId = menuId;
        this.parentId = parentId;
        this.menuName = menuName;
        this.menuCode = menuCode;
    }

    public long getMenuId() {
        return menuId;
    }

    public void setMenuId(long menuId) {
        this.menuId = menuId;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }
}
