package net.linlan.tools.board.service;

import net.linlan.tools.board.dao.DashMenuDao;
import net.linlan.tools.board.dto.ViewDashMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Filename:DashMenuService.java
 * Desc:
 *
 * @author Linlan
 * CreateTime:2018/1/3 12:02
 *
 * @version 1.0
 * @since 1.0
 *
 */
@Repository
public class DashMenuService {

    private static List<ViewDashMenu> menuList = new ArrayList<>();

    static {
        menuList.add(new ViewDashMenu(1, -1, "SIDEBAR.CONFIG", "config"));
        menuList.add(new ViewDashMenu(2, 1, "SIDEBAR.DATA_SOURCE", "config.datasource"));
        menuList.add(new ViewDashMenu(3, 1, "SIDEBAR.DATASET", "config.dataset"));
        menuList.add(new ViewDashMenu(4, 1, "SIDEBAR.WIDGET", "config.widget"));
        menuList.add(new ViewDashMenu(5, 1, "SIDEBAR.DASHBOARD", "config.board"));
        menuList.add(new ViewDashMenu(6, 1, "SIDEBAR.DASHBOARD_CATEGORY", "config.category"));
        menuList.add(new ViewDashMenu(7, -1, "SIDEBAR.ADMIN", "admin"));
        menuList.add(new ViewDashMenu(8, 7, "SIDEBAR.USER_ADMIN", "admin.user"));
        menuList.add(new ViewDashMenu(9, 1, "SIDEBAR.JOB", "config.job"));
        menuList.add(new ViewDashMenu(10, 1, "SIDEBAR.SHARE_RESOURCE", "config.role"));

    }

    public List<ViewDashMenu> getMenuList() {
        return menuList;
    }

    public List<Long> getMenuIdByUserRole(String userId) {
        return dao.getMenuIdByUserRole(userId);
    }



    public List<Long> getMenuIdByRoleAdmin(String userId) {
        return dao.getMenuIdByRoleAdmin(userId);
    }

    @Autowired
    private DashMenuDao dao;
}
