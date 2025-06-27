package net.linlan.tools.board.controller;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import net.linlan.tools.board.dto.ViewDashAdminRoleRes;
import net.linlan.tools.board.entity.DashAdminRole;
import net.linlan.tools.board.entity.DashAdminRoleRes;
import net.linlan.tools.board.service.DashAdminRoleService;
import net.linlan.commons.core.RandomUtils;
import net.linlan.commons.core.Rcode;
import net.linlan.commons.db.query.Query;
import net.linlan.commons.db.page.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * Filename:DashAdminRoleController.java
 * Desc:
 *
 * @author Linlan
 * CreateTime:2017-12-18 20:35:17
 *
 * @version 1.0
 * @since 1.0
 *
 */
@RestController
@RequestMapping("/dash/adminrole")
public class DashAdminRoleController extends BaseController {
    /** get the list with request params
     * 列表方法，返回{@link Rcode}，包括状态和page
     * @param params the input params
     * @return {@link Rcode} with page info
     */
    @RequestMapping("/list")
    public Rcode list(@RequestParam Map<String, Object> params){
        //查询列表数据
        Query query = new Query(params);

        List<DashAdminRole> dashAdminRoleList = dashAdminRoleService.getList(query);
        int total = dashAdminRoleService.getCount(query);

        Pagination pagination = new Pagination(dashAdminRoleList, total, query.getLimit(), query.getPage());

        return Rcode.ok().put("page", pagination);
    }


    /** get the detail info of entity
     * 详情方法，返回{@link Rcode}，包括状态和dashAdminRole
     * @param id the input id
     * @return {@link Rcode} with dashAdminRole info
     */
    @RequestMapping("/info/{id}")
    public Rcode info(@PathVariable("id") String id){
        //添加其他初始化数据，用于数据初始化
        if (!id.equals("null")){
            //当id为null是表示新增数据
            DashAdminRole dashAdminRole = dashAdminRoleService.findById(id);
            return Rcode.ok().put("dashAdminRole", dashAdminRole);
        }
        //如果有其他数据，则把其他初始化数据返回
        return Rcode.ok();
    }

    /** save entity with object
     * 保存方法，返回{@link Rcode}，状态
     * @param dashAdminRole the input object
     * @return {@link Rcode}
     */
    @RequestMapping("/save")
    public Rcode save(@RequestBody DashAdminRole dashAdminRole){
        //从数据库层面为了保证数据的有效性，进行实体类信息合格性验证
        //ValidatorUtils.validateEntity(dashAdminRole);
        dashAdminRoleService.save(dashAdminRole);

        return Rcode.ok();
    }

    /** update entity with object
     * 更新方法，返回{@link Rcode}，状态
     * @param dashAdminRole the input object
     * @return {@link Rcode}
     */
    @RequestMapping("/update")
    public Rcode update(@RequestBody DashAdminRole dashAdminRole){
        //从数据库层面为了保证数据的有效性，进行实体类信息合格性验证
        //ValidatorUtils.validateEntity(dashAdminRole);
        dashAdminRoleService.update(dashAdminRole);

        return Rcode.ok();
    }

    /** delete entity with input ids
     * 删除方法，返回{@link Rcode}，状态
     * @param ids the input ids
     * @return {@link Rcode}
     */
    @RequestMapping("/delete")
    public Rcode delete(@RequestBody String[] ids){
        dashAdminRoleService.deleteByIds(ids);

        return Rcode.ok();
    }

    @RequestMapping("/saveRole")
    public Rcode saveRole(@RequestParam(name = "role") String role) {
        JSONObject json = JSONObject.parseObject(role);
        dashAdminRoleService.addRole(RandomUtils.random("R_"), json.getString("name"), json.getString("userId"));
        return Rcode.ok();
    }

    @RequestMapping("/updateRole")
    public Rcode updateRole(@RequestParam(name = "role") String role) {
        JSONObject json = JSONObject.parseObject(role);
        dashAdminRoleService.updateRole(json.getString("roleId"), json.getString("roleName"), json.getString("userId"));
        return new Rcode().ok();
    }


    @RequestMapping("/getRoleList")
    public List<DashAdminRole> getRoleList() {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", user.getUserId());
        List<DashAdminRole> list = dashAdminRoleService.getList(params);
        return list;
    }

    @RequestMapping("/getRoleListAll")
    public List<DashAdminRole> getRoleListAll() {
        List<DashAdminRole> list = dashAdminRoleService.getList(new HashMap<>());
        return list;
    }

    @RequestMapping("/getRoleResList")
    public List<ViewDashAdminRoleRes> getRoleResList() {
        List<DashAdminRoleRes> list = dashAdminRoleService.getRoleResList();
        return list.stream().map(ViewDashAdminRoleRes::new).collect(Collectors.toList());
    }

    @RequestMapping("/updateRoleRes")
    public Rcode updateRoleRes(@RequestParam(name = "roleIdArr") String roleIdArr, @RequestParam(name = "resIdArr") String resIdArr) {
        return dashAdminRoleService.updateRoleRes(JSONArray.parseArray(roleIdArr).toArray(new String[]{}), JSONArray.parseArray(resIdArr));
    }

    @RequestMapping("/updateRoleResUser")
    public Rcode updateRoleResUser(@RequestParam(name = "roleIdArr") String roleIdArr, @RequestParam(name = "resIdArr") String resIdArr) {
        return dashAdminRoleService.updateRoleResUser(JSONArray.parseArray(roleIdArr).toArray(new String[]{}), JSONArray.parseArray(resIdArr).toArray(new String[]{}));
    }

    @Value("${admin_user_id}")
    private String adminUserId;


    @Autowired
    private DashAdminRoleService dashAdminRoleService;

}
