package io.linlan.tools.board.controller;

import io.linlan.tools.board.dto.ViewDashMenu;
import io.linlan.tools.board.service.DashMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 
 * Filename:DashMenuController.java
 * Desc:
 *
 * @author hcday
 * @author <a href="mailto:hcday@qq.com">hcday soo</a>
 * CreateTime:2017/12/18 15:50
 *
 * @version 1.0
 * @since 1.0
 *
 */
@RestController
@RequestMapping("/dash/menu")
public class DashMenuController extends BaseController {

    @RequestMapping("/list")
    public List<ViewDashMenu> list() {
        return dashMenuService.getMenuList();
    }

    @RequestMapping("/isConfig")
    public boolean isConfig(@RequestParam(name = "type") String type) {
        if (user.getUserId().equals(adminUserId)) {
            return true;
        } else if (type.equals("widget")) {
            List<Long> menuIdList = dashMenuService.getMenuIdByUserRole(user.getUserId());
            if (menuIdList.contains(1L) && menuIdList.contains(4L)) {
                return true;
            }
        }
        return false;
    }

    @RequestMapping("/getMenuList")
    public List<ViewDashMenu> getMenuList() {
        if (adminUserId.equals(user.getUserId())) {
            return dashMenuService.getMenuList();
        } else {
            List<Long> menuId = dashMenuService.getMenuIdByRoleAdmin(user.getUserId());
            return dashMenuService.getMenuList().stream().filter(e -> menuId.stream().anyMatch(id -> id.equals(e.getMenuId()))).collect(Collectors.toList());
        }
    }

    @Value("${admin_user_id}")
    private String adminUserId;

    @Autowired
    private DashMenuService dashMenuService;
}
