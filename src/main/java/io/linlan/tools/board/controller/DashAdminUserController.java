package io.linlan.tools.board.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import io.linlan.tools.board.dto.ViewDashBoard;
import io.linlan.tools.board.dto.ViewDashWidget;
import io.linlan.tools.board.entity.*;
import io.linlan.tools.board.service.*;
import io.linlan.tools.board.service.role.RolePermission;
import io.linlan.commons.core.Rcode;
import io.linlan.commons.db.query.Query;
import io.linlan.commons.db.page.Pagination;
import io.linlan.commons.script.json.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * Filename:AdminUser.java
 * Desc:
 *
 * @author Linlan
 * CreateTime:2017-12-18 20:35:18
 *
 * @version 1.0
 * @since 1.0
 *
 */
@RestController
@RequestMapping("/dash/adminuser")
public class DashAdminUserController extends BaseController {
    /** get the list with request params
     * 列表方法，返回{@link Rcode}，包括状态和page
     * @param params the input params
     * @return {@link Rcode} with page info
     */
    @RequestMapping("/list")
    public Rcode list(@RequestParam Map<String, Object> params){
        //查询列表数据
        Query query = new Query(params);

        List<DashAdminUser> dashAdminUserList = dashAdminUserService.getList(query);
        int total = dashAdminUserService.getCount(query);

        Pagination pagination = new Pagination(dashAdminUserList, total, query.getLimit(), query.getPage());

        return Rcode.ok().put("page", pagination);
    }


    /** get the detail info of entity
     * 详情方法，返回{@link Rcode}，包括状态和dashAdminUser
     * @param id the input id
     * @return {@link Rcode} with dashAdminUser info
     */
    @RequestMapping("/info/{id}")
    public Rcode info(@PathVariable("id") String id){
        //添加其他初始化数据，用于数据初始化
        if (!id.equals("null")){
            //当id为null是表示新增数据
            DashAdminUser dashAdminUser = dashAdminUserService.findById(id);
            return Rcode.ok().put("dashAdminUser", dashAdminUser);
        }
        //如果有其他数据，则把其他初始化数据返回
        return Rcode.ok();
    }

    /** save entity with object
     * 保存方法，返回{@link Rcode}，状态
     * @param dashAdminUser the input object
     * @return {@link Rcode}
     */
    @RequestMapping("/save")
    public Rcode save(@RequestBody DashAdminUser dashAdminUser){
        //从数据库层面为了保证数据的有效性，进行实体类信息合格性验证
        //ValidatorUtils.validateEntity(dashAdminUser);
        dashAdminUserService.save(dashAdminUser);

        return Rcode.ok();
    }

    /** update entity with object
     * 更新方法，返回{@link Rcode}，状态
     * @param dashAdminUser the input object
     * @return {@link Rcode}
     */
    @RequestMapping("/update")
    public Rcode update(@RequestBody DashAdminUser dashAdminUser){
        //从数据库层面为了保证数据的有效性，进行实体类信息合格性验证
        //ValidatorUtils.validateEntity(dashAdminUser);
        dashAdminUserService.update(dashAdminUser);

        return Rcode.ok();
    }

    /** delete entity with input ids
     * 删除方法，返回{@link Rcode}，状态
     * @param ids the input ids
     * @return {@link Rcode}
     */
    @RequestMapping("/delete")
    public Rcode delete(@RequestBody String[] ids){
        dashAdminUserService.deleteByIds(ids);

        return Rcode.ok();
    }

    @RequestMapping("/saveNewUser")
    public Rcode saveNewUser(@RequestParam(name = "user") String user) {
        JSONObject json = JsonUtils.parseJO(user);
        dashAdminUserService.addUser(UUID.randomUUID().toString(), json.getString("username"), json.getString("name"), json.getString("password"));
        return new Rcode().ok();
    }

    @RequestMapping("/updateUser")
    public Rcode updateUser(@RequestParam(name = "user") String user) {
        JSONObject json = JsonUtils.parseJO(user);
        dashAdminUserService.updateUser(json.getString("id"), json.getString("username"), json.getString("name"), json.getString("password"));
        return new Rcode().ok();
    }

    @RequestMapping("/deleteUser")
    public Rcode deleteUser(@RequestParam(name = "userId") String userId) {
        dashAdminUserService.deleteUser(userId);
        return new Rcode().ok();
    }

    @RequestMapping("/getUserList")
    public List<DashAdminUser> getUserList() {
        List<DashAdminUser> list = dashAdminUserService.getList(new HashMap<>());
        return list;
    }


    @RequestMapping("/deleteRole")
    public Rcode deleteRole(@RequestParam(name = "roleId") String roleId) {
        dashAdminUserService.deleteUserRoleByRoleId(roleId);
        return new Rcode().ok();
    }


    @RequestMapping("/updateUserRole")
    public Rcode updateUserRole(@RequestParam(name = "userIdArr") String userIdArr, @RequestParam(name = "roleIdArr") String roleIdArr) {
        return dashAdminUserService.updateUserRole(
                JSONArray.parseArray(userIdArr).toArray(new String[]{}),
                JSONArray.parseArray(roleIdArr).toArray(new String[]{}),
                user.getUserId()
        );
    }

    @RequestMapping("/deleteUserRole")
    public Rcode deleteUserRole(@RequestParam(name = "userIdArr") String userIdArr, @RequestParam(name = "roleIdArr") String roleIdArr) {
        return dashAdminUserService.deleteUserRoles(
                JSONArray.parseArray(userIdArr).toArray(new String[]{}),
                JSONArray.parseArray(roleIdArr).toArray(new String[]{}),
                user.getUserId()
        );
    }

    @RequestMapping("/getUserRoleList")
    public List<DashAdminUserRole> getUserRoleList() {
        List<DashAdminUserRole> list = dashAdminUserService.getUserRoleList();
        return list;
    }


    @RequestMapping("/isAdmin")
    public boolean isAdmin() {
        return adminUserId.equals(authService.getCurrentUser().getUserId());
    }


    @RequestMapping("/getBoardListUser")
    public List<ViewDashBoard> getBoardListUser() {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", user.getUserId());
        List<DashBoard> list = dashBoardService.getList(params);
        if (!adminUserId.equals(user.getUserId())) {
            List<DashAdminRoleRes> resList = dashAdminRoleService.getUserRoleResList(user.getUserId(), "board");
            List<String> resIdList = new ArrayList<>();
            Set<String> uniqueValues = new HashSet<>();
            for (DashAdminRoleRes dashAdminRoleRes : resList) {
                if (RolePermission.isEdit(dashAdminRoleRes.getPermission())) {
                    String resId = dashAdminRoleRes.getResId();
                    if (uniqueValues.add(resId)) {
                        resIdList.add(resId);
                    }
                }
            }
            list.stream().filter(e -> resIdList.contains(e.getId()) || e.getUserId().equals(user.getUserId())).collect(Collectors.toList());
        }
        //get folder decsendant
        Set<DashFolder> itemFolders = dashFolderService.getFolderDescendant(dashAdminRoleService.getFolderIds(user.getUserId()));

        String [] para = itemFolders.stream().map(i -> i.getId()).collect(Collectors.toSet()).toArray(new String[itemFolders.size()]);

        Set<DashBoard> merge = new LinkedHashSet<>(list);

        if (para != null && para.length > 0) {
            merge.addAll(dashBoardService.getBoardListByFolderIds(para));
        }
        list = new ArrayList<>(merge);


        return Lists.transform(list, ViewDashBoard.TO);
    }



    @RequestMapping("/getWidgetListUser")
    public List<ViewDashWidget> getWidgetListUser() {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", user.getUserId());
        List<DashWidget> list = dashWidgetService.getList(params);
        if (!adminUserId.equals(user.getUserId())) {
            List<DashAdminRoleRes> resList = dashAdminRoleService.getUserRoleResList(user.getUserId(), "widget");
            List<String> resIdList = new ArrayList<>();
            Set<String> uniqueValues = new HashSet<>();
            for (DashAdminRoleRes dashAdminRoleRes : resList) {
                if (RolePermission.isEdit(dashAdminRoleRes.getPermission())) {
                    String resId = dashAdminRoleRes.getResId();
                    if (uniqueValues.add(resId)) {
                        resIdList.add(resId);
                    }
                }
            }
            list.stream().filter(e -> resIdList.contains(e.getId()) || e.getUserId().equals(user.getUserId())).collect(Collectors.toList());
        }

        //get Folder's auth
        List<DashAdminRoleRes> roleres = dashAdminRoleService.getUserRoleResList(user.getUserId(), "folder");
        Set<String> resIds = new HashSet<>();
        for (DashAdminRoleRes r : roleres) {
            String value = r.getResId();
            resIds.add(value);
        }

        //get folder decsendant
        Set<DashFolder> itemFolders = dashFolderService.getFolderDescendant(resIds);

        String[] para = itemFolders.stream().map(i -> i.getId()).collect(Collectors.toSet()).toArray(new String[itemFolders.size()]);

        Set<DashWidget> merge = new LinkedHashSet<>(list);

        if (para != null && para.length > 0) {
            merge.addAll(dashWidgetService.getListByFolderIds(para));
        }
        list = new ArrayList<>(merge);


        return Lists.transform(list, ViewDashWidget.TO);
    }

    @RequestMapping("/changePwd")
    public Rcode changePwd(@RequestParam(name = "curPwd") String curPwd, @RequestParam(name = "newPwd") String newPwd, @RequestParam(name = "cfmPwd") String cfmPwd) {
        return dashAdminUserService.changePwd(user.getUserId(), curPwd, newPwd, cfmPwd);
    }


    @Value("${admin_user_id}")
    private String adminUserId;


    @Autowired
    private DashBoardService dashBoardService;

    @Autowired
    private DashAdminUserService dashAdminUserService;

    @Autowired
    private DashWidgetService dashWidgetService;

    @Autowired
    private DashAdminRoleService dashAdminRoleService;

    @Autowired
    private DashFolderService dashFolderService;

}
